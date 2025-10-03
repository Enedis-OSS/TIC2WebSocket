// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.protocol.tic.channels;

import enedis.lab.io.channels.serialport.ChannelSerialPortConfiguration;
import enedis.lab.io.channels.serialport.Parity;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionary;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.KeyDescriptor;
import enedis.lab.types.datadictionary.KeyDescriptorEnum;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration class for TIC serial port channels.
 *
 * <p>This class extends {@link ChannelSerialPortConfiguration} to provide specific configuration
 * for TIC protocol communication over serial ports. It handles TIC mode selection (HISTORIC,
 * STANDARD, or AUTO) and automatically configures the appropriate serial port parameters based on
 * the selected TIC mode.
 *
 * <p>The class supports both manual TIC mode selection and automatic detection, with different baud
 * rates for HISTORIC (1200 bps) and STANDARD (9600 bps) modes.
 *
 * @author Enedis Smarties team
 */
public class ChannelTICSerialPortConfiguration extends ChannelSerialPortConfiguration {

  protected static final String KEY_TIC_MODE = "ticMode";

  protected static final Number BAUDRATE_HISTORIC = 1200;
  protected static final Number BAUDRATE_STANDARD = 9600;
  protected static final Number[] BAUDRATE_ACCEPTED_VALUES = {BAUDRATE_HISTORIC, BAUDRATE_STANDARD};
  protected static final Parity PARITY_ACCEPTED_VALUE = Parity.EVEN;
  protected static final Number DATA_BITS_ACCEPTED_VALUE = 7;
  protected static final Number STOP_BITS_ACCEPTED_VALUE = 1.0d;
  protected static final TICMode TIC_MODE_DEFAULT_VALUE = TICMode.AUTO;

  private List<KeyDescriptor<?>> keys = new ArrayList<KeyDescriptor<?>>();

  protected KeyDescriptorEnum<TICMode> kTicMode;

  protected ChannelTICSerialPortConfiguration() {
    super();
    this.loadKeyDescriptors();

    this.kBaudrate.setAcceptedValues(BAUDRATE_ACCEPTED_VALUES);
    this.kParity.setAcceptedValues(PARITY_ACCEPTED_VALUE);
    this.kDataBits.setAcceptedValues(DATA_BITS_ACCEPTED_VALUE);
    this.kStopBits.setAcceptedValues(STOP_BITS_ACCEPTED_VALUE);
  }

  /**
   * Creates a new TIC serial port configuration from a map of parameters.
   *
   * <p>The map should contain configuration parameters that will be copied to this configuration
   * object. The TIC mode and serial port parameters will be automatically configured based on the
   * provided values.
   *
   * @param map the map containing configuration parameters
   * @throws DataDictionaryException if the map contains invalid parameters
   */
  public ChannelTICSerialPortConfiguration(Map<String, Object> map) throws DataDictionaryException {
    this();
    this.copy(fromMap(map));
  }

  /**
   * Creates a new TIC serial port configuration by copying from another data dictionary.
   *
   * <p>This constructor creates a new configuration object by copying all parameters from the
   * provided data dictionary. The TIC mode and serial port parameters will be automatically
   * configured based on the copied values.
   *
   * @param other the data dictionary to copy configuration from
   * @throws DataDictionaryException if the data dictionary contains invalid parameters
   */
  public ChannelTICSerialPortConfiguration(DataDictionary other) throws DataDictionaryException {
    this();
    this.copy(other);
  }

  /**
   * Creates a new TIC serial port configuration with the specified name and file.
   *
   * <p>This constructor initializes the configuration with default values and associates it with
   * the provided name and configuration file. The TIC mode will be set to AUTO by default, allowing
   * automatic detection of the appropriate mode.
   *
   * @param name the configuration name
   * @param file the configuration file
   */
  public ChannelTICSerialPortConfiguration(String name, File file) {
    this();
    this.init(name, file);
  }

