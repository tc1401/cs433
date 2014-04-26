/*
 **   SemantV.java
 **
 **   The SemantV class does performs the task of semantic analysis on the 
 **   abstract syntax tree.  This means primarily typechecking, and the 
 **   traversal of the AST (through the visitor pattern dispatch) reflects that.   
 **   Like all implementations of IAbsynVisitor, SemantV is an imperative 
 **   visitor, which means that the visit methods have void return type, and 
 **   results are accumulated rather than returned.  
 **
 **   This is the workhorse of the "middle end".  It is primarily responsible 
 **   for typechecking, and that is the version you see here.   The full 
 **   also serves as the glue between the front and back ends.  In particular,
 **   the code generation stage -- whether we translate to an internal 
 **   three-address code form or write raw assembler to the output -- requires 
 **   access to the computed type information of each expression and 
 **   declaration.
 **
 **   Author:  John H. E. Lasseter
 **   Modified by:             <YOUR NAME HERE>
 **
 **   Revised: 03/28/2014
 **       
 */
 
 
package tigerc.semant;

import tigerc.util.Symbol;
import tigerc.util.ErrorMsg;
import tigerc.syntax.absyn.*;
import tigerc.semant.types.*;
import tigerc.util.List;
import tigerc.util.Pair;

public class SemantV implements IAbsynVisitor {

    private tigerc.semant.types.Type ty = null;
    // The "return value" of each visit() call is stored in ty

    private Env env;

	private boolean inLoop = false;

    // See header comments in Env.java. We could nest Env inside the
    // definition of SemantV, as it is not relevant elsewhere. However,
    // the source file is bloated enough as it is.

    /*********************************************************************/

    public SemantV(ErrorMsg e) {
        env = new Env(e);
    }

    private SemantV(Env e) {
        env = e;
    }
    /*********************************************************************/

    public void visit(DeclGroupFunction d) {
        /*
         * Declarations of function groups occur only in let expressions.  
         * A new scope has already been declared there, and it will end 
         * at the end of the let-body.  Hence, we do not open a new scope 
         * here.  Also note that there is no type being computed here, and  
         * hence no accumulation of a result in the ty field.
         */
        
        /*
         * ZEROth PASS: check for re-declaration of functions within this 
         * block.
         */
        java.util.Set<Symbol> funNames = new java.util.HashSet<Symbol>();
        List<DeclFn> cur = d.fns;

        while (cur != null) {
            if (!funNames.add(cur.head.name))
                env.err.error(cur.head.getPos(), "Redeclaration of function "
                        + cur.head.name
                        + " in the same mutual recursion block.");
            cur = cur.tail;

        }

        /*
         * FIRST PASS: Enter the signature of each function in this 
         * declaration block.
         */

        cur = d.fns;
        while (cur != null) {
            // Check that the result type makes sense
            Type result;
            if (cur.head.resultTy != null) {
                cur.head.resultTy.accept(this);   
                // Give this visitor to the resultTy object, in order to 
                // correctly dispatch the visit method (one of the 
                // visit(Ty*** ) methods)
                result = this.ty;
            } else {
                // cur.head has no declared return type (i.e. a procedure).
                result = PrimTy.VOID_T;
            }

            // Check the list of formals & build the FunEntry component
            List<Pair<Symbol, Type>> formals = visitTypeFields(cur.head.params);
 
            // Put the function in venv
            env.venv.put(cur.head.name, new FunEntry(formals, result));
            cur = cur.tail;
        }

        /*
         * SECOND PASS: Check the body of each function in this declaration
         * block. For each of the parameters, we make a new VarEntry in venv,
         * for the duration of the time we spend checking that function's 
         * body.  The body's type much match the declared return type
         */

        
        for (cur = d.fns; cur != null; cur = cur.tail) {
            FunEntry curEntr = (FunEntry) env.venv.get(cur.head.name);
            // Note:  this cast is safe, since we have just made FunEntry
            // bindings for every element in d.fns
            
            // (1) Open a new scope, and enter the formals in venv
            env.venv.beginScope(); 
            List<Pair<Symbol, Symbol>>  p = cur.head.params;
            while (p != null) {
                env.venv.put(p.head.fst, 
                             new VarEntry(env.tenv.get(p.head.snd)));
                // Note:  if p.head.snd does not refer to a legitimate type
                // there will be a tenv binding to ERROR: see visitTypeFields
                p = p.tail;
            }
            
            // (2) Now create a new Semant for the  body of cur.  
            // Why?  Because certain matters of nesting and scope are "reset" 
            // inside a new function body.  In particular, you can use this to 
            // leverage the checking of correct nesting of break statements.  
            // You'll have to think about how.  This is a hint here.
            
            SemantV newSem = new SemantV(this.env);
            cur.head.body.accept(newSem);
            Type bodyTy = newSem.ty;
            
            // Done with checking the current body.  Close this scope.
            env.venv.endScope();

            // Did the type of the function body match what was declared?
            if (!bodyTy.coerceTo(curEntr.result))
                env.err.error(cur.head.body.getPos(),
                        "Return value of function body must be of type "
                                + curEntr.result.actual());
            
            
        } // for

    }

