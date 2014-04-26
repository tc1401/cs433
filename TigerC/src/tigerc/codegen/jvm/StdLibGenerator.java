package tigerc.codegen.jvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class StdLibGenerator {

	public static void makeStdLibrary() {
		if (new File("TigerStdLib.class").exists())
			return;

		PrintWriter stdlibOut = null;
		try {
			stdlibOut = new PrintWriter(new File("TigerStdLib.j"));
		} catch (FileNotFoundException e) {
			System.err.println("fatal error:  cannot create standard library.");
			System.exit(1);
		}

		// Write out the prelude for the TigerStdLib class
		new JVMGeneratorV(stdlibOut).emitPrelude("TigerStdLib",
				"standard library for the Tiger language");
		stdlibPrint(stdlibOut);
		stdlibPrinti(stdlibOut);
		stdlibFlush(stdlibOut);
		stdLibGetChar(stdlibOut);
		stdlibOrd(stdlibOut);
		stdlibChr(stdlibOut);
		stdlibSize(stdlibOut);
		stdlibSubstring(stdlibOut);
		stdlibConcat(stdlibOut);
		stdlibNot(stdlibOut);
		stdlibExit(stdlibOut);
		// remaining standard library definitions go here
	}

	
	private static void stdlibExit(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibNot(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibConcat(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibSubstring(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibSize(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibChr(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibOrd(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdLibGetChar(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}

	private static void stdlibFlush(PrintWriter out) {
		// TODO Auto-generated method stub
		
		assert out != null;		
		out.println(".method flush()V");
		out.println(".limit stack 2");
		out.println(".limit locals 3");
		out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
		out.println("aload_1");
		out.println("invokevirtual java/io/PrintStream/flush()V");
		out.println("return");
		out.println(".end method");
		
		
	}

	private static void stdlibPrinti(PrintWriter out) {
		// TODO Auto-generated method stub

		assert out != null;		
		out.println(".method print(I)V");
		out.println(".limit stack 2");
		out.println(".limit locals 3");
		out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
		out.println("aload_1");
		out.println("invokevirtual java/io/PrintStream/print(I)V");
		out.println("return");
		out.println(".end method");
		
	}

	// //// Standard library definitions
	private static void stdlibPrint(PrintWriter out) {
		assert out != null;		
		out.println(".method print(Ljava/lang/String;)V");
		out.println(".limit stack 2");
		out.println(".limit locals 3");
		out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
		out.println("aload_1");
		out.println("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");
		out.println("return");
		out.println(".end method");
		
		
	}
}