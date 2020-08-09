/* Initailize the specified form to upload the files using an AJAX
 * call rather than a form submit.  Have to create a fake object to
 * hold static methods first.  */

function AJUP() { };

/* Method which is passed the element ID of a form.  It changes the
 * submit action of the form so that the form will be submitted to an
 * iFrame and passes the body content of the return page to the
 * callback function. */
AJUP.frameSub = function(formID, cbFun) {
  /* Get a reference to the form which will be submitted via the
   * iFrame.*/
    var jForm = $("#" + formID);
    var cbFunc = cbFun;
    /* Attach an event to the submit method. Instead of submitting the
     * actual form to the primary page, the form is submitted to a
     * hidden iFrame that is created dynamically and then destroyed
     * immediately after use. */
    jForm.submit(
      function(objEvent) {
	var jThis = $(this);	// This refers to the form

	/* Create a unique name for the iFrame by using the tick count
	 * from the date. */
	var strName = ("upr" + (new Date()).getTime());

	/* Create a named iFrame that does not point to any page - the
	 * address "about:blank" makes this happen. */

	var jFrame = $("<iframe name=\"" + strName +
		       "\" src=\"about:blank\" />");

	/* The iFrame is not attached to the document.  Make sure it
	 * will not be seen when it is attached. */
	jFrame.css("display", "none");

	/* The form is being submitted to the iFrame.  Set up an event
	 * listener for the LOAD event of the iFrame to be able to get
	 * the results of the form submission.  */
	jFrame.load(
	  function(objEvent){
	    /* Get a reference to the body tag of the loaded iFrame.
	     * This element contains the return string. */
	    var objUploadBody = window.frames[ strName ].document.getElementsByTagName("body")[ 0 ];

	    /* Get a jQuery object of the body to get better access to
	     * it. */
	    var jBody = $(objUploadBody);
	    var jhtml = jBody.html();

	    if (cbFunc) { cbFunc(jhtml); }

	    /* Remove the iFrame from the document.  Put a small delay
	     * on the frame removal because FireFox has issues with
	     * "Infinite thinking." */
	    setTimeout( function() { jFrame.remove(); }, 100 );
	  }
	 );

	// Attach the iFrame to the body of the current document.
	$("body:first").append(jFrame);

	/* Now that the iFrame is in place, hook up the frame to post
	 * to the iFrame.  */
	jThis
	  .attr("action",   "../fupload.jsp")
	  .attr("method",   "post")
	  .attr("enctype",  "multipart/form-data")
	  .attr("encoding", "multipart/form-data")
	  .attr("target",   strName);
      }
      );
};
