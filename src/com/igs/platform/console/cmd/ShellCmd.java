package com.igs.platform.console.cmd;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import com.igs.platform.console.CmdCtx;
import com.igs.platform.console.formatter.CommandShellOutputFormatter;

public class ShellCmd extends AbstractCmd {

	private boolean interactive;
	private String[] helpFile;

	public ShellCmd() {
		this(false);
	}

	public ShellCmd(boolean interactive) {
		this.interactive = interactive;
		ArrayList<String> help = new ArrayList<String>();
		help.addAll(Arrays
				.asList(new String[] {
						interactive ? "Usage: [task] [task-options] [task data]"
								: "Usage: Main [--extdir <dir>] [task] [task-options] [task data]",
						"", "Tasks:" }));

		ArrayList<Cmd> commands = getCommands();
		Collections.sort(commands, new Comparator<Cmd>() {
			@Override
			public int compare(Cmd command, Cmd command1) {
				return command.getName().compareTo(command1.getName());
			}
		});

		for (Cmd command : commands) {
			help.add(String.format("    %-24s - %s", command.getName(),
					command.getOneLineDescription()));
		}

		help.addAll(Arrays
				.asList(new String[] {
						"",
						"Task Options (Options specific to each task):",
						"    --extdir <dir>  - Add the jar files in the directory to the classpath.",
						"    --version       - Display the version information.",
						"    -h,-?,--help    - Display this help information. To display task specific help, use "
								+ (interactive ? "" : "Main ")
								+ "[task] -h,-?,--help",
						"",
						"Task Data:",
						"    - Information needed by each specific task.",
						"",
						"JMX system property options:",
						"    -Dactivemq.jmx.url=<jmx service uri> (default is: 'service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi')",
						"    -Dactivemq.jmx.user=<user name>",
						"    -Dactivemq.jmx.password=<password>", "" }));

		this.helpFile = help.toArray(new String[help.size()]);
	}

	@Override
	public String getName() {
		return "shell";
	}

	@Override
	public String getOneLineDescription() {
		return "Runs the activemq sub shell";
	}

	/**
	 * Main method to run a command shell client.
	 * 
	 * @param args
	 *            - command line arguments
	 * @param in
	 *            - input stream to use
	 * @param out
	 *            - output stream to use
	 * @return 0 for a successful run, -1 if there are any exception
	 */
	public static int main(String[] args, InputStream in, PrintStream out) {

		CmdCtx context = new CmdCtx();
		context.setFormatter(new CommandShellOutputFormatter(out));

		// Convert arguments to list for easier management
		List<String> tokens = new ArrayList<String>(Arrays.asList(args));

		ShellCmd main = new ShellCmd();
		try {
			main.setCommandContext(context);
			main.execute(tokens);
			return 0;
		} catch (Exception e) {
			context.printException(e);
			return -1;
		}
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	/**
	 * Parses for specific command task.
	 * 
	 * @param tokens
	 *            - command arguments
	 * @throws Exception
	 */
	protected void runTask(List<String> tokens) throws Exception {

		// Process task token
		if (tokens.size() > 0) {
			Cmd command = null;
			String taskToken = (String) tokens.remove(0);

			for (Cmd c : getCommands()) {
				if (taskToken.equals(c.getName())) {
					command = c;
					break;
				}
			}
			if (command == null) {
				if (taskToken.equals("help")) {
					printHelp();
				} else {
					printHelp();
				}
			}

			if (command != null) {
				command.setCommandContext(context);
				command.execute(tokens);
			}
		} else {
			printHelp();
		}

	}

	ArrayList<Cmd> getCommands() {
		ServiceLoader<Cmd> loader = ServiceLoader.load(Cmd.class);
		ArrayList<Cmd> rc = new ArrayList<Cmd>();
		for (Cmd command : loader) {
			rc.add(command);
		}
		return rc;
	}

	/**
	 * Print the help messages for the browse command
	 */
	protected void printHelp() {
		context.printHelp(helpFile);
	}
}