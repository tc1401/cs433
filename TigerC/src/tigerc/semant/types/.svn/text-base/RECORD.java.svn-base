package tigerc.semant.types;

import tigerc.util.Symbol;
import tigerc.util.List;
import tigerc.util.Pair;

public class RECORD implements Type {
	public final List<Pair<Symbol, Type>> fields;

	public Type actual() {
		return this;
	}

	public RECORD(List<Pair<Symbol, Type>> fs) {
		fields = fs;
	}

	public boolean coerceTo(Type t) {
		return this == t.actual();
	}

	public String toString() {
		String result = "{ ";
		List<Pair<Symbol, Type>> cur = fields;

		while (cur != null) {
			result = result + cur.head.fst + ":" + cur.head.snd;
			// fieldName:fieldtype
			cur = cur.tail;
			if (cur != null)
				result += ",";
		}

		return result + " }";
	}
}
