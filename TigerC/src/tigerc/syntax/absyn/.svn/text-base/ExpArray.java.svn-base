package tigerc.syntax.absyn;

import tigerc.util.Symbol;

public class ExpArray extends Exp {
	public final Symbol typ;
	public final Exp size, init;

	public ExpArray(int p, Symbol t, Exp s, Exp i) {
		super(p);
		typ = t;
		size = s;
		init = i;
	}

	public void accept(IAbsynVisitor v) {
		v.visit(this);
	}
}
