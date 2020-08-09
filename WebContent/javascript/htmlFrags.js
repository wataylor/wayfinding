/* Methods which return various DIV fragments.  Each fragment is
 * appended to the end of an element.  Each method returns the Div ID
 * if the element is found and returns both the div ID and the text of
 * the div in case it is needed when the element is not defined.

 * Some conventions:

 * Element IDs are of the form <desc><number>_ repeated as deep as it
 * has to get.  The number always ends with an _ to facilitate parsing
 * the IDs.

 * DIVs which are to have a number of elements inserted into them have
 * IDs which end with "content"

 * Elements whose innerHTML is to be set to label them have IDs which
 * end with "header" but when there is more than one such element in a
 * generated div, the names end with "info" and "infoAbout."

*/

function Frag() {};

/* One of these DIVs is inserted into the routeXX_content div for each
 * floor traversed by a route.  Many destinations may fall within one
 * of these DIVs so long as they are on the same floor.  Its header
 * and content change background when it is selected.  */

Frag.floorBox = function(route, floor, elem) {
  var divID = "route" + route.rteNo + '_Fl' + floor + '_';
  var frag =
  '<div id="' + divID + '" style="display:visible;">' +
  '<div id="' + divID + 'display" class="borderBottomBlack" ' +
  'style="display:visible;">' +
  '<div id="' + divID + 'header" ' +
  // Swap to border off.
  'class="mapInfoHeaderDivPad bgSelectedHeader mapInfoheaderBorderOn">' +
  '<h4  id="' + divID + 'description" ' +
  'class="marginL5px paddingT8px cursorHand">' +
  'Floor Box Title...' +
  '</h4>' +
  '</div>' +
  '<div id="' + divID + 'content" class="mapInfoDivPad bgSelectedBody">' +
  '</div>' +
  '</div>' +
  '</div>';
  if (elem) {
    $(elem).append(frag);
    return divID;
  }
  alert("No element for floor " + '[' + divID + ',' + frag + "]");
  return null;
};

Frag.vertIconImage  = "../images/icon-16_vertical_";
Frag.startIconImage = "../images/icon-16_startpoint.png";
Frag.otherIconImage = "../images/icon-16_destination_";
Frag.alarmIconImage = "../images/icon-16_alarm_";

/* A route can have only one start point, it is always numbered Dst0_
 * within the route */
Frag.startPoint = function(route, dest, elem, chgFunc, delFunc, selFunc) {
  var divID = "route" + route.rteNo + "_Dst0_";
  var frag =

  '<div id="' + divID + '">' +
  '<p class="floatRight paddingT5px">' +
  '<a id="' + divID + 'changeTrigger" href="javascript:void(0)">Change</a></p>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'clickable">' +
  '<div class="floatLeft cursorHand" style="width:24px;">' +
  '<img id="' + divID + 'icon"' +
  'src="../images/icon-24_startpoint.png" height="24" width="24" alt="" />' +
  '</div>' +
  '<div class="floatLeft paddingL5px paddingT4px cursorHand overflowHidden" ' +
  'style="width:260px;display:visible">' +
  '<strong>Start: </strong>' +
  '<span id="' + divID + 'name">Lorem ipsum dolor sit amet' +
  '</span>' +
  '</div>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'info" ' +
  'class="marginT5px marginL5px" style="display:visible;">' +
  '<p id="' + divID + 'infoAbout">Info about start point...</p>' +
  '</div>' +
  '</div>' +
  '<hr class="dashedRuleOn">' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, ["changeTrigger"], chgFunc);
    UIU.assignClick(divID, ["clickable"],     selFunc);
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

Frag.alphabet = "SABCDEFGHJKLMNOPQRSTUVWXYZ";

/* A route can have any number of destinatins, each destination is
 * numbered Dstxx_ within the route starting with 1.  The start point
 * is always Dst0_.  */
