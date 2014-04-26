package tigerc.semant.types;

import tigerc.util.Symbol;

public class THUNK implements Type {
	public Symbol name;
	private Type binding;

	public THUNK(Symbol n) {
		name = n;
	}

	public boolean isLoop() {
		Type b = binding;
		boolean any;
		binding = null;
		if (b == null)
			any = true;
		else if (b instanceof THUNK)
			any = ((THUNK) b).isLoop();
		else
			any = false;
		binding = b;
		return any;
	}

	public Type actual() {
		return binding.actual();
	}

	public boolean coerceTo(Type t) {
		return this.actual().coerceTo(t);
	}

	public void bind(Type t) {
		binding = t;
	}

	public String toString() {
		return name.toString();
	}
}
