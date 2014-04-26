package tigerc.semant.types;

public class ARRAY implements Type {
  public Type element;

  public ARRAY (Type e) { element = e;}

  public Type actual () { return this; }

  public boolean coerceTo (Type t) {
    return this==t.actual();
  }

  public String toString () {
    return "array of " + element.toString();
  }
}