    private List<Pair<Symbol, Type>> visitTypeFields(List<Pair<Symbol, Symbol>> params) {
        // Check that the type of each declared parameter are defined. 
    	// Build the corresponding list of Formal objects.  
    	// In the event that a parameter is declared with a bad type 
    	// (not found in tenv), make
        // a Formal binding of params.fst and ERROR.instance()
    	List<Pair<Symbol, Type>> types = null; //head of the list
    	List<Pair<Symbol,Type>> typesEnd = types; //end of the list
    	
     	Type t; 
    	List<Pair<Symbol,Symbol>> runner = params; 
    	while( runner != null ){
    		t = env.tenv.get(runner.head.snd);
    		if( t == null ){
    			t = ERROR.instance();
    		}
    		if(types == null){
    			types = new List<Pair<Symbol,Type>>(new Pair<Symbol,Type>(runner.head.fst,t),null );
    			typesEnd = types;    			
    		}else{
    			typesEnd.tail = new List<Pair<Symbol,Type>>(new Pair<Symbol,Type>(runner.head.fst,t),null); 	
    			typesEnd = typesEnd.tail; 	
    		}
    		runner = runner.tail; 
    	}
    	return types;
    }

    public void visit(DeclGroupType d) {
    	List<DeclTy> decls = d.decls;
    	List<DeclTy> runner = decls;
    	Type t = null;
    	while(runner != null){
    		runner.head.ty.accept(this);
    		runner = runner.tail; 
    	}
    }

    public void visit(DeclVar d) {
    	d.init.accept(this);
    	Type init = this.ty;
    	Type type = env.tenv.get(d.typ);
    	if(type != null && init.coerceTo(type) ){
    		env.venv.put(d.name, new VarEntry(type,true));
    		this.ty = type; 
    	}else if(type == null && !this.ty.coerceTo(PrimTy.NIL_T) 
    			&& this.ty.coerceTo(PrimTy.VOID_T)){
    		this.ty = env.tenv.get(d.name);
    	}else{
    		this.ty = ERROR.instance();
    	}

    }

    public void visit(ExpArray e) {
    	e.init.accept(this);
    	Type init = this.ty;
    	e.size.accept(this);
    	Type size = this.ty;
    	Type array = env.tenv.get(e.typ);
    	if( size.coerceTo(PrimTy.INT_T) && array.coerceTo(init)){
    		this.ty = array;
    	}else{
    		this.ty = ERROR.instance();
    	}
    }

    public void visit(ExpAssign e) {
    	e.exp.accept(this);
    	Type exp = this.ty;
    	e.var.accept(this);
    	Type var = this.ty;
    	
    	if(exp.coerceTo(var) && inLoop ){
    		this.ty = PrimTy.VOID_T;
    	}else{
    		this.ty = ERROR.instance();
    	}
    	
    }

    public void visit(ExpBreak e) {
    	if(inLoop){
    		this.ty = PrimTy.VOID_T;
    		
    	}else{
    		this.ty = ERROR.instance();
    	}
    	
    }

