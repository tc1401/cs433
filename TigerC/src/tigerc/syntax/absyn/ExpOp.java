package tigerc.syntax.absyn;


public class ExpOp extends Exp {
	public enum Op {
		PLUS, MIN, MUL, DIV, EQ, NE, LT, LE, GT, GE, AND, OR
	};

	public Exp left, right;
	public Op oper;

	public ExpOp(int p, Exp l, Op o, Exp r) {
		super(p);
		left = l;
		oper = o;
		right = r;
	}
	
	public void accept(IAbsynVisitor v) {
		v.visit(this);
	}
}

