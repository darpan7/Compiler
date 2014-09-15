
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;


public class Compiler141 {
	List<String> final_output = new ArrayList<String>();
	List<String> store_tokens = new ArrayList<String>();
	int index = 0;
	int counts = 0;
	String sysGoal = "";
	String Lparen = ""; String Rparen = ""; String Stm = ""; String StmList = ""; String Read = ""; String Begin = ""; String End = "";
	String Id = ""; String Semicolon = ""; String Write = ""; String before = "";
	int increase = 0; int check = 0; int d = 1; int h=1;
	
	
	public Compiler141() {	
	}
	public void parseTokens(File output_file, int counts) {
		try {
			this.counts = counts;
			BufferedReader input = new BufferedReader(new FileReader(output_file));
			try {
				String line = null;
				while(( line = input.readLine()) != null) {
					store_tokens.add(line);
				}
			} catch(IOException ie) {
				ie.printStackTrace();
			} finally {
				input.close();
			}
		} catch(IOException ie) {
			ie.printStackTrace();
		}
		Proc_SystemGoal();
		Proc_Program(index, counts);
		System.out.println("Call Finish ");
		System.out.println("*************** HALT ");
	}
	
	public void Proc_SystemGoal() {
		System.out.println(" Call SystemGoal");		
	}
	public void Proc_Program(int index, int counts) {
		System.out.println("Call Program");
		int id_1 = matchBegin(index, counts);
		int id_2 = CallStatementLists(id_1, counts);
		int id_3 = matchEnd(id_2);
		System.out.println("Match EndSym");
		
	}
	public int matchBegin(int index, int counts) {
		String str = store_tokens.get(index);
		if(str.equals("BEGIN")) {
			System.out.println("Match Begin");
			Begin = "begin";
			
			int i = index + 1; this.index = this.index + 1;
			return i;
		}
		else return 0;
	}
	public int CallStatementLists(int id_1, int counts) {
		System.out.println("Call StatementList");
		this.counts = counts;
		for(int i=0; i<counts; i++) {
			 CallStatement(id_1, counts);	 
		}
		return this.index;
	}
	public void CallStatement(int id_1, int counts) {
		System.out.println("Call Statement");
		id_1 = this.index;
		String st = store_tokens.get(id_1);
		if(st.equals("READ")) {
			System.out.println("Match Read");
			Read = "read";
			id_1 = this.index;
			++id_1; this.index++;
			if(store_tokens.get(id_1).equals("LParen")) {
				System.out.println("Match LParen");
				
				Lparen = "(";
				++id_1; this.index++;
				System.out.println("Call IdList");
				int id_temp = Idlists(id_1); this.index = id_temp;
				Rparen = ")";
				++id_temp; ++this.index;
				if(store_tokens.get(id_temp).equals("SemiColon")) {
					System.out.println("Match SemiColon");
				
					Semicolon = ";";
					id_1 = this.index;
					++id_1;
					++this.index;
				} 
				else {
					System.out.println("Error occurred during parsing Semicolon inside Read");
				}
			}
			else {
				System.out.println("Error occurred during parsing READ");
			}
		}
		else if(st.equals("WRITE")) {
			System.out.println("Match Write");
			
			
			id_1 = this.index;
			
			Write = "write";
			++id_1; ++this.index;
			if(store_tokens.get(id_1).equals("LParen")) {
				System.out.println("Match LParen");
			
				Lparen = "(";
				++id_1; this.index++;
				int id_temp = Exprsnlists(id_1); this.index = id_temp;
				Rparen = ")";
				++id_temp; ++this.index;
				if(store_tokens.get(id_temp).equals("RParen")) {
					final_output.add("RParen");
					id_1 = this.index;
					++id_1;
					++this.index;
				}
				else {
					System.out.println("Error occurred during parsing RParen inside write");
				}
				if(store_tokens.get(id_1).equals("SemiColon")) {
					final_output.add(";");
					Semicolon = ";";
					id_1 = this.index;
					++id_1;
					++this.index;
				} 
				else {
					System.out.println("Error occurred during parsing Semicolon inside write");
				}
			}
			else {
				System.out.println("Error occurred during parsing WRITE");
			}
		}
		else if(!st.equals("WRITE") && !st.equals("READ") && !st.equals("END") && st.matches("[a-zA-Z0-9_]+")) {
			
			String Identifier = st;
			counts = this.counts; Stm = "";
			System.out.println("Call Ident");
			System.out.println("Match "+ st);
			System.out.println("****************** Declare "+ st + " , Integer");
			id_1 = this.index;
			Id = "";
			
			Id = Id + "Id";
			++id_1; ++this.index;
			if(store_tokens.get(id_1).equals("AssignOp")) {
				final_output.add(":=");
				System.out.println("Match AssignOp");
				Id = Id + ":=";
				++id_1; ++this.index;
				while(!store_tokens.get(id_1).equals("SemiColon")) {
					int ret = _Expression(id_1);
					
					id_1 = ret; this.index = ret;
					++id_1;
				}
				
				Semicolon = ";";
				id_1 = this.index;
				++id_1; ++this.index; int inct = increase - 1;
				_Assign(Identifier, "Temp&"+inct);
				System.out.println("Match SemiColon");
			}
			else System.out.println("Error occurred during parsing AssignOp :" + store_tokens.get(id_1) + " at - "+id_1);
			
		}
		else {
			System.out.println("Error occurred during parsing No TOKEN MATCHED!!!");
		}
	}
	public void _Assign(String st1, String st2) {
		System.out.println(" ********* Store " + st2 + ", " + st1);
	}
	public int Idlists(int id_1) {
		List<String> _sublist = store_tokens.subList(id_1, store_tokens.size());
		int _index = _sublist.indexOf("RParen");
		_index = id_1 + _index;
		Id = "";
		System.out.println("Call Ident(result)");
		for(int i = id_1; i<_index; i++) {
			if(store_tokens.get(i).equals("Comma")) {
				System.out.println("Match Comma");
				Id = Id + ",";
			}
			else if (store_tokens.get(i).matches("[a-zA-Z0-9_]+")) {
				System.out.println("Match " + store_tokens.get(i));
				System.out.println("*********** Declare " + store_tokens.get(i) + " , Integer");
				Id = Id + "Id";
			}
			else System.out.println("Error occurred during parsing Idlists");
		}
		System.out.println("Match RParen");
		return _index;
	}
	public int Exprsnlists(int id_1) {
		System.out.println("Call ExprList");
		System.out.println("Call Expression");
		List<String> _sublist = store_tokens.subList(id_1, store_tokens.size());
		int __index = _Expression(id_1);	// returns next token;
		if(store_tokens.get(__index).equals("Comma")) {
			System.out.println("Match Comma");
			__index = __index + 1;
			int x = Exprsnlists(__index);
			return x;
		}
		else {
			return 0;
		}
		
	}
	public int _Expression(int id_1) {
		System.out.println("Call Expression["+d+"]" ); ++d;
		int left = _Primary(id_1);
		if(left > 0 ) {
			if(store_tokens.get(left+1).equals("PlusOp") || store_tokens.get(left+1).equals("MinusOp")) {
				System.out.println("Match "+ store_tokens.get(left+1));
				int right = left + 1; ++right; // ++right is for next token;
				int __right = _Expression(right);
				int incr = increase - 1;
				if(check > 0) {
					String result = GenInfix("Temp&"+incr, store_tokens.get(right), store_tokens.get(left)); 
					check = 1;
					return __right;
				}
				else {
					String result = GenInfix(store_tokens.get(left), store_tokens.get(right), store_tokens.get(__right));
					check = 1;
					return __right;
				}	
			}
			else {
				return left;
			}
		} 
		else return 0;	
	}
	
	public int _Primary(int id_1) {
		System.out.println("Call Primary["+h+"]"); ++h;
		if(store_tokens.get(id_1).equals("LParen")) {
			System.out.println("Match LParen");
			int _res = _Expression(id_1+1);
			if(store_tokens.get(_res+1).equals("RParen")) {
				System.out.println("Match RParen");
			}
			return _res + 1;
		}
		else if (store_tokens.get(id_1).matches("[0-9]+")) {
			System.out.println("Match " + store_tokens.get(id_1));
			return id_1;
		}
		else if (store_tokens.get(id_1).matches("[a-zA-Z0-9_]+")) {
			System.out.println("Call Ident");
			System.out.println("Match " + store_tokens.get(id_1));
			System.out.println("***************** Declare " + store_tokens.get(id_1) + " Integer");
			return id_1;
		}
		else {
			System.out.println("System Error inside Primary Call");
			return 0;
		}
	}
	public String GenInfix(String left, String op, String right) {
		System.out.println("  Call GenInfix");
		System.out.println("***************** Declare Temp&" + increase + ", Integer");
		System.out.println("***************** Add "+ left + "," + " "+ right + "," + "Temp&" + increase);
		int tem = increase; ++increase;
		return "Temp&"+tem+"";
	}
	
	public int matchEnd(int id_2) {
		String str = store_tokens.get(id_2);
		if(str.equals("END")) {
			return id_2;
		}
		else return 0;
	}
	public static void main(String[] args) {
		Compiler141 parser = new Compiler141();
		File testFile = new File("D:\\Fall 2014\\Compiler - Boris\\Programs\\input2.txt");
		File testFile1 = new File("D:\\Fall 2014\\Compiler - Boris\\Programs\\input.txt");
		System.out.println(" ");
		Scanner14 scanner = new Scanner14();
		List<String> output = scanner.generateTokens(testFile);
		scanner.displayTokens(output);
		System.out.println("+++++++++++++++++++ Scanned..!!! +++++++++++++++++++");
		System.out.println("+++++++++++++++++++ Parsed..!!!  +++++++++++++++++++");
		System.out.println("+++++++++++++++++++ Compiling... +++++++++++++++++++");
		System.out.println(" ");
		File output_file = new File("D:\\Fall 2014\\Compiler - Boris\\Programs\\output.txt");
		parser.parseTokens(output_file, scanner.getCounts());
		System.out.println(" ");
		System.out.println("+++++++++++++++++++ Compiled..!!! +++++++++++++++++++");
	}
}