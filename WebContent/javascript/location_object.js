/** Stores information about a location within an edifice so that the
 * location can be edited as part of the Admin functionlity.*/

function Location() {
  /* JSON object returned from the server when the location is found
   * in the database.  Note that most of the properties have the same
   * names as the columns in the SQL table.  Properties are subject to
   * change as the database table changes.  */
  this.locationJson = undefined;
  /* Object which is managing the current list of selections*/
  this.suggestObj = undefined;
  /* Object which lists all the photos which are associated with this
   * location.  */
  this.photoJson  = undefined;
  this.photoCount = 0;
}

/* These fields match field names in the form, in the Json object, and
 * in the database table. */
Location.prototype.fieldList = ["name", "tags", "displayName", "details" ];

Location.imRequiredFields = ["addImageSrc", "addImageCaption"];
Location.imRequiredNames  = ["Image File", "Caption"];
Location.selectionLists =
  ["assignedPersonnelList", "unassignedPersonnelList", "filterCompany",
   "assignedCompanies", "companyNameList" ];

/* Called when the location find button is pressed */
Location.findChosenLocation = function() {
  var cl = currentLocation;
  var choice;
  if (cl.suggestObj.inserted >= 0) {
    choice = cl.suggestObj.suggestions[cl.suggestObj.inserted];
    $.getJSON('../json.jsp?verb=7',
	      choice,
	      function(data) {
		var destF; var floorDWG;
		if (data['msg'])  {
		  UIU.failure(data['msg']);
		} else {
		  cl.locationJson = data;
		  Location.showLocationFields(cl);
		  UIU.hideMessageDivs();
		  UIU.cantSmite("findBtn");
		  Location.getPhotos(cl);
		  Location.goGeneral(); // shift to the general tab
		}
	      });
  } else {
    alert("Make a choice before hitting the find button, please");
  }
  return false;			// Suppress normal submit
};

Location.showLocationFields = function(cl) {
  // debugger;
  var i, fnam, val, elem, eleml;
  for (i=0; i<cl.fieldList.length; i++) {
    fnam = cl.fieldList[i];
    if (elem = document.getElementById("admin_loc_" + fnam)) {
      if (val =  cl.locationJson[fnam]) {
	  elem.innerHTML = val; // use value to set input fields
	  elem.value     = val; // use innerHTML set non-editable fields
	} else {
	  elem.innerHTML = ""; // use value to set input fields
	  elem.value     = ""; // use innerHTML set non-editable fields
      }
    }
    eleml= UIU.getElementOrWhinge("locationAccessibilityLbl");
    elem = UIU.getElementOrWhinge("locationAccessibility");
    if (val = cl.locationJson.blocked) {
      if (val == 'Y') {
	elem.selectedIndex = 2;
      } else {
	elem.selectedIndex = 1;
      }
      $(eleml).show();
      $(elem).show();
    } else {
      elem.selectedIndex = 0;
      $(eleml).hide();
      $(elem).hide();
    }
  }
};

Location.getPhotos = function() {
  $.getJSON('../json.jsp?verb=11',
	    function(data) {
		if (data['msg'])  {
		  UIU.failure(data['msg']);
		} else {
		  currentLocation.photoJson = data['scs'];
		  Location.showLocationPhotos(currentLocation);
		}
	      });
};

Location.showLocationPhotos = function(cl) {
  $("#images_content").empty();
  var pho, i, elID, el, elem = UIU.getElementOrWhinge("images_content");
  for (i in cl.photoJson) {
    pho = cl.photoJson[i];
    if (i > 0) { $(elem).append('<hr />'); }
    elID = Frag.locationImage(elem, i, Location.deletePhoto);
    el = UIU.getElementOrWhinge(elID + "delete");
    if (el) {
      el.relID = pho.id;
    }
    UIU.assignHTML(elID + "caption",  pho.cap);
    UIU.assignHTML(elID + "filename", pho.fil);
    Edifice.showPickature(elID, pho.fil);
  }
};

Location.deletePhoto = function() {
  console.log("del ph " + this.relID + " " + this.id);
  $.getJSON('../json.jsp?verb=12&rID=' + this.relID,
	    function(data) {
		if (data['msg'])  {
		  UIU.failure(data['msg']);
		} else {
		  Location.getPhotos();
		}
	      });
};

Location.clearLocationSearch = function() {
  UIU.cantSmite("findBtn");
  UIU.assignHTML("SrchInputField", "");
  var suggestObj = currentLocation.suggestObj;
  if (suggestObj) {
    suggestObj.hideDiv();
  }
  Location.clearLocationFields(); // This is odd - there is no clear button
  Location.goGeneral();
};

/* Return to the general page*/
Location.goGeneral = function() {
  $("#locationGeneralContent").show();$("#locationPersonnelContent").hide();$("#locationCompanyContent").hide();$("#locationImageContent").hide();$("#locationCameraContent").hide();$("#locationBulkContent").hide();
};

/* New locations cannot be created from admin so there are no required
 * fields in locations, but there are such fields in image upload.*/
Location.resetErrorFields = function() {
  UIU.resetErrorFields("", Location.imRequiredFields);
};

Location.clearLocationFields = function() {
  UIU.hideMessageDivs();
  Location.justClearFields();
};

Location.justClearFields = function() {
  Location.resetErrorFields();
  // debugger;
  var i, fnam, val, cl, elem;
  cl = currentLocation;
  for (i=0; i<cl.fieldList.length; i++) {
    fnam = cl.fieldList[i];
    UIU.assignHTML("admin_loc_" + fnam, "");
  }
  elem = UIU.getElementOrWhinge("locationAccessibility");
  if (elem) { elem.selectedIndex = 0; }
  $("#images_content").empty();
  // Should call clear location search, but there is no clear button.
  for (i=0; i<Location.selectionLists.length; i++) {
    UIU.clearSelectionListByID(Location.selectionLists[i]);
    // console.log(Location.selectionLists[i]);
  }
};