Frag.destPoint = function(route, dest, elem, chgFunc, delFunc,selFunc,outFunc) {
  var divID = "route" + route.rteNo + "_Dst" + dest + "_";
  var frag =

  '<div id="' + divID + '">' +
  '<div class="floatRight paddingL5px paddingT5px">' +
  '<img id="' + divID + 'delete"' +
  'src="../images/icon_delete.png" height="16" width="16" class="cursorHand"' +
  'alt="Delete Destination Icon" title="Delete Destination" onclick="" />' +
  '</div>' +
  '<p class="floatRight paddingT5px">' +
  '&nbsp;<a id="' + divID + 'changeTrigger" href="javascript:void(0)">Change</a> | </p>' +
  '<p class="floatRight paddingT5px">' +
  '<a id="' + divID + 'bail" href="javascript:void(0)">Exit From Here</a> | </p>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'clickable">' +
  '<div class="floatLeft cursorHand" style="width:24px;">' +
  '<img id="' + divID + 'icon"' +
  'src="../images/icon-24_destination_' + Frag.alphabet.substring(dest, 1+dest) + '.png" height="24" width="24" alt="" />' +
  '</div>' +
  '<div ' +
  'class="floatLeft paddingL5px paddingT4px cursorHand overflowHidden" ' +
  'style="width:260px; display:visible">' +
  '<strong>Destination: </strong>' +
  '<span  id="' + divID + 'name">Loc Name</span>' +
  '</div>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'info" class="marginT5px marginL5px" ' +
  'style="display:visible;">' +
  '<p id="' + divID + 'infoAbout">Info about destination...</p>' +
  '</div>' +
  '</div>' +
  '<hr class="dashedRuleOn">' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, ["changeTrigger"], chgFunc);
    UIU.assignClick(divID, ["delete"],        delFunc);
    UIU.assignClick(divID, ["clickable"],     selFunc);
    UIU.assignClick(divID, ["bail"],          outFunc);
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

/* Any number of vertical points are inserted to make the transitions
 * from one floor to another.  They act like destinations, but are
 * auto-generated.  Their numbers are assigned from one for a
 * route. vert numbers in a destination start with one.*/
Frag.vertPoint = function(route, dest, vert, elem, selFunc) {
  var divID = "route" + route.rteNo + "_Dst" + dest + "_Ve" + vert + "_";
  var vv = ((vert - 1) >> 1)  + 1;
  var frag =
  '<div id="' + divID + '" style="display:visible;">' +
  '<div id="' + divID + 'clickable" class="cursorHand">' +
  '<div class="floatLeft cursorHand" style="width:24px;">' +
  '<img id="' + divID + 'icon" ' +
  'src="../images/icon-24_vertical_' + vv +
  '.png" height="24" width="24" alt="" />' +
  '</div>' +
  '<div class="floatLeft paddingL5px paddingT4px cursorHand overflowHidden" ' +
  'style="width:260px;display:visible">' +
  '<strong id="' + divID + 'eox">Enter: </strong>' +
  '<span id="' + divID + 'name">Loc Name</span>' +
  '</div>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'info" ' +
  'class="marginT5px marginLNeg12px" style="display:visible;">' +
  '<ol id="' + divID + 'list">' +
  '<li>instructions</li>' +
  '</ol>' +
  '</div>' +
  '</div>' +
  '<hr class="dashedRuleOn">' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, [""], selFunc); // Whole thing is clickable
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

/* Note that a thumbnail gets the same base element ID as a vertical,
 * but it does not define any elements with that ID, only that ID + a
 * string.  This means that there will be no conflict in assigning
 * IDs.*/
Frag.mapThumb = function(route, dest, thno, elem, selFunc) {
  var divID = "route" + route.rteNo + "_Dst" + dest + "_Th" + thno + "_";
  var frag =
  '<div id="' + divID + '" class="alignCenter cursorHand">' +
  '<div id="' + divID + 'thumb" class="alignCenter"' +
  'style="display:visible;">' +
  '<img id="' + divID + 'thumbImg" src="../images/thumbnail_map.png"' +
  'class="borderBlack mapThumbMargin" width="120" height="80" alt="" />' +
  '</div>' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, [""],      selFunc);
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

