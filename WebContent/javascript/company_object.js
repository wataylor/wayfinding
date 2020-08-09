 /** The standard company properties are listed here and set to
  * undefined.  That means that the properties exist so that their
  * names can be found by examining this file, but (company.property)
  * returns false until they are assigned explicit values.  There is
  * only one of these objects named "currentCompany"; it is
  * instantiated when the company info page is read.*/
function Company () {
  /* JSON object returned from the server when the company is found
   * in the database.  Note that most of the properties have the same
   * names as the columns in the SQL table.  Properties are subject to
   * change as the database table changes.  Note: A JSON object is a
   * text representation of a Java Script object.  By the time the
   * variable value is set, the text has been converted into a Java
   * Script object.*/
  this.companyJson = undefined;
  /* Object which is managing the current list of selections*/
  this.suggestObj = undefined;
};

/* This array defines the names of the building parameters which are
 * to be displayed when building data are retrieved from the server.
 * These names must match the names send in the Json object and in the
 * DOM.*/
Company.prototype.fieldList =
  ["name", "mainFax", "mainPhone", "division",
   "mainEmail", "details", "tags"];

/* These fields must be filled in before someone can be added to the
 * database */
Company.requiredFields =
  ["name", "mainPhone" ];

/* Human-readable descriptions of the required data fields. */
Company.requiredNames =
  ["Name", "Phone Number" ];

/* Called when the company find button is pressed to take a suggestion */
Company.findChosenCompany = function() {
  var cp = currentCompany;
  var choice;
  if (cp.suggestObj.inserted >= 0) {
    choice = cp.suggestObj.suggestions[cp.suggestObj.inserted];
    $.getJSON('../json.jsp?verb=4',
	      choice,
	      function(data) {
		UIU.cantSmite("findBtn");
		var destF; var floorDWG;
		if (data['msg'])  {
		  UIU.failure(data['msg']);
		} else {
		  cp.companyJson = data;
		  Company.showCompanyFields(cp);
		}
	      });
  } else {
    alert("Make a choice before hitting the find button, please");
  }
  return false;			// Suppress normal submit
};

Company.showCompanyFields = function(cp) {
  Company.resetErrorFields();
  UIU.hideMessageDivs();
  // debugger;
  var i, fnam, val, elem;
  UIU.assignHTML("admin_co_name2",  cp.companyJson["name"]);
  for (i=0; i<cp.fieldList.length; i++) {
    fnam = cp.fieldList[i];
    val  = cp.companyJson[fnam];
    UIU.assignHTML("admin_co_" + fnam, val);
  }
  Edifice.showPickature("admin_co_", cp.companyJson.image);
  UIU.assignHTML("admin_co_image",   cp.companyJson.image);
  UIU.visibility("deleteBtn", true);
  UIU.visibility("updateBtn", true);
  UIU.visibility("addBtn", false);
  UIU.visibility("clearPersonBtn", true);
  UIU.canSmite("updateBtn");
  UIU.cantSmite("addBtn");
  UIU.canSmite("deleteBtn");
};

Company.clearCompanySearch = function() {
  UIU.hideMessageDivs();
  UIU.cantSmite("findBtn");
  var elem = UIU.getElementOrWhinge("SrchInputField");
  if (elem) {
    elem.innerHTML = ""; // use value to set input fields
    elem.value     = ""; // use innerHTML set non-editable fields
  }
  var suggestObj = currentCompany.suggestObj;
  if (suggestObj) {
    suggestObj.hideDiv();
  }
};

Company.resetErrorFields = function() {
  UIU.resetErrorFields("admin_co_", Company.requiredFields);
};

Company.clearCompanyFields = function() {
  UIU.hideMessageDivs();
  Company.justClearFields();
};

Company.justClearFields = function() {
  Company.resetErrorFields();

  var cp = currentCompany;
  // debugger;
  UIU.assignHTML("admin_co_name2", "");
  var i, fnam, val;
  for (i=0; i<cp.fieldList.length; i++) {
    fnam = cp.fieldList[i];
    UIU.assignHTML("admin_co_" + fnam, "");
  }
  Edifice.hidePickature("admin_co_");
  Company.clearCompanySearch();
  cp.companyJson = undefined;	// get rid of old data
  UIU.visibility("deleteBtn", false);
  UIU.visibility("updateBtn", false);
  UIU.visibility("addBtn", true);
  UIU.visibility("clearPersonBtn", true);
  UIU.cantSmite("deleteBtn");
  UIU.cantSmite("updateBtn");
  UIU.canSmite("addBtn");
};

/* Called when the company update button is pressed.*/
Company.modifyCompany = function() {
  UIU.hideMessageDivs();
  // console.log("update");
  $.getJSON('../json.jsp?verb=8&fKey=admin_co_',
	    $('#companyGeneralInfoForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
		Company.justClearFields();
	      }
	    });
  return false;
};

Company.deleteCompany = function() {
  console.log("Delete company");
};

Company.addCompany = function() {
  // console.log("add company");
  if (UIU.fieldValueCheck("admin_co_", // Also used in person_object.js
			  Company.requiredFields, Company.requiredNames)) {
    $.getJSON('../json.jsp?verb=10&ojCl=Organization&bdEO=y&fKey=admin_co_',
	    $('#companyGeneralInfoForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		var ix, msg = data['scs'];
		ix = msg.indexOf("_");
		UIU.success(msg.substring(ix+1));
		Company.justClearFields();
	      }
	    });
  }
  return false;
};

/* Create a change logo dialog box if necessary and show it.  Such
 * modal pop-ups can be instantiated durectly by jQuery using code
 * something like

 $("a.modalPopup").boxy({modal:true,title:true,closeable:true});

 * where the jQuery selects a <a element whose class is modalPopup and
 * whose title attribute gives the title of the box as in:

 <a href="#addImageWizard" class="modalPopup"
 title="Upload/Change Logo">Upload/Change Logo</a>

 * addImageWizard identifies a DIV whose content is to be incorporated
 * into the pop up.  Unfortunately, it does not seem to be possible to
 * find the pop up object created by that method.  There is a method
 * in the pop up file which is supposed to find the pop up which is
 * associated with a given href, but that does not seem to work.  This
 * method creates the pop up in a way which makes it posible to
 * manipulate it.

 */
Company.changeLogo = function() {
//  if (!currentCompany.companyJson) {
//    UIU.failure("Please select a company before trying to change the logo.");
//    return;
//  }
  if (!Company.changeLogoBox) {
    var elem = UIU.getElementOrWhinge("addImageWizard");
    if (elem) {
      Company.changeLogoBox = new Boxy(elem, {modal:true,single:true,title:"Upload/Change Logo",closeable:true});
      // Make the submit work properly
      // AJUP.frameSub("addImageForm", Company.logoCallback);
    }
  }
  if (!Company.changeLogoBox) { return; }
  UIU.assignHTML("imageSrc", ""); // Clean out the input line
  Company.changeLogoBox.show();
};

Company.cancelChangeLogo = function() {
  Company.changeLogoBox.hide();
};

/* Validate the image file name and submit the form.*/
Company.submitChangeLogo = function() {
  var val, elem = UIU.getElementOrWhinge("imageSrc");
  if (elem) {
    if (!elem.files || !elem.files[0] || !(val = elem.files[0].fileName) ||
	(val.length <= 0)) {
      UIU.failure("Please select an image file before trying to upload.");
    return false;
    }
    var fileUp = val.toLowerCase();
    if ((fileUp.indexOf('.jpg')  < 0) &&
	(fileUp.indexOf('.jpeg') < 0) &&
	(fileUp.indexOf('.png')  < 0) &&
	(fileUp.indexOf('.bmp')  < 0) &&
	(fileUp.indexOf('.gif')  < 0) ) {
      UIU.failure("File names must end in bmp, jpg, jpeg, png, or gif.");
      return false;
    }
    if (currentCompany.companyJson) {
      currentCompany.companyJson.image = val;
    }
    elem = UIU.getElementOrWhinge("addImageForm");
    if (elem) {
      elem.submit();
    }
    UIU.assignHTML("admin_co_image", val);
    Company.changeLogoBox.fileName = val;
  }
  Company.changeLogoBox.hide();
  return false;
};

/* Replace the old image file name with the new one and display the image.*/
Company.logoCallback = function(data) {
  if (UIU.processResultString(data)) {
    Edifice.showPickature("admin_co_", Company.changeLogoBox.fileName);
  }
};

Company.changeLogoBox = undefined;

Company.customFields = function() {
  if (!Company.customFieldBox) {
    var elem = UIU.getElementOrWhinge("addCustomFieldsWizard");
    if (elem) {
      Company.customFieldBox = new Boxy(elem, {modal:true,single:true,title:"Add Custom Field",closeable:true});
    }
  }
  if (!Company.customFieldBox) { return; }
  //UIU.assignHTML("imageSrc", ""); // Clean out the input line
  Company.customFieldBox.show();
};

Company.cancelCustField = function() {
  Company.customFieldBox.hide();
};

Company.submitCustField = function() {
  console.log("submit custom ");
};

Company.customFieldBox = undefined;
