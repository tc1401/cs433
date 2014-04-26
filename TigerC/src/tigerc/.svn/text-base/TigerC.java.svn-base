package tigerc;
/*************************************************************************
 **  tigerc/Main.java 
 **
 **  Author:  John Lasseter
 **  Created:  04/11/2014
 **  Last Modified: 04/11/2014
 **
 **  Driver for the tigerc compiler. 
 ** 
 ************************************************************************/

import tigerc.syntax.parse.*;
import tigerc.syntax.absyn.*;

import tigerc.util.ErrorMsg;
import tigerc.util.AbsynPrintVisitor;
import tigerc.util.Symbol;
import tigerc.semant.SemantV;
import tigerc.codegen.jvm.JVMGeneratorV;
import tigerc.codegen.*;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.PrintWriter;

public class TigerC {
	public static void main(String[] args) {
		String fname = null;
		InputStream inp = null;
		PrintWriter outp = null;
		ErrorMsg errorMsg = null;
		try {
			if (args.length >= 1) {
				fname = args[0];
				if (fname.endsWith(".tig")) {
					errorMsg = new ErrorMsg(fname);
					inp = new FileInputStream(fname);
					outp = new PrintWriter(new File(fname.substring(0,
							fname.length() - 4)
							+ ".j"));
				} else {
					System.err.println("error: source format " 
							+ fname.substring(fname.length()-4) + " not recognized.");
					System.exit(1);
				}
			} else {
				errorMsg = new ErrorMsg(null);
				inp = System.in;
				outp = new PrintWriter(System.out);
			}

			TigerParse parser = new TigerParse(new TigerLex(inp, errorMsg),
					errorMsg);
			IAbsyn prog = (IAbsyn) parser./* debug_ */parse().value;
			inp.close();

			SemantV typechecker = new SemantV(errorMsg);
			prog.accept(typechecker);

			if (errorMsg.anyErrors) {
				System.err.println("Error - no code was generated.");
				System.exit(1);
			} else {
				ICodegen generator = new JVMGeneratorV(outp);
				String classname = fname.substring(0,fname.length() - 4);
				generator.emitPrelude(classname, "YOU JUST GOT COMPILED");
				generator.emitStdLibrary();
				generator.emitProcedures();
				generator.emitMain();
				System.err.println("Code written to " + fname.substring(0,fname.length()-4) + ".j");
				System.err.println("JVM byte code can be produced using Jasmin.");
				System.exit(0);
			}
		} catch (java.io.FileNotFoundException e) {
			System.err.println("Cannot open input file " + args[0] + "");
			System.exit(1);
		} catch (Throwable e) {
			System.err.println("Error: " + e);
			e.printStackTrace();
		}
	}
}