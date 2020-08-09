<%@ page import="java.util.Enumeration,
java.util.Vector,
java.util.Properties,
java.lang.Exception" %>
<html>
<head>
<title>JSP Tables</title>
<!-- paramCheck.jsp

   Copyright (c) 2002-2008 Zetek Corporation (All Rights Reserved)

-------- Licensed Software Proprietary Information Notice -------------

This software is a working embodiment of certain trade secrets of
Zetek Corporation.  The software is licensed only for the
day-to-day business use of the licensee.  Use of this software for
reverse engineering, decompilation, use as a guide for the design of a
competitive product, or any other use not for day-to-day business use
is strictly prohibited.

All screens and their formats, color combinations, layouts, and
organization are proprietary to and copyrighted by Zetek Corporation.

All rights are reserved.

Authorized Zetek customer use of this software is subject to the
terms and conditions of the software license executed between Customer
and Zetek Corporation.

------------------------------------------------------------------------

--><%--
JSP script to look at input parameters and display them.  This helps
programmers determine exactly what happens with each different type of
input device.

It appears that when check boxes are not checked, their names do not
appear in getParameterNames().  Text Boxes and Areas, in contrast,
contain "" if the value has not been specified.

   Under revision by: $Locker:  $
   Change Log:
   $Log: $
--%>
<style type="text/css">
</style>

<SCRIPT type="text/javascript">
<!-- Hide from browsers that do not understand JavaScript

var OpenWindow

function commifyArray(obj) {
  var s="";
  if(obj==null||obj.length<=0){return s;}
  for(var i=0;i<obj.length;i++){
    s=s+((s=="")?"":",")+obj[i].toString();
  }
  return s;
}

function getSingleInputValue(obj,use_default) {
  switch(obj.type){
  case 'radio': case 'checkbox':
    return(((use_default)?obj.defaultChecked:obj.checked)?obj.value:null);
  case 'text': case 'hidden': case 'textarea':
    return(use_default)?obj.defaultValue:obj.value;
  case 'select-one':
    if(use_default){
      var o=obj.options;
      for(var i=0;i<o.length;i++){if(o[i].defaultSelected){return o[i].value;}}
      return null;
    }
    return(obj.options.length>0)?obj.options[obj.selectedIndex].value:null;
  case 'select-multiple':
    var values=new Array();
    for(var i=0;i<obj.options.length;i++) {
      if((use_default&&obj.options[i].defaultSelected)||(!use_default&&obj.options[i].selected)) {
	values[values.length]=obj.options[i].value;
      }
    }
    return (values.length==0)?null:commifyArray(values);
  }
  alert("FATAL ERROR: Field type "+obj.type+" is not supported for this function");
  return null;
}

function clickedCheck(obj) {
alert ("You clicked the checkbox " + obj.name + " and its status is " + obj.checked + " and its value is " + obj.value);
}

function whoWhat() {
var content = "<h1 align=center>JavaScript Data Info</h1>";

var whichIdxStr    = "<p>Index of selector &quot;which&quot;: " + document.testForm.which.selectedIndex;
var whichValStr    = "<br>Defined value call of selector &quot;which&quot;: " + document.testForm.which.value;
var whichValIdxStr = "<br>Defined value/index deduce of selector &quot;which&quot;: " + document.testForm.which.options[document.testForm.which.selectedIndex].value;
var autoIdxStr     = "<p>Index of selector &quot;automatic&quot;: " + document.testForm.automatic.selectedIndex;
var autoValStr     = "<br>Defined value call of selector &quot;automatic&quot;: " + document.testForm.automatic.value;
var autoValIdxStr  = "<br>Defined value/index deduce of selector &quot;automatic&quot;: " + document.testForm.automatic.options[document.testForm.automatic.selectedIndex].value;

var multiValStr = "<p>Multi-Value select: " + getSingleInputValue(document.testForm.multi,false);

var listValStr     = "<p>Contents of TextBox &quot;list&quot;: " + document.testForm.list.value;
var whenValStr     = "<br>Contents of TextBox &quot;when&quot;: " + document.testForm.when.value;
var textValStr     = "<br>Contents of TextArea &quot;text&quot;: " + document.testForm.text.value;
var doorCheckStr   = "<br>Checkbox &quot;door&quot is checked: " + document.testForm.door.checked;
var doorValStr     = "<br>Current value of checkbox &quot;door&quot;: " + document.testForm.door.value;
var dosCheckStr    = "<br>Checkbox &quot;dos&quot is checked: " + document.testForm.dos.checked;
var dosValStr      = "<br>Current value of checkbox &quot;dos&quot;: " + document.testForm.dos.value;
var anivValStr     = "<br>Current value of radio &quot;aniv&quot;: " + document.testForm.aniv.value;
content = content + whichIdxStr + whichValStr + whichValIdxStr + autoIdxStr + autoValStr + autoValIdxStr + multiValStr + listValStr + whenValStr + textValStr + doorCheckStr + doorValStr + dosCheckStr + dosValStr + anivValStr + "</p>"

openChildWindow(content);

}

