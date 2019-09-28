import tw.walter.stack.WSL;

public class Main {

	public static void main(String... args) {

		WSL wsl = new WSL();
		// wsl.execute("123 456 add 789 sub string \"Hello world\\\" \\\\ \" concat print");

		//wsl.execute("\"Hello\" \"world\" \" \" exch concat \"!\n\" concat concat print");
		//wsl.execute("5.5 2 div string \"\\n\" concat print");
		//wsl.execute("6 7 mul string (hello world) \"\\n\" exch pop concat print");
		//wsl.execute("\"mygroup\" (\"function\\n\" print \"sub\" 123 def) def \"test\\n\" print mygroup mygroup.sub string \"\\n\" concat print");
		//wsl.execute("\"a\"(\"b\"(\"c\"(\"d\" \"coucou\" def)def a.b.c)def a.b)def a a.b.c.d print");

		wsl.executeFile("libs/lib.wsl");
		wsl.executeFile("libs/test_lib.wsl");
		wsl.execute("\"Tests passed\" uprintln");
		
		// WSL top level
		wsl.topLevel();
		
		//wsl.__debug_print_env();

	}

}
