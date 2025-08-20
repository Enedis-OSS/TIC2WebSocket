// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io;

import enedis.lab.types.DataList;

/**
 * Interface used to find all port descriptor
 * @param <T> the port descriptor class
 */
public interface PortFinder<T>
{
	/**
	 * Find all port descriptor
	 *
	 * @return The port descriptor list found
	 */
	public DataList<T> findAll();
}