function openChildWindow(content) {

if (!(OpenWindow!=null)) {
OpenWindow=window.open("", "childwin","height=520,width=400,toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes");
} else if (OpenWindow.closed) {
OpenWindow=window.open("", "childwin","height=520,width=400,toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes");
}

OpenWindow.document.write("<HTML>")
OpenWindow.document.write("<HEAD>")
OpenWindow.document.write("<TITLE>More Information</TITLE>")
OpenWindow.document.write("<BODY BGCOLOR='lightblue'>")
OpenWindow.document.write(content)
OpenWindow.document.write("<p align=center><a href='' onClick='self.close()'>Click here to close</a></p>")
OpenWindow.document.write("</body>")
OpenWindow.document.write("</html>")
OpenWindow.document.close()
OpenWindow.focus()

}

-->
</script>

</head>
<body>
<%!
String table = "<table border=\"1\" cellpadding=\"2\">";
String tr = "<tr><td>";
String trC = "</td></tr>";
String tableC = trC + "</table>";
String th = "<th>Name</th><th>Value</th>";
%>

<%!
StringBuffer makeFormParamTable(HttpServletRequest request)
{
  StringBuffer sb = new StringBuffer();
  Enumeration enumV;
  String name;
  String value = null;
  String [] selections;

  sb.append(table);
  sb.append("<tr><th colspan=2>HTTPServletRequest getParameterNames</th></tr>");
  enumV = request.getParameterNames();
  while (enumV.hasMoreElements()) {
    name = (String) enumV.nextElement();
    selections = request.getParameterValues(name); // May be multi-valued
    if (selections == null) {
      value = null;
    } else {
      if (selections.length < 2) {
	value = selections[0];
      } else {
	value = "";
	for (int axi = 0; axi < selections.length; axi++) {
	  value=value+((("").equals(value))?"":",")+selections[axi];
	}
      }
    }
    if (value == null) { value = "null"; }
    if ("".equals(value)) { value = "empty string"; }
    sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
  }

  sb.append(tableC);

  Cookie[] aCookies = request.getCookies();
  if ((aCookies != null) && (aCookies.length > 0)) {
    sb.append(table);
    sb.append("<tr><th>Cookie</th><th>Value</th></tr>\n");
    for (int i = 0; i < aCookies.length; i++) {
      sb.append("<tr><td>" + aCookies[i].getName()  + "</td>" +
		"<td>" + aCookies[i].getValue() + "</td></tr>");
    }
    sb.append(tableC);
  } else {
    sb.append("No cookies for this session<BR>");
  }

//    enumV = request.getAttributeNames();
//    while (enumV.hasMoreElements()) {
//        name = (String) enumV.nextElement();
//        value = request.getAttribute(name);
//        if (value == null) { value = "null"; }
//        sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
//      }
  return sb;
}
%>

<%!

StringBuffer makeRequestHeaderTable(HttpServletRequest request)
{
  StringBuffer sb = new StringBuffer();
  Enumeration enumV;
  String name;
  String value;

  sb.append(table);
  sb.append("<tr><th colspan=2>HTTPServletRequest getHeaderNames</th></tr>\n");
  enumV = request.getHeaderNames();
  while (enumV.hasMoreElements()) {
    name = (String) enumV.nextElement();
    value = request.getHeader(name);
    if (value == null) { value = "null"; }
    sb.append("<tr><td>" + name + "</td><td>" + value + "</td></tr>");
  }

  sb.append("<tr><th>Method</th><th>Result</th></tr>");
  sb.append("<tr><td>getAuthType()</td><td>" +
	    request.getAuthType() + "</td></tr>");
  sb.append("<tr><td>getContentLength()</td><td>" +
	    request.getContentLength() + "</td></tr>");
  sb.append("<tr><td>getContextPath()</td><td>" +
	    request.getContextPath() + "</td></tr>");
  sb.append("<tr><td>getContentType()</td><td>" +
	    request.getContentType() + "</td></tr>");
  sb.append("<tr><td>getCharacterEncoding()</td><td>" +
	    request.getCharacterEncoding() + "</td></tr>");
  sb.append("<tr><td>getLocalAddr()</td><td>" +
	    request.getLocalAddr() + "</td></tr>");
  sb.append("<tr><td>getLocalName()</td><td>" +
	    request.getLocalName() + "</td></tr>");
  sb.append("<tr><td>getLocalPort()</td><td>" +
	    request.getLocalPort() + "</td></tr>");
  sb.append("<tr><td>getPathTranslated()</td><td>" +
	    request.getPathTranslated() + "</td></tr>");
  sb.append("<tr><td>getPathInfo()</td><td>" +
	    request.getPathInfo() + "</td></tr>");
  sb.append("<tr><td>getQueryString()</td><td>" +
	    request.getQueryString() + "</td></tr>");
  sb.append("<tr><td>getRemoteAddr()</td><td>" +
	    request.getRemoteAddr() + "</td></tr>");
  sb.append("<tr><td>getRemoteHost()</td><td>" +
	    request.getRemoteHost() + "</td></tr>");
  sb.append("<tr><td>getRemoteUser()</td><td>" +
	    request.getRemoteUser() + "</td></tr>");
  sb.append("<tr><td>getMethod()</td><td>"+request.getMethod()+"</td></tr>");
  sb.append("<tr><td>getRequestURI()</td><td>" +
	    request.getRequestURI() + "</td></tr>");
  sb.append("<tr><td>getServerName()</td><td>" +
	    request.getServerName() + "</td></tr>");
  sb.append("<tr><td>getServerPort()</td><td>" +
	    request.getServerPort() + "</td></tr>");
  sb.append("<tr><td>getProtocol()</td><td>" +
	    request.getProtocol() + "</td></tr>");

  sb.append(tableC);
  return sb;
}
%>

<%!
StringBuffer makeServletTables(Servlet servlet,
				       ServletContext context) {
  StringBuffer sb = new StringBuffer();
  ServletConfig config;
//  ServletContext context;
  Enumeration enumV;
  String name;
  String value;
  Object obj;

  config = servlet.getServletConfig();

// "+config.getServletName()+"

  sb.append(table);
  sb.append("<tr><th colspan=2>ServletConfig getInitParameterNames</th></tr>");

  enumV = config.getInitParameterNames();
  while (enumV.hasMoreElements()) {
    name = (String)enumV.nextElement();
    value = config.getInitParameter(name);
    if (value == null) { value = "null"; }
    sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
  }
  sb.append(tableC);

  // context = config.getServletContext();

  sb.append(table);
  sb.append("<tr><th colspan=2>ServletContext getInitParameterNames</th></tr>");

  enumV = context.getInitParameterNames();
  while (enumV.hasMoreElements()) {
    name = (String)enumV.nextElement();
    value = context.getInitParameter(name);
    if (value == null) { value = "null"; }
    sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
  }
  sb.append(tableC);

  sb.append(table);
  sb.append("<tr><th colspan=2>ServletContext getAttributeNames</th></tr>");
  try {
    enumV = context.getAttributeNames();
    if (enumV != null) {
      sb.append("<tr><td colspan=2>" + enumV.toString() + "</td></tr>");
      while (enumV.hasMoreElements()) {
	name = (String)enumV.nextElement();
	try {
	  obj = context.getAttribute(name);
	  if (obj instanceof String) { value = (String)obj; }
	  else                       { value = obj.toString(); }
	} catch (Exception e) {
	  value = e.toString();
	}
	if (value == null) { value = "null"; }
	sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
      }
    }
  } catch (Exception e) {
    sb.append("<tr><td colspan=2>" + e.toString() + "</td></tr>");
  }
  sb.append(tableC);

//  set = context.getResourcePaths();
//  sb.append(set.toString);

  try {
    sb.append("Major version " + context.getMajorVersion() +
	      " Minor version " + context.getMinorVersion() + "<br>\n" +
	      "Server Info " + context.getServerInfo() + "<br>\n");
  } catch (Exception e) {
    sb.append("context exception" + e.toString() + "<br>\n");
  }

  return sb;
}
%>

