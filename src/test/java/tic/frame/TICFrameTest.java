// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import tic.frame.group.TICGroup;

public class TICFrameTest {

  @Test
  public void test_constructor_historic_mode() {
    // Given
    TICMode mode = TICMode.HISTORIC;

    // When
    TICFrame frame = new TICFrame(mode);

    // Then
    Assert.assertEquals("TIC frame mode mismatch", TICMode.HISTORIC, frame.getMode());
    Assert.assertEquals("TIC frame group list size mismatch", 0, frame.getGroupList().size());
  }

  @Test
  public void test_constructor_standard_mode() {
    // Given
    TICMode mode = TICMode.STANDARD;

    // When
    TICFrame frame = new TICFrame(mode);

    // Then
    Assert.assertEquals("TIC frame mode mismatch", TICMode.STANDARD, frame.getMode());
    Assert.assertEquals("TIC frame group list size mismatch", 0, frame.getGroupList().size());
  }

  @Test
  public void test_constructor_null_mode() {
    // Given
    TICMode mode = null;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> new TICFrame(mode));

    // Then
    Assert.assertEquals("TICFrame mode must not be null", exception.getMessage());
  }

  @Test
  public void test_constructor_auto_mode() {
    // Given
    TICMode mode = TICMode.AUTO;

    // When
    IllegalArgumentException exception =
        Assert.assertThrows(IllegalArgumentException.class, () -> new TICFrame(mode));

    // Then
    Assert.assertEquals("TICFrame cannot be created with AUTO mode", exception.getMessage());
  }

  @Test
  public void test_addGroup() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    // When
    groups.forEach(group -> frame.addGroup(group));

    // Then
    Assert.assertEquals("TIC frame group list size mismatch", 9, frame.getGroupList().size());
    Assert.assertEquals(
        "TIC frame group n°1 mismatch",
        new TICGroup("ADCO", "812164417227"),
        frame.getGroupList().get(0));
    Assert.assertEquals(
        "TIC frame group n°2 mismatch",
        new TICGroup("OPTARIF", "BASE"),
        frame.getGroupList().get(1));
    Assert.assertEquals(
        "TIC frame group n°3 mismatch", new TICGroup("ISOUSC", "15"), frame.getGroupList().get(2));
    Assert.assertEquals(
        "TIC frame group n°4 mismatch",
        new TICGroup("BASE", "000000104"),
        frame.getGroupList().get(3));
    Assert.assertEquals(
        "TIC frame group n°5 mismatch", new TICGroup("PTEC", "TH.."), frame.getGroupList().get(4));
    Assert.assertEquals(
        "TIC frame group n°6 mismatch", new TICGroup("IINST", "000"), frame.getGroupList().get(5));
    Assert.assertEquals(
        "TIC frame group n°7 mismatch", new TICGroup("IMAX", "000"), frame.getGroupList().get(6));
    Assert.assertEquals(
        "TIC frame group n°8 mismatch", new TICGroup("PAPP", "00000"), frame.getGroupList().get(7));
    Assert.assertEquals(
        "TIC frame group n°9 mismatch",
        new TICGroup("MOTDETAT", "000000"),
        frame.getGroupList().get(8));
  }

  @Test
  public void test_getGroup_match() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    TICGroup targetGroup = new TICGroup("IINST", "000");
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            targetGroup,
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    groups.forEach(group -> frame.addGroup(group));

    // When
    TICGroup retrievedGroup = frame.getGroup("IINST");

    // Then
    Assert.assertEquals("TIC frame getGroup mismatch", targetGroup, retrievedGroup);
  }

  @Test
  public void test_getGroup_null() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    groups.forEach(group -> frame.addGroup(group));

    // When
    TICGroup retrievedGroup = frame.getGroup("IINSTI");

    // Then
    Assert.assertEquals("TIC frame getGroup should not match", null, retrievedGroup);
  }

  @Test
  public void test_hasInvalidGroup_no_invalid_group() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));
    groups.forEach(group -> frame.addGroup(group));

    // When
    boolean hasInvalidGroup = frame.hasInvalidGroup();

    // Then
    Assert.assertEquals("TIC frame has invalid group mismatch", false, hasInvalidGroup);
  }

  @Test
  public void test_hasInvalidGroup_single_invalid_group() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE", false),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));
    groups.forEach(group -> frame.addGroup(group));

    // When
    boolean hasInvalidGroup = frame.hasInvalidGroup();

    // Then
    Assert.assertEquals("TIC frame has invalid group mismatch", true, hasInvalidGroup);
  }

  @Test
  public void test_hasInvalidGroup_multiple_invalid_groups() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE", false),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104", false),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));
    groups.forEach(group -> frame.addGroup(group));

    // When
    boolean hasInvalidGroup = frame.hasInvalidGroup();

    // Then
    Assert.assertEquals("TIC frame has invalid group mismatch", true, hasInvalidGroup);
  }

  @Test
  public void test_containsGroupLabel_match() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    TICGroup targetGroup = new TICGroup("IINST", "000");
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            targetGroup,
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    groups.forEach(group -> frame.addGroup(group));

    // When
    boolean containsGroup = frame.containsGroupLabel("IINST");

    // Then
    Assert.assertEquals("TIC frame containsGroupLabel mismatch", true, containsGroup);
  }

  @Test
  public void test_containsGroupLabel_mismatch() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    TICGroup targetGroup = new TICGroup("IINST", "000");
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE"),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104"),
            new TICGroup("PTEC", "TH.."),
            targetGroup,
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    groups.forEach(group -> frame.addGroup(group));

    // When
    boolean containsGroup = frame.containsGroupLabel("IINSTI");

    // Then
    Assert.assertEquals("TIC frame containsGroupLabel mismatch", false, containsGroup);
  }

  @Test
  public void test_toString() {
    // Given
    TICFrame frame = new TICFrame(TICMode.HISTORIC);
    List<TICGroup> groups =
        Arrays.asList(
            new TICGroup("ADCO", "812164417227"),
            new TICGroup("OPTARIF", "BASE", false),
            new TICGroup("ISOUSC", "15"),
            new TICGroup("BASE", "000000104", false),
            new TICGroup("PTEC", "TH.."),
            new TICGroup("IINST", "000"),
            new TICGroup("IMAX", "000"),
            new TICGroup("PAPP", "00000"),
            new TICGroup("MOTDETAT", "000000"));

    groups.forEach(group -> frame.addGroup(group));

    // When
    String text = frame.toString();

    // Then
    Assert.assertEquals(
        "TIC frame string unexpected",
        "mode=HISTORIC, groupList=[(label=ADCO, value=812164417227, isValid=true), (label=OPTARIF,"
            + " value=BASE, isValid=false), (label=ISOUSC, value=15, isValid=true), (label=BASE,"
            + " value=000000104, isValid=false), (label=PTEC, value=TH.., isValid=true),"
            + " (label=IINST, value=000, isValid=true), (label=IMAX, value=000, isValid=true),"
            + " (label=PAPP, value=00000, isValid=true), (label=MOTDETAT, value=000000,"
            + " isValid=true)]",
        text);
  }
}
