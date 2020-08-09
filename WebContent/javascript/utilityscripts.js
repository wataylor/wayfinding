// For loading multiple functions into a single event type (i.e. onload).
function addEvent(obj, evType, fn) {
  if (obj.addEventListener) {
    obj.addEventListener(evType, fn, false);
    return true;
  } else if (obj.attachEvent) {
    var r = obj.attachEvent("on"+evType, fn);
    return r;
  } else {
    return false;
  }
};

function UIU() {};

  /* Used to get the usable width of the window in IE or Firefox
   * window.innerHeight is FF only, IE uses document.body.clientHeight */
UIU.getW = function() {
  if (typeof window.innerWidth == 'undefined') {
    return(document.body.clientWidth);
  } else {
    return(window.innerWidth);
  }
}

  /* Used to get the usable height of the window in IE or Firefox
   * window.innerHeight is FF only, IE uses document.body.clientHeight */
UIU.getH = function() {
  if (typeof window.innerHeight == 'undefined') {
    return(document.body.clientHeight);
  } else {
    return(window.innerHeight);
  }
}

/* Servlets which are supposed to return .html fragments or .json
 * objects return a string staring with ERR on error.  This function
 * alerts the user when such an untoward eventuality eventuates and
 * returns false so that the caller knows not to incorporate the
 * string into the DOM. */
UIU.goodData = function(data) {
  if ((typeof data == "string") && (data.indexOf("ERR") == 0)) {
    UIU.failure(data.substring(3));
    return false;
  }
  return true;
};

/* Generate an error message if a given element ID is not found in the
 * document.  jQuery fails silently if it does not find an element on
 * which to operate.  If someone accidentally changes an element ID in
 * a .html file or in a Java Script string constant, this method
 * issues an error message during testing.  The console log call will
 * be replaced with an alert for testing and production. */
UIU.getElementOrWhinge = function(id) {
  var elem = document.getElementById(id);
  if (!elem) {
    console.log("No element with id " + id);
    return undefined;
  }
  return elem;
};

/* Assign an on-click function to all element IDs which are created by
 * combining a leading string with an element of a string array.  An
 * error message is generated if the element is not found, so this
 * method fails silently. */
UIU.assignClick = function(divID, strings, func) {
  var i, elem, eID;
  for (i in strings) {		// for returns valid subscripts
    eID = divID + strings[i];
    // console.log("crick " + eID);
    elem = UIU.getElementOrWhinge(eID);
    if (elem) {
      elem.onclick = func;
    }
  }
};

UIU.visibility = function(id, visible) {
  var elem = UIU.getElementOrWhinge(id);
  if (elem) {
    if (visible) { $(elem).show(); } else { $(elem).hide(); }
  }
};

UIU.visibilities = function(idS, visible) {
  var i;
  for (i=0; i<idS.length; i++) {
    UIU.visibility(idS[i], visible);
  }
};

/* Set a text to a value in a manner that will cause an error message
 * if the element is not found.*/
UIU.assignHTML = function(id, value) {
  if (!value) { value = ""; }
  var elem = UIU.getElementOrWhinge(id);
  if (elem) {
    elem.innerHTML = value;	// Get non-editable fields
    elem.value     = value;	// Get editable fields
  }
};

/* Set a source of an image to a value in a manner that will cause an
 * error message if the element is not found.*/
UIU.assignSrc = function(id, value) {
  var elem = UIU.getElementOrWhinge(id);
  if (elem) {
    elem.src = value;
  }
};

/* Set an attribute of a DOM element to a value */
UIU.setElementAttr = function(id, attr, value) {
  var elem = UIU.getElementOrWhinge(id);
  if (elem) {
    elem[attr] = value;
  }
  return elem;
};

/* Iterates over an array of objects, dumping the value of object
 * attribute b.*/
UIU.dumpElRayAttr = function(a, b) {
  var i;
  var el;
  // debugger;
  for (i=0; i<a.length; i++) {
    el = a[i];
    console.log(i + " " + el[b]);
  }
};

/* This does not seem to be able to dump all of the attributes of a
 * DOM object.  When dumping a button, for example, the click function
 * seems not to be accessible in this manner, although dumping a
 * button produces a LOT of output.  */
UIU.dumpObj = function(obj, indent) {
  if (!indent) { indent = ""; }
  var s; var elem;
  for (s in obj) {
    elem = obj[s];
    if (elem instanceof Object) {
      UIU.dumpObj(elem, indent + "  ");
    } else {
      console.log(indent + s + " " + elem);
    }
  }
};

/* The UI has a convention of using two different classes to indicate
 * mutually exclusive display states.  Only one class of such pairs
 * should be assigned to an element at a time.  This method takes a
 * class out and puts the other one in.  The element is presumed to
 * have been found in a manner that objects to its absence.  For
 * example, UIU.swapClasses(elem, "bgNotSelected",
 * "bgSelectedHeader");*/
UIU.swapClasses = function(element, inClass, outClass) {
  if (element) {
    $(element).removeClass(outClass).addClass(inClass);
  }
};

/* Show one part of a div and hide another part based on the div ID and
 * the IDs of the parts within it to be hidden and shown*/
UIU.hideShow = function(divID, toHide, toShow) {
  var elem = UIU.getElementOrWhinge(divID + toHide);
  if (elem) { $(elem).hide(); }
  elem = UIU.getElementOrWhinge(divID + toShow);
  if (elem) { $(elem).show(); }
};

/* Indicate that a button may not be pressed*/
UIU.cantSmite = function(btnID) {
  var elem = UIU.getElementOrWhinge(btnID);
  if (elem) {
    elem.disabled=true;
    $(elem).removeClass("btnBlue").addClass("btnGrey");
  }
};

