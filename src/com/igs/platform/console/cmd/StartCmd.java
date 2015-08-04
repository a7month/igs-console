package com.igs.platform.console.cmd;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

public class StartCmd extends AbstractCmd {

	public static final String DEFAULT_CONFIG_URI = "xbean:igs.xml";

	protected String[] helpFile = new String[] {
			"Task Usage: Main start [start-options] [uri]",
			"Description: Creates and starts a broker using a configuration file, or a broker URI.",
			"",
			"Start Options:",
			"    -D<name>=<value>      Define a system property.",
			"    --version             Display the version information.",
			"    -h,-?,--help          Display the start broker help information.",
			"",
			"URI:",
			"",
			"    XBean based broker configuration:",
			"",
			"        Example: Main xbean:file:activemq.xml",
			"            Loads the xbean configuration file from the current working directory",
			"        Example: Main xbean:activemq.xml",
			"            Loads the xbean configuration file from the classpath",
			"",
			"    URI Parameter based broker configuration:",
			"",
			"        Example: Main broker:(tcp://localhost:61616, tcp://localhost:5000)?useJmx=true",
			"            Configures the broker with 2 transport connectors and jmx enabled",
			"        Example: Main broker:(tcp://localhost:61616, network:tcp://localhost:5000)?persistent=false",
			"            Configures the broker with 1 transport connector, and 1 network connector and persistence disabled",
			"" };

	private URI configURI;

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public String getOneLineDescription() {
		return "Creates and starts a broker using a configuration file, or a broker URI.";
	}

	/**
	 * The default task to start a broker or a group of brokers
	 * 
	 * @param brokerURIs
	 */
	protected void runTask(List<String> brokerURIs) throws Exception {
		try {
			// If no config uri, use default setting
			if (brokerURIs.isEmpty()) {
				setConfigUri(new URI(DEFAULT_CONFIG_URI));
				startBroker(getConfigUri());

				// Set configuration data, if available, which in this case
				// would be the config URI
			} else {
				String strConfigURI;

				while (!brokerURIs.isEmpty()) {
					strConfigURI = (String) brokerURIs.remove(0);

					try {
						setConfigUri(new URI(strConfigURI));
					} catch (URISyntaxException e) {
						context.printException(e);
						return;
					}

					startBroker(getConfigUri());
				}
			}

			// Prevent the main thread from exiting unless it is terminated
			// elsewhere
		} catch (Exception e) {
			context.printException(new RuntimeException(
					"Failed to execute start task. Reason: " + e, e));
			throw new Exception(e);
		}

		// The broker start up fine. If this unblocks it's cause they were
		// stopped
		// and this would occur because of an internal error (like the DB going
		// offline)
		waitForShutdown();
	}

	/**
	 * Create and run a broker specified by the given configuration URI
	 * 
	 * @param configURI
	 * @throws Exception
	 */
	public void startBroker(URI configURI) throws Exception {
		createApplicationContext(configURI.getSchemeSpecificPart());
	}

	/**
	 * Wait for a shutdown invocation elsewhere
	 * 
	 * @throws Exception
	 */
	protected void waitForShutdown() throws Exception {
		final boolean[] shutdown = new boolean[] { false };

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
			}
		});

		synchronized (shutdown) {
			while (!shutdown[0]) {
				try {
					shutdown.wait();
				} catch (InterruptedException e) {
				}
			}
		}

	}

	/**
	 * Sets the current configuration URI used by the start task
	 * 
	 * @param uri
	 */
	public void setConfigUri(URI uri) {
		configURI = uri;
	}

	/**
	 * Gets the current configuration URI used by the start task
	 * 
	 * @return current configuration URI
	 */
	public URI getConfigUri() {
		return configURI;
	}

	/**
	 * Print the help messages for the browse command
	 */
	protected void printHelp() {
		context.printHelp(helpFile);
	}

	// ------------------

	protected ApplicationContext createApplicationContext(String uri)
			throws MalformedURLException {
		Resource resource = resourceFromString(uri);
		// LOG.debug("Using " + resource + " from " + uri);
		try {
			return new ResourceXmlApplicationContext(resource) {
				@Override
				protected void initBeanDefinitionReader(
						XmlBeanDefinitionReader reader) {
					reader.setValidating(true);
				}
			};
		} catch (FatalBeanException errorToLog) {
			// LOG.error("Failed to load: " + resource + ", reason: " +
			// errorToLog.getLocalizedMessage(), errorToLog);
			throw errorToLog;
		}
	}

	public static Resource resourceFromString(String uri)
			throws MalformedURLException {
		Resource resource;
		File file = new File(uri);
		if (file.exists()) {
			resource = new FileSystemResource(uri);
		} else if (ResourceUtils.isUrl(uri)) {
			resource = new UrlResource(uri);
		} else {
			resource = new ClassPathResource(uri);
		}
		return resource;
	}
}