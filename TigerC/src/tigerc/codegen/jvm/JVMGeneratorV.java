/*
    JVMGenerator.java
    
    Tiger code generator, targeting the JVM
    
    Author:  John Lasseter and <your name here>
    History: 
        04/13/2014 (jhel) created

 */

package tigerc.codegen.jvm;

import tigerc.semant.types.PrimTy;
import tigerc.semant.types.Type;
import tigerc.syntax.absyn.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Stack;

public class JVMGeneratorV implements tigerc.codegen.ICodegen,
		tigerc.syntax.absyn.IAbsynVisitor {

	private Stack<Label> labelStack = new Stack<Label>();
	
	private tigerc.semant.Env env; 
	
	private PrintWriter out;

	private Type currentT; 
	
	public JVMGeneratorV() {
		this(System.out);
	}

	public JVMGeneratorV(PrintWriter o) {
		assert o != null;
		out = o;
	}

	public JVMGeneratorV(PrintStream o) {
		assert o != null;
		out = new PrintWriter(o);
	}

	/*
	 * In particular, you are to complete the visit methods for the following
	 * AST constructs: ExpInt, ExpString, ExpNil, ExpOp, ExpIf, ExpIfElse,
	 * ExpSeq, ExpBreak, and ExpWhile
	 */
	/**************** ICodegen implementation ************************/

	@Override
	public void emitPrelude(String classname, String initComment) {
		// TODO Auto-generated method stub
		emitComment("class setup instructions");
		emitLn(".class " + classname);
		emitLn(".super java/lang/Object");
		emitLn(".method public <init>()V");
		emitLn("aload_0");
		emitLn("invoke nonvirtual java/lang/Object/<init>()V");
		emitLn("return");
		emitComment("end initial setup for class " + classname);
	}

	@Override
	public void emitMain() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}
	
	@Override
	public void emitStdLibrary() {
		StdLibGenerator.makeStdLibrary();
	}

	@Override
	public void emitProcedures() {
		// TODO Auto-generated method stub

	}

	/**************** IAbsynVisitor implementation **********************/

	@Override
	public void visit(DeclGroupFunction d) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(DeclGroupType d) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(DeclVar d) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpArray e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpAssign e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpBreak e) {
		emitLn("goto " + labelStack.peek());
		this.currentT = PrimTy.VOID_T;
	}

	@Override
	public void visit(ExpCall e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpFor e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpIf e) {
		Label endLabel = new Label("end");
		Label thenLabel = new Label("then");
		e.test.accept(this);
		emitLn("ifeq " + endLabel);
		emitLn(thenLabel.toString() + ": ");
		e.thenclause.accept(this);
		emitLn(endLabel.toString() + ": ");

	}

	/*
	 * 
	 * 
	 * int a<b
	 * code for a
	 * code for b
	 * if_icmplt T
	 * iconst_0 
	 * goto E
	 * T: iconst_1
	 * E: ...
	 * 
	 * 
	 * string a<b 
	 * code for a
	 * code for b
	 * invoke virtual Ljava/lang/String/compareTo(Ljava/lang/String;)Z //boolean
	 * iflt T
	 * iconst_0
	 * goto E
	 * T: iconst_1
	 * E:
	 * 
	 * 
	 * (non-Javadoc)
	 * @see tigerc.syntax.absyn.IAbsynVisitor#visit(tigerc.syntax.absyn.ExpIfElse)
	 */
	
	@Override
	public void visit(ExpIfElse e) {
		Label endLabel = new Label("end");
		Label thenLabel = new Label("then");
		Label elseLabel = new Label("else");
		e.test.accept(this);
		emitLn("ifeq " + elseLabel);
		emitLn(thenLabel.toString() + ": ");
		e.thenclause.accept(this);
		emitLn("goto "+ endLabel.toString());
		emitLn(elseLabel.toString() + ": ");
		e.elseclause.accept(this);
		emitLn(endLabel.toString() + ": ");
		
	}

	@Override
	public void visit(ExpInt e) {
		emit("ldc " + e.value);
		this.currentT = PrimTy.INT_T;
	}

	@Override
	public void visit(ExpLet e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpNil e) {

		e.accept(this);
		emitLn("aconst_null");
		this.currentT = PrimTy.NIL_T;
	}

	@Override
	public void visit(ExpOp e) {
		switch (e.oper) {
			case AND: {
				e.left.accept(this);
				e.right.accept(this);
				emit("iand ");
				break;
			}
			case DIV: {
				e.left.accept(this);
				e.right.accept(this);
				emit("idiv ");
				break;
			}
			
			case EQ: {
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				
				
				emitLn("if_icmpne " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
				
			}
			case GE: {	
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				emitLn("if_icmplt " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
				
			}
			case GT: {	
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				emitLn("if_icmple " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
			
			}
			
			case LE: {
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				emitLn("if_icmpgt " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
			}
			
			case LT: {
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				emitLn("if_icmpge " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
			}
			case MIN: {
				e.left.accept(this);
				e.right.accept(this);
				emit("isub ");
				break;
			}
			case MUL: {
				e.left.accept(this);
				e.right.accept(this);
				emit("imul ");
				break;
			}
			case NE: {
				e.left.accept(this);
				e.right.accept(this);
				Label f = new Label("false");
				Label end = new Label("end");
				emitLn("if_icmpeq " + f.toString() );
				emitLn("iconst_1");
				emitLn("goto"+ end);
				emitLn(f.toString() + ": ");
				emitLn("iconst_0");
				emitLn(end+": ");
				break;
			}
			case OR: {
				e.left.accept(this);
				e.right.accept(this);
				emit("ior ");
				break;
			}
			case PLUS: {
				e.left.accept(this);
				e.right.accept(this);
				emit("iadd ");
				break;
			}
		}
		
	}

	@Override
	public void visit(ExpRecord e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpSeq e) {
		tigerc.util.List<Exp> runner = e.list;
		while (runner != null) {
			runner.head.accept(this);
			runner = runner.tail;
		}
	}

	@Override
	public void visit(ExpString e) {
		// e.accept(this);
		emit("ldc " + e.value);
		this.currentT = PrimTy.STRING_T;

	}

	@Override
	public void visit(ExpVar e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(ExpWhile e) {

		Label loopBegin = new Label("loopBegin");
		Label loopEnd = new Label("loopEnd");
		emitLn(loopBegin.toString() + ": ");
		e.test.accept(this);
		// loopEnd added to the stack
		labelStack.push(loopEnd);
		emitLn("iconst_1");
		emitLn("if_icmpne " + loopEnd.toString());
		// emits all of the code for the body.
		e.body.accept(this);
		labelStack.pop();
		this.currentT = PrimTy.VOID_T;
	}

	@Override
	public void visit(TyArray t) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(TyName t) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(TyRecord t) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(VarField v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(VarSimple v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	@Override
	public void visit(VarSubscript v) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");

	}

	/************************* private methods *******************************/

	private void emit(String s) {
		out.println(s);
	}

	private void emitLn(String s) {
		emit(s + "\n");
	}

	private void emitComment(String comment) {
		emitLn("; " + comment);
	}

}
