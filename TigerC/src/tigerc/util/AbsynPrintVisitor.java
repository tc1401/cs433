package tigerc.util;

import tigerc.syntax.absyn.*;
import tigerc.util.List;
import tigerc.util.Pair;
 
public class AbsynPrintVisitor implements IAbsynVisitor {

	private java.io.PrintStream out;
	private int wspace = 0;
	private static final int TAB = 2; // 2 spaces

	public AbsynPrintVisitor(java.io.PrintStream o) {
		if (o != null)
			out = o;
		else
			out = System.err;
	}

	@Override
	public void visit(DeclGroupFunction d) {
		sayln(wspace, "DeclGroupFunction(");

		wspace += TAB;
		prFnDecls(d.fns);
		wspace -= TAB;
		_say(")");
	}

	@Override
	public void visit(DeclGroupType d) {
		sayln(wspace, "DeclGroupType(");

		wspace += TAB;
		prTyDecls(d.decls);
		wspace -= TAB;
		_say(")");

	}


	@Override
	public void visit(DeclVar d) {
		say(wspace, "DeclVar(");
		_say(d.name.toString());
		_sayln(",");

		wspace += TAB;
		if (d.typ != null) {
			say(wspace, d.typ.toString());
			_sayln(",");
		}
		d.init.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpArray e) {
		sayln(wspace, "ArrayExp(");

		wspace += TAB;
		say(wspace, e.typ.toString());
		_sayln(",");
		e.size.accept(this);
		_sayln(",");
		e.init.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpAssign e) {
		sayln(wspace, "ExpAssign(");

		wspace += TAB;
		e.var.accept(this);
		_sayln(",");
		e.exp.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpBreak e) {
		say(wspace, "ExpBreak()");
	}

	@Override
	public void visit(ExpCall e) {
		sayln(wspace, "ExpCall(");

		wspace += TAB;
		say(wspace, e.func.toString());
		_sayln(",");
		prExplist(e.args);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpFor e) {
		sayln(wspace, "ExpFor(");

		wspace += TAB;
		say(wspace, e.var.toString());
		_sayln(",");
		e.lo.accept(this);
		_sayln(",");
		e.hi.accept(this);
		_sayln(",");
		e.body.accept(this);
		wspace -= TAB;

		_say(")");

	}

	@Override
	public void visit(ExpIf e) {
		sayln(wspace, "ExpIf(");

		wspace += TAB;
		e.test.accept(this);
		_sayln(",");
		e.thenclause.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpIfElse e) {
		sayln(wspace, "ExpIfElse(");
		wspace += TAB;

		e.test.accept(this);
		_sayln(",");
		e.thenclause.accept(this);
		_sayln(",");
		e.elseclause.accept(this);

		wspace -= TAB;
		_say(")");
	}

	@Override
	public void visit(ExpInt e) {
		say(wspace, "ExpInt(");
		_say("" + e.value);
		_say(")");
	}

	@Override
	public void visit(ExpLet e) {
		sayln(wspace, "ExpLet(");

		wspace += TAB;
		prDecList(e.decls);
		_sayln(",");
		e.body.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpNil e) {
		say(wspace, "ExpNil()");
	}