/* Called when the location update button is pressed.*/
Location.modifyLocation = function() {
  var val, elem, extra = "";
  if ((currentLocation.locationJson) &&
      (val = currentLocation.locationJson.blocked)) {
    elem = UIU.getElementOrWhinge("locationAccessibility");
    if (elem.selectedIndex == 2) {
      extra = '&admin_loc_blocked=Y';
    } else {
      extra = '&admin_loc_blocked=n';
    }
  }
  // console.log("update");
  $.getJSON('../json.jsp?verb=8' + extra + '&fKey=admin_loc_',
	    $('#locationDefaultInfo').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
		Location.justClearFields();
	      }
	    });
  return false;
};

Location.clearAddImage = function() {
  //console.log("clear add image");
  Location.resetErrorFields();
  UIU.assignHTML("addImageSrc", "");
  UIU.assignHTML("addImageCaption", "");
};

Location.submitAddImage = function() {
  //console.log("submit add image");
  if (UIU.fieldValueCheck("", Location.imRequiredFields,
			  Location.imRequiredNames)) {
    var val, elem = UIU.getElementOrWhinge("addImageSrc");
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
      elem = UIU.getElementOrWhinge("addImageForm");
      if (elem) {
	elem.submit();
      }
    }
  }
};

Location.addImageCB = function(data) {
  if (UIU.processResultString(data)) {
    Location.getPhotos();
  }
};

Location.populateCompanyFilter = function() {
  if (!currentLocation.locationJson) { return; }
  if (!currentEdifice.orgSelector) {
    $.getJSON('../json.jsp?verb=9&wLs=0',
	      function(data) {
		if (data['msg']) {
		  UIU.failure(data['msg']);
		} else {
		  currentEdifice.orgSelector = data['scs'];
		  currentEdifice.orgSelector[0] = ["All","All Companies"];
		  Location.populateCompanyFilter();
		}
	      });
    return;
  }
  UIU.fillSelector("filterCompany", currentEdifice.orgSelector);
};

Location.populateCurrentPersonnel = function() {
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=13&wLs=1&ast=y',
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		if (!UIU.fillSelector("assignedPersonnelList", data['scs'])) {
		  //UIU.failure();
		}
	      }
	    });
};

Location.populatePotentialPersonnel = function() {
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=13&wLs=1&ast=n',
	    $('#assignPersonnelForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		if (!UIU.fillSelector("unassignedPersonnelList", data['scs'])) {
		  //UIU.failure("No companies were found.");
		}
	      }
	    });
};

/* Get list of companies currently assigned to the location being edited*/
Location.populateCurrentCompanies = function() {
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=9&wLs=1&ast=y',
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		if (!UIU.fillSelector("assignedCompanies", data['scs'])) {
		  //UIU.failure("There are no companies assigned to this location.");
		}
	      }
	    });
};

Location.populatePotentialCompanies = function() {
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=9&wLs=1&ast=n',
	    $('#assignCompanyForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		if (!UIU.fillSelector("companyNameList", data['scs'])) {
		  //UIU.failure("No companies were found.");
		}
	      }
	    });
};

Location.goingToAssignPersonnel = function() {
  // console.log("going personnel");
  Location.populateCurrentPersonnel();
  Location.populatePotentialPersonnel();
  Location.populateCompanyFilter();
};

Location.goingToAssignCompany = function() {
  // console.log("going company");
  Location.populateCurrentCompanies();
  Location.populatePotentialCompanies();
};

Location.assignPersonnel = function() {
  console.log("assign personnel");
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=14&wLs=0',
	    $('#assignPersonnelForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
	      }
	      Location.populateCurrentPersonnel();
	      Location.populatePotentialPersonnel();
	    });
};

Location.unassignPersonnel = function() {
  console.log("unassign personnel");
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=14&wLs=1',
	    $('#unassignPersonnelForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
	      }
	      Location.populateCurrentPersonnel();
	      Location.populatePotentialPersonnel();
	    });
};

Location.assignCompany = function() {
  console.log("assign company");
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=14&wLs=2',
	    $('#assignCompanyForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
	      }
	      Location.populateCurrentCompanies();
	      Location.populatePotentialCompanies();
	    });
};

Location.unassignCompany = function() {
  console.log("unassign company");
  if (!currentLocation.locationJson) { return; }
  $.getJSON('../json.jsp?verb=14&wLs=3',
	    $('#unassignCompanyForm').serialize(),
	    function(data) {
	      if (data['msg']) {
		UIU.failure(data['msg']);
	      } else {
		UIU.success(data['scs']);
	      }
	      Location.populateCurrentCompanies();
	      Location.populatePotentialCompanies();
	    });
};

Location.customFields = function() {
  if (!Location.customFieldBox) {
    var elem = UIU.getElementOrWhinge("addCustomFieldsWizard");
    if (elem) {
      Location.customFieldBox = new Boxy(elem, {modal:true,single:true,title:"Add Custom Field",closeable:true});
    }
  }
  if (!Location.customFieldBox) { return; }
  //UIU.assignHTML("imageSrc", ""); // Clean out the input line
  Location.customFieldBox.show();
};

Location.cancelCustField = function() {
  Location.customFieldBox.hide();
};

Location.submitCustField = function() {
  console.log("submit custom ");
};

Location.customFieldBox = undefined;
