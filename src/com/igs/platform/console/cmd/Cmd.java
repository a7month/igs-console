package com.igs.platform.console.cmd;

import java.util.List;

import com.igs.platform.console.CmdCtx;

public interface Cmd {
	String getName();

	String getOneLineDescription();

	void setCommandContext(CmdCtx context);

	/**
	 * Execute the specified command
	 * 
	 * @param tokens
	 *            - arguments to the command
	 * @throws Exception
	 */
	void execute(List<String> tokens) throws Exception;
}
