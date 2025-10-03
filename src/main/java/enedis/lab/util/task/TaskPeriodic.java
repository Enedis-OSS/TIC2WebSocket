// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.task;

import enedis.lab.util.time.Time;
import java.util.concurrent.atomic.AtomicLong;

/** Periodic task with configurable period */
public abstract class TaskPeriodic extends TaskBase {
  /** Default period (in milliseconds) used to execute process */
  public static final long DEFAULT_PERIOD = 1000;

  private AtomicLong period = new AtomicLong();

  /**
   * Constructor with default parameter
   *
   * @see #DEFAULT_PERIOD
   */
  public TaskPeriodic() {
    super();
    this.setPeriod(DEFAULT_PERIOD);
  }

  /**
   * Constructor with polling period
   *
   * @param period the period (in milliseconds) used to execute process
   */
  public TaskPeriodic(long period) {
    super();
    this.setPeriod(period);
  }

  @Override
  public void run() {
    this.runOnStart();
    while (!this.isStopRequired()) {
      this.runProcess();
      if (this.isStopRequired()) {
        break;
      }
      this.waitPeriod();
    }
    this.runOnTerminate();
  }

  /**
   * Get polling period
   *
   * @return The period (in milliseconds) used to execute process
   */
  public long getPeriod() {
    return this.period.get();
  }

  /**
   * Set the polling period
   *
   * @param period the period (in milliseconds) used to execute process
   * @throws IllegalArgumentException if period <= 0
   */
  public void setPeriod(long period) {
    if (period <= 0) {
      throw new IllegalArgumentException("Cannot set period " + period + " : must be > 0");
    }
    this.period.set(period);
  }

  private void waitPeriod() {
    Time.sleep(this.getPeriod());
  }
}
