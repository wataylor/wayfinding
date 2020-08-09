/* @name MethodExtraction.java

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

package zetek.readmeta.classes;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Project-specific utilities to manage lists of methods for manipulating
 * model objects by using application-independent reflection utilities.  As
 * long as the class attribute names match the names found in the ascii
 * input file, attributes can be set appropriately.  The attribute extractor
 * caches certain important attributes for the sake of speed; this is what
 * makes this class application-specific to a degree.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see zetek.readmeta.classes.ClassConstructors
 */

public class MethodExtraction {

  public static final long serialVersionUID = 1;

  /**
   * Table which maps classes to the ability to convert them to and
   * from AcDb ascii dumps. */
  public static Hashtable<Class<?>, AcDbClassMap> classToTable =
    new Hashtable<Class<?>, AcDbClassMap>(30);

  public static PropertyDescriptor[]
  getPropertyDescriptors(Class<?> beanClass) {
    PropertyDescriptor descriptors[] = null;
    if (beanClass == null) {
      throw new IllegalArgumentException("No bean class specified");
    }
    // Introspect the bean and cache the generated descriptors
    BeanInfo beanInfo = null;
    try {
      beanInfo = Introspector.getBeanInfo(beanClass);
    } catch (IntrospectionException e) {
      return (new PropertyDescriptor[0]);
    }
    descriptors = beanInfo.getPropertyDescriptors();
    if (descriptors == null) {
      descriptors = new PropertyDescriptor[0];
    }
    Introspector.flushCaches(); // The data are stored in cache maps
    return (descriptors);
  }

  public static AcDbClassMap getMapForClass(Class<?> aClass) {

    AcDbClassMap tblMap;
    if ( (tblMap = classToTable.get(aClass)) == null) {
      tblMap = createAcDbTblMap(aClass);
      classToTable.put(aClass, tblMap); // cache for later use
    }
    return tblMap;
  }

  /**
   * Create a map of field manipulation methods for a class.
   * @param aClass class for which methods are needed
   * @return map between attribute names and methods.  Note that the
   * field manipulation methods are indexed by the field name with the
   * first character set to upper case.  This is because the data
   * extracted from the .dwg files has field names with initial caps.
   * The actual Java class attribute name is also stored.
   */
  static AcDbClassMap createAcDbTblMap(Class<?> aClass) {
    AcDbAttributeMap attrMap;
    AcDbClassMap tblMap;
    Field field;
    MethodExtraction me = new MethodExtraction();
    PropertyDescriptor[] descriptors;
    String attrIndexName;
    String attrName;

    descriptors = getPropertyDescriptors(aClass);

//    GB.logger.info("Property names from class " + aClass.getName());
/*    for (PropertyDescriptor desc : descriptors) {
      GB.logger.info(desc.getName());
      }
*/
    tblMap = me.new AcDbClassMap();
    tblMap.className = aClass.getName();
    tblMap.attrMaps  = new Hashtable<String, AcDbAttributeMap>(descriptors.length);

    for (PropertyDescriptor desc : descriptors) {
      attrName = desc.getName();
      attrIndexName  = (Character.toUpperCase(attrName.charAt(0)) +
			attrName.substring(1)).intern();
      field = null;
      try {
        field = aClass.getField(attrIndexName + "Changed");
      }  catch (NoSuchFieldException e) {
        if (!"Class".equals(attrIndexName)) { // All classes support getClass
          //GB.logger.info("No changed field " + aClass.getName() +
	  //                      " " + attrName + " " + e.toString());
        }
      } catch (SecurityException e) {
        System.out.println("Security Exception " + e.toString());
      }

      attrMap  = me.new AcDbAttributeMap();
      attrMap.writeMethod = desc.getWriteMethod();
      attrMap.readMethod  = desc.getReadMethod();

      attrMap.attribute  = field;

      /* VERY BAD HACK - EMF does not allow the same attribute name in
       * classes which extend other classes.  A parent class and a
       * child class cannot have the same method name.*/
      tblMap.attrMaps.put(attrIndexName, attrMap);
      if ("Parent".equals(attrIndexName) &&
		 (tblMap.setParent == null))  {
	tblMap.setParent    = attrMap.writeMethod;
	tblMap.getParent    = attrMap.readMethod;
      } else if ("Reference".equals(attrIndexName))  {
	tblMap.setReference = attrMap.writeMethod;
	tblMap.getReference = attrMap.readMethod;
      } else if ("Ilk".equals(attrIndexName))  {
	tblMap.setIlk = attrMap.writeMethod;
	tblMap.getIlk = attrMap.readMethod;
      }
    }
    return tblMap;

  }

  /** Class to store information about the class itself as well as
   * about mappings between AcDb attribute names and object
   * attributes.  Certain methods are stored twice for cinvenience of
   * access.  In particular, getparent is stored so that the parent is
   * not dumped as part of the XML output.  */
  public class AcDbClassMap {
    String className;
    public Method setParent;
    public Method getParent;
    public Method getReference;
    public Method setReference;
    public Method getIlk;
    public Method setIlk;
    public Hashtable<String, AcDbAttributeMap> attrMaps;
  }

  /** Class to store information about the methods which support one
   * attribute of the class.  These objects are indexed under the
   * attribute name as found in the input data file.  Input attribute
   * names have initial caps.*/
  public class AcDbAttributeMap {
    public Method writeMethod;
    public Method readMethod;
    public Field  attribute;	// Field from the object metadata
  }

}
