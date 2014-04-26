//package test;

import tigerc.syntax.parse.*;

import tigerc.util.ErrorMsg;


public class TestRDParser {
	public static void main(String[] args) throws java.io.IOException {
		String fname;
		java.io.InputStream inp;
		ErrorMsg errorMsg;
		try {
			if (args.length >= 1) {
				fname = args[0];
				errorMsg = new ErrorMsg(fname);
				inp = new java.io.FileInputStream(fname);
			} else {
				errorMsg = new ErrorMsg(null);
				inp = System.in;
			}

			TigerParseRD parser = new TigerParseRD(new TigerLex(inp, errorMsg),
					errorMsg);
			parser.parse();
			inp.close();
		} catch (java.io.FileNotFoundException e) {
			System.err.println("Cannot open input file " + args[0] + "");
			System.exit(1);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.toString());
		}
	}
}
