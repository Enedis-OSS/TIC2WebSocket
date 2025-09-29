// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.channels;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.task.TaskPeriodic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class providing common functionality for channel
 * implementations.
 *
 * <p>
 * This class serves as a foundation for all channel implementations, providing
 * common setup
 * logic, configuration management, and logging capabilities. It extends
 * {@link TaskPeriodic} to
 * provide periodic task execution and implements the {@link Channel} interface.
 *
 * <p>
 * The class handles configuration validation, backup and restoration in case of
 * setup failures,
 * and provides a template method pattern for channel-specific setup operations
 * through the abstract
 * {@link #setup()} method.
 *
 * <p>
 * Subclasses should implement the abstract {@link #setup()} method to provide
 * channel-specific
 * initialization logic.
 *
 * @author Enedis Smarties team
 * @see Channel
 * @see TaskPeriodic
 * @see ChannelConfiguration
 */
public abstract class ChannelBase extends TaskPeriodic implements Channel {

  /** Logger instance for this channel implementation. */
  protected Logger logger;

  /** Configuration used by this channel. */
  protected ChannelConfiguration configuration;

  /**
   * Constructs a new channel with the specified configuration.
   *
   * <p>
   * This constructor initializes the logger for the specific channel
   * implementation and
   * immediately sets up the channel using the provided configuration. If the
   * setup fails, a {@link
   * ChannelException} is thrown.
   *
   * @param configuration the configuration to use for setting up the channel
   * @throws ChannelException if the configuration is invalid or the channel
   *                          cannot be properly
   *                          initialized
   */
  protected ChannelBase(ChannelConfiguration configuration) throws ChannelException {
    this.logger = LogManager.getLogger(this.getClass());
    this.setup(configuration);
  }

  /**
   * Sets up the channel with the provided configuration.
   *
   * <p>
   * This method implements a robust setup process that includes:
   *
   * <ul>
   * <li>Configuration validation
   * <li>Configuration backup (if reconfiguring)
   * <li>Channel-specific setup via {@link #setup()}
   * <li>Automatic rollback on setup failure
   * </ul>
   *
   * <p>
   * If the setup fails, the previous configuration is automatically restored to
   * maintain the
   * channel in a consistent state.
   *
   * @param configuration the configuration to apply to the channel
   * @throws ChannelException if the configuration is null, invalid, or if the
   *                          channel-specific
   *                          setup fails
   */
  @Override
  public void setup(ChannelConfiguration configuration) throws ChannelException {
    /* Check configuration */
    if (configuration == null) {
      ChannelException.raiseInvalidConfiguration("null");
    }
    /* Copy configuration */
    ChannelConfiguration configurationBackUp = null;
    try {
      if (this.configuration == null) {
        this.configuration = (ChannelConfiguration) configuration.clone();
      } else {
        configurationBackUp = (ChannelConfiguration) this.configuration.clone();
        this.configuration.copy(configuration);
      }
    } catch (DataDictionaryException exception) {
      ChannelException.raiseInvalidConfiguration(exception.getMessage());
    }
    /* Set up from internal configuration */
    try {
      this.setup();
    } catch (Exception exception) {
      /* Restore internal configuration */
      if (configurationBackUp != null) {
        try {
          this.configuration.copy(configurationBackUp);
        } catch (DataDictionaryException dataDictionaryException) {
          this.logger.error("", dataDictionaryException);
        }
      }
    }
  }

  /**
   * Performs channel-specific setup operations.
   *
   * <p>
   * This method is called by the public {@link #setup(ChannelConfiguration)}
   * method after the
   * configuration has been validated and applied. Subclasses should override this
   * method to
   * implement channel-specific initialization logic, such as opening
   * communication ports,
   * establishing network connections, or initializing hardware.
   *
   * <p>
   * If this method throws an exception, the configuration will be automatically
   * rolled back to
   * its previous state.
   *
   * @throws ChannelException if the channel-specific setup fails
   */
  protected void setup() throws ChannelException {
  }
}
