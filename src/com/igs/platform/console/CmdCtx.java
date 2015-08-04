package com.igs.platform.console;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import javax.management.AttributeList;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import com.igs.platform.console.formatter.OutputFormatter;

public class CmdCtx {
	private OutputFormatter formatter;

	/**
	 * Retrieve the output stream being used by the global formatter
	 * 
	 * @return formatter's output stream
	 */
	public OutputStream getOutputStream() {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		return formatter.getOutputStream();
	}

	/**
	 * Print an ObjectInstance format of an mbean
	 * 
	 * @param mbean
	 *            - mbean to print
	 */
	public void printMBean(ObjectInstance mbean) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMBean(mbean);
	}

	/**
	 * Print an ObjectName format of an mbean
	 * 
	 * @param mbean
	 *            - mbean to print
	 */
	public void printMBean(ObjectName mbean) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMBean(mbean);
	}

	/**
	 * Print an AttributeList format of an mbean
	 * 
	 * @param mbean
	 *            - mbean to print
	 */
	public void printMBean(AttributeList mbean) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMBean(mbean);
	}

	/**
	 * Print a Map format of an mbean
	 * 
	 * @param mbean
	 */
	@SuppressWarnings("rawtypes")
	public void printMBean(Map mbean) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMBean(mbean);
	}

	/**
	 * Print a Collection format of mbeans
	 * 
	 * @param mbean
	 *            - collection of mbeans
	 */
	@SuppressWarnings("rawtypes")
	public void printMBean(Collection mbean) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMBean(mbean);
	}

	/**
	 * Print a Map format of a JMS message
	 * 
	 * @param msg
	 */
	@SuppressWarnings("rawtypes")
	public void printMessage(Map msg) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMessage(msg);
	}

	/**
	 * Print a collection of JMS messages
	 * 
	 * @param msg
	 *            - collection of JMS messages
	 */
	@SuppressWarnings("rawtypes")
	public void printMessage(Collection msg) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printMessage(msg);
	}

	/**
	 * Print help messages
	 * 
	 * @param helpMsgs
	 *            - help messages to print
	 */
	public void printHelp(String[] helpMsgs) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printHelp(helpMsgs);
	}

	/**
	 * Print an information message
	 * 
	 * @param info
	 *            - information message to print
	 */
	public void printInfo(String info) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printInfo(info);
	}

	/**
	 * Print an exception message
	 * 
	 * @param e
	 *            - exception to print
	 */
	public void printException(Exception e) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printException(e);
	}

	/**
	 * Print a version information
	 * 
	 * @param version
	 *            - version info to print
	 */
	public void printVersion(String version) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.printVersion(version);
	}

	/**
	 * Print a generic key value mapping
	 * 
	 * @param map
	 *            to print
	 */
	@SuppressWarnings("rawtypes")
	public void print(Map map) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.print(map);
	}

	/**
	 * Print a generic array of strings
	 * 
	 * @param strings
	 *            - string array to print
	 */
	public void print(String[] strings) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.print(strings);
	}

	/**
	 * Print a collection of objects
	 * 
	 * @param collection
	 *            - collection to print
	 */
	@SuppressWarnings("rawtypes")
	public void print(Collection collection) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.print(collection);
	}

	/**
	 * Print a java string
	 * 
	 * @param string
	 *            - string to print
	 */
	public void print(String string) {
		if (formatter == null) {
			throw new IllegalStateException(
					"No OutputFormatter specified. Use GlobalWriter.instantiate(OutputFormatter).");
		}
		formatter.print(string);
	}

	public OutputFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(OutputFormatter formatter) {
		this.formatter = formatter;
	}
}
