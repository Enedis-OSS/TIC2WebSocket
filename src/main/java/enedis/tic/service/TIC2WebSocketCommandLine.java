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

/** Class used for command line */
@Command()
public class TIC2WebSocketCommandLine {
  static class VerboseLevelConverter implements ITypeConverter<Level> {
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

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Display help")
  boolean helpRequested = false;

  @Option(
      names = {"--version"},
      versionHelp = true,
      description = "Display version")
  boolean versionRequested = false;

  // @formatter:off
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

  @Option(
      names = {"--configFile"},
      paramLabel = "PATH",
      description = "Set configuration file")
  String configFile = null;
}
;
