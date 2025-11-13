// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.util.task;

/**
 * Interface representing a generic asynchronous task.
 *
 * <p>
 * This interface defines the contract for executing tasks, typically in a
 * concurrent or
 * event-driven environment. Implementations may represent background jobs,
 * scheduled actions, or
 * event handlers.
 */
public interface Task {
    /** Start task */
    public void start();

    /** Stop task */
    public void stop();

    /**
     * Get the task running state
     *
     * @return true if the task running
     */
    public boolean isRunning();
}
