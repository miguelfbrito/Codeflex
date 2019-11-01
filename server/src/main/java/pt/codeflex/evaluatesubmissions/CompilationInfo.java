package pt.codeflex.evaluatesubmissions;

public class CompilationInfo {

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