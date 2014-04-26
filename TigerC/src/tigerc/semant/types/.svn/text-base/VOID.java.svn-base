package tigerc.semant.types;

public class VOID implements Type {
	private VOID() {
	}

	private static VOID inst = new VOID();

	public static VOID instance() {
		return inst;
	}

	public Type actual() {
		return this;
	}

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof VOID);
	}

	public String toString() {
		return "void";
	}
}
