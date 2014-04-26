package tigerc.syntax.absyn;

import tigerc.util.List;
import tigerc.util.Symbol;

public class ExpCall extends Exp {
	public Symbol func;
	public List<Exp> args;

	public ExpCall(int p, Symbol f, List<Exp> a) {
		super(p);
		func = f;
		args = a;
	}

	public void accept(IAbsynVisitor v) {
		v.visit(this);
	}
}
