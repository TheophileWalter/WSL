import java.util.Scanner;

import tw.walter.stack.WSL;

public class Main {

	public static void main(String... args) {
		
		/*
		 * TODO
		 * prompt: User input from stdin
		 * Group "string"
		 * ... one group per data type kois
		 * Including files
		 * mmb function (to move back multiple values, ex: "0 0 0 0 8 9 2 3 mmb" -> "0 8 9 0 0 0")
		 */

		WSL wsl = new WSL();
		// wsl.execute("123 456 add 789 sub string \"Hello world\\\" \\\\ \" concat print");

		//wsl.execute("\"Hello\" \"world\" \" \" exch concat \"!\n\" concat concat print");
		//wsl.execute("5.5 2 div string \"\\n\" concat print");
		//wsl.execute("6 7 mul string (hello world) \"\\n\" exch pop concat print");
		//wsl.execute("\"mygroup\" (\"function\\n\" print \"sub\" 123 def) def \"test\\n\" print mygroup mygroup.sub string \"\\n\" concat print");
		//wsl.execute("\"a\"(\"b\"(\"c\"(\"d\" \"coucou\" def)def a.b.c)def a.b)def a a.b.c.d print");

		wsl.executeFile("lib.wsl");
		wsl.executeFile("test_lib.wsl");
		wsl.execute("\"Tests passed\" uprintln");
		
		// WSL top level
		@SuppressWarnings( "resource" )
		Scanner scanner = new Scanner(System.in);
		while (true) {
		    // Get the line and execute it
		    System.out.print("\nwsl> ");
		    String inputString = scanner.nextLine();
		    wsl.execute(inputString, "<stdin>");
		}
		
		//wsl.__debug_print_env();

	}

}
