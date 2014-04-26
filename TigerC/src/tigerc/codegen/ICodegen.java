package tigerc.codegen;


public interface ICodegen {
	void emitPrelude(String classname, String initComment);
	// There is almost always some amount of boilerplate that
	// must begin an executable file.  See, for example "0xCAFEBABE".
	
	void emitStdLibrary();  
	// Either generate code for library functions directly 
	// or generate code for linking to existing binaries.
	
	void emitProcedures();  
	// code for each defined function/method/procedure
	
	void emitMain();
	// code for the "main" procedure, if applicable
	// (in Tiger, it always is)
}
