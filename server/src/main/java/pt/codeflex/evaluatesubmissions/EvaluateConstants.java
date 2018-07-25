package pt.codeflex.evaluatesubmissions;

import java.io.File;

import pt.codeflex.utils.Path;

public class EvaluateConstants {

	// Name of the account on the evaluate machine (ubuntu at EC2)
	public static final String SERVER_USER = "mbrito";
	
	// Path where files are created before getting sent to the compiler machine
	public static final String PATH_SPRING = System.getProperty("user.home") + Path.separator + "Submissions";
	
	// Path where testcases and code are temporarily stored before getting compiled and executed
	public static final String PATH_SERVER = "Submissions";
	
	// Base path used to isolate the execution through sandbox
	public static final String PATH_FIREJAIL = Path.separator + "home" +  Path.separator + 
			SERVER_USER + Path.separator + PATH_SERVER;

	// File name used to save code to posterior compilation
	public static final String CLASS_FILE_NAME = "Solution";


	// ERRORS
	public static final String COMPILER_ERROR = "compiler_error.txt";
	public static final String RUN_ERROR = "runtime_error.txt";
	

}
