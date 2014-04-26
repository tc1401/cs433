/**************************************************************
*  tigerc/src/semant/Entry.java
*
*  Author:  John Lasseter
*  Created:  10/27/2011
*  Last Modified: 12/07/2011
*
*  Various classes used for recording variable bindings in the environment
*  Because the environment is an implementation detail of semantic 
*  analysis, it should be hidden from outside the tigerc.semant package.
*  We do that here by keeping everything in package-only visibility.
* 
*  Source code taken from Andrew Appel's _Modern Compiler Implementation 
*  in Java_, aggregating several files into this one.  Updated (J. Lasseter) 
*  to use generics and the tigerc.util.List class.
*  
**************************************************************/


package tigerc.semant;
import tigerc.semant.types.Type;
import tigerc.util.Symbol;
import tigerc.util.List;
import tigerc.util.Pair;

/************* Entry interface **********************/
// The value environment has two kinds of entries: variable entries
// and function entries.
// We're going to keep these at package visibility, as they should never
// be touched outside of the semantic analysis

// Boy, it would be nice to have traits or union types right now.

interface Entry {
}

/************* Function Entries **********************/
// For function bindings. For now the only information we need
// about a function is the types of the formals and the type of
// the result.


class FunEntry implements Entry {

	List<Pair<Symbol, Type>> formals;
	Type result;

	FunEntry(List<Pair<Symbol, Type>> fs, Type rTy) {
		formals = fs;
		result = rTy;
	}
}

/************* Variable Entries **********************/
// For variable bindings. For now, the only information we need
// about a variable is its type and whether it is assignable.

class VarEntry implements Entry {
	Type ty;
	boolean assignable;

	VarEntry(Type t) {
		this(t, true);
	}

	VarEntry(Type t, boolean a) {
		this.ty = t;
		this.assignable = a;
	}
}