	@Override
	public void visit(ExpOp e) {
		sayln(wspace, "ExpOp(");

		wspace += TAB;
		say(wspace, e.oper.toString());
		_sayln(",");
		e.left.accept(this);
		_sayln(",");
		e.right.accept(this);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpRecord e) {
		say(wspace, "ExpRecord(");
		_say(e.type.toString());
		_sayln(",");

		wspace += TAB;
		prFieldExpList(e.fields);
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(ExpSeq e) {
		sayln(wspace, "ExpSeq(");
		wspace += TAB;
		prExplist(e.list);
		wspace -= TAB;
		_say(")");
	}

	@Override
	public void visit(ExpString e) {
		say(wspace, "ExpString(");
		_say(e.value);
		_say(")");
	}

	@Override
	public void visit(ExpVar e) {
		sayln(wspace, "ExpVar(");
		wspace += TAB;
		e.var.accept(this);
		wspace += TAB;
		_say(")");

	}

	@Override
	public void visit(ExpWhile e) {
		sayln(wspace, "ExpWhile(");
		wspace += TAB;

		e.test.accept(this);
		_sayln(",");
		e.body.accept(this);

		wspace -= TAB;
		_say(")");
	}

	@Override
	public void visit(TyArray t) {
		say(wspace, "TyArray(");
		_say(t.typ.toString());
		_say(")");
	}

	@Override
	public void visit(TyName t) {
		say(wspace, "TyName(");
		_say(t.name.toString());
		_say(")");

	}

	@Override
	public void visit(TyRecord t) {
		sayln(wspace, "TyRecord(");
		wspace += TAB;
		prFieldlist(t.fields);
		wspace -= TAB;
		_say(")");
	}

	@Override
	public void visit(VarField v) {
		sayln(wspace, "VarField(");

		wspace += TAB;
		v.var.accept(this);
		_sayln(",");
		say(wspace, v.field.toString());
		wspace -= TAB;

		_say(")");
	}

	@Override
	public void visit(VarSimple v) {
		say(wspace, "VarSimple(");
		_say(v.name.toString());
		_say(")");
	}

	@Override
	public void visit(VarSubscript v) {
		sayln(wspace, "VarSubscript(");

		wspace += TAB;
		v.var.accept(this);
		_sayln(",");
		v.index.accept(this);
		wspace -= TAB;

		_say(")");
	}

	// ///////////////////////// UTILITY METHODS //////////////////////

	void indent(int d) {
		for (int i = 0; i < d; i++)
			out.print(' ');
	}

	private void _say(String s) {
		out.print(s);
	}

	private void say(int wsp, String s) {
		indent(wsp);
		_say(s);
	}

	private void _sayln(String s) {
		_say(s);
		_say("\n");
	}

	private void sayln(int wsp, String s) {
		indent(wsp);
		_sayln(s);
	}

	private void prFieldlist(List<Pair<Symbol, Symbol>> f) {
		sayln(wspace, "[List of Fields:");
		wspace += TAB;

		for (List<Pair<Symbol, Symbol>> cur = f; cur != null; cur = cur.tail) {
			say(wspace, cur.head.fst.toString());
			_say(":");
			_say(cur.head.snd.toString());
			if (cur.tail != null)
				_sayln(",");
			else
				_sayln("");
		}

		wspace -= TAB;
		say(wspace, " ]");
	}

	private void prFieldExpList(List<Pair<Symbol, Exp>> f) {
		sayln(wspace, "[List of Record Fields:");
		wspace += TAB;

		for (List<Pair<Symbol, Exp>> cur = f; cur != null; cur = cur.tail) {
			say(wspace, "(");
			_say(cur.head.fst.toString());
			_sayln(":");
			wspace += TAB;
			cur.head.snd.accept(this);
			wspace -= TAB;
			_say(")");
			if (cur.tail != null)
				_sayln(",");
			else
				_sayln("");
		}

		wspace -= TAB;
		say(wspace, " ]");
	}

	private void prExplist(List<Exp> es) {
		sayln(wspace, "[List of Exps:");
		wspace += TAB;

		for (List<Exp> cur = es; cur != null; cur = cur.tail) {
			cur.head.accept(this);
			if (cur.tail != null)
				_sayln(",");
			else
				_sayln("");
		}

		wspace -= TAB;
		say(wspace, " ]");
	}

	private void prDecList(List<Decl> ds) {
		sayln(wspace, "[List of Decls:");
		wspace += TAB;

		for (List<Decl> cur = ds; cur != null; cur = cur.tail) {
			cur.head.accept(this);
			if (cur.tail != null)
				_sayln(",");
			else
				_sayln("");
		}

		wspace -= TAB;
		say(wspace, " ]");
	}

	private void prFnDecls(List<DeclFn> ds) {
		sayln(wspace, "[List of Function Decls:");
		wspace += TAB;

		for (List<DeclFn> cur = ds; cur != null; cur = cur.tail) {
			DeclFn f = cur.head;
			say(wspace, f.name.toString());
			_sayln(",");

			wspace += TAB;
			prFieldlist(f.params);
			_sayln(",");
			if (f.resultTy != null) {
				say(wspace, f.resultTy.name.toString());
			} else {
				say(wspace, "resultTy == null");

			}
			_sayln(",");
			f.body.accept(this);
			if (cur.tail != null)
				_sayln(",");
			else
				_sayln("");
			wspace -= TAB;
		} // for

		wspace -= TAB;
		say(wspace, " ]");
	} // prFnDecls

	private void prTyDecls(List<DeclTy> ts) {
		sayln(wspace, "[List of Type Decls:");
		wspace += TAB;

		for (List<DeclTy> cur = ts; cur != null; cur = cur.tail) {
			DeclTy t = cur.head;

			wspace += TAB;
			say(wspace,t.name.toString());_sayln(",");

			t.ty.accept(this);
			wspace -= TAB;
			
			if (cur.tail != null) {
				_sayln(",");
			} else {
				_sayln("");
			}
		} // for 
		
		wspace -= TAB;
		say(wspace,"]");
	} // prTyDecls

	
}
