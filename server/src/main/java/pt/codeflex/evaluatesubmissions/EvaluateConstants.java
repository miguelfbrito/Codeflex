package pt.codeflex.evaluatesubmissions;

import java.io.File;

public class EvaluateConstants {

	// Name of the account on the evaluate machine (ubuntu at EC2)
	public static final String SERVER_USER = "mbrito";
	
	// Path where files are created before getting sent to the compiler machine
	public static final String PATH_SPRING = System.getProperty("user.home") + File.separator + "Submissions";
	
	// Path where testcases and code are temporarily stored before getting compiled and executed
	public static final String PATH_SERVER = "Submissions";
	
	// Base path used to isolate the execution through sandbox
	public static final String PATH_FIREJAIL = File.separator + "home" +  File.separator + 
			SERVER_USER + File.separator + PATH_SERVER;

	// File name used to save code to posterior compilation
	public static final String CLASS_FILE_NAME = "Solution";

	
	// --------------------------------------------------------------------------------------------------------//
	
	// Format of the name of the file created when a compiler error is thrown
	public static String compilerErrorFile(long id) {
		return "compiler_error" + id + ".txt";
	}
}
