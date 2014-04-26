package tigerc.semant.types;


public class ERROR implements Type {
	private ERROR() {
	}
	
	private static ERROR inst = new ERROR();

	public static ERROR instance() {
		return inst;
	}

	public Type actual() {
		return this;
	}

	public boolean coerceTo(Type t) {
		return false;
	}


}
