// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.group.TICGroup;
import tic.io.modem.ModemDescriptor;
import tic.io.modem.ModemFinderMock;
import tic.io.modem.ModemType;
import tic.mock.FunctionCall;
import tic.util.task.Task;
import tic.util.time.Time;

@SuppressWarnings("javadoc")
public class TICCoreBaseTest {
  private static final int NOTIFICATION_TIMEOUT = 1000;

  private ModemFinderMock ticPortFinder;
  private long plugNotifierPeriod;
  private TICCoreBase ticCore;

  @Before
  public void startTICCore() throws TICCoreException {
    this.ticPortFinder = new ModemFinderMock();
    this.plugNotifierPeriod = 50;
    this.ticCore =
        new TICCoreBase(
            this.ticPortFinder,
            this.plugNotifierPeriod,
            TICMode.AUTO,
            null);

    this.ticCore.start();
    this.waitTaskRunning(this.ticCore);
  }

  @After
  public void stopTICCore() throws TICCoreException {
    this.ticCore.stop();
    TICCoreStreamMock.streams.clear();
  }

  @Test
  public void test_getAvailableTICs_empty() {
    // Given
    List<TICIdentifier> availableTICs;

    // When
    availableTICs = this.ticCore.getAvailableTICs();

    // Then
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(0, availableTICs.size());
  }

  @Test
  public void test_getAvailableTICs_plug() throws TICCoreException {
    // Given
    List<TICIdentifier> availableTICs;
    ModemDescriptor descriptor1 =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor descriptor2 =
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build();
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    // When
    availableTICs = this.ticCore.getAvailableTICs();

    // Then
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(2, availableTICs.size());
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("1", "COM3", null)));
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("2", "COM4", null)));
  }

  @Test
  public void test_getAvailableTICs_unplug() throws TICCoreException {
    // Given
    List<TICIdentifier> availableTICs;
    ModemDescriptor descriptor1 =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor descriptor2 =
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build();
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    // When
    this.unplugModem(descriptor1);
    availableTICs = this.ticCore.getAvailableTICs();

    // Then
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(1, availableTICs.size());
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("2", "COM4", null)));
  }

  @Test
  public void test_getModemsInfo_empty() {
    // Given
    List<ModemDescriptor> modemsInfo;

    // When
    modemsInfo = this.ticCore.getModemsInfo();

    // Then
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(0, modemsInfo.size());
  }

  @Test
  public void test_getModemsInfo_plug() throws TICCoreException {
    // Given
    List<ModemDescriptor> modemsInfo;
    ModemDescriptor descriptor1 =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor descriptor2 =
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build();
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    // When
    modemsInfo = this.ticCore.getModemsInfo();

    // Then
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(2, modemsInfo.size());
    Assert.assertTrue(modemsInfo.contains(descriptor1));
    Assert.assertTrue(modemsInfo.contains(descriptor2));
  }

  @Test
  public void test_getModemsInfo_unplug() throws TICCoreException {
    // Given
    List<ModemDescriptor> modemsInfo;
    ModemDescriptor descriptor1 =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor descriptor2 =
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build();
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    // When
    this.unplugModem(descriptor1);
    modemsInfo = this.ticCore.getModemsInfo();

    // Then
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(1, modemsInfo.size());
    Assert.assertTrue(modemsInfo.contains(descriptor2));
  }

  @Test
  public void test_readNextFrame_ok() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier);
    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();
    TICCoreFrame frame =
        this.createFrame(identifier, TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));

    // When
    stream.notifyOnData(frame);
    this.waitTaskTerminated(task);

    // Then
    Assert.assertNull(task.exception);
    Assert.assertNotNull(task.frame);
    Assert.assertTrue(task.frame == frame);
  }

  @Test
  public void test_readNextFrame_error_OTHER_REASON() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier);
    TICCoreError error =
        new TICCoreError(identifier, TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");

    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();

    // When
    stream.notifyOnError(error);
    this.waitTaskTerminated(task);

    // Then
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(error.getErrorCode(), exception.getErrorCode());
    Assert.assertEquals(error.getErrorMessage(), exception.getErrorInfo());
  }

  @Test
  public void test_readNextFrame_error_STREAM_IDENTIFIER_NOT_FOUND() throws TICCoreException {
    // Given
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build());
    TICCoreReadNextFrameTask task =
        new TICCoreReadNextFrameTask(this.ticCore, new TICIdentifier("2", "COM3", null));

    // When
    task.start();
    this.waitTaskRunning(task);
    this.waitTaskTerminated(task);

    // Then
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(), exception.getErrorCode());
  }

  @Test
  public void test_readNextFrame_timeout() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier, 200);

    // When
    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();
    this.waitTaskTerminated(task);

    // Then
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(TICCoreErrorCode.DATA_READ_TIMEOUT.getCode(), exception.getErrorCode());
  }

  @Test
  public void test_subscribe_any() {
    // Given
    Exception exception = null;
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();

    // When
    try {
      this.ticCore.subscribe(subscriber);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    Assert.assertNull(exception);
  }

  @Test
  public void test_unsubscribe_any() {
    // Given
    Exception exception = null;
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);

    // When
    try {
      this.ticCore.unsubscribe(subscriber);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    Assert.assertNull(exception);
  }

  @Test
  public void test_subscribe_withIdentifier_ok() {
    // Given
    Exception exception = null;
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    TICIdentifier identifier;

    // When
    try {
      identifier = this.plugModem(descriptor);
      this.ticCore.subscribe(identifier, subscriber);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    Assert.assertNull(exception);
  }

  @Test
  public void test_unsubscribe_withIdentifier_ok() {
    // Given
    Exception exception = null;
    ModemDescriptor descriptor =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    TICIdentifier identifier;

    // When
    try {
      identifier = this.plugModem(descriptor);
      this.ticCore.subscribe(identifier, subscriber);
      this.ticCore.unsubscribe(identifier, subscriber);
    } catch (Exception e) {
      exception = e;
    }

    // Then
    Assert.assertNull(exception);
  }

  @Test
  public void test_subscribe_withIdentifier_notFound() throws TICCoreException {
    // Given
    Exception exception = null;
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build());
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    TICIdentifier identifier = new TICIdentifier(null, "COM4", null);

    // When
    try {
      this.ticCore.subscribe(identifier, subscriber);
      Assert.fail(
          "Subscribe should have thrown an exception since stream identifier does not match !");
    } catch (Exception e) {
      exception = e;
    }

    // Then
    Assert.assertTrue(exception instanceof TICCoreException);
    TICCoreException ticCoreException = (TICCoreException) exception;
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(), ticCoreException.getErrorCode());
  }

  @Test
  public void test_onError_whenUnplugModem() throws TICCoreException {
    // Given
    ModemDescriptor descriptor1 =
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build();
    ModemDescriptor descriptor2 =
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build();
    TICIdentifier identifier1 = this.plugModem(descriptor1);
    TICIdentifier identifier2 = this.plugModem(descriptor2);
    TICCoreSubscriberMock subscriber1 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber2 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber3 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber4 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber5 = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber1);
    this.ticCore.subscribe(identifier1, subscriber2);
    this.ticCore.subscribe(identifier1, subscriber3);
    this.ticCore.subscribe(identifier2, subscriber4);
    this.ticCore.subscribe(identifier2, subscriber5);

    // When
    this.unplugModem(descriptor1);

    // Then
    this.waitSubscriberNotification(subscriber1.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber1.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber1.onErrorCalls.get(0).error.getErrorCode());
    this.waitSubscriberNotification(subscriber2.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber2.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber2.onErrorCalls.get(0).error.getErrorCode());
    this.waitSubscriberNotification(subscriber3.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber3.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber3.onErrorCalls.get(0).error.getErrorCode());
    Assert.assertEquals(0, subscriber4.onErrorCalls.size());
    Assert.assertEquals(0, subscriber5.onErrorCalls.size());

    // When
    this.unplugModem(descriptor2);

    // Then
    this.waitSubscriberNotification(subscriber4.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber4.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber4.onErrorCalls.get(0).error.getErrorCode());
    this.waitSubscriberNotification(subscriber5.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber5.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber5.onErrorCalls.get(0).error.getErrorCode());
    this.waitSubscriberNotification(subscriber1.onErrorCalls, 1);
    Assert.assertEquals(2, subscriber1.onErrorCalls.size());
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_UNPLUGGED.getCode(),
        subscriber1.onErrorCalls.get(1).error.getErrorCode());
    Assert.assertEquals(1, subscriber2.onErrorCalls.size());
    Assert.assertEquals(1, subscriber3.onErrorCalls.size());
  }

  @Test
  public void test_onData_any_oneModem() throws TICCoreException {
    // Given
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));

    // When
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);

    // Then
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
  }

  @Test
  public void test_onData_any_twoModems() throws TICCoreException {
    // Given
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build());
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));
    TICCoreFrame frame2 =
        this.createFrame(
            stream2.getIdentifier(), TICMode.HISTORIC, LocalDateTime.of(2017, 2, 14, 9, 37, 12, 0));

    // When
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);
    stream2.notifyOnData(frame2);
    this.waitSubscriberNotification(subscriber.onDataCalls, 2);

    // Then
    Assert.assertEquals(2, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
    Assert.assertTrue(frame2 == subscriber.onDataCalls.get(1).frame);
  }

  @Test
  public void test_onData_withIdentifier_oneModem() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));

    // When
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);

    // Then
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
  }

  @Test
  public void test_onData_withIdentifier_twoModems() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("2")
            .portName("COM4")
            .modemType(ModemType.TELEINFO)
            .build());

    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));
    TICCoreFrame frame2 =
        this.createFrame(
            stream2.getIdentifier(), TICMode.HISTORIC, LocalDateTime.of(2017, 2, 14, 9, 37, 12, 0));

    // When
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);
    stream2.notifyOnData(frame2);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);

    // Then
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
  }

  @Test
  public void test_onError_any() throws TICCoreException {
    // Given
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);
    this.plugModem(
        new ModemDescriptor.Builder<>()
            .portId("1")
            .portName("COM3")
            .modemType(ModemType.MICHAUD)
            .build());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreError error1 =
        new TICCoreError(
            stream1.getIdentifier(), TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");

    // When
    stream1.notifyOnError(error1);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 1);

    // Then
    Assert.assertEquals(1, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);
  }

  @Test
  public void test_onError_withIdentifier() throws TICCoreException {
    // Given
    TICIdentifier identifier =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreError error1 =
        new TICCoreError(
            stream1.getIdentifier(), TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");

    // When
    stream1.notifyOnError(error1);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 1);

    // Then
    Assert.assertEquals(1, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);
  }

  @Test
  public void test_getIdentifiers() throws TICCoreException {
    // Given
    TICIdentifier identifier1 =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("1")
                .portName("COM3")
                .modemType(ModemType.MICHAUD)
                .build());
    TICIdentifier identifier2 =
        this.plugModem(
            new ModemDescriptor.Builder<>()
                .portId("2")
                .portName("COM4")
                .modemType(ModemType.TELEINFO)
                .build());

    TICCoreSubscriberMock subscriber1 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber2 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber3 = new TICCoreSubscriberMock();
    List<TICIdentifier> indentifierList1_subscribed;
    List<TICIdentifier> indentifierList2_subscribed;
    List<TICIdentifier> indentifierList3_subscribed;
    List<TICIdentifier> indentifierList1_unsubscribed;
    List<TICIdentifier> indentifierList2_unsubscribed;
    List<TICIdentifier> indentifierList3_unsubscribed;

    // When
    this.ticCore.subscribe(subscriber1);
    indentifierList1_subscribed = this.ticCore.getIndentifiers(subscriber1);
    this.ticCore.subscribe(identifier1, subscriber2);
    indentifierList2_subscribed = this.ticCore.getIndentifiers(subscriber2);
    this.ticCore.subscribe(identifier2, subscriber3);
    indentifierList3_subscribed = this.ticCore.getIndentifiers(subscriber3);
    this.ticCore.unsubscribe(subscriber1);
    indentifierList1_unsubscribed = this.ticCore.getIndentifiers(subscriber1);
    this.ticCore.unsubscribe(identifier1, subscriber2);
    indentifierList2_unsubscribed = this.ticCore.getIndentifiers(subscriber2);
    this.ticCore.unsubscribe(identifier2, subscriber3);
    indentifierList3_unsubscribed = this.ticCore.getIndentifiers(subscriber3);

    // Then
    Assert.assertNotNull(indentifierList1_subscribed);
    Assert.assertEquals(2, indentifierList1_subscribed.size());
    Assert.assertTrue(indentifierList1_subscribed.contains(identifier1));
    Assert.assertTrue(indentifierList1_subscribed.contains(identifier2));
    Assert.assertNotNull(indentifierList2_subscribed);
    Assert.assertEquals(1, indentifierList2_subscribed.size());
    Assert.assertTrue(indentifierList2_subscribed.contains(identifier1));
    Assert.assertNotNull(indentifierList3_subscribed);
    Assert.assertEquals(1, indentifierList3_subscribed.size());
    Assert.assertTrue(indentifierList3_subscribed.contains(identifier2));
    Assert.assertNotNull(indentifierList1_unsubscribed);
    Assert.assertEquals(0, indentifierList1_unsubscribed.size());
    Assert.assertNotNull(indentifierList2_unsubscribed);
    Assert.assertEquals(0, indentifierList2_unsubscribed.size());
    Assert.assertNotNull(indentifierList3_unsubscribed);
    Assert.assertEquals(0, indentifierList3_unsubscribed.size());
  }

  private TICIdentifier plugModem(ModemDescriptor descriptor) {
    this.ticPortFinder.addDescriptor(descriptor);
    this.waitPlugNotifierUpdate();

    return new TICIdentifier(descriptor.portId(), descriptor.portName(), null);
  }

  private void unplugModem(ModemDescriptor descriptor) {
    this.ticPortFinder.removeDescriptor(descriptor);
    this.waitPlugNotifierUpdate();
  }

  private TICCoreFrame createFrame(
      TICIdentifier identifier, TICMode mode, LocalDateTime localDateTime) {
    TICFrame content = new TICFrame(mode);
    if (mode == TICMode.STANDARD) {
      content.addGroup(new TICGroup("ADSC", identifier.getSerialNumber()));
    } else {
      content.addGroup(new TICGroup("ADCO", identifier.getSerialNumber()));
    }
    return new TICCoreFrame(identifier, mode, localDateTime, content);
  }

  private void waitPlugNotifierUpdate() {
    Time.sleep(2 * this.plugNotifierPeriod);
  }

  private void waitTaskRunning(Task task) {
    while (!task.isRunning()) {
      Time.sleep(50);
    }
  }

  private void waitTaskTerminated(Task task) {
    while (task.isRunning()) {
      Time.sleep(50);
    }
  }

  private void waitReadNextFrameSubscription() {
    Time.sleep(100);
  }

  private void waitSubscriberNotification(List<? extends FunctionCall> onCalls, int expectedSize) {
    long begin = System.nanoTime();
    long elapsed = 0;
    while (onCalls.size() < expectedSize && elapsed < NOTIFICATION_TIMEOUT) {
      Time.sleep(50);
      elapsed = (System.nanoTime() - begin) / 1000000;
    }
  }
}
