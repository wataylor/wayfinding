/********************************************************
 The AutoSuggest class binds to a text input field
 and creates an automatic suggestion dropdown in the style
 of the "IntelliSense" and "AutoComplete" features of some
 desktop apps.

 Parameters:
 elem: A DOM element for an INPUT TYPE="text" form field

 sugDiv: a DOM element where the suggestions may be put.  It is moved
 so as to be directly under the input field element.

formID: DOM ID of the .html form which includes any extra parameters
of the suggestion search.

btnID: DOM ID of a button to be enabled when the input field has a
complete entry.

*********************************************************/
function AutoSuggest(elem, sugDiv, formID, btnID) {

  if (!elem) {
    alert("No element specified");
    return;
  }
  // We need to be able to reference the elem by id. If it doesn't have an id, set one.
  if (!elem.id) {
    alert("element has no ID, suggestions cannot work.");
    return;
  }
  if (!sugDiv) {
    alert("No suggestion division specified");
    return;
  }

  elem.focus();

  // The 'me' variable allow you to access the AutoSuggest object
  // from the elem's event handlers defined below.
  var me = this;

  // A reference to the element we're binding the list to.
  this.elem = elem;

  // If not null, the ID for a form which includes extra search
  // control parameters.
  this.formID = formID;
  this.btnID  = btnID;
  this.suggestions = null;

  this.autocompleted = false;

  // Array to store a subset of eligible suggestions that match the user's input
  this.eligible = new Array();

  // The text input by the user.
  this.inputText = null;

  // A pointer to the index of the highlighted eligible item.
  // -1 means nothing highlighted.
  this.highlighted = -1;

  // The element in the eligible array that was put in the input text
  // box.  An element must be put in the box in order to be sent to
  // the server.  The Java Script code refuses to send data to the
  // server unless it matches one and only one eligible item.
  this.inserted = -1;

  // A div to use to create the dropdown.
  this.div = sugDiv;
  //me.disable();			// Flag the input button as disabled

  // Key codes to which the program is sensitive
  var BACK  =  8;
  var TAB   =  9;
  var ENTER = 13;
  var ESC   = 27;
  var KEYUP = 38;		// up arrow
  var KEYDN = 40;		// down arrow

  // The browsers' own autocomplete feature can be problematic, since
  // it will be making suggestions from the users' past input.
  // Setting this attribute should turn it off.
  elem.setAttribute("autocomplete","off");

  /********************************************************

  onkeydown event handler for the input elem.  Tab key = use the
  highlighted suggestion, if there is one.  Esc key = get rid of the
  autosuggest dropdown Up/down arrows = Move the highlight up and down
  in the suggestions.

  A separate instance of the function has to be created each time so
  that the callback can access variables such as me.  This is
  equivalent to instantiating a key handler class and passing the
  caller as an instance variable.

  ********************************************************/

  elem.onkeydown = function(ev) {

    var key = me.getKeyCode(ev);
    // Matched, ignore input
    var elem = document.getElementById(me.btnID);
    if (!elem.disabled && (key != BACK) && (key != ENTER)) { return false; }

    switch(key) {
    case ENTER:
      me.findSuggestion(); // Copy the suggestion into the input field use it
      return false;
      break;
    case TAB:
      me.useSuggestion();	// Copy the suggestion into the input field
      break;

    case ESC:
      me.hideDiv();
      break;

    case KEYUP:
      if (me.highlighted > 0) {
	me.highlighted--;
      }
      me.changeHighlight(key);
      break;

    case KEYDN:
      if (me.highlighted < (me.eligible.length - 1)) {
	me.highlighted++;
      }
      me.changeHighlight(key);
      break;
    }
  };

  /********************************************************
	onkeyup handler for the elem
	If the text is of sufficient length, and has been changed,
	then display a list of eligible suggestions.
  ********************************************************/

  elem.onkeyup = function(ev) {
    var key = me.getKeyCode(ev);

    // Matched, ignore input

    var elem = document.getElementById(me.btnID);
    if (!elem.disabled && (key != BACK)) { return false; }

    switch(key) {
      // The control keys were already handled by onkeydown, so do nothing.
    case ENTER:
      return false;
      break;
    case TAB:
    case ESC:
    case KEYUP:
    case KEYDN:
      return false;
    default:			// normal key to trim suggestion list

      if (this.value != me.inputText && this.value.length > 0) {
	me.inputText = this.value;
	me.getEligible();	// Goes to server to get suggestions for match
	me.showEligible();	// Display the eligible suggestions
      } else {
	me.hideDiv();
	me.disable();
      }
    }
  };

  this.disable = function() {
    if (me.btnID) {
      var elem = document.getElementById(me.btnID);
      elem.disabled=true;
      $(elem).removeClass("btnBlue").addClass("btnGrey");
    }
  };

  this.enable = function() {
    if (me.btnID) {
      var elem = document.getElementById(me.btnID);
      elem.disabled=false;
      $(elem).addClass("btnBlue").removeClass("btnGrey");
    }
  };

  this.showEligible = function () {
    me.createDivContent(); // Create a UL with one LI per eligible suggestion
    me.positionDiv();	// Determine absolute position of text field
    me.highlighted = 0;
    me.changeHighlight();	// Use the first one as the guess
    // debugger;
    if (me.eligible.length == 1) { // If only one match, force it
      me.useSuggestion();	   // Enables the button
      me.hideDiv();
    } else {
      me.disable();
      me.inserted = -1;
      me.showDiv();		// Set display to "block"
    }
  };

  this.showError = function () {
    me.createDivContent(); // Create a UL with one LI per eligible suggestion
    me.positionDiv();	// Determine absolute position of text field
    me.highlighted = -1;
    me.changeHighlight();	// Use the first one as the guess
    me.disable();
    me.inserted = -1;
    me.showDiv();		// Set display to "block"
  };

  /********************************************************
	Insert the highlighted suggestion into the input box, and
	remove the suggestion dropdown.  Then call the method that should
        get called when $(button).click() is called.  Why $(button).click()
        does not work would be very interseting to understand.
  ********************************************************/
  this.findSuggestion = function() {
    if (this.highlighted > -1) {
      this.elem.value = this.eligible[this.highlighted];
      this.inserted   = this.highlighted;
    }
    if (this.highlighted > -1 || this.autocompleted) {
      this.hideDiv();
      this.enable();
      var button = document.getElementById(me.btnID);
      Route.findAddDestination.call(button);
    }
  };

  /********************************************************
	Insert the highlighted suggestion into the input box, and
	remove the suggestion dropdown.
  ********************************************************/
  this.useSuggestion = function() {
    if (this.highlighted > -1) {
      this.autocompleted = true;
      this.elem.value = this.eligible[this.highlighted];
      this.inserted   = this.highlighted;
      this.hideDiv();
      this.enable();
      //It's impossible to cancel the Tab key's default behavior.
      //So this undoes it by moving the focus back to our field right after
      //the event completes.
      setTimeout("document.getElementById('" + this.elem.id + "').focus()",0);
    }
  };

  /********************************************************
	Display the dropdown. Pretty straightforward.
  ********************************************************/
  this.showDiv = function() {
    this.div.style.display = 'block';
  };

  /********************************************************
	Hide the dropdown and clear any highlight.
  ********************************************************/
  this.hideDiv = function() {
    this.div.style.display = 'none';
    this.highlighted = -1;
  };

  /********************************************************
	Modify the HTML in the dropdown to move the highlight.
  ********************************************************/
  this.changeHighlight = function() {
    var lis = this.div.getElementsByTagName('LI');
    var li;
    var i;
    // debugger;
    for (i=0; i<lis.length; i++) {
     li = lis[i];

      if (this.highlighted == i) {
	li.className = "selected";
      } else {
	li.className = "";
      }
    }
  };

  /********************************************************
	Position the dropdown div below the input text field.
  ********************************************************/
  this.positionDiv = function() {
    var el = this.elem;
    var eH = el.offsetHeight;
    var x  = $(el).position().left;
    var y  = $(el).position().top+eH;
    var wH = UIU.getH();
    var dH = this.div.offsetHeight;
    //console.log("el.id:"+el.id);
    //console.log("eH:"+eH+" x:"+x+" y:"+y+" wH:"+wH+" dH:"+dH);
    if ((y+dH) > wH) {
      y  = $(el).position().top-dH;
      //console.log("y:"+y);
    }
    this.div.style.left = x + 'px';
    this.div.style.top  = y + 'px';
  };

  /********************************************************
	Build the HTML for the dropdown div
  ********************************************************/
  this.createDivContent = function() {
    var ul = document.createElement('ul');

    //Create an array of LI's for the words.
    for (i in this.eligible) {
      var word = this.eligible[i];

      var li = document.createElement('li');
      var a  = document.createElement('a');
      a.href="javascript:false";
      a.innerHTML = word;
      li.appendChild(a);

      if (me.highlighted == i) {
	li.className = "selected";
      }
      ul.appendChild(li);
    }

    this.div.replaceChild(ul,this.div.childNodes[0]);

    /********************************************************
		mouseover handler for the dropdown ul
		move the highlighted suggestion with the mouse
    ********************************************************/
    ul.onmouseover = function(ev) {
      //Walk up from target until you find the LI.
      var target = me.getEventSource(ev);
      while (target.parentNode && target.tagName.toUpperCase() != 'LI') {
	target = target.parentNode;
      }

      var lis = me.div.getElementsByTagName('LI');

      for (i in lis) {
	var li = lis[i];
	if (li == target) {
	  me.highlighted = i;
	  break;
	}
      }
      me.changeHighlight();
    };

    /********************************************************
		click handler for the dropdown ul
		insert the clicked suggestion into the input
    ********************************************************/
    ul.onclick = function(ev) {
      me.useSuggestion();
      me.hideDiv();
      me.cancelEvent(ev);
      return false;
    };
    this.div.className = "suggestion_list";
    this.div.style.position = 'absolute';
  };

  /********************************************************
    find all of the suggestions which match the input
  ********************************************************/
  this.getEligible = function() {
    // Blank the suggestions in case something goes wrong with the server
    this.eligible    = new Array();
    this.suggestions = undefined;
    // Pass this object to the callback functino
    var suggesr      = this;
    //console.log(this.formID + " >" + this.inputText + "< " +
    //	$(this.formID).serialize());

    /* JQuery has a gross inadequacy - if the URL has a trailing
     * space, the space is dropped.  That is why the verb is sent
     * AFTER the input string - the verb protects any spaces at the
     * end of the input string, causing them to be encoded properly.*/
    $.getJSON("../json.jsp?verb=3",
	      $('#' + this.formID).serialize(),
	      function(data) { // 2nd arg is status, success is good
		// debugger;
		// console.log(this);    // get object
		// console.log(datum);
		suggesr.autocompleted = false;
		if (data['msg'])  {
		  $(suggesr.elem).addClass("input_error");
		  suggesr.eligible[suggesr.eligible.length] = data['msg'];
		  suggesr.showError();
		} else {
		  $(suggesr.elem).removeClass("input_error");
		  suggesr.suggestions = data;
		  for (i in data) {
		    var suggestion = data[i];
		    suggesr.eligible[suggesr.eligible.length] =
		      suggestion.sug;
		  }
		  suggesr.showEligible();
		}
	      });

  };

  /********************************************************
	Helper function to determine the keycode pressed in a
	browser-independent manner.
  ********************************************************/
  this.getKeyCode = function(ev) {
    if(ev) {			//Moz
      return ev.keyCode;
    }
    if (window.event) {	//IE
      return window.event.keyCode;
    }
  };

  /********************************************************
	Helper function to determine the event source element in a
	browser-independent manner.
  ********************************************************/
  this.getEventSource = function(ev) {
    if (ev) {			//Moz
      return ev.target;
    }

    if (window.event) {	//IE
      return window.event.srcElement;
    }
  };

  /********************************************************
	Helper function to cancel an event in a
	browser-independent manner.
	(Returning false helps too).
  ********************************************************/
  this.cancelEvent = function(ev) {
    if (ev) {			//Moz
      ev.preventDefault();
      ev.stopPropagation();
    }
    if (window.event) {	//IE
      window.event.returnValue = false;
    }
  }
};

AutoSuggest.prototype.finalize = function() {
  this.div            = null;
  this.elem.onkeydown = null;
  this.elem.onkeyup   = null;
  this.elem.value     = "";
  this.elem           = null;
  this.inputText      = null;
  this.me             = null;
  this.suggestions    = null;
};
