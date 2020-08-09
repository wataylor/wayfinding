/*******************************************************

AutoSuggest - a javascript automatic text input completion component
Copyright (C) 2005 Joe Kepley, The Sling & Rock Design Group, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*******************************************************

Please send any useful modifications or improvements via
email to joekepley at yahoo (dot) com

*******************************************************/

/********************************************************
 The AutoSuggest class binds to a text input field
 and creates an automatic suggestion dropdown in the style
 of the "IntelliSense" and "AutoComplete" features of some
 desktop apps.
 Parameters:
 elem: A DOM element for an INPUT TYPE="text" form field
 suggestions: an array of strings to be used as suggestions
              when someone's typing.

 Example usage:

 Please enter the name of a fruit.
 <input type="text" id="fruit" name="fruit" />
 <script language="Javascript">
 var fruits=new Array("apple","orange","grape","kiwi","cumquat","banana");
 new AutoSuggest(document.getElementById("fruit",fruits));
 </script>

 Requirements:

 Unfortunately the AutoSuggest class doesn't seem to work
 well with dynamically-created DIVs. So, somewhere in your
 HTML, you'll need to add this:
 <div id="autosuggest"><ul></ul></div>

 Here's a default set of style rules that you'll also want to
 add to your CSS:

 .suggestion_list
 {
 background: white;
 border: 1px solid;
 padding: 4px;
 }

 .suggestion_list ul
 {
 padding: 0;
 margin: 0;
 list-style-type: none;
 }

 .suggestion_list a
 {
 text-decoration: none;
 color: navy;
 }

 .suggestion_list .selected
 {
 background: navy;
 color: white;
 }

 .suggestion_list .selected a
 {
 color: white;
 }

 #autosuggest
 {
 display: none;
 }
*********************************************************/
function AutoSuggest(elem, sugDiv, suggestions) {

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

  // The 'me' variable allow you to access the AutoSuggest object
  // from the elem's event handlers defined below.
  var me = this;

  // A reference to the element we're binding the list to.
  this.elem = elem;

  this.suggestions = suggestions;

  // Arrow to store a subset of eligible suggestions that match the user's input
  this.eligible = new Array();

  // The text input by the user.
  this.inputText = null;

  // A pointer to the index of the highlighted eligible item. -1 means nothing highlighted.
  this.highlighted = -1;

  // The element in the eligible array that was put in the input text
  // box.  An element must be put in the box in order to be sent to
  // the server.  The Java Script code refuses to send data to the
  // server unless it matches one and only one eligible item.
  this.inserted = -1;

  // A div to use to create the dropdown.
  this.div = sugDiv;

  // Do you want to remember what keycode means what? Me neither.
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

    switch(key) {
    case ENTER:
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
    switch(key) {
      //The control keys were already handled by onkeydown, so do nothing.
    case ENTER:
    case TAB:
    case ESC:
    case KEYUP:
    case KEYDN:
      return;
    default:

      if (this.value != me.inputText && this.value.length > 0) {
	me.inputText = this.value;
	me.getEligible();	// Search suggestions for a match
	me.createDivContent();		// Create a UL with one LI per eligible suggestion
	me.positionDiv();	// Determine absolute position of text field
	me.highlighted = 0;
	me.changeHighlight();	// Use the first one as the guess
	// debugger;
        if (me.eligible.length == 1) { // If only one match, force it
	  me.useSuggestion();
	  me.hideDiv();
	} else {
	  me.showDiv();		// Set display to "block"
	}
      } else {
	me.hideDiv();
      }
    }
  };

  /********************************************************
	Insert the highlighted suggestion into the input box, and
	remove the suggestion dropdown.
  ********************************************************/
  this.useSuggestion = function() {
    if (this.highlighted > -1) {
      this.elem.value = this.eligible[this.highlighted];
      this.inserted   = this.highlighted;
      this.hideDiv();
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
    var x = 0;
    var y = el.offsetHeight;

    //Walk up the DOM and add up all of the offset positions.
    while (el.offsetParent && el.tagName.toUpperCase() != 'BODY') {
      x += el.offsetLeft;
      y += el.offsetTop;
      el = el.offsetParent;
    }

    x += el.offsetLeft;
    y += el.offsetTop;

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

    this.div.className="suggestion_list";
    this.div.style.position = 'absolute';

  };

  /********************************************************
    find all of the suggestions which match the input
  ********************************************************/
  this.getEligible = function() {
    this.eligible = new Array();
    for (i in this.suggestions) {
      var suggestion = this.suggestions[i];

      if (suggestion.toLowerCase().indexOf(this.inputText.toLowerCase()) == "0") {
	this.eligible[this.eligible.length]=suggestion;
      }
    }
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
}

AutoSuggest.prototype.finalize = function() {
  this.div            = null;
  this.elem.onkeydown = null;
  this.elem.onkeyup   = null;
  this.elem           = null;
  this.inputText      = null;
  this.me             = null;
  this.suggestions    = null;
};