<%!
StringBuffer makeSessionTable(HttpServletRequest request)
{
  StringBuffer sb = new StringBuffer();
  Enumeration enumV;
  String name;
  String value;
  HttpSession session;
  Object obj;

  sb.append(table);
  sb.append("<tr><th colspan=2>HTTPSession getAttributeNames</th></tr>");

  try {
    session = request.getSession();
    enumV = session.getAttributeNames();
    if (enumV != null) {
      sb.append("<tr><td colspan=2>" + enumV.toString() + "</td></tr>");
      while (enumV.hasMoreElements()) {
	name = (String)enumV.nextElement();
	try {
	  obj = session.getAttribute(name);
	  if (obj instanceof String) { value = (String)obj; }
	  else                       { value = obj.toString(); }
	} catch (Exception e) {
	  value = e.toString();
	}
	if (value == null) { value = "null"; }
	sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
      }
    }
  } catch (Exception e) {
    sb.append("<tr><td colspan=2>" + e.toString() + "</td></tr>");
  }

  sb.append(tableC);

  try {
    session = request.getSession();
    sb.append("Id " + session.getId() + " New " + session.isNew() +
	      " Timeout " + session.getMaxInactiveInterval() +
	"<br>Created " +
    java.text.DateFormat.getInstance().format(new java.util.Date(session.getCreationTime())) +
    " Displayed " +
    java.text.DateFormat.getInstance().format(new java.util.Date()) +
 "<br>\n");
  } catch (Exception e) {
    sb.append("session exception" + e.toString() + "<br>\n");
  }

  return sb;
}
%>

<%!
StringBuffer makeSystemTable()
{
  StringBuffer sb = new StringBuffer();
  Enumeration enumV;
  String name;
  String value;
  HttpSession session;
  Object obj;
  Properties prop;

  sb.append(table);
  sb.append("<tr><th colspan=2>System getProperties</th></tr>");

  try {
    prop = System.getProperties();
    enumV = prop.propertyNames();
      if (enumV != null) {
	while (enumV.hasMoreElements()) {
	  name = (String)enumV.nextElement();
	  try {
	    obj = System.getProperty(name);
	    if (obj instanceof String) { value = (String)obj; }
	    else                       { value = obj.toString(); }
	  } catch (Exception e) {
	    value = e.toString();
	  }
	  if (value == null) { value = "null"; }
	  sb.append("<tr><td>" + name + "</td>" + "<td>" + value + "</td></tr>");
	}
      }
  } catch (Exception e) {
    sb.append("<tr><td colspan=2>" + e.toString() + "</td></tr>");
  }

  sb.append(tableC);

  return sb;
}
%>

<FORM METHOD=Get ACTION="<%=response.encodeURL("paramCheck.jsp")%>" name="testForm">

<p>Selector named which:
<SELECT NAME="which">
<OPTION VALUE="One">one</option>
<OPTION VALUE="Two">two</option>
<OPTION VALUE="Three">three</option>
</SELECT>

<p>Submit selector named automatic:
<SELECT NAME="automatic" onchange='document.testForm.submit()'>
<OPTION VALUE="AOne">aone</option>
<OPTION VALUE="ATwo">atwo</option>
<OPTION VALUE="AThree">athree</option>
</SELECT>

<p>Multi-Selector named multi:
<SELECT NAME="multi" size="4" multiple id="multi">
  <OPTION VALUE="Aerospace" SELECTED>Aerospace</option>
  <OPTION VALUE="Component MFR">Component MFR</option>
  <OPTION VALUE="Consumer Products">Consumer Products</option>
  <OPTION VALUE="Kitting">Kitting</option>
  <OPTION VALUE="Medical">Medical</option>
  <OPTION VALUE="Military">Military</option>
  <OPTION VALUE="Power Supply" SELECTED>Power Supply</option>
  <OPTION VALUE="Telecom" selected>Telecom</option>
  <OPTION VALUE="Other">Other</option>
</select></td>

<br>TextBox named list <input type=text name="list" size="10">
TextBox named when <input type=text name="when" size="5" value="1000">
<br>TextArea named text:<br>
<TEXTAREA COLS="30" ROWS="3" NAME="text"></TEXTAREA>
<br>
Check box named door: <INPUT NAME="door" TYPE=checkbox>
Check box named dos:  <INPUT NAME="dos"  TYPE=checkbox onClick="clickedCheck(this)" value="fred">
<INPUT TYPE="reset" VALUE="Reset Form"><BR>
Button name button value button <input type=button value="button" name=button><br><BR>
<input name="aniv" type="radio" value="aniv" CHECKED>Radio group aniv, button named aniv
<input name="aniv" type="radio" value="inter">Radio group aniv, button named inter<BR>
<input type=submit value="submit1" name="submit1">
<input type=submit value="submit2" name="submit2">
<input type=button value="JavaScript values" onClick="whoWhat()">
</form>

Session from Cookie <%= request.isRequestedSessionIdFromCookie() %><br>
Session from URL    <%= request.isRequestedSessionIdFromUrl() %><br>

<%= makeFormParamTable(request) %>

<%= makeSessionTable(request) %>

<%= makeRequestHeaderTable(request) %>

<%= makeServletTables(this, application) %>

<%= makeSystemTable() %>

</body>
</html>
