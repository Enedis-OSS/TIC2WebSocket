// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

/**
 * DataStream status
 */
public enum DataStreamStatus
{
	/** Unknown, default status */
	UNKNOWN,
	/** Closed */
	CLOSED,
	/** Opened */
	OPENED,
	/** Error */
	ERROR
}
