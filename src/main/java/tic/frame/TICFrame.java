// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import enedis.lab.protocol.tic.TICMode;
import java.util.ArrayList;
import java.util.List;
import tic.frame.group.TICGroup;

/**
 * Abstract base class for TIC frames.
 *
 * <p>This class provides the structure for TIC frames.
 *
 * @author Enedis Smarties team
 */
public class TICFrame {

  private TICMode mode = TICMode.UNKNOWN;
  private List<TICGroup> groupList = new ArrayList<>();

  /**
   * Constructs a TIC frame with the specified mode.
   *
   * @param mode the TIC mode
   */
  public TICFrame(TICMode mode) {
    if (mode == null) {
      throw new IllegalArgumentException("mode must not be null");
    }
    if (mode == TICMode.AUTO) {
      throw new IllegalArgumentException("TICFrame cannot be created with AUTO mode");
    }
    if (mode == TICMode.UNKNOWN) {
      throw new IllegalArgumentException("TICFrame cannot be created with UNKNOWN mode");
    }
    this.mode = mode;
  }

  /**
   * Adds a new group to the TIC frame.
   *
   * @param group the TIC group to add
   */
  public void addGroup(TICGroup group) {
    this.groupList.add(group);
  }

  /**
   * Returns the TIC mode for this frame.
   *
   * @return the TIC mode
   */
  public TICMode getMode() {
    return this.mode;
  }

  /**
   * Returns the list of info groups in this TIC frame.
   *
   * @return the list of info groups
   */
  public List<TICGroup> getGroupList() {
    return this.groupList;
  }

  /**
   * Retrieves the TIC group with the specified label.
   *
   * @param label the label of the group to retrieve
   * @return the TIC group with the specified label, or null if not found
   */
  public TICGroup getGroup(String label) {
    for (TICGroup group : this.groupList) {
      if (group.getLabel().equals(label)) {
        return group;
      }
    }
    return null;
  }

  /**
   * Checks if the TIC frame contains any invalid groups.
   *
   * @return true if there is at least one invalid group, false otherwise
   */
  public boolean hasInvalidGroup() {
    for (TICGroup group : this.groupList) {
      if (!group.isValid()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the TIC frame contains a group with the specified label.
   *
   * @param label the label to search for
   * @return true if a group with the specified label exists, false otherwise
   */
  public boolean containsGroupLabel(String label) {
    for (TICGroup group : this.groupList) {
      if (group.getLabel().equals(label)) {
        return true;
      }
    }
    return false;
  }
}
