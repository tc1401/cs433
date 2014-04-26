/**
 *  Interface with constants for the four primitive types:
 *  INT, NIL, STRING, and VOID	
 */

package tigerc.semant.types;

/**
 * @author jlasseter
 *
 */
public interface PrimTy {
	Type INT_T = INT.instance();
	Type NIL_T = NIL.instance();
	Type STRING_T = STRING.instance();
	Type VOID_T = VOID.instance();
}
