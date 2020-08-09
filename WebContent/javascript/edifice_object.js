 /** The standard edifice properties are listed here and set to
  * undefined.  That means that the properties exist so that their
  * names can be found by examining this file, but (edifice.property)
  * returns false until they are assigned explicit values.  There is
  * only one of these objects named "currentEdifice"; it is
  * instantiated when general_client is read.*/
function Edifice () {
  /* JSON object returned from the server when the building is found
   * in the database.  Note that most of the properties have the same
   * names as the columns in the SQL table.  Properties are subject to
   * change as the database table changes.  Note: A JSON object is a
   * text representation of a Java Script object.  By the time the
   * variable value is set, the text has been converted into a Java
   * Script object.*/
  this.edificeJson = undefined;
  /* JSON object which stores all the people associated directly with
   * the building.*/
  this.edificeFolks = undefined;
  /* String that stores the error message*/
  this.msg = undefined;
  /* Array of selector entires*/
  this.orgSelector = undefined;
};

/* This array defines the names of the building parameters which are
 * to be displayed when building data are retrieved from the server.
 * These names must match the names send in the Json object and in the
 * DOM.*/
Edifice.prototype.fieldList = ["name", "address1Line1", "mainPhone", "mainFax",
			       "mainEmail"];
//Edifice.prototype.personList = ["secMgrID", "facMgrID", "opsMgrID"];
Edifice.prototype.perPerson  = ["firstName", "lastName", "workPhone", "emailAddress"];

/* This method passes the search parameters back to the server but is
 * guaranteed to return one and only one edifice regardless of how
 * many edifices match the search criteria.  This is intended for
 * single-edifice systems, but it returns a default edifice for
 * multi-edifice systems.*/
Edifice.prototype.getDefaultEdifice = function() {
  $.getJSON('../json.jsp?verb=1&dump=y',
	    $('#admin_bldgSrchForm').serialize(),
	    Edifice.processEdifice);
  return false;		// Suppress the normal submit
};

/* Handle a building object from the server but do not object if there
 * is no current building.*/
Edifice.processOptionalEdifice = function(data) {
  if (!data['msg']) {		// bail out on error, this is OK
    Edifice.processEdifice(data);
  }
};

/* Show the current building field values in the form */
Edifice.showEdificeFields = function(ce) {
  var i, fnam, val, elem;
// for (i=0; i<ce.fieldList.length; i++) { // unclear why this does not work
  for (i in ce.fieldList) {
    fnam = ce.fieldList[i];
    if (elem = UIU.getElementOrWhinge("admin_bldg_" + fnam)) {
      if (val =  ce.edificeJson[fnam]) {
	elem.innerHTML = val; // use value to set input fields
	elem.value     = val; // use innerHTML set non-editable fields
      }
    }
  }
};

/* Handle a building object returned from the server.*/
Edifice.processEdifice = function(data) {
  var i; var elem; var fnam; var val; var must = "";
  currentEdifice.edificeJson = data;
  if (data['msg']) {
    alert(data['msg']);
  } else {
    currentEdifice.edificeJson = data;
    Edifice.showEdificeFields(currentEdifice);
    // console.log(must);

    $.getJSON('../json.jsp?verb=5' + must,
	      function(data) {
		var field;
		if (data['msg'])  {
		  alert(data['msg']);
		} else {
		  currentEdifice.edificeFolks = data;
		  // Two parallel arrays
		  var people = data[0];
		  var titles = data[1];
		  var elem = UIU.getElementOrWhinge("contacts_content");
		  $(elem).empty();
		  for (i=0; i<people.length; i++) {
		    Edifice.persShow(titles[i], people[i]);
		  }
		}
	      });

  }
};

Edifice.contactSelect = function() {
  console.log("Selected contact " + this.id);
};

Edifice.persShow = function(cat, pers) {
  // console.log(cat + " " + pers);
  var i, val, field, elem, dID;
  elem = UIU.getElementOrWhinge("contacts_content");
  if (!elem) { return; }

  dID = Frag.buildingContact(elem, Edifice.contactSelect);
  UIU.assignHTML(dID + "title", cat);

  for (i=0; i<currentEdifice.perPerson.length; i++) {
    if (val = pers[field = currentEdifice.perPerson[i]]) {
      UIU.assignHTML(dID + field, val);
    }
  }
};

/* Get the current edifice if there is one and use the appropriate
 * callback to handle the data.  This is used when switching to any
 * .html page that needs to know anything about the current building if
 * there is one.  */
Edifice.getCurrentEdifice = function(cbfunc) {
  $.getJSON('../json.jsp?verb=6', cbfunc);
};

/* Handle building load callbacks when switching to the locations page
 * which needs to know the current building.  This should happen only
 * when the location information page is loaded.*/
Edifice.getCBLocations = function(data) {
  var ce = currentEdifice;
  if (data['msg'])  {
    ce.edificeJson = undefined;
    ce.msg = data['msg'];
    alert("You must select a building before you can work with a location in the building.");
    document.location.href = "building_info.jsp";
  } else {
    ce.edificeJson = data;
    ce.msg = undefined;
    Edifice.showEdificeFields(ce);
  }
};

/* Handle building load callbacks when switching to the locations page
 * which needs to know the current building.  This should happen only
 * when the location information page is loaded.*/
Edifice.getCBLocationsNoShow = function(data) {
  var ce = currentEdifice;
  if (data['msg'])  {
    ce.edificeJson = undefined;
    ce.msg = data['msg'];
    alert("You must select a building before you can work with a location in the building.");
    document.location.href = "building_info.jsp";
  } else {
    ce.edificeJson = data;
    ce.msg = undefined;
    UIU.assignHTML("admin_bldg_name", ce.edificeJson.name);
  }
};

/* Utilities for showing and hiding pictures.*/

Edifice.showPickature = function(idHead, im) {
  //var im = obj['image'];
  if (!im) { Edifice.hidePickature(idHead); return; }
  if (!currentEdifice || !currentEdifice.edificeJson) {
    Edifice.hidePickature(idHead);
    return;
  }
  var nn = currentEdifice.edificeJson.nickName;
  if (!nn) { Edifice.hidePickature(idHead); return; }
  var elem = UIU.getElementOrWhinge(idHead + "thumb");
  if (!elem) { return; }
  elem.src = "/wayfinding/photos/" + nn + "/photos/" + im;
};

Edifice.hidePickature = function(idHead) {
  var elem = UIU.getElementOrWhinge(idHead + "thumb");
  if (elem) {
    elem.src = "../images/thumbnail.png";
  }
};

