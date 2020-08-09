/* @name PropLoader.java

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

*/

package zetek.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import zetek.dbcommon.DBParams;

/**
 * Explore the machinations of loading resource bundles and / or
 * properties files.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PropLoader {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public PropLoader() { /* */ }

  public static void main(String[] args) {
    ResourceBundle bundle = null;
    String lib = "/usr/local/tomcat/common/classes/";
    // ClassLoader loader = PropLoader.class.getClassLoader();
    URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
    Class sysclass = URLClassLoader.class;
    File f = new File(lib);
    System.out.println("Loading from directory " + lib);

    try {
      Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
      method.setAccessible(true);
      method.invoke(sysloader,new Object[]{ f.toURI().toURL() });
    } catch (Throwable t) {
      t.printStackTrace();
      System.out.println("Error, could not add URL to system classloader");
      System.exit(-1);
    }//end try catch

    URL url = sysloader.getResource("Panel.properties");
    System.out.println("URL " + url);
    try {
      bundle = ResourceBundle.getBundle("Panel.properties", Locale.getDefault(), sysloader);
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println(bundle);

    try {
      bundle = ResourceBundle.getBundle(lib + "Panel.properties", Locale.getDefault(), sysloader);
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println(bundle);

    Properties props;
    props = new Properties();
    try {
      props.load(new InputStreamReader(new FileInputStream(new File(lib + "Panel.properties"))));
    } catch (Exception e) {
      System.out.println(e);
      props = null;
    }
    if (props != null) {
      Set<Object> keys = props.keySet();
      for (Object o : keys) {
        System.out.println(o + " " + props.get(o));
      }
    }

    DBParams dbParam = new DBParams();
    System.out.println(dbParam.getParameter("user"));
  }
}
