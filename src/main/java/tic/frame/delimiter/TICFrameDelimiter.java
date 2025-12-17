package tic.frame.delimiter;

public enum TICFrameDelimiter {
  /** Begin byte (STX, 0x02) for TIC frames. */
  BEGIN((byte) 0x02),
  /** Begin byte (STX, 0x02) for TIC frames. */
  END((byte) 0x03);

  private final byte value;

  private TICFrameDelimiter(byte value) {
    this.value = value;
  }

  /**
   * Gets the hexadecimal value of the delimiter.
   *
   * @return the hexadecimal value as a byte
   */
  public byte getValue() {
    return value;
  }
}
