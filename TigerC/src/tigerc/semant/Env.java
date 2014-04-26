/*
 **   Env.java
 **
 **   The Env class aggregates the two symbol tables we need in order to 
 **   manage typechecking:  one for variable/type bindings, the other for 
 **   type name/type bindings.  
 **
 **   Package-only visibility, as the concept of "environment" is relevant  
 **   only as a utility for semantic analysis
 **
 **   Author:  John Lasseter
 **   Modified by:             <YOUR NAME HERE>
 **
 **   Revised: 11-21-2011
 **       
 */

package tigerc.semant;

import tigerc.semant.types.*;
import tigerc.util.ErrorMsg;
import tigerc.util.Pair;
import tigerc.util.Symbol;
import tigerc.util.List;


public class Env {
	Table<Entry> venv;
	Table<Type> tenv;
	ErrorMsg err;

	private static Symbol sym(String s) {
		return Symbol.symbol(s);
	}

	Env(ErrorMsg e) {
		err = e;

		venv = new Table<Entry>();
		tenv = new Table<Type>();

		// Bindings for the primitive types.

		tenv.put(sym("int"), PrimTy.INT_T);
		tenv.put(sym("string"), PrimTy.STRING_T);
		

		makeStdLib();
	}

	private void makeStdLib() {
		// Standard library...
		// Java generics do offer type-safe reusability, but at the cost of
		// god-awful verbosity.  I've kept the formals list constructions
		// separate from the venv bindings, which I hope enhances clarity.

		// print
		List<Pair<Symbol, Type>> printFmls = makeFmls(sym("s"), PrimTy.STRING_T);
			
		venv.put(sym("print"), new FunEntry(printFmls, PrimTy.VOID_T));

		//printi
		List<Pair<Symbol, Type>> printiFmls = 
			new List<Pair<Symbol, Type>>(new Pair<Symbol,Type>(sym("i"), PrimTy.INT_T), null);
		venv.put(sym("printi"), new FunEntry(printiFmls, PrimTy.VOID_T));

		//flush
		venv.put( sym("flush"), new FunEntry( null , PrimTy.VOID_T));
		
		//getchar
		venv.put(sym("getChar"), new FunEntry(null, PrimTy.STRING_T));

		//ord
		List<Pair<Symbol, Type>> ordFmls = makeFmls(sym("s"), PrimTy.STRING_T);
		venv.put(sym("ord"), new FunEntry(ordFmls, PrimTy.INT_T));
		
		//chr 
		List<Pair<Symbol, Type>> chrFmls = makeFmls(sym("i"), PrimTy.INT_T);
		venv.put(sym("chr"), new FunEntry(chrFmls, PrimTy.STRING_T));
		
		//size
		List<Pair<Symbol, Type>> sizeFmls = makeFmls(sym("s"), PrimTy.STRING_T);
		venv.put(sym("size"), new FunEntry(sizeFmls, PrimTy.INT_T));
		
		//substring
		//Why is this so incredibly terrible?
		List<Pair<Symbol, Type>> substringfmls = 
				new List<Pair<Symbol, Type>>(
					new Pair<Symbol,Type>(sym("s"), PrimTy.STRING_T),
						new List<Pair<Symbol,Type>>(
								new Pair<Symbol,Type>(sym("f"), PrimTy.INT_T),
							new List<Pair<Symbol,Type>>(
									new Pair<Symbol,Type>(sym("n"), PrimTy.INT_T), null)));
		
		venv.put(sym("substring"), new FunEntry(substringfmls, PrimTy.STRING_T) );
		
		//concat
		List<Pair<Symbol, Type>> concatfmls = 
				new List<Pair<Symbol, Type>>(
					new Pair<Symbol,Type>(sym("s1"), PrimTy.STRING_T),
						new List<Pair<Symbol,Type>>(
								new Pair<Symbol,Type>(sym("s2"), PrimTy.STRING_T), null));
		venv.put(sym("concat"), new FunEntry(concatfmls, PrimTy.STRING_T));
		
		//not
		venv.put(sym("not"), new FunEntry(makeFmls(sym("i"), PrimTy.INT_T), PrimTy.INT_T));
		
		//exit
		venv.put(sym("exit"), new FunEntry(makeFmls(sym("i"), PrimTy.INT_T), PrimTy.VOID_T));
		
		
		//FunEntry example = FunEntry(parameters, returnType);

	}
	
	List<Pair<Symbol,Type>> makeFmls(Symbol symbol, Type type){
		return  new List<Pair<Symbol, Type>> (new Pair<Symbol,Type>(symbol,type), null);
	}
}
