package tigerc.semant.types;

public class NIL implements Type {
	private NIL() {
	}

	private static NIL inst = new NIL();

	public static NIL instance() {
		return inst;
	}

	public Type actual() {
		return this;
	}

	public boolean coerceTo(Type t) {
		Type a = t.actual();
		return (a instanceof RECORD) || (a instanceof NIL);
	}

	public String toString() {
		return "nil";
	}
}
