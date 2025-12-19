package tic.stream;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tic.frame.delimiter.TICPattern;
import tic.util.time.Time;

public class TICStreamReader {

  private static final Logger logger = LogManager.getLogger(TICStream.class);
  private SerialPort serialPort;
  private int timeoutMillis;
  private int baudrate;
  private int pollingPeriod;

  /** Polling period in milliseconds for data reception */
  private static final int RECEIVE_DATA_POLLING_PERIOD = 100;

  private static final int DATA_BITS = 7;
  private static final int STOP_BITS = SerialPort.STOPBITS_1;
  private static final int PARITY = SerialPort.PARITY_EVEN;

  public TICStreamReader(String portName, int baudrate, int timeoutMillis) {
    this.serialPort = new SerialPort(portName);
    this.baudrate = baudrate;
    this.pollingPeriod = RECEIVE_DATA_POLLING_PERIOD;
    this.timeoutMillis = timeoutMillis;
  }

  /**
   * Small CLI helper that opens a TIC stream with inline configuration and prints the first
   * received frame to the console.
   *
   * <p>Usage: {@code java tic.stream.TICStreamReader <PORT_NAME>}
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java tic.stream.TICStreamReader <PORT_NAME>");
      System.exit(1);
    }

    String portName = args[0];

    int baudrate = 9600;
    int timeout = 10000;

    TICStreamReader streamReader = new TICStreamReader(portName, baudrate, timeout);

    System.out.println("Listening TIC stream on PORT_NAME=" + portName);
    try {
      byte[] frame = streamReader.read();
      if (frame == null) {
        System.err.println("No TIC frame received within timeout (" + timeout + "s)");
        System.exit(3);
      } else {
        String payload = new String(frame);
        System.out.println("TIC frame read:\n" + payload);
        System.exit(0);
      }
    } catch (Exception exception) {
      System.err.println("Error while reading TIC frame: " + exception.getMessage());
      exception.printStackTrace(System.err);
      System.exit(2);
    } finally {
      try {
        streamReader.close();
      } catch (Exception closeException) {
        System.err.println("Warning: failed to close stream: " + closeException.getMessage());
      }
    }
  }

  public byte[] read() {
    long beginTime = System.currentTimeMillis();
    long elapsedTime = 0;
    boolean startFrameFound = false;
    StringBuffer buffer = new StringBuffer();

    while (hasRemainingTime(elapsedTime)) {
      Character nextByte = readNextByteIfAvailable();
      if (nextByte != null) {
        if (!startFrameFound) {
          if (isStartDelimiter(nextByte)) {
            startFrameFound = true;
            buffer.append(nextByte);
          }
        } else {
          buffer.append(nextByte);
          if (isEndDelimiter(nextByte)) {
            return buffer.toString().getBytes();
          }
        }
      }
      elapsedTime = System.currentTimeMillis() - beginTime;
    }

    return null;
  }

  private boolean hasRemainingTime(long elapsedTime) {
    return this.timeoutMillis == 0 || elapsedTime < this.timeoutMillis;
  }

  private Character readNextByteIfAvailable() {
    try {
      if (this.available() <= 0) {
        Time.sleep(this.pollingPeriod);
        return null;
      }
    } catch (Exception e) {
      return null;
    }

    byte[] chunk = this.read(1);
    if (chunk.length == 0) {
      return null;
    }

    return (char) chunk[0];
  }

  private boolean isStartDelimiter(char value) {
    return value == TICPattern.STX;
  }

  private boolean isEndDelimiter(char value) {
    return value == TICPattern.ETX;
  }

  /** Reads the specified number of bytes from the serial port input buffer. */
  private synchronized byte[] read(int byteCount) {
    if (byteCount <= 0) {
      throw new IllegalArgumentException("byteCount must be strictly positive");
    }
    this.open();
    try {
      byte[] data = this.serialPort.readBytes(byteCount);
      return (data != null) ? data : new byte[0];
    } catch (SerialPortException exception) {
      throw new IllegalStateException("Cannot read serial port", exception);
    }
  }

  /** Returns the number of bytes available to read from the serial port input buffer. */
  private synchronized int available() {
    this.open();
    try {
      return this.serialPort.getInputBufferBytesCount();
    } catch (SerialPortException exception) {
      throw new IllegalStateException("Cannot query input buffer size", exception);
    }
  }

  /** Flushes the serial port input and output buffers. */
  private synchronized void flush() {
    this.open();
    try {
      int mask =
          SerialPort.PURGE_RXCLEAR
              | SerialPort.PURGE_RXABORT
              | SerialPort.PURGE_TXCLEAR
              | SerialPort.PURGE_TXABORT;
      this.serialPort.purgePort(mask);
    } catch (SerialPortException exception) {
      throw new IllegalStateException("Cannot flush serial port", exception);
    }
  }

  /** Opens the serial port if needed and returns the handler. */
  private synchronized void open() {
    if (this.isOpened()) {
      return;
    }

    if (this.serialPort == null) {
      throw new IllegalStateException("Serial port is not initialized");
    }

    try {
      this.serialPort.openPort();
      this.serialPort.setParams(this.baudrate, DATA_BITS, STOP_BITS, PARITY);
      this.serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

      logger.info("Opened TIC serial port {}", this.serialPort.getPortName());
    } catch (SerialPortException exception) {
      throw new IllegalStateException(
          "Failed to open serial port " + this.serialPort.getPortName(), exception);
    }
  }

  /** Closes the serial port if opened. */
  private synchronized void close() {
    if (this.serialPort == null) {
      return;
    }
    try {
      if (this.serialPort.isOpened()) {
        this.serialPort.closePort();
        logger.debug("Closed serial port {}", this.serialPort.getPortName());
      }
    } catch (SerialPortException exception) {
      throw new IllegalStateException("Cannot close serial port", exception);
    } finally {
      this.serialPort = null;
    }
  }

  protected void reset() {
    this.close();
    this.open();
    try {
      this.flush();
    } catch (Exception e) {
    }
  }

  /** Indicates whether the serial port is currently opened. */
  private synchronized boolean isOpened() {
    return this.serialPort != null && this.serialPort.isOpened();
  }
}