Frag.changeForm = function(route, elem, hlpFunc, canFunc, finFunc) {
//  var divID = "route" + route.rteNo + "_Dst" + dest + "_";
  var divID = "route" + route.rteNo + "_Chg0_";
//  var what = ((dest == 0) ? "Start Point" : "Destination");
  var frag =
  '<div id="' + divID + '">' +
  '<div id="' + divID + 'ChngFormBox" ' +
  'class="chngStartPtBox" style="display:visible;">' +
  '<form id="' + divID + 'ChngDestForm"' +
  'name="' + divID + 'chngForm">' +
  '<h4 class="floatLeft">Change <span Id="' + divID + 'what"></span></h4>' +
  '<div class="floatRight marginL4px">' +
  '<img id="' + divID + 'chngHelp" ' +
  'src="../images/icon_help.png" width="16" height="16" ' +
  'class="cursorHand" title="Help..."/>' +
  '</div>' +
  '<br class="clearFloat" />' +
  '<div id="' + divID + 'chngFldOpts" class="alignLeft" >' +
  '<select id="' + divID + 'SrchOptsMenu"' +
  'name="' + divID + 'SrchOptsMenu" class="width99">' +
  '<option value="all" selected>Find <span Id="' + divID + 'what2"></span> By...</option>' +
  '<option value="roomD">Room Name</option>' +
  '<option value="personD">Person</option>' +
  '<option value="deviceD">Alarm</option>' +
  '<option value="floorD">Floor Name</option>' +
  '<option value="locationD">Point of Interest</option>' +
  '<option value="organizationD">Company</option>' +
  '</select>' +
  '<input id="' + divID + 'SrchInputField" ' +
  'name="' + divID + 'SrchInputField" type="text" class="width98" />' +
  '</div>' +
  '<div id="' + divID + 'Suggestions"><ul></ul></div>' +
  '<div class="alignCenter">' +
  '<button type="reset" id="' + divID + 'ChngCancelBtn" ' +
  'name="' + divID + 'chngCancelBtn" ' +
  'onClick="" class="btnRed cursorHand">Cancel</button>' +
  '<button type="reset" id="' + divID + 'ChngFindBtn" ' +
  'name="' + divID + 'chngFindBtn" onClick="" ' +
  'class="btnBlue cursorHand">Find</button>' +
  '</div>' +
  '</form>' +
  '</div>' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, ["chngHelp"],      hlpFunc);
    UIU.assignClick(divID, ["ChngCancelBtn"], canFunc);
    UIU.assignClick(divID, ["ChngFindBtn"],   finFunc);
    elem = UIU.getElementOrWhinge(divID + "ChngFindBtn");
    if (elem) {
      elem.disabled = true;
      UIU.swapClasses(elem, "btnGrey", "btnBlue");
    }
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

Frag.contactNumber = 0;
/* For resons obscure, wrapping the contact information blocks in DIVs
 * makes them string out horizontally instead of stacking them
 * vertically.*/
Frag.buildingContact = function(elem, selFunc) {
  var divID =  "contact" + Frag.contactNumber + "_";
  Frag.contactNumber++;		// Lint dislikes this in the string
  var frag =
  // '<div   id="' + divID + '" class="floatLeft">' +
  '<label id="' + divID + 'title" class="floatLeft">Facility/Building Manager:</label>' +
  '<p class="floatLeft marginT3px">' +
  '<span id="' + divID + 'firstName"></span>' +
  '<span id="' + divID + 'lastName"></span> | ' +
  '<span id="' + divID + 'workPhone"></span> | ' +
  '<a    id="' + divID + 'select">' +
  '<span id="' + divID + 'emailAddress"></span></a>' +
  '</p>' +
  '<hr class="clearFloat" />';
  //'</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, ["select"], selFunc);
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};

Frag.locationImage = function(elem, no, delFunc) {
  var divID = "imPh" + no + '_';
  var frag =

  '<div id="' + divID + '" class="margin8px">' +
  '<div class="floatLeft marginR5px">' +
  '<img id="' + divID + 'thumb" src="../images/thumbnail.png"' +
  'width="100" height="100" class="borderGrey" />' +
  '</div>' +
  '<div class="floatLeft">' +
  '<p><strong id="' + divID + 'caption">caption</strong></p>' +
  '<p><em id="' + divID + 'filename">filename</em></p>' +
  '</div>' +
  '<div class="floatRight paddingT40px">' +
  '<img id="' + divID + 'delete" src="../images/icon_delete.png"' +
  'width="16" height="16"' +
  'class="cursorHand" onmouseover="Tip(tip_delete)" onmouseout="UnTip()" />' +
  '</div>' +
  '<div class="clearFloat"></div>' +
  '</div>';

  if (elem) {
    $(elem).append(frag);
    UIU.assignClick(divID, ["delete"], delFunc);
    return divID;
  }
  return eval('[' + divID + ',' + frag + "]");
};
