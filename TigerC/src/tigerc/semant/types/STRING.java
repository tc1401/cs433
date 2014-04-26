package tigerc.semant.types;

public class STRING implements Type {
	private STRING() {
	}

	private static STRING inst = new STRING();

	public static STRING instance() {
		return inst;
	}

	public Type actual() {
		return this;
	}

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof STRING);
	}

	public String toString() {
		return "string";
	}
}