    public void visit(ExpCall e) {
        // e = f(args)
        // rule: tenv,venv |- f:'a -> 'b tenv,venv |- args:'a
        // --------------------------------------------------
        // tenv,venv |- f(args): 'b

        assert e != null && e.func != null;

        Entry en = env.venv.get(e.func);

        if (en == null) {  // no entry for e.func
            env.err.error(e.getPos(), "Symbol " + e.func + " is undefined.");
            ty = ERROR.instance();

        } else if (en instanceof FunEntry) {
            FunEntry f = (FunEntry) en;
            tigerc.util.List<Pair<Symbol, Type>> param = f.formals;
            tigerc.util.List<Exp> arg = e.args;
            boolean sound = true;

            while ((param != null) && (arg != null)) {
                arg.head.accept(this); // typecheck the next argument
                if (!ty.coerceTo(param.head.snd)) {
                    // Is its type compatible with the parameter?
                    env.err.error(arg.head.getPos(),
                            "Incompatible argument type for function " + e.func
                                    + "  << expected: " + param.head.snd
                                    + ", found: " + ty + " >>");
                    sound = false;
                    // Mark the error, but keep going, in case we can report
                    // more.
                }

                param = param.tail;
                arg = arg.tail;
            }
            if ((param != null) || (arg != null)) {
                env.err.error(e.getPos(),
                        "Wrong number of arguments for function " + e.func
                                + ".");
                sound = false;
            }
            if (sound) {
                this.ty = f.result;
            } else {
                this.ty = ERROR.instance();
            }
            return;
        }

        else {// if (en instanceof VarEntry) {
            env.err.error(e.getPos(), "Variable " + e.func
                                      + " is not a function.");
            ty = ERROR.instance();
        
        } 
    } // visit(ExpCall)


    public void visit(ExpFor e) {
    	Type t = env.tenv.get(e.var);
    	env.tenv.beginScope();
    	env.tenv.put(e.var, PrimTy.INT_T);
    	env.venv.put(e.var, new VarEntry(PrimTy.INT_T, false));
    	e.hi.accept(this);
    	Type hi = this.ty;
    	e.lo.accept(this);
    	Type lo = this.ty;
    	e.body.accept(this);
    	Type body = this.ty;
    	if(hi.coerceTo(PrimTy.INT_T) && lo.coerceTo(PrimTy.INT_T)
    			&& body.coerceTo(PrimTy.VOID_T)){
    		this.ty = PrimTy.VOID_T;
    	}
    	env.tenv.endScope();
    }

    public void visit(ExpIf e) {
    	//if
    	e.test.accept(this);
    	Type test = this.ty; 
    	//then
    	e.thenclause.accept(this);
    	Type then = this.ty;
    	
    	//if (int) then (void). 
    	if(test.coerceTo(PrimTy.INT_T) && then.coerceTo(PrimTy.VOID_T)){
    		this.ty = PrimTy.VOID_T;
    	}else{
    		env.err.error(e.getPos(), "Incorrect If/then expression.");
    		ty = ERROR.instance();
    	}
    	

    }

    public void visit(ExpIfElse e) {

    	//if
    	e.test.accept(this);
    	Type test = this.ty; 
    	//then
    	e.thenclause.accept(this);
    	Type then = this.ty;
    	//else
    	e.elseclause.accept(this);
    	Type elseClause = this.ty;
    	if(test.coerceTo(PrimTy.INT_T) && (then.coerceTo(elseClause))){
    		this.ty = PrimTy.VOID_T;
    	}else{
    		env.err.error(e.getPos(), "Incorrect If/then/else expression.");
    		ty = ERROR.instance();
    	}
    	
    	

    }

    public void visit(ExpLet e) {
    	e.body.accept(this);
    	List<Decl> runner = e.decls;
    	while(runner!= null){
    		runner.head.accept(this);
    		runner = runner.tail;
    	}
    }

    public void visit(ExpNil e) {
    	ty = PrimTy.NIL_T;

    }

    public void visit(ExpOp e) {
        
        e.left.accept(this);
        Type s = this.ty;
        
        e.right.accept(this);
        Type t = this.ty;
        
        Type u = applyOp(e.oper,s,t,e.getPos());
        this.ty = u;
    }

    private Type applyOp(ExpOp.Op op, Type t1, Type t2, int pos) {
        switch(op) {
            case PLUS:{
            	  if ((t1.coerceTo(PrimTy.INT_T)) && (t2.coerceTo(PrimTy.INT_T))) {
                      return PrimTy.INT_T; // return new INT();
                   } else {
                       env.err.error(pos,"Expected integer arguments.");
                       return ERROR.instance();
                   }
            }
            case MIN:{
            	  if ((t1.coerceTo(PrimTy.INT_T)) && (t2.coerceTo(PrimTy.INT_T))) {
                      return PrimTy.INT_T; // return new INT();
                   } else {
                       env.err.error(pos,"Expected integer arguments.");
                       return ERROR.instance();
                   }
            }
            case MUL:{
            	  if ((t1.coerceTo(PrimTy.INT_T)) && (t2.coerceTo(PrimTy.INT_T))) {
                      return PrimTy.INT_T; // return new INT();
                   } else {
                       env.err.error(pos,"Expected integer arguments.");
                       return ERROR.instance();
                   }
            }
            case DIV: {
                 if ((t1.coerceTo(PrimTy.INT_T)) && (t2.coerceTo(PrimTy.INT_T))) {
                    return PrimTy.INT_T; // return new INT();
                 } else {
                     env.err.error(pos,"Expected integer arguments.");
                     return ERROR.instance();
                 }
            }
            case EQ:{
            	  if ((t1.coerceTo(PrimTy.INT_T)) && (t2.coerceTo(PrimTy.INT_T))) {
                      return PrimTy.INT_T; // return new INT();
                   } else {
                       env.err.error(pos,"Expected integer arguments.");
                       return ERROR.instance();
                   }
            }
            case NE: {
                if (t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)) {
                    return PrimTy.INT_T;
                } else {
                     env.err.error(pos,"Incompatible types for comparison.");
                     return ERROR.instance();
                 }
            }
            
            case GE: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            	
            }
            case GT: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            }
            case LE: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            }
            case LT: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            }
            case AND: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            }
            case OR: {
            	if( t1.coerceTo(t2) && t1.coerceTo(PrimTy.INT_T)){
            		return PrimTy.INT_T;
            	} else {
                    env.err.error(pos,"Incompatible types for comparison.");
                    return ERROR.instance();
                }
            }
            
            
            
        }
    	return null; 
    }

    public void visit(ExpRecord e) {
    	if(env.venv.get(e.type) != null){
    		List<Pair<Symbol, Exp>> runner =  e.fields;
    		while(runner != null){
    			runner.head.snd.accept(this);
    			if (env.tenv.get(runner.head.fst).coerceTo(this.ty)){
    				runner = runner.tail;
    			}else{
    				this.ty = ERROR.instance();
    				break;
    			}
    		}
    	}else{
    		this.ty = ERROR.instance();
    	}
    	

    }

    public void visit(ExpSeq e) {
    	if(e.list == null){
    		ty = PrimTy.VOID_T;
    	}else{
    		List<Exp> runner = e.list;
    		while(runner != null){
    			runner.head.accept(this);
    			runner = runner.tail;
    		}
    	}
    	

    }

    public void visit(ExpString e) {
    	this.ty = PrimTy.STRING_T;
    }

    public void visit(ExpVar e) {
    	e.var.accept(this);
    	
    }

    public void visit(ExpWhile e) {
    	e.test.accept(this);
    	Type test = ty;
    	e.body.accept(this);
    	Type body = ty;
    	if(test.coerceTo(PrimTy.INT_T) && (body.coerceTo(PrimTy.VOID_T))){
    		this.ty = PrimTy.VOID_T;
    	}else{
    		env.err.error(e.getPos(), "Incorrect WHILE expression.");
    		this.ty = ERROR.instance();
    	}

    }

    public void visit(TyArray t) {

    	if(env.tenv.get(t.typ)!= null){
    		this.ty = env.tenv.get(t.typ);
    	}else{
    		this.ty = ERROR.instance();
    	}

    }

    public void visit(TyName tn) {
        Type t = env.tenv.get(tn.name);
        if (t == null) {
            env.err.error(tn.getPos(), "Undefined type.");
            this.ty = ERROR.instance();
        } else{
            this.ty = t;
        }
    }

    public void visit(TyRecord t) {
    	t.accept(this);
    	ty = new RECORD(visitTypeFields(t.fields));

    }

    public void visit(VarField v) {
    	v.var.accept(this);
    	Type var = this.ty;
    	if(env.venv.get(v.field)!= null){
    		this.ty = var;
    	}else{
    		this.ty = ERROR.instance();
    	}
    }

    public void visit(VarSimple v) {
    	if(env.venv.get(v.name)!= null){
    		VarEntry varEntry = (VarEntry) env.venv.get(v.name);
    		this.ty = varEntry.ty;
    	}else{
    		this.ty = ERROR.instance();
    	}
    }

    public void visit(VarSubscript v) {
    	v.index.accept(this);
    	Type index = this.ty;
    	v.var.accept(this);
    	Type var = this.ty;
    	if(index.coerceTo(PrimTy.INT_T) && var != null ){
    		this.ty = var; 
    	}else{
    		this.ty = ERROR.instance();
    	}
  
    }

	@Override
	public void visit(ExpInt e) {
		this.ty = PrimTy.INT_T;
		
	}

}
