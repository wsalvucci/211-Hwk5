package turtle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LanguageInterpreter {

	public static void main(String[] args) {
		Grammar grammar = new Grammar(new File("testProgramStep1.txt"));
	}

}
