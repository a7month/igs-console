package com.igs.platform.console.cmd;

import java.util.List;

import com.igs.platform.console.CmdCtx;

public abstract class AbstractCmd implements Cmd {

	 public static final String COMMAND_OPTION_DELIMETER = ",";

	    private boolean isPrintHelp;
	    private boolean isPrintVersion;

	    protected CmdCtx context;

	    public void setCommandContext(CmdCtx context) {
	        this.context = context;
	    }
	    
	    /**
	     * Execute a generic command, which includes parsing the options for the
	     * command and running the specific task.
	     * 
	     * @param tokens - command arguments
	     * @throws Exception
	     */
	    public void execute(List<String> tokens) throws Exception {
	        // Parse the options specified by "-"
	        parseOptions(tokens);

	        // Print the help file of the task
	        if (isPrintHelp) {
	            printHelp();

	            // Print the AMQ version
	        } else if (isPrintVersion) {
//	            context.printVersion(ActiveMQConnectionMetaData.PROVIDER_VERSION);

	            // Run the specified task
	        } else {
	            runTask(tokens);
	        }
	    }

	    /**
	     * Parse any option parameters in the command arguments specified by a '-'
	     * as the first character of the token.
	     * 
	     * @param tokens - command arguments
	     * @throws Exception
	     */
	    protected void parseOptions(List<String> tokens) throws Exception {
	        while (!tokens.isEmpty()) {
	            String token = tokens.remove(0);
	            if (token.startsWith("-")) {
	                // Token is an option
	                handleOption(token, tokens);
	            } else {
	                // Push back to list of tokens
	                tokens.add(0, token);
	                return;
	            }
	        }
	    }

	    /**
	     * Handle the general options for each command, which includes -h, -?,
	     * --help, -D, --version.
	     * 
	     * @param token - option token to handle
	     * @param tokens - succeeding command arguments
	     * @throws Exception
	     */
	    protected void handleOption(String token, List<String> tokens) throws Exception {
	        isPrintHelp = false;
	        isPrintVersion = false;
	        // If token is a help option
	        if (token.equals("-h") || token.equals("-?") || token.equals("--help")) {
	            isPrintHelp = true;
	            tokens.clear();

	            // If token is a version option
	        } else if (token.equals("--version")) {
	            isPrintVersion = true;
	            tokens.clear();
	        } else if (token.startsWith("-D")) {
	            // If token is a system property define option
	            String key = token.substring(2);
	            String value = "";
	            int pos = key.indexOf("=");
	            if (pos >= 0) {
	                value = key.substring(pos + 1);
	                key = key.substring(0, pos);
	            }
	            System.setProperty(key, value);
	        } else {
	            // Token is unrecognized
	            context.printInfo("Unrecognized option: " + token);
	            isPrintHelp = true;
	        }
	    }

	    /**
	     * Run the specific task.
	     * 
	     * @param tokens - command arguments
	     * @throws Exception
	     */
	    protected abstract void runTask(List<String> tokens) throws Exception;

	    /**
	     * Print the help messages for the specific task
	     */
	    protected abstract void printHelp();

}
