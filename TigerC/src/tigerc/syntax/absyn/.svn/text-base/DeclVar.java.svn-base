package tigerc.syntax.absyn;

import tigerc.util.Symbol;

public class DeclVar extends Decl {
	public final Symbol name;
	public final Symbol typ; /* optional */
	public final Exp init;

	public DeclVar(int p, Symbol n, Symbol t, Exp i) {
		super(p);
		name = n;
		typ = t;
		init = i;
	}

	public void accept(IAbsynVisitor v) {
		v.visit(this);
	}
}
