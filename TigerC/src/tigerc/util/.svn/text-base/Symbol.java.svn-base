/**************************************************************
** Symbol.java
*
*  The Symbol class provides a representation of Tiger symbols that supports 
*  fast equality and comparison checking.
* 
*  Source code taken from Andrew Appel's _Modern Compiler Implementation in Java_, 
*  updated (J. Lasseter) to use the new java.util.Map interface and generics.
*  
**************************************************************/

package tigerc.util;

public class Symbol {
  private String name;
  private static java.util.Map<String,Symbol> dict = new java.util.Hashtable<String,Symbol>();

  private Symbol(String n) {
    name=n;
  }

  public String toString() {
	return name;
  }

  /** 
   * Make return the unique symbol associated with a string.
   * Repeated calls to <tt>symbol("abc")</tt> will return the same Symbol.
   */

  public static Symbol symbol(String n) {
	String u = n.intern();
	Symbol s = dict.get(u);
	if (s==null) {
		s = new Symbol(u);
		dict.put(u,s);
	}
	return s;
  }
}

