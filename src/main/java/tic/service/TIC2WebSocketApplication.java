// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.service;

import enedis.lab.types.datadictionary.DataDictionaryBase;
import enedis.tic.core.TICCore;
import enedis.tic.core.TICCoreBase;
import tic.service.client.TIC2WebSocketClientPool;
import tic.service.client.TIC2WebSocketClientPoolBase;
import tic.service.config.TIC2WebSocketConfiguration;
import tic.service.netty.TIC2WebSocketServer;
import tic.service.requesthandler.TIC2WebSocketRequestHandler;
import tic.service.requesthandler.TIC2WebSocketRequestHandlerBase;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import picocli.CommandLine;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.ParseResult;

/**
 * Main application class for TIC2WebSocket.
 *
 * <p>This class manages the lifecycle of the TIC2WebSocket application, including initialization,
 * configuration loading, command-line parsing, server startup and shutdown, and error handling.
 * It integrates with the core TIC logic, client pool, request handler, and Netty server to provide
 * a complete WebSocket-based interface for TIC data exchange.
 *
 * <p>Responsibilities include:
 * <ul>
 *   <li>Parsing command-line arguments and loading configuration
 *   <li>Starting and stopping the TIC2WebSocket server and core
 *   <li>Managing application state and error codes
 *   <li>Handling logging configuration and shutdown hooks
 *   <li>Providing entry point and main execution loop
 * </ul>
 *
 * @author Enedis Smarties team
 * @see TIC2WebSocketServer
 * @see TIC2WebSocketClientPool
 * @see TIC2WebSocketRequestHandler
 * @see TICCore
 */
public class TIC2WebSocketApplication {
  /** Project properties loaded from TIC2WebSocket.properties. */
  public static final Properties PROJECT_PROPERTIES = new Properties();

  static {
    try {
      InputStream stream =
          TIC2WebSocketApplication.class
              .getClassLoader()
              .getResourceAsStream("TIC2WebSocket.properties");

      if (stream == null) {
        System.err.println("Can't find projet property file!");
      } else {
        TIC2WebSocketApplication.PROJECT_PROPERTIES.load(stream);
      }
    } catch (Exception exception) {
      System.err.println("Can't load projet property file!");
      exception.printStackTrace(System.err);
    }
  }

  /** Project name ("project_name" from project.properties). */
  public static final String NAME = PROJECT_PROPERTIES.getProperty("project_name", "");

  /** Project version ("project_version" from project.properties). */
  public static final String VERSION = PROJECT_PROPERTIES.getProperty("project_version", "");

  /** Project description ("project_description" from project.properties). */
  public static final String DESCRIPTION =
      PROJECT_PROPERTIES.getProperty("project_description", "");

  /**
   * Program entry point for TIC2WebSocket.
   *
   * <p>Initializes and runs the application, handling any startup errors.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      TIC2WebSocketApplication application = new TIC2WebSocketApplication(args);
      int result = application.run();
      System.exit(result);
    } catch (Exception exception) {
      exception.printStackTrace(System.err);
      System.exit(-1);
    }
  }

  private String[] commandLineArgs;
  private CommandLine commandLineParser;
  private TIC2WebSocketCommandLine commandLine;
  private Logger logger;
  private boolean started;
  private TIC2WebSocketConfiguration configuration;

  private TIC2WebSocketClientPool clientPool;
  private TIC2WebSocketServer server;
  private TIC2WebSocketRequestHandler requestHandler;
  private TICCore ticCore;

  /**
   * Constructs a new TIC2WebSocketApplication instance.
   *
   * @param args command line arguments
   */
  public TIC2WebSocketApplication(String[] args) {
    this.commandLineArgs = args;
    this.commandLine = new TIC2WebSocketCommandLine();
    this.commandLineParser = new CommandLine(this.commandLine);
    this.commandLineParser.setCommandName(TIC2WebSocketApplication.NAME);
    this.commandLineParser
        .getCommandSpec()
        .versionProvider(
            new IVersionProvider() {
              @Override
              public String[] getVersion() throws Exception {
                return new String[] {
                  TIC2WebSocketApplication.NAME + " v" + TIC2WebSocketApplication.VERSION
                };
              }
            });
    this.commandLineParser.registerConverter(
        Level.class, new TIC2WebSocketCommandLine.VerboseLevelConverter());
    this.updateLoggerConfiguration(Level.ERROR);
    this.started = false;
  }

