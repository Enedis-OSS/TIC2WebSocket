// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream.identifier;

/**
 * Enumeration representing the types of TIC stream identifiers.
 *
 * <p>This enum defines the different ways to identify a serial port, such as by its name or ID.
 *
 * @author Enedis Smarties team
 */
public enum TICStreamIdentifierType {
    /** Identifier type for the serial port name. */
    PORT_NAME,
    /** Identifier type for the serial port ID. */
    PORT_ID;
}
