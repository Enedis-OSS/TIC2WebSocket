// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.frame.group;

import enedis.lab.protocol.tic.TICMode;
import org.junit.Assert;
import org.junit.Test;

public class TICGroupTest {
  @Test
  public void test_default_constructor() {
    // Given

    // When
    TICGroup group = new TICGroup("ADCO", "12345");

    // Then
    Assert.assertEquals("TIC group label mismatch", "ADCO", group.getLabel());
    Assert.assertEquals("TIC group value mismatch", "12345", group.getValue());
    Assert.assertEquals("TIC group isValid mismatch", true, group.isValid());
  }

  @Test
  public void test_full_constructor() {
    // Given

    // When
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // Then
    Assert.assertEquals("TIC group label mismatch", "ADSC", group.getLabel());
    Assert.assertEquals("TIC group value mismatch", "156FE", group.getValue());
    Assert.assertEquals("TIC group isValid mismatch", false, group.isValid());
  }

  @Test
  public void test_equals_identical() {
    // Given
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // When
    boolean matches = group.equals(group);

    // Then
    Assert.assertEquals("TIC group first mismatch itself", true, matches);
  }

  @Test
  public void test_equals_match() {
    // Given
    TICGroup firstGroup = new TICGroup("ADSC", "156FE", false);
    TICGroup secondGroup = new TICGroup("ADSC", "156FE", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first mismatch second", true, firstMatchesSecond);
    Assert.assertEquals("TIC group second mismatch first", true, secondMatchesFirst);
  }

  @Test
  public void test_equals_label_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADSC", "156FE", false);
    TICGroup secondGroup = new TICGroup("ADCO", "156FE", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_value_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADSC", "156FE", false);
    TICGroup secondGroup = new TICGroup("ADSC", "156FF", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_isValid_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADSC", "156FE", false);
    TICGroup secondGroup = new TICGroup("ADSC", "156FE", true);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_label_and_value_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADCO", "156FE", false);
    TICGroup secondGroup = new TICGroup("ADSC", "156FF", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_label_and_isValid_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADCO", "156FF", true);
    TICGroup secondGroup = new TICGroup("ADSC", "156FF", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_value_and_isValid_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADCO", "156FF", true);
    TICGroup secondGroup = new TICGroup("ADCO", "156FE", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_full_mismatch() {
    // Given
    TICGroup firstGroup = new TICGroup("ADSC", "156FF", true);
    TICGroup secondGroup = new TICGroup("ADCO", "156FE", false);

    // When
    boolean firstMatchesSecond = firstGroup.equals(secondGroup);
    boolean secondMatchesFirst = secondGroup.equals(firstGroup);

    // Then
    Assert.assertEquals("TIC group first should not match second", false, firstMatchesSecond);
    Assert.assertEquals("TIC group second should not match first", false, secondMatchesFirst);
  }

  @Test
  public void test_equals_null() {
    // Given
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // When
    boolean matches = group.equals(null);

    // Then
    Assert.assertEquals("TIC group match null", false, matches);
  }

  @Test
  public void test_equals_other_class() {
    // Given
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // When
    boolean matches = group.equals((Object) TICMode.HISTORIC);

    // Then
    Assert.assertEquals("TIC group match other class", false, matches);
  }

  @Test
  public void test_hashCode() {
    // Given
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // When
    int hashCode = group.hashCode();

    // Then
    Assert.assertEquals("TIC group hash code unexpected", 48891225, hashCode);
  }

  @Test
  public void test_toString() {
    // Given
    TICGroup group = new TICGroup("ADSC", "156FE", false);

    // When
    String text = group.toString();

    // Then
    Assert.assertEquals(
        "TIC group string unexpected", "(label=ADSC, value=156FE, isValid=false)", text);
  }
}