  /**
   * Executes the application main loop.
   *
   * <p>Initializes, starts, and manages the application lifecycle, including shutdown handling.
   *
   * @return 0 if success, else an error code
   * @throws InterruptedException if the application thread is interrupted
   * @see TIC2WebSocketApplicationErrorCode
   */
  public int run() throws InterruptedException {
    int result = this.init();

    if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code()) {
      return result;
    }
    result = this.start();
    if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code()) {
      return result;
    }

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                new Runnable() {
                  @Override
                  public void run() {
                    TIC2WebSocketApplication.this.stop();
                  }
                }));

    while (this.isStarted()) {
      Thread.sleep(1000);
    }

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Initializes the application and its components.
   *
   * <p>Parses command-line arguments, loads configuration, and prepares core components.
   *
   * @return 0 if success, else an error code
   * @see TIC2WebSocketApplicationErrorCode
   */
  public int init() {
    if (this.isStarted()) {
      this.logger.error("Application already started!");
      return TIC2WebSocketApplicationErrorCode.APPLICATION_ALREADY_STARTED.code();
    }
    int result = this.parseCommandLine();
    if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code()) {
      return result;
    }
    result = this.updateLoggerConfiguration(this.commandLine.verboseLevel);
    if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code()) {
      return result;
    }

    result = this.loadConfiguration();
    if (result != TIC2WebSocketApplicationErrorCode.NO_ERROR.code()) {
      return result;
    }

    this.logger.info(TIC2WebSocketApplication.NAME + " initialized");

    this.ticCore =
        new TICCoreBase(this.configuration.getTicMode(), this.configuration.getTicPortNames());
    this.clientPool = new TIC2WebSocketClientPoolBase();
    this.requestHandler = new TIC2WebSocketRequestHandlerBase(this.ticCore);

    this.server =
        new TIC2WebSocketServer(
            "localhost",
        this.configuration.getServerPort(),
            this.clientPool,
            this.requestHandler);

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Starts the application and its main services.
   *
   * <p>Starts the TIC core and WebSocket server, and logs startup events.
   *
   * @return 0 if success, else an error code
   */
  public int start() {
    if (this.commandLineParser.isUsageHelpRequested()) {
      this.commandLineParser.usage(System.out);
    }
    if (this.commandLineParser.isVersionHelpRequested()) {
      this.commandLineParser.printVersionHelp(System.out);
    }
    this.logger.info(TIC2WebSocketApplication.NAME + " starting");

    try {
      this.ticCore.start();
      this.server.start();
    } catch (Exception exception) {
      this.logger.error(exception.getMessage(), exception);
      return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
    }

    this.logger.info(TIC2WebSocketApplication.NAME + " started");
    this.started = true;

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Indicates whether the application is currently started.
   *
   * @return true if the application is started, false otherwise
   */
  public boolean isStarted() {
    return this.started;
  }

  /**
   * Stops the application and releases resources.
   *
   * <p>Stops the WebSocket server and TIC core, and logs shutdown events.
   *
   * @return 0 if success, else an error code
   * @see TIC2WebSocketApplicationErrorCode
   */
  public int stop() {
    if (!this.isStarted()) {
      this.logger.error("Application not started!");
      return TIC2WebSocketApplicationErrorCode.APPLICATION_NOT_STARTED.code();
    }

    try {
      this.server.stop();
      this.ticCore.stop();
    } catch (Exception exception) {
      this.logger.error(exception.getMessage(), exception);
      return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
    }

    this.logger.info(TIC2WebSocketApplication.NAME + " stopped");
    this.started = false;

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Parses command-line arguments and validates them.
   *
   * <p>Logs errors for invalid arguments and returns appropriate error codes.
   *
   * @return 0 if success, else an error code
   */
  private int parseCommandLine() {
    try {
      ParseResult parseResult = this.commandLineParser.parseArgs(this.commandLineArgs);
      if (parseResult.errors().size() > 0) {
        this.logger.error("Invalid command line!");
        for (int i = 0; i < parseResult.errors().size(); i++) {
          this.logger.error(parseResult.errors().get(i).getMessage());
        }
        return TIC2WebSocketApplicationErrorCode.COMMAND_LINE_INVALID.code();
      }
    } catch (Exception exception) {
      this.logger.error("Invalid command line!");
      this.logger.error(exception.getMessage());
      return TIC2WebSocketApplicationErrorCode.COMMAND_LINE_INVALID.code();
    }

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Updates the logger configuration based on the specified log level.
   *
   * <p>Reconfigures Log4j and sets the application logger.
   *
   * @param level the log level to use
   * @return 0 if success, else an error code
   */
  private int updateLoggerConfiguration(Level level) {
    try {
      System.setProperty(
          "log4j.configurationFile", "log4j2-" + level.name().toLowerCase() + ".xml");
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.reconfigure();
      this.logger = LogManager.getLogger();
    } catch (Exception exception) {
      System.err.println("Log configuration update failure!");
      System.err.println(exception.getMessage());
      return TIC2WebSocketApplicationErrorCode.UPDATE_LOGGER_CONFIGURATION_FAILURE.code();
    }

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }

  /**
   * Loads the application configuration from file.
   *
   * <p>Reads the configuration file and initializes the configuration object.
   *
   * @return 0 if success, else an error code
   */
  private int loadConfiguration() {
    String configFile = null;

    if (this.commandLine.configFile != null) {
      configFile = this.commandLine.configFile;
    } else {
      configFile = System.getProperty("configFile", NAME + "Configuration.json");
    }
    File configPath = new File(configFile);
    if (!configPath.exists()) {
      this.logger.error(
          "No configuration file path '" + configPath.getAbsolutePath() + "' not found");
      return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
    }
    try {
      this.logger.info("Loading configuration file " + configPath);
      this.configuration = TIC2WebSocketConfigurationLoader.load(configPath.getAbsolutePath());
    } catch (Exception exception) {
      this.logger.error("Loading configuration file " + configPath + " failed", exception);
      return TIC2WebSocketApplicationErrorCode.LOAD_CONFIGURATION_FAILURE.code();
    }

    return TIC2WebSocketApplicationErrorCode.NO_ERROR.code();
  }
}
