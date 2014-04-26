package tigerc.syntax.parse;

import java.io.IOException;

import java_cup.runtime.Symbol;
import tigerc.util.ErrorMsg;

public class TigerParseRD {
	private Lexer lex;
	private ErrorMsg errorMsg;

	public TigerParseRD(Lexer lx, ErrorMsg err) throws java.io.IOException {
		if (lx == null)
			throw new java.io.IOException("Parser created with no input stream");
		if (err == null)
			throw new java.io.IOException(
					"Parser created without error reporting");

		this.errorMsg = err;
		this.lex = lx;
	}

	public void report_error(String message, java_cup.runtime.Symbol info) {
		errorMsg.error(info.left, message);
	}

	public void parse() {
		try {
			boolean success = E();
			if (success) {
				System.out.println("Accepted");
			} else {
				System.out.println("Rejected");
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			System.out.println("Rejected");
		}
	}

	private boolean match(int symID) {
		Symbol t = new Symbol(symID);
		try {
			System.out.print(t.sym + "  ");
			System.out.println(lex.peek().sym);
			if (lex.peek().sym == t.sym) { // maybe change this test
				lex.next_token();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 
	 * Create predict sets:
	 * 
	 * PREDICT(A) contains all tokens that could appear next on the token stream
	 * when we are expecting an A.
	 * 
	 * Predict(E) = {empty} Predict(Or) = {O'} Predict(OrPrime) = {|}
	 * Predict(And) = {AndPrime} Predict(AndPrime) = {&} Predict(Comparator) =
	 * {CompPrime} Predict(BinOperator) = {TT'} Predict(CompPrime) = {=, <, >,
	 * <>, <=, >=} Predict(Term) = {} Predict(TermPrime) = {+,-,empty}
	 * Predict(Factor) = {FactorPrime} Predict(FactorPrime) =
	 * {<intConst>,<StrConst>, <nil>, <break>, <id>, while, *} Predict(LValue) =
	 * {<id>} Predict(Decl) = {var} Predict(ESeq) = {ESeqPrime}
	 * Predict(ESeqPrime) = {;}
	 */

	private boolean E() {

		return Or() && OrPrime();
	}

	private boolean Or() {
		return And() && AndPrime();

	}

	private boolean OrPrime() {
		if (match(TigerSyms.OR))
			return E();
		return true;
	}

	private boolean And() {
		return Comparator() && BinOperation();
	}

	private boolean AndPrime() {
		if (match(TigerSyms.AND))
			return E();
		else
			return true;

	}

	private boolean Comparator() {
		return CompPrime() && BinOperation();
	}

	private boolean BinOperation() {
		return Term() && TermPrime();
	}

	private boolean CompPrime() {
		return match(TigerSyms.EQ) || match(TigerSyms.NEQ)
				|| match(TigerSyms.GT) || match(TigerSyms.LT)
				|| match(TigerSyms.GE) || match(TigerSyms.LE);
	}

	private boolean Term() {
		return Factor() && FactorPrime();
	}

	private boolean TermPrime() {
		if (match(TigerSyms.PLUS)) {
			return Term() && TermPrime();
		} else if (match(TigerSyms.MINUS)) {
			return Term() && TermPrime();
		}
		return true;
	}

	private boolean Factor() {

		if (match(TigerSyms.NIL))
			return true;
		else if (match(TigerSyms.INT))
			return true;
		else if (match(TigerSyms.STRING))
			return true;
		else if (match(TigerSyms.BREAK))
			return true;
		else if (match(TigerSyms.LPAREN)) {
			return ESeq() && match(TigerSyms.RPAREN);
		} else if (match(TigerSyms.UMINUS)) {
			return E();
		} else if (match(TigerSyms.FOR)) {
			return match(TigerSyms.ID) && match(TigerSyms.ASSIGN) && E()
					&& match(TigerSyms.TO) && E() && match(TigerSyms.DO) && E();
		} else if (match(TigerSyms.WHILE)) {
			return E() && match(TigerSyms.DO) && E();
		} else if (match(TigerSyms.LET)) {
			return Decl() && match(TigerSyms.IN) && ESeq()
					&& match(TigerSyms.END);

		} else if (match(TigerSyms.ID)) {
			return true;
		} else {
			try {
				report_error("Factor symbol messed up", lex.peek());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	private boolean FactorPrime() {
		if (match(TigerSyms.TIMES)) {
			return Factor() && FactorPrime();
		} else if (match(TigerSyms.DIVIDE)) {
			return Factor() && FactorPrime();
		} else {
			return true;
		}

	}

	private boolean LValue() {
		return match(TigerSyms.ID);
	}

	private boolean Decl() {
		if (match(TigerSyms.VAR)) {
			return match(TigerSyms.ID) && match(TigerSyms.ASSIGN) && E();
		} else {
			return true;
		}

	}

	private boolean ESeq() {
		return E() && ESeqPrime();
	}

	private boolean ESeqPrime() {
		if (match(TigerSyms.SEMICOLON)) {
			return ESeqPrime();
		} else {
			return true;
		}
	}

}