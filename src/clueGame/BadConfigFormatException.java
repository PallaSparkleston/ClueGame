package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Throwable {

	public BadConfigFormatException() throws FileNotFoundException {
		System.out.println("Bad configuration for file. Please check your Board configuration files.");
		
		// Print to a file (extra credit)
		PrintWriter writer = new PrintWriter("ExceptionLog.txt");
		writer.println("\r\nError: BadConfigFormatException. Bad configuration for file. Please check your Board configuration files.\r\n");
		writer.close();
	}
	
	public BadConfigFormatException(String filename) throws FileNotFoundException {
		System.out.println("Bad configuration for file. Please check " + filename);
		
		// Print to a file (extra credit)
		PrintWriter writer = new PrintWriter("ExceptionLog.txt");
		writer.println("\r\nError: BadConfigFormatException. Bad configuration for file. Please check your " + filename + "\r\n");
		writer.close();
	}
}