/* Indicate that a button may be pressed*/
UIU.canSmite = function(btnID) {
  var elem = UIU.getElementOrWhinge(btnID);
  if (elem) {
    elem.disabled=false;
    $(elem).addClass("btnBlue").removeClass("btnGrey");
  }
};

/* Set a selectoin list to its first entry*/
UIU.resetSelectionList = function(elem) {
  if (!elem) { return; }
  elem.selectedIndex = 0;	// Set the selection to the first
};

UIU.resetSelectionListByID = function(id) {
  UIU.resetSelectionList(UIU.getElementOrWhinge(id));
};

UIU.resetManySelectionListIDs = function(IDs) {
  if (!IDs) { return; }
  for (var i = 0; i <IDs.length; i++) {
    UIU.resetSelectionListByID(IDs[i]);
  }
};

/* Delete all selections from a selection list.  This is used when
 * selection lists are populated from the server and it is necessary
 * to clear them.*/
UIU.clearSelectionList = function(elem) {
  if (!elem) { return; }
  elem.length = 0;		// Set the selection array to be emtpy.
};

/* Clear a selection list based on its element id */
UIU.clearSelectionListByID = function(iD) {
  UIU.clearSelectionList(UIU.getElementOrWhinge(iD));
};

/* Fill in a selection list if its ID can be found in the document.
 * Return true if there were elements in the selection list.*/
UIU.fillSelector = function(id, content) {
  var i, sel, elem = UIU.getElementOrWhinge(id);
  if (!elem) { return; }
  for (i in content) {
    sel = content[i];
    elem[i] = new Option(sel[1], sel[0], 0, 0);
  }
  elem.length = content.length; // Force array length
  return (content.length > 0);
}

/* The next section of the file deals with handling user input errors
 * and system errors.  The success message stays up until someone sets
 * focus on any input field or hits clear. */

UIU.setClearOnFocus = function() {
  $('*').focus(UIU.hideSuccessDiv);
};

UIU.somethingBad = false;
UIU.badString = "";

/* Require that a field have a value.  Turns deficient fields red.
 * TODO - require data of specific types and limits.*/
UIU.mustHaveValue = function (field, name) {
  if (!field.value || (field.value == "")) {
    UIU.somethingBad = true;
    field.style.background="red";
    UIU.badString = UIU.badString + "<li>" + name + "</li>";
    return false; // todo
  }
  return true;
};

/* Restore the original background to the fields. */
UIU.resetErrorFields = function(base, fields, color) {
  if (!color) { color = "white"; }
  var field, i, snam;
  for (i=0; i<fields.length; i++) {
    snam = fields[i];
    field = UIU.getElementOrWhinge(base + snam);
    if (field) {
      field.style.background=color;
    }
  }
};

/* Check required fields for having a value; return true if everything
 * is OK as far as this program is concerned.*/
UIU.fieldValueCheck = function(base, fields, names) {
  var i, field, snam;

  UIU.somethingBad = false;
  UIU.badString = "The following fields have errors or have not been filled in.&nbsp; Please correct the problems and re-submit the form.<ul>";
  for (i=0; i<fields.length; i++) {
    snam = fields[i];
    field = UIU.getElementOrWhinge(base + snam);
    if (field) {
      UIU.mustHaveValue(field, names[i]);
    }
  }

  if (UIU.somethingBad) {
    UIU.badString = UIU.badString  + "</ul>";
    var elem = UIU.getElementOrWhinge("formMsgBoxErrorContent");
    if (elem) {
      UIU.assignHTML("formMsgBoxErrorContent", UIU.badString);
      UIU.visibility("formMsgBoxError", true);
    } else {
      alert(UIU.badString);
    }
    return false;
  }
  return true;
};

/* Use the error box as if it were an alert box or just put up an
 * alert if the error DIV is not defined.*/
UIU.failure = function(data) {
  if (document.getElementById("formMsgBoxErrorContent")) {
    UIU.assignHTML("formMsgBoxErrorContent", data);
    UIU.visibility("formMsgBoxError", true);
  } else {
    alert(data);
  }
};

UIU.hideSuccessDiv = function() {
  UIU.visibility("formMsgBoxSuccess", false);
};

/* All pages have the same elements for failure and for success; hide
 * them both.*/
UIU.hideMessageDivs = function() {
  UIU.visibilities(["formMsgBoxError", "formMsgBoxSuccess"], false);
};

/* Proclaim success at something using the success DIV if it is
 * defined or an alert box if it is not defined. */
UIU.success = function(data) {
  if (!data) { data = "Operation completed successfully."; }
  if (document.getElementById("formMsgBoxSuccessContent")) {
    UIU.assignHTML("formMsgBoxSuccessContent", data);
    UIU.visibility("formMsgBoxSuccess", true);
    UIU.visibility("formMsgBoxError", false);
  } else {
    alert(data);
  }
};

/* Figure out whether a result string was an error or success, display
 * it accordingly, and signal the result.*/
UIU.processResultString = function(data) {
  if (data.indexOf("ERR") == 0) {
    UIU.failure(data.substring(3));
    return false;
  } else {
    UIU.success(data);
  }
  return true;
}

/* Pair used when adding or removing points from a point array */
UIU.X = 0;
UIU.Y = 1;

/* Used when adding or removing values of a rectangle */
UIU.LEFT   = 0; /* LEFT should be same as X */
UIU.TOP    = 1; /* TOP should be same as Y */
UIU.WIDTH  = 2;
UIU.HEIGHT = 3;

//EOF
