// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

/**
 * Task interface
 */
public interface Task
{
	/**
	 * Start task
	 */
	public void start();

	/**
	 * Stop task
	 */
	public void stop();

	/**
	 * Get the task running state
	 * 
	 * @return true if the task running
	 */
	public boolean isRunning();
}
