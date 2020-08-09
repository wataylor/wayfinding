/* @name CollectStrings.java

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

package zetek.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

/**
 * Class to turn a set of strings about findable entities in a
 * building into an array of JSON objects so that they can be sent to
 * the client in an intelligible manner.</p>

 * <P>The advantage of using JSON is that test program can generate an
 * instance of the findable strings to test the suggester.  Each class
 * needs its own <code>toString</code> method to generate itself in
 * JSON.  The JSON objects do not need setter methods, only getter
 * methods.  Any parameter which is returned by a get is converted
 * into JSON.</p>

 * <P>This JavaScript code creates an array named
 * <code>eligible</code> which contains strings which match the
 * current characters in <code>inputText</code>.  The variable
 * <code>suggestions</code> is an array of <code>KeyedStringers</code>
 * which have a key, a length, and an array of <code>Stringers</code>,
 * each of which has a string and an ID of the table from which it
 * came.  All <code>Stringers</code> in a <code>KeyedStringers</code>
 * object match the key.  Keys can vary in length; the length is an
 * attribute of the <code>KeyedStringer</code>.</p>

 * <P> The servlet returns an array of <code>Stringer</code> objects
 * to the client.  These strings become Java Script objects with
 * attributes and values.  The data entry system in the client
 * displays the attribute named <code>sug</code> for "suggestion".
 * When the user chooses a suggestion, the entire
 * <code>Stringer</code> object is returned to characterize the selection.
 * Thus, it would be easy to add new characteristics of each
 * suggestion by adding them to the Stringer class.

<pre>
   this.getEligible = function() {
    var j;
    this.eligible = new Array();
    var lowin = this.inputText.toLowerCase();
    for (i in this.suggestions) {
      var category = this.suggestions[i];
      var len = category.len;
      var subst = lowin.substring(0, len);
      if (subst == category.key) {
        var slist = category.sl;
        // console.log(slist);
        for (j in slist) {
          var sugdata = slist[j];
          // console.log(sugdata);
          var suggestion = sugdata.sug;
          // console.log(suggestion);
          if(suggestion.toLowerCase().indexOf(lowin) == "0") {
            this.eligible[this.eligible.length]=suggestion;
          }
        }
      }
    }
  };
</pre>

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class CollectStrings {

  public static final long serialVersionUID = 1;

  /** All stringers sorted by length of their key*/
  public Map<Integer, List<Stringer>> stringersByKeylen =
    new HashMap<Integer, List<Stringer>>();
  /** Stringers with the same key collected into lists in an object which
   * specifies the key.  All keys have been converted to lower case for ease
   * of searching.*/
  public List<KeyedStringer> keyedStringerList;

  /** Obligatory constructor.*/
  public CollectStrings() { /* */ }

  /**
   * Add a findable string to the collection which is associated with
   * its key length
   * @param len now many characters are needed to match the suggestion
   * @param suggestion text of the suggestion
   * @param ilk type of object with which this string is associated.
   * @param flo floor construction order
   */
  public void addString(Integer len, String suggestion, int ilk, long flo) {
    List<Stringer> sList;

    if (suggestion.length() < len) {
      System.out.println("Bad suggstion " + suggestion + " " + len);
      return;
    }

    if ( (sList = stringersByKeylen.get(len)) == null) {
      sList = new ArrayList<Stringer>();
      stringersByKeylen.put(len, sList);
    }
    sList.add(new Stringer(suggestion, ilk, flo));
  }

  /**
   * Convert the accumulated strings into a sorted list based on the
   * first key letters being the same.  All keys are converted to
   * lower case for later ease of comparison.  Java Script appears not
   * to have a case-independent string comparison method.
   * @return list of keyed string sets which can be converted to a
   * JSON array.
   */
  public List<KeyedStringer> makeKeyedStrings() {
    if (keyedStringerList != null) { return keyedStringerList; }

    List<KeyedStringer> stringerList = new ArrayList<KeyedStringer>(20);
    List<Stringer> sList;
    List<Stringer> matches;
    Stringer[] sArray;
    Set<Integer> sKeys = stringersByKeylen.keySet();
    int len;
    int i;
    String key;
    Stringer st;
    String newKey;

    for (Integer in : sKeys) {
      len = in.intValue();
      sList = stringersByKeylen.get(in);
      sArray = sList.toArray(new Stringer[sList.size()]);
      Arrays.sort(sArray);
      matches = new ArrayList<Stringer>();
      key = "";
      for (i=0; i<sArray.length; i++) {
	st = sArray[i];
	// Skip duplicates - the sort brings ilks together as well as names
	if ((i > 0) && (st.compareTo(sArray[i-1]) == 0)) { continue; }

	newKey = st.sug.substring(0, len).toLowerCase();
	if (newKey.equalsIgnoreCase(key)) {
	  matches.add(st);
	} else {
	  if (matches.size() > 0) {
	    stringerList.add(new KeyedStringer(key, matches));
	    matches = new ArrayList<Stringer>();
	  }
	  matches.add(st);
	  key = newKey;
	}
      }
      if (matches.size() > 0) {
	stringerList.add(new KeyedStringer(key, matches));
      }
    }
    stringersByKeylen = null;		// Promote garbage collection
    return keyedStringerList = stringerList;
  }

  public Stringer[] matchInput(String input) {
    List<Stringer> sList = new ArrayList<Stringer>();

    if (keyedStringerList == null) {
      makeKeyedStrings();
    }

    input = input.toLowerCase();
    for (KeyedStringer ks : keyedStringerList) {
      if (input.startsWith(ks.key)) {
	for (Stringer s : ks.sList) {
	  if (s.sug.toLowerCase().startsWith(input)) {
	    sList.add(s);
	  }
	}
      }
    }

    if (sList.size() <= 0) { return null; }
    Stringer[] ss = sList.toArray(new Stringer[sList.size()]);
    Arrays.sort(ss);
    return ss;
  }

  /**
   * Convert the object to a JSON string.  Each keyed stringer becomes
   * a separate JSON object; the entire collection becomes an array of
   * stringer objects. Note that a Java Script array is defined by
   * comma separated constants inside [].  Java Script objects are key
   * - value pairs within {}.
   */
  public String toString() {
    boolean did = false;
    List<KeyedStringer> keyList = makeKeyedStrings();

    StringBuilder sb = new StringBuilder(20 * keyList.size());
    sb.append("[");
    for (KeyedStringer ks : keyList) {
      if (did) { sb.append(","); }
      sb.append(ks.toString());
      did = true;
    }
    sb.append("]");
    return sb.toString();
  }

  /** Class to hold information about one suggestion.  A suggestion
   * consists of a string and a type so that the server knows what
   * table to search when the name is returned.  These objects sort on
   * the suggestion and type so that suggestions can be sorted into
   * alphabetical order and extras can be skipped.  */
  public class Stringer implements Comparable<Stringer> {
    String sug;
    int    ilk;
    long   flo;

    @Override
      public int compareTo(Stringer arg0) {
      int c;
      if ( (c = sug.compareTo(arg0.sug)) != 0) { return c; }
      return new Integer(ilk).compareTo(arg0.ilk);
    }
    public String getSug() {
      return sug;
    }
    public int getIlk() {
      return ilk;
    }
    public long getFlo() {
      return flo;
    }
    public Stringer (String sugg, int ilk, long flo) {
      this.sug = sugg;
      this.ilk = ilk;
      this.flo = flo;
    }
    public String toString () {
      JSONObject js = new JSONObject(this, false);
      // System.out.println(js.toString());
      return js.toString();
    }
  }

  /** Store a key which indicates the first character or characters
   * of all of the suggestions in the list.  The suggestion code
   * does not have to do as much searching to find viable suggestions.
   * These objects sort on the sort key.*/
  public class KeyedStringer implements Comparable<KeyedStringer> {
    String key;
    int    len;
    List<Stringer> sList;

    @Override
      public int compareTo(KeyedStringer o) {
      return key.compareTo(o.key);
    }

    public int getLen() {
      return len;
    }

    public String getKey() {
      return key;
    }

    public List<Stringer> getSl() {
      return sList;
    }

    public KeyedStringer (String key, List<Stringer> sList) {
      this.key   = key;
      this.sList = sList;
      this.len   = key.length();
    }

    public String toString() {
      JSONObject jo = new JSONObject(this);
      return jo.toString();
    }
  }
}
