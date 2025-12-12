// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.delimiter;

/** 
 * Enumeration representing TIC frame start patterns for different TIC modes.
 */
public enum TICStartLabel {
    /** Start pattern (STX, LF, A, D, C, O) for historic TIC frames. */
    HISTORIC (new byte[] {TICPattern.STX, TICPattern.LF, 65, 68, 67, 79}),
    /** Start pattern (STX, LF, A, D, S, C) for standard TIC frames. */
    STANDARD (new byte[] {TICPattern.STX, TICPattern.LF, 65, 68, 83, 67});

    private final byte[] hexValue;

    /** 
     * Constructor to initialize the TICStartLabel with its hexadecimal value.
     * 
     * @param hexValue the hexadecimal value as a byte array
     */
    private TICStartLabel(byte[] hexValue) {
        this.hexValue = hexValue;
    }

    /** 
     * Gets the hexadecimal value of the start pattern.
     * 
     * @return the hexadecimal byte array
     */
    public byte[] getHexValue() {
        return hexValue;
    }

    /** 
     * Gets the ASCII representation of the start pattern.
     * 
     * @return the ASCII character array
     */
    public char[] getAsciiValue() {
        char[] asciiValue = new char[hexValue.length];
        for (int i = 0; i < hexValue.length; i++) {
            asciiValue[i] = (char) hexValue[i];
        }
        return asciiValue;
    }

    /** 
     * Gets the length of the start pattern.
     * 
     * @return the length as an integer
     */
    public static int length() {
        return 6;
    }
}