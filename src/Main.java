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
		 * Ajouter mod et coder syrracuse en exemple
		 * Ajouter fact au group math
		 * Ajouter un mode pour générer un code texte à partir d'une liste de tokens (mode compact ou indenté)
"fact" (
	1 exch ** The result
	1 exch ** The counter that will be incremented
	(
		copy ** Copy the counter
		1 add 2 mb ** Increment it and move it back
		mul ** Multiply the result and the counter
		exch ** Move the result back
	) exch repeat ** Repeat the number of time given in the stack
	pop ** Remove the counter
) def
		 * Remplacer group_prefix et parent_prefix par current et parent, et ajouter current_prefix et parent_prefix dans la lib en ajoutant le point final
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
