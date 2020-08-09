<%@include file='login_code.inc'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/xhtml1-loose.dtd">
<html>
<head>
<!-- START META TAGS -->
<meta name="author" content="Zetek Corporation" />
<meta name="copyright" content="2008 Zetek Corporation (All Rights Reserved)" />
<meta name="robots" content="noindex" />
<meta name="robots" content="nofollow" />
<meta name="robots" content="noodp" />
<!-- END META TAGS -->
<!-- START STYLESHEETS -->
<link rel="stylesheet" type="text/css" href="../css/importer_admin.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<!-- END STYLESHEETS -->
<!-- START JAVASCRIPT -->
<script type="text/javascript" src="../javascript/jquery-1.2.6.js"></script>
<script type="text/javascript" src="../javascript/jquery.popupwindow.js"></script>
<script type="text/javascript" src="../javascript/jquery.boxy.js"></script>
<script type="text/javascript" src="../javascript/niftycube.js"></script>
<script type="text/javascript" src="../javascript/utilityscripts.js"></script>
<script type="text/javascript" src="../javascript/general_admin.js"></script>
<script type="text/javascript" src="../javascript/htmlFrags.js"></script>
<script type="text/javascript" src="../javascript/edifice_object.js"></script>
<script type="text/javascript">
<!-- LOOSE JS HERE -->
</script>
<!-- END JAVASCRIPT -->
<title>User Login - Zetek's Wayfinding System</title>
</head>
<body class="adminMode">
<!-- TOOLTIP JAVASCRIPT MUST STAY HERE - DO NOT MOVE! -->
<script type="text/javascript" src="../javascript/wz_tooltip.js"></script>
<!-- TOOLTIP JAVASCRIPT MUST STAY HERE - DO NOT MOVE! -->
<!-- START OUTER CONTAINER -->
<div id="way_outerContainer">
<!-- START MASTHEAD CONTAINER -->
<script type="text/javascript" src="../javascript/include_masthead.js"></script>
<!-- END MASTHEAD CONTAINER -->

<!-- START CONTENT CONTAINER -->
<div id="way_loginContentContainer" class="bgWhite width50">
<!-- START LOGIN FORM CONTAINER -->
<div id="way_loginFormContainer" class="bgWhite">

<!--  ****REMOVE ONCE LOGIN IS CODED****  -->
<p class="alignCenter"><a href="buildingInfo.jsp" title="Use this to enter Admin until Login is coded">ENTER ADMIN</a></p>
<!--  ****REMOVE ONCE LOGIN IS CODED****  -->

<!-- START GENERAL INFO FORM -->
<form id="way_loginForm" name="way_loginForm">
<!-- START LOGIN FEILD SET -->

<fieldset>
<legend>Wayfinding System Login <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_login)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width30">Username:</label><input type="text" id="way_username" name="way_username" class="floatLeft marginB5px width40" />
<br class="clearFloat" />
<label class="floatLeft width30">Password:</label><input type="text" id="way_password" name="way_password" class="floatLeft marginB5px width40" />
<br class="clearFloat" />
<p class="alignCenter fontSize07Em"><a href="javascript:void(0)">Forgot Your Password?</a></p>
</fieldset>
<!-- END LOGIN FEILD SET -->
<!-- START BUTTON CONTAINER -->
<div id="formBtnContainer" class="alignCenter marginTB5px">
<button type="reset" id="clearBtn" name="clearBtn" class="btnRed cursorHand">Clear</button>
<button type="submit" id="loginBtn" name="loginBtn" class="btnBlue cursorHand">Login</button>
</div>
<!-- END BUTTON CONTAINER -->

</div>
<!-- END LOGIN FORM CONTAINER -->

</div>
<!-- END CONTENT CONTAINER -->
<!-- START LOGIN FOOTER CONTAINER -->
<%@include file='footer.inc'%>
<!-- END LOGIN FOOTER CONTAINER -->
</div>
<!-- END OUTER CONTAINER -->
</body>
</html>
<!-- EOF -->