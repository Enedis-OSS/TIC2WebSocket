// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class representing a generic asynchronous task.
 *
 * <p>This class provides a foundation for implementing tasks that can be executed asynchronously,
 * typically in concurrent or event-driven environments.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Running background operations
 *   <li>Scheduling periodic or delayed actions
 *   <li>Extending for custom task logic
 * </ul>
 *
 * @author Enedis Smarties team
 */
public abstract class TaskBase implements Task, Runnable {
  private AtomicBoolean stopRequired;
  private Thread task;
  protected static Logger logger = LogManager.getLogger();

  /** Default constructor */
  public TaskBase() {
    super();
    this.stopRequired = new AtomicBoolean();
  }

  @Override
  public void start() {
    if (this.task == null || !this.task.isAlive()) {
      this.stopRequired.set(false);
      this.task = new Thread(this);
      this.task.start();
    }
  }

  @Override
  public void stop() {
    try {
      if (this.task != null && this.task.isAlive()) {
        this.stopRequired.set(true);
        if (this.task != Thread.currentThread()) {
          this.task.join();
        }
      }
    } catch (InterruptedException exception) {
      logger.error("Stop task interrupted", exception);
    }
  }

  @Override
  public final boolean isRunning() {
    return this.task == null ? false : this.task.isAlive();
  }

  @Override
  public void run() {
    this.runOnStart();
    this.runProcess();
    this.runOnTerminate();
  }

  protected final boolean isStopRequired() {
    return this.stopRequired.get();
  }

  protected final void runOnStart() {
    try {
      this.onStart();
    } catch (Exception exception) {
      logger.error("Task on start aborted", exception);
      this.runOnError(exception);
    }
  }

  protected final void runProcess() {
    try {
      this.process();
    } catch (Exception exception) {
      logger.error("Task process aborted", exception);
      this.runOnError(exception);
    }
  }

  protected final void runOnTerminate() {
    try {
      this.onTerminate();
    } catch (Exception exception) {
      logger.error("Task on terminate aborted", exception);
      this.runOnError(exception);
    }
  }

  protected final void runOnError(Exception exception) {
    try {
      this.onError(exception);
    } catch (Exception onErrorException) {
      logger.error("Task on error aborted", exception);
    }
  }

  /** Task core process method */
  protected abstract void process();

  protected void onStart() {}

  protected void onTerminate() {}

  protected void onError(Exception exception) {}
}
