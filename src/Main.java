import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import tw.walter.stack.WSL;

public class Main {

	public static void main(String... args) {
		
		/*
		 * TODO
		 * prompt: User input from stdin
		 * random: [0,1] float random number
		 * Group "number" in wsl.lib
		 *   - "nan"
		 *   - "not_a_number"
		 *   - "is_nan"
		 * Group "string"
		 * ... one group per data type kois
		 * Stacktrace for errors
		 * Including files
		 * Pretty reccursive string value for TGroup
		 */

		WSL wsl = new WSL();
		// wsl.execute("123 456 add 789 sub string \"Hello world\\\" \\\\ \" concat print");

		//wsl.execute("\"Hello\" \"world\" \" \" exch concat \"!\n\" concat concat print");
		//wsl.execute("5.5 2 div string \"\\n\" concat print");
		//wsl.execute("6 7 mul string (hello world) \"\\n\" exch pop concat print");
		//wsl.execute("\"mygroup\" (\"function\\n\" print \"sub\" 123 def) def \"test\\n\" print mygroup mygroup.sub string \"\\n\" concat print");
		//wsl.execute("\"a\"(\"b\"(\"c\"(\"d\" \"coucou\" def)def a.b.c)def a.b)def a a.b.c.d print");

		wsl.execute(readFile("lib.wsl"));
		wsl.execute(readFile("test_lib.wsl"));
		wsl.execute("\"Tests passed\" uprintln");
		
		// WSL top level
		@SuppressWarnings( "resource" )
		Scanner scanner = new Scanner(System.in);
		while (true) {
		    // Get the line and execute it
		    System.out.print("\nwsl> ");
		    String inputString = scanner.nextLine();
		    wsl.execute(inputString);
		}
		
		//wsl.__debug_print_env();

	}

	// Read an entire file
	public static String readFile(String path) {
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			return new String(data, "UTF-8");
		} catch (Exception e) {
			System.err.println("Error: Unable to read file!");
			return "";
		}
	}

}
