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
public enum TICStartPattern {
    /** Start pattern (STX, LF, A, D, C, O) for historic TIC frames. */
    HISTORIC (new byte[] {2, 10, 65, 68, 67, 79}),
    /** Start pattern (STX, LF, A, D, S, C) for standard TIC frames. */
    STANDARD (new byte[] {2, 10, 65, 68, 83, 67});

    private final byte[] hexValue;

    /** 
     * Constructs a TICStartPattern with the specified hexadecimal value.
     *
     * @param hexValue the byte array representing the start pattern
     */
    private TICStartPattern(byte[] hexValue) {
        this.hexValue = hexValue;
    }

    /** 
     * Gets the hexadecimal value of the start pattern.
     * 
     * @return the hexadecimal value as a byte array
     */
    public byte[] getHexValue() {
        return hexValue;
    }

    /** 
     * Gets the ASCII character representation of the start pattern.
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