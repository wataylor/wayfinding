 /** The standard person properties are listed here and set to
  * undefined.  That means that the properties exist so that their
  * names can be found by examining this file, but (person.property)
  * returns false until they are assigned explicit values.  There is
  * only one of these objects named "currentPerson"; it is
  * instantiated when the person info page is read.*/
function Person () {
  /* JSON object returned from the server when the person is found
   * in the database.  Note that most of the properties have the same
   * names as the columns in the SQL table.  Properties are subject to
   * change as the database table changes.  Note: A JSON object is a
   * text representation of a Java Script object.  By the time the
   * variable value is set, the text has been converted into a Java
   * Script object.*/
  this.personJson = undefined;
  /* Object which is managing the current list of selections*/
  this.suggestObj = undefined;
};

/* This array defines the names of the building parameters which are
 * to be displayed when building data are retrieved from the server.
 * These names must match the names send in the Json object and in the
 * DOM.*/
Person.prototype.fieldList =
  ["firstName", "lastName", "fax", "title", "mobilePhone",
   "emailAddress", "workPhone", "details", "tags"];

/* These fields must be filled in before someone can be added to the
 * database */
Person.requiredFields =
  ["firstName", "lastName",
   "emailAddress", "workPhone" ];

/* Human-readable descriptions of the required data fields. */
Person.requiredNames =
  ["First Name", "Last Name", "Email Address", "Work Phone" ];

/* Called when the person find button is pressed to take a suggestion */
Person.findChosenPerson = function() {
  var cp = currentPerson;
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
		  cp.personJson = data;
		  Person.showPersonFields(cp);
		}
	      });
  } else {
    alert("Make a choice before hitting the find button, please");
  }
  return false;			// Suppress normal submit
};

Person.showPersonFields = function(cp) {
  // debugger;
  Person.resetErrorFields();
  var i, fnam, val;
  for (i=0; i<cp.fieldList.length; i++) {
    fnam = cp.fieldList[i];
    val  = cp.personJson[fnam];
    UIU.assignHTML("admin_pers_" + fnam, val);
  }
  Edifice.showPickature("admin_pers_", cp.personJson.image);
  Person.fixSelector();
  UIU.assignHTML("admin_pers_image", cp.personJson.image);
  UIU.visibility("deleteBtn", true);
  UIU.visibility("updateBtn", true);
  UIU.visibility("addBtn", false);
  UIU.visibility("clearPersonBtn", true);
  UIU.canSmite("updateBtn");
  UIU.cantSmite("addBtn");
  UIU.canSmite("deleteBtn");
};

Person.clearPersonSearch = function() {
  UIU.cantSmite("findBtn");
  var elem = UIU.getElementOrWhinge("SrchInputField");
  if (elem) {
    elem.innerHTML = ""; // use value to set input fields
    elem.value     = ""; // use innerHTML set non-editable fields
  }
  var suggestObj = currentPerson.suggestObj;
  if (suggestObj) {
    suggestObj.hideDiv();
  }
};

Person.resetErrorFields = function() {
  UIU.resetErrorFields("admin_pers_", Person.requiredFields);
};

Person.clearPersonFields = function() {
  UIU.hideMessageDivs();
  Person.justClearFields();
};

Person.justClearFields = function() {
  Person.resetErrorFields();

  var cp = currentPerson;
  // debugger;
  var i, fnam, val;
  for (i=0; i<cp.fieldList.length; i++) {
    fnam = cp.fieldList[i];
    UIU.assignHTML("admin_pers_" + fnam, "");
  }
  Edifice.hidePickature("admin_pers_");
  Person.clearPersonSearch();
  cp.personJson = undefined;
  UIU.visibility("deleteBtn", false);
  UIU.visibility("updateBtn", false);
  UIU.visibility("addBtn", true);
  UIU.visibility("clearPersonBtn", true);
  UIU.cantSmite("deleteBtn");
  UIU.cantSmite("updateBtn");
  UIU.canSmite("addBtn");
};

/* Request the organization selection list.*/
Person.fixSelector = function() {
  if (!currentEdifice.orgSelector) {
    $.getJSON('../json.jsp?verb=9&wLs=0',
	      function(data) {
		if (data['msg']) {
		  UIU.failure(data['msg']);
		} else {
		  currentEdifice.orgSelector = data['scs'];
		  Person.fixSelector();
		}
	      });
    return;
  }
  var val,len,op,sel,i, elem = UIU.getElementOrWhinge("admin_pers_organizationID");
  if (!elem) { return; }
  for (i in currentEdifice.orgSelector) {
    sel = currentEdifice.orgSelector[i];
    elem[i] = new Option(sel[1], sel[0], 0, 0);
  }
  elem.length = len = currentEdifice.orgSelector.length; // Force array length
  if (!currentPerson.personJson) { return; }
  if (!(sel = currentPerson.personJson.organizationID)) { return; }
  for (i=0; i<len; i++) {
    op  = elem[i];
    val = op.value;
    if (sel === val) {		// This could be a boolean assignment statement,
      op.selected = true;	// but the debugger likes this notation better
    } else {
      op.selected = false;
    }
  }
};

/* Called when the person update button is pressed.*/
Person.modifyPerson = function() {
  // console.log("update person");
  $.getJSON('../json.jsp?verb=8&fKey=admin_pers_',
	    $('#personnelGeneralInfoForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
		Person.justClearFields();
	      }
	    });
  return false;
};

Person.deletePerson = function() {
  console.log("delete person");
  return false;
};

Person.addPerson = function() {
  // console.log("add person");
  if (UIU.fieldValueCheck("admin_pers_",
			  Person.requiredFields, Person.requiredNames)) {
    $.getJSON('../json.jsp?verb=10&ojCl=Person&bdEO=y&fKey=admin_pers_',
	    $('#personnelGeneralInfoForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		var ix, msg = data['scs'];
		ix = msg.indexOf("_");
		UIU.success(msg.substring(ix+1));
		Person.justClearFields();
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
Person.changeLogo = function() {
//  if (!currentPerson.personJson) {
//    UIU.failure("Please select a person before trying to change the logo.");
//    return;
//  }
  if (!Person.changeLogoBox) {
    var elem = UIU.getElementOrWhinge("addImageWizard");
    if (elem) {
      Person.changeLogoBox = new Boxy(elem, {modal:true,single:true,title:"Upload/Change Logo",closeable:true});
      // Make the submit work properly
      // AJUP.frameSub("addImageForm", Person.logoCallback);
    }
  }
  if (!Person.changeLogoBox) { return; }
  UIU.assignHTML("imageSrc", ""); // Clean out the input line
  Person.changeLogoBox.show();
};

Person.cancelChangeLogo = function() {
  Person.changeLogoBox.hide();
};

/* Validate the image file name and submit the form.*/
Person.submitChangeLogo = function() {
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
    if (currentPerson.personJson) {
      currentPerson.personJson.image = val;
    }
    elem = UIU.getElementOrWhinge("addImageForm");
    if (elem) {
      elem.submit();
    }
    UIU.assignHTML("admin_pers_image", val);
    Person.changeLogoBox.fileName = val;
  }
  Person.changeLogoBox.hide();
  return false;
};

/* Replace the old image file name with the new one and display the image.*/
Person.logoCallback = function(data) {
  if (UIU.processResultString(data)) {
    Edifice.showPickature("admin_pers_", Person.changeLogoBox.fileName);
  }
};

Person.changeLogoBox = undefined;

Person.newCo = function() {
  if (!currentPerson.personJson) {
    UIU.failure("Please select a person before trying to add a company.");
    return;
  }
  if (!Person.newCoBox) {
    var elem = UIU.getElementOrWhinge("addCompanyWizard");
    if (elem) {
      Person.newCoBox = new Boxy(elem, {modal:true,single:true,title:"Upload/Change Logo",closeable:true});
      // Make the submit work properly
      // AJUP.frameSub("addImageForm", Person.logoCallback);
    }
  }
  if (!Person.newCoBox) { return; }
  UIU.assignHTML("admin_co_name", ""); // Clean out the input line
  UIU.assignHTML("admin_co_division", ""); // Clean out the input line
  UIU.assignHTML("admin_co_mainPhone", ""); // Clean out the input line
  UIU.resetErrorFields("admin_co_", Company.requiredFields);
  Person.newCoBox.show();
};

Person.cancelNewCo = function() {
  Person.newCoBox.hide();
};

/* Submit the form.*/
Person.submitNewCo = function() {

  if (UIU.fieldValueCheck("admin_co_",
			  Company.requiredFields, Company.requiredNames)) {
    // console.log("submit newco");
    $.getJSON('../json.jsp?verb=10&ojCl=Organization&fKey=admin_co_',
	    $('#addCompanyForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		var ix, msg = data['scs'];
		ix = msg.indexOf("_");
		currentPerson.personJson.organizationID = parseInt(msg);
		currentEdifice.orgSelector = undefined;
		Person.fixSelector();
		UIU.success(msg.substring(ix+1));
	      }
	    });
  }

//currentPerson.personJson.image = val;

  Person.newCoBox.hide();
  return false;
};

Person.newCoBox = undefined;

Person.customFields = function() {
  console.log("custom fields " + this.id);
  if (!Person.customFieldBox) {
    var elem = UIU.getElementOrWhinge("addCustomFieldsWizard");
    if (elem) {
      Person.customFieldBox = new Boxy(elem, {modal:true,single:true,title:"Add Custom Field",closeable:true});
    }
  }
  if (!Person.customFieldBox) { return; }
  //UIU.assignHTML("imageSrc", ""); // Clean out the input line
  Person.customFieldBox.show();
};

Person.cancelCustField = function() {
  Person.customFieldBox.hide();
};

Person.submitCustField = function() {
  console.log("submit custom ");
};

Person.customFieldBox = undefined;
