package tic.frame.constants;

/** 
 * Enumeration representing TIC frame separators for different TIC modes.
 */
public enum TICSeparator {
    /** Separator character (space, 0x20) for historic TIC frames. */
    HISTORIC (' ', (byte) 0x20),
    /** Separator character (tab, 0x09) for standard TIC frames. */
    STANDARD ('\t', (byte) 0x09);

    private final byte hexValue;
    private final char asciiValue;

    /** 
     * Constructs a TICSeparator with the specified ASCII and hexadecimal values.
     *
     * @param asciiValue the ASCII character value of the separator
     * @param hexValue the hexadecimal byte value of the separator
     */
    private TICSeparator(char asciiValue, byte hexValue) {
        this.asciiValue = asciiValue;
        this.hexValue = hexValue;
    }

    /** 
     * Gets the hexadecimal value of the separator.
     * 
     * @return the hexadecimal value as a byte
     */
    public byte getHexValue() {
        return hexValue;
    }

    /** 
     * Gets the ASCII character value of the separator.
     * 
     * @return the ASCII character
     */
    public char getAsciiValue() {
        return asciiValue;
    }
}