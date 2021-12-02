import tw.walter.stack.WSL;

import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String... args) {

		List<String> argsList = Arrays.asList(args);

		WSL wsl = new WSL();

		if (argsList.contains("--wsl-test-lib")) {
			wsl.executeFile("libs/lib.wsl");
			wsl.executeFile("libs/test_lib.wsl");
			wsl.execute("\"Tests passed\" uprintln");
		}

		if (argsList.contains("--wsl-print-env")) {
			wsl.__debug_print_env();
		}
		
		// WSL top level
		wsl.topLevel();

	}

}
