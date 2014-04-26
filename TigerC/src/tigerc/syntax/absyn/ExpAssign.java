package tigerc.syntax.absyn;


public class ExpAssign extends Exp {
	public Var var;
	public Exp exp;

	public ExpAssign(int p, Var v, Exp e) {
		super(p);
		var = v;
		exp = e;
	}

	public void accept(IAbsynVisitor v) {
		v.visit(this);
	}
}
