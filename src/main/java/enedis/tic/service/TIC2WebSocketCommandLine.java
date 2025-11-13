// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.service;

import org.apache.logging.log4j.Level;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.TypeConversionException;

/**
 * Command line options and converters for TIC2WebSocket.
 *
 * <p>This class defines the command line arguments, options, and type converters used to configure
 * and launch the TIC2WebSocket application. It leverages Picocli annotations for option parsing
 * and provides a custom converter for verbosity levels.
 *
 * <p>Responsibilities include:
 * <ul>
 *   <li>Defining supported command line options (help, version, verbosity, config file)
 *   <li>Providing a converter for log verbosity levels
 *   <li>Storing parsed option values for use in application initialization
 * </ul>
 *
 * @author Enedis Smarties team
 */
@Command()
public class TIC2WebSocketCommandLine {
  /**
   * Picocli type converter for log verbosity levels.
   *
   * <p>Converts integer or string values to Log4j {@link Level} instances for controlling
   * application logging verbosity.
   */
  static class VerboseLevelConverter implements ITypeConverter<Level> {
  /**
   * Converts a string value to a Log4j {@link Level}.
   *
   * <p>Accepts both integer and string representations of log levels.
   *
   * @param value the string value to convert
   * @return the corresponding Log4j Level
   * @throws Exception if conversion fails
   */
  @Override
  public Level convert(String value) throws Exception {
      try {
        switch (Integer.parseInt(value)) {
          case 0:
            return Level.OFF;
          case 1:
            return Level.FATAL;
          case 2:
            return Level.ERROR;
          case 3:
            return Level.WARN;
          case 4:
            return Level.INFO;
          case 5:
            return Level.DEBUG;
          case 6:
            return Level.TRACE;
          default:
            throw new TypeConversionException(value);
        }
      } catch (NumberFormatException exception) {
        Level level = Level.getLevel(value);
        if (level == null) {
          throw new TypeConversionException(value);
        }
        return level;
      }
    }
  }

  /**
   * Option to display help information.
   */
  @Option(
    names = {"-h", "--help"},
    usageHelp = true,
    description = "Display help")
  boolean helpRequested = false;

  /**
   * Option to display version information.
   */
  @Option(
    names = {"--version"},
    versionHelp = true,
    description = "Display version")
  boolean versionRequested = false;

  // @formatter:off
  /**
   * Option to set the log verbosity level.
   */
  @Option(
    names = {"-v", "--verbose"},
    defaultValue = "2",
    paramLabel = "LEVEL",
    description =
      "Define verbosity level:\n"
        + "0 ou OFF = muted\n"
        + "1 ou FATAL = critical errors\n"
        + "2 ou ERROR = error (default)\n"
        + "3 ou WARN = warnings\n"
        + "4 ou INFO = informations\n"
        + "5 ou DEBUG = debugging\n"
        + "6 ou TRACE = traces\n")
  Level verboseLevel;

  // @formatter:on

  /**
   * Option to specify the configuration file path.
   */
  @Option(
      names = {"--configFile"},
      paramLabel = "PATH",
      description = "Set configuration file")
  String configFile = null;
}
