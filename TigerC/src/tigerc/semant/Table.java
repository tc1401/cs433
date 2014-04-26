/**************************************************************
** Table.java
*
*  The Table class is similar to java.util.Map, except that each key must
*  be a Symbol and there is a scope mechanism.
* 
*  Source code taken from Andrew Appel's _Modern Compiler Implementation in Java_, 
*  updated (J. Lasseter) to use the new java.util.Map interface and generics.
*  
**************************************************************/

package tigerc.semant;

import tigerc.util.Symbol;

class Binder<T> {
	T value;
	Symbol prevtop;
	Binder<T> tail;

	Binder(T v, Symbol p, Binder<T> t) {
		value = v;
		prevtop = p;
		tail = t;
	}
}

/**
 * 
 */

public class Table<T> {

	private java.util.Map<Symbol, Binder<T>> dict;
	private Symbol top;
	private Binder<T> marks;

	public Table() {
		dict = new java.util.Hashtable<Symbol, Binder<T>>();
	}

	/**
	 * Gets the object associated with the specified symbol in the Table.
	 */
	public T get(Symbol key) {
		Binder<T> e =  dict.get(key);
		if (e == null)
			return null;
		else
			return e.value;
	}

	/**
	 * Puts the specified value into the Table, bound to the specified Symbol.
	 */
	public void put(Symbol key, T value) {
		dict.put(key, new Binder<T>(value, top, dict.get(key)));
		top = key;
	}

	/**
	 * Remembers the current state of the Table.
	 */
	public void beginScope() {
		marks = new Binder<T>(null, top, marks);
		top = null;
	}

	/**
	 * Restores the table to what it was at the most recent beginScope that has
	 * not already been ended.
	 */
	public void endScope() {
		while (top != null) {
			Binder<T> e =  dict.get(top);
			if (e.tail != null)
				dict.put(top, e.tail);
			else
				dict.remove(top);
			top = e.prevtop;
		}
		top = marks.prevtop;
		marks = marks.tail;
	}

	/**
	 * Returns an enumeration of the Table's symbols.
	 */
	public java.util.Iterator<Symbol> keys() {
		return dict.keySet().iterator();
	}
}
