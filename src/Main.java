import java.io.File;
import java.io.FileInputStream;

import tw.walter.stack.WSL;

public class Main {

    public static void main(String... args) {
        
        /*
         * TODO
         * X Comment loop
         * Catch stack overflow on recursive call
         * X Detect sub-group element calling "group1.sub.toto"
         * X Check for variable name format with def
         * X Create HashMap to associate String name to Token on def call
         */
        
        
        WSL wsl = new WSL();
        //wsl.execute("123 456 add 789 sub string \"Hello world\\\" \\\\ \" concat print");
        
        /*wsl.execute("\"Hello\" \"world\" \" \" exch concat \"!\n\" concat concat print");
        wsl.execute("5.5 2 div string \"\\n\" concat print");
        wsl.execute("6 7 mul string (hello world) \"\\n\" exch pop concat print");
        wsl.execute("\"mygroup\" (\"function\\n\" print \"sub\" 123 def) def \"test\\n\" print mygroup mygroup.sub string \"\\n\" concat print");
        
        wsl.execute("\"a\"(\"b\"(\"c\"(\"d\" \"coucou\" def)def a.b.c)def a.b)def a a.b.c.d print");*/
        
        wsl.execute(readFile("lib.wsl"));
        wsl.execute("wsl.copyright println");
        wsl.execute("6 4 sum uprintln");
        //wsl.__debug_print_env();
        
    }
    
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
