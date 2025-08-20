// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.tic.core.TICCore;
import enedis.tic.core.TICCoreBase;
import enedis.tic.service.client.TIC2WebSocketClientPool;
import enedis.tic.service.client.TIC2WebSocketClientPoolBase;
import enedis.tic.service.config.TIC2WebSocketConfiguration;
import enedis.tic.service.netty.TIC2WebSocketServer;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandler;
import enedis.tic.service.requesthandler.TIC2WebSocketRequestHandlerBase;
import picocli.CommandLine;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.ParseResult;

/**
 * Class used to handle the application
 */
public class TIC2WebSocketApplication
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Project properties (see file project.properties)
	 */
	public static final Properties PROJECT_PROPERTIES = new Properties();

	static
	{
		try
		{
			InputStream stream = TIC2WebSocketApplication.class.getClassLoader().getResourceAsStream("TIC2WebSocket.properties");

			if (stream == null)
			{
				System.err.println("Can't find projet property file!");
			}
			else
			{
				TIC2WebSocketApplication.PROJECT_PROPERTIES.load(stream);
			}
		}
		catch (Exception exception)
		{
			System.err.println("Can't load projet property file!");
			exception.printStackTrace(System.err);
		}
	}

	/**
	 * Project name ("project_name" from project.properties)
	 */
	public static final String	NAME		= PROJECT_PROPERTIES.getProperty("project_name", "");

	/**
	 * Project version ("project_version" from project.properties)
	 */
	public static final String	VERSION		= PROJECT_PROPERTIES.getProperty("project_version", "");

	/**
	 * Project description ("project_description" from project.properties)
	 */
	public static final String	DESCRIPTION	= PROJECT_PROPERTIES.getProperty("project_description", "");

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// TYPES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// STATIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Program entry point
	 *
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			TIC2WebSocketApplication application = new TIC2WebSocketApplication(args);
			int result = application.run();
			System.exit(result);
		}
		catch (Exception exception)
		{
			exception.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String[]					commandLineArgs;
	private CommandLine					commandLineParser;
	private TIC2WebSocketCommandLine		commandLine;
	private Logger						logger;
	private boolean						started;
	private TIC2WebSocketConfiguration		configuration;

	private TIC2WebSocketClientPool		clientPool;
	private TIC2WebSocketServer 		server;
	private TIC2WebSocketRequestHandler	requestHandler;
	private TICCore						ticCore;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// interfaceName
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Application constructor
	 *
	 * @param args
	 *            Command line arguments
	 */
	public TIC2WebSocketApplication(String[] args)
	{
		this.commandLineArgs = args;
		this.commandLine = new TIC2WebSocketCommandLine();
		this.commandLineParser = new CommandLine(this.commandLine);
		this.commandLineParser.setCommandName(TIC2WebSocketApplication.NAME);
		this.commandLineParser.getCommandSpec().versionProvider(new IVersionProvider()
		{
			@Override
			public String[] getVersion() throws Exception
			{
				return new String[] { TIC2WebSocketApplication.NAME + " v" + TIC2WebSocketApplication.VERSION };
			}
		});
		this.commandLineParser.registerConverter(Level.class, new TIC2WebSocketCommandLine.VerboseLevelConverter());
		this.updateLoggerConfiguration(Level.ERROR);
		this.started = false;
	}

	/**
	 * Application execution
	 *
	 * @return 0 if success, else an error code
	 * @throws InterruptedException
	 *             If the application thread gets interrupted
	 * @see TIC2WebSocketApplicationErrorCode
	 */
	public int run() throws InterruptedException
	{
		int result = this.init();

		if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code())
		{
			return result;
		}
		result = this.start();
		if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code())
		{
			return result;
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				TIC2WebSocketApplication.this.stop();
			}
		}));

		while (this.isStarted())
		{
			Thread.sleep(1000);
		}

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	/**
	 * Application initialization
	 *
	 * @return 0 if success, else an error code
	 * @see TIC2WebSocketApplicationErrorCode
	 */
	public int init()
	{
		if (this.isStarted())
		{
			this.logger.error("Application already started!");
			return TIC2WebSocketApplicationErrorCode.APPLICATION_ALREADY_STARTED.code();
		}
		int result = this.parseCommandLine();
		if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code())
		{
			return result;
		}
		result = this.updateLoggerConfiguration(this.commandLine.verboseLevel);
		if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code())
		{
			return result;
		}

		result = this.loadConfiguration();
		if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code())
		{
			return result;
		}

		this.logger.info(TIC2WebSocketApplication.NAME + " initialized");

		this.ticCore = new TICCoreBase(this.configuration.getTicMode(), this.configuration.getTicPortNames());
		this.clientPool = new TIC2WebSocketClientPoolBase();
		this.requestHandler = new TIC2WebSocketRequestHandlerBase(this.ticCore);

		this.server = new TIC2WebSocketServer("localhost", this.configuration.getServerPort().intValue(),
				this.clientPool, this.requestHandler);

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	/**
	 * Application start-up
	 *
	 * @return 0 if success, else an error code
	 */
	public int start()
	{
		if (this.commandLineParser.isUsageHelpRequested())
		{
			this.commandLineParser.usage(System.out);
		}
		if (this.commandLineParser.isVersionHelpRequested())
		{
			this.commandLineParser.printVersionHelp(System.out);
		}
		this.logger.info(TIC2WebSocketApplication.NAME + " starting");

		try
		{
			this.ticCore.start();
			this.server.start();
		}
		catch (Exception exception)
		{
			this.logger.error(exception.getMessage(), exception);
			return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
		}

		this.logger.info(TIC2WebSocketApplication.NAME + " started");
		this.started = true;

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	/**
	 * Application start-up indicator
	 *
	 * @return true if the application is started, false otherwise
	 */
	public boolean isStarted()
	{
		return this.started;
	}

	/**
	 * Application stop
	 *
	 * @return 0 if success, else an error code
	 * @see TIC2WebSocketApplicationErrorCode
	 */
	public int stop()
	{
		if (!this.isStarted())
		{
			this.logger.error("Application not started!");
			return TIC2WebSocketApplicationErrorCode.APPLICATION_NOT_STARTED.code();
		}

		try
		{
			this.server.stop();
			this.ticCore.stop();
		}
		catch (Exception exception)
		{
			this.logger.error(exception.getMessage(), exception);
			return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
		}

		this.logger.info(TIC2WebSocketApplication.NAME + " stopped");
		this.started = false;

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int parseCommandLine()
	{
		try
		{
			ParseResult parseResult = this.commandLineParser.parseArgs(this.commandLineArgs);
			if (parseResult.errors().size() > 0)
			{
				this.logger.error("Invalid command line!");
				for (int i = 0; i < parseResult.errors().size(); i++)
				{
					this.logger.error(parseResult.errors().get(i).getMessage());
				}
				return TIC2WebSocketApplicationErrorCode.COMMAND_LINE_INVALID.code();
			}
		}
		catch (Exception exception)
		{
			this.logger.error("Invalid command line!");
			this.logger.error(exception.getMessage());
			return TIC2WebSocketApplicationErrorCode.COMMAND_LINE_INVALID.code();
		}

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	private int updateLoggerConfiguration(Level level)
	{
		try
		{
			System.setProperty("log4j.configurationFile", "log4j2-" + level.name().toLowerCase() + ".xml");
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			context.reconfigure();
			this.logger = LogManager.getLogger();
		}
		catch (Exception exception)
		{
			System.err.println("Log configuration update failure!");
			System.err.println(exception.getMessage());
			return TIC2WebSocketApplicationErrorCode.UPDATE_LOGGER_CONFIGURATION_FAILURE.code();
		}

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
	}

	private int loadConfiguration()
	{
		String configFile = null;

		if (this.commandLine.configFile != null)
		{
			configFile = this.commandLine.configFile;
		}
		else
		{
			configFile = System.getProperty("configFile", NAME + "Configuration.json");
		}
		File configPath = new File(configFile);
		if (!configPath.exists())
		{
			this.logger.error("No configuration file path '" + configPath.getAbsolutePath() + "' not found");
			return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
		}
		try
		{
			this.logger.info("Loading configuration file " + configPath);
			this.configuration = (TIC2WebSocketConfiguration) DataDictionaryBase.fromFile(configPath, TIC2WebSocketConfiguration.class);
		}
		catch (Exception exception)
		{
			this.logger.error("Loading configuration file " + configPath + " failed", exception);
			return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
		}

		return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();

	}
}