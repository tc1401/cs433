package tigerc.util;

public class List<T> {
    // NB:  This really should implement IAbsyn, as well, since
    // the position in the source code is needed for error reporting
    
   public final T head;
   public List<T> tail;
   public List(T h, List<T> t) {head=h; tail=t;}
}