  /**
   * Creates a new TIC serial port configuration with specific parameters.
   *
   * <p>This constructor creates a configuration with the specified name, port name, and TIC mode.
   * The serial port parameters (baud rate, parity, data bits, stop bits) will be automatically
   * configured based on the selected TIC mode.
   *
   * @param name the configuration name
   * @param portName the serial port name (e.g., "/dev/ttyUSB0" on Linux, "COM1" on Windows)
   * @param ticMode the TIC mode (HISTORIC, STANDARD, or AUTO)
   * @throws DataDictionaryException if the provided parameters are invalid
   */
  public ChannelTICSerialPortConfiguration(String name, String portName, TICMode ticMode)
      throws DataDictionaryException {
    this();

    this.setName(name);
    this.setPortName(portName);
    this.setTicMode(ticMode);

    this.checkAndUpdate();
  }

  @Override
  protected void updateOptionalParameters() throws DataDictionaryException {
    if (!this.exists(KEY_TIC_MODE)) {
      this.setTicMode(TIC_MODE_DEFAULT_VALUE);
    }
    switch (this.getTicMode()) {
      case HISTORIC:
        {
          this.setBaudrate(BAUDRATE_HISTORIC);
          this.setParity(PARITY_ACCEPTED_VALUE);
          this.setDataBits(DATA_BITS_ACCEPTED_VALUE);
          this.setStopBits(STOP_BITS_ACCEPTED_VALUE);
          break;
        }
      case STANDARD:
      case AUTO:
      default:
        {
          this.setBaudrate(BAUDRATE_STANDARD);
          this.setParity(PARITY_ACCEPTED_VALUE);
          this.setDataBits(DATA_BITS_ACCEPTED_VALUE);
          this.setStopBits(STOP_BITS_ACCEPTED_VALUE);
        }
    }
    super.updateOptionalParameters();
  }

  /**
   * Gets the current TIC mode configuration.
   *
   * <p>Returns the TIC mode that has been set for this configuration. The mode determines the
   * communication protocol and serial port parameters used for TIC communication.
   *
   * @return the current TIC mode (HISTORIC, STANDARD, or AUTO)
   */
  public TICMode getTicMode() {
    return (TICMode) this.data.get(KEY_TIC_MODE);
  }

  /**
   * Sets the TIC mode for this configuration.
   *
   * <p>When the TIC mode is set, the serial port parameters are automatically updated to match the
   * requirements of the selected mode:
   *
   * <ul>
   *   <li>HISTORIC: 1200 bps, 7 data bits, even parity, 1 stop bit
   *   <li>STANDARD: 9600 bps, 7 data bits, even parity, 1 stop bit
   *   <li>AUTO: Automatically detects the appropriate mode
   * </ul>
   *
   * @param ticMode the TIC mode to set
   * @throws DataDictionaryException if the TIC mode is invalid
   */
  public void setTicMode(TICMode ticMode) throws DataDictionaryException {
    this.setTicMode((Object) ticMode);
  }

  /**
   * Sets the TIC mode using an object value and automatically configures the baud rate based on the
   * selected mode.
   *
   * <p>This protected method is used internally to set the TIC mode from various object types and
   * automatically adjusts the baud rate:
   *
   * <ul>
   *   <li>HISTORIC mode: sets baud rate to 1200 bps
   *   <li>STANDARD or AUTO mode: sets baud rate to 9600 bps
   * </ul>
   *
   * @param ticMode the TIC mode object to set
   * @throws DataDictionaryException if the TIC mode object is invalid
   */
  protected void setTicMode(Object ticMode) throws DataDictionaryException {
    this.data.put(KEY_TIC_MODE, this.kTicMode.convert(ticMode));
    switch (this.getTicMode()) {
      case HISTORIC:
        this.setBaudrate(BAUDRATE_HISTORIC);
        break;
      case STANDARD:
      case AUTO:
        this.setBaudrate(BAUDRATE_STANDARD);
        break;
      default:
        break;
    }
  }

  /**
   * Loads and initializes the key descriptors for TIC mode configuration.
   *
   * <p>This private method sets up the key descriptor for the TIC mode parameter, which is used for
   * validation and conversion of TIC mode values. It creates a key descriptor for the TICMode enum
   * and adds it to the list of keys managed by this configuration object.
   *
   * <p>If an error occurs during initialization, it wraps the DataDictionaryException in a
   * RuntimeException to maintain the constructor's contract.
   */
  private void loadKeyDescriptors() {
    try {
      this.kTicMode = new KeyDescriptorEnum<TICMode>(KEY_TIC_MODE, true, TICMode.class);
      this.keys.add(this.kTicMode);

      this.addAllKeyDescriptor(this.keys);
    } catch (DataDictionaryException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
