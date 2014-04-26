package tigerc.semant.types;

public interface Type {

	Type actual();

	boolean coerceTo(Type t);
}
