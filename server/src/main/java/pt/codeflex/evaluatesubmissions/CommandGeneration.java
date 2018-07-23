package pt.codeflex.evaluatesubmissions;

import static pt.codeflex.evaluatesubmissions.EvaluateConstants.PATH_SERVER;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.codeflex.databasemodels.Submissions;

import static pt.codeflex.evaluatesubmissions.EvaluateConstants.CLASS_FILE_NAME;

public class CommandGeneration {

	public static final Logger LOGGER = Logger.getLogger(CommandGeneration.class.getName());
	
	public static CompilationInfo compilation(Submissions submission) {

		long uniqueId = submission.getId();
		String command = "cd " + PATH_SERVER + File.separator + uniqueId + "_" + submission.getLanguage().getName()
				+ " && ";
		String suffix = "";
		String compilerError = EvaluateConstants.compilerErrorFile(uniqueId);

		// TODO : add memory limit
		// TODO : load compiler commands from files ?!
		
		switch (submission.getLanguage().getCompilerName()) {
		case "Java 8":
			command += "javac " + CLASS_FILE_NAME + ".java 2> " + compilerError; 
			suffix = ".java";
			break;
		case "C++11 (gcc 5.4.0)":
			command += "g++ -std=c++11 -o " + CLASS_FILE_NAME + "_exec_" + uniqueId + " " + CLASS_FILE_NAME + ".cpp 2> "
					+ compilerError;
			suffix = ".cpp";
			break;
		case "Python 2.7":
			// TODO : get a compiler for python
			break;
		case "C# (mono 4.2.1)":
			command += "mcs -out:" + CLASS_FILE_NAME + "_exec_" + uniqueId + " " + CLASS_FILE_NAME + ".cs 2> "
					+ compilerError;
			suffix = ".cs";
			break;
		default:
			LOGGER.log(Level.WARNING, "Compiler command not found!");
			break;
		}
		
		return new CompilationInfo(command, suffix);
	}
	
	

}

class CompilationInfo {

	private String command;
	private String languageSuffix;

	public CompilationInfo(String command, String languageSuffix) {
		this.command = command;
		this.languageSuffix = languageSuffix;
	}
	
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLanguageSuffix() {
		return languageSuffix;
	}

	public void setLanguageSuffix(String languageSuffix) {
		this.languageSuffix = languageSuffix;
	}

}