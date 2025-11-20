// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.io.tic.TICModemType;
import enedis.lab.io.tic.TICPortDescriptor;
import enedis.lab.io.tic.TICPortFinderMock;
import enedis.lab.mock.FunctionCall;
import enedis.lab.protocol.tic.TICMode;
import enedis.lab.types.DataDictionaryException;
import enedis.lab.types.datadictionary.DataDictionaryBase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tic.util.task.Task;
import tic.util.time.Time;

@SuppressWarnings("javadoc")
public class TICCoreBaseTest {
  private static final int NOTIFICATION_TIMEOUT = 1000;

  private TICPortFinderMock ticPortFinder;
  private long plugNotifierPeriod;
  private TICCoreBase ticCore;

  @Before
  public void startTICCore() throws TICCoreException {
    this.ticPortFinder = new TICPortFinderMock();
    this.plugNotifierPeriod = 50;
    this.ticCore =
        new TICCoreBase(
            this.ticPortFinder,
            this.plugNotifierPeriod,
            TICCoreStreamMock.class,
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
  public void test_getAvailableTICs() throws TICCoreException, DataDictionaryException {
    List<TICIdentifier> availableTICs = this.ticCore.getAvailableTICs();
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(0, availableTICs.size());

    TICPortDescriptor descriptor1 =
        new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD);
    TICPortDescriptor descriptor2 =
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO);
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    availableTICs = this.ticCore.getAvailableTICs();
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(2, availableTICs.size());
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("1", "COM3", null)));
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("2", "COM4", null)));

    this.unplugModem(descriptor2);

    availableTICs = this.ticCore.getAvailableTICs();
    Assert.assertNotNull(availableTICs);
    Assert.assertEquals(1, availableTICs.size());
    Assert.assertTrue(availableTICs.contains(new TICIdentifier("1", "COM3", null)));
  }

  @Test
  public void test_getModemsInfo() throws TICCoreException, DataDictionaryException {
    List<TICPortDescriptor> modemsInfo = this.ticCore.getModemsInfo();
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(0, modemsInfo.size());

    TICPortDescriptor descriptor1 =
        new TICPortDescriptor(null, "COM3", null, null, null, null, TICModemType.MICHAUD);
    TICPortDescriptor descriptor2 =
        new TICPortDescriptor(null, "COM4", null, null, null, null, TICModemType.TELEINFO);
    this.plugModem(descriptor1);
    this.plugModem(descriptor2);

    modemsInfo = this.ticCore.getModemsInfo();
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(2, modemsInfo.size());
    Assert.assertTrue(modemsInfo.contains(descriptor1));
    Assert.assertTrue(modemsInfo.contains(descriptor2));

    this.unplugModem(descriptor2);
    modemsInfo = this.ticCore.getModemsInfo();
    Assert.assertNotNull(modemsInfo);
    Assert.assertEquals(1, modemsInfo.size());
    Assert.assertTrue(modemsInfo.contains(descriptor1));
  }

  @Test
  public void test_readNextFrame_ok() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    Assert.assertEquals(identifier, stream.getIdentifier());

    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier);
    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();
    TICCoreFrame frame =
        this.createFrame(identifier, TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));
    stream.notifyOnData(frame);
    this.waitTaskTerminated(task);
    Assert.assertNull(task.exception);
    Assert.assertNotNull(task.frame);
    Assert.assertTrue(task.frame == frame);
  }

  @Test
  public void test_readNextFrame_error_OTHER_REASON()
      throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    Assert.assertEquals(identifier, stream.getIdentifier());

    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier);
    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();
    TICCoreError error =
        new TICCoreError(identifier, TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");
    stream.notifyOnError(error);
    this.waitTaskTerminated(task);
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(error.getErrorCode(), exception.getErrorCode());
    Assert.assertEquals(error.getErrorMessage(), exception.getErrorInfo());
  }

  @Test
  public void test_readNextFrame_error_STREAM_IDENTIFIER_NOT_FOUND()
      throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    Assert.assertEquals(identifier, stream.getIdentifier());

    TICCoreReadNextFrameTask task =
        new TICCoreReadNextFrameTask(this.ticCore, new TICIdentifier("2", "COM3", null));
    task.start();
    this.waitTaskRunning(task);
    this.waitTaskTerminated(task);
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(
        TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(), exception.getErrorCode());
  }

  @Test
  public void test_readNextFrame_timeout() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream = TICCoreStreamMock.streams.get(0);
    Assert.assertEquals(identifier, stream.getIdentifier());

    TICCoreReadNextFrameTask task = new TICCoreReadNextFrameTask(this.ticCore, identifier, 200);
    task.start();
    this.waitTaskRunning(task);
    this.waitReadNextFrameSubscription();
    this.waitTaskTerminated(task);
    Assert.assertNull(task.frame);
    Assert.assertNotNull(task.exception);
    Assert.assertTrue(task.exception instanceof TICCoreException);
    TICCoreException exception = (TICCoreException) task.exception;
    Assert.assertEquals(TICCoreErrorCode.DATA_READ_TIMEOUT.getCode(), exception.getErrorCode());
  }

  @Test
  public void test_subscribe_any() throws TICCoreException, DataDictionaryException {
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);
  }

  @Test
  public void test_unsubscribe_any() throws TICCoreException, DataDictionaryException {
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);
    this.ticCore.unsubscribe(subscriber);
  }

  @Test
  public void test_subscribe_withIdentifier_ok() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));

    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);
  }

  @Test
  public void test_unsubscribe_withIdentifier_ok()
      throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));

    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);
    this.ticCore.unsubscribe(identifier, subscriber);
  }

  @Test
  public void test_subscribe_withIdentifier_notFound()
      throws TICCoreException, DataDictionaryException {
    this.plugModem(
        new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));

    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    TICIdentifier identifier = new TICIdentifier(null, "COM4", null);
    try {
      this.ticCore.subscribe(identifier, subscriber);
      Assert.fail(
          "Subscribe should have thrown an exception since stream identifier does not match !");
    } catch (Exception e) {
      Assert.assertTrue(e instanceof TICCoreException);
      TICCoreException exception = (TICCoreException) e;
      Assert.assertEquals(
          TICCoreErrorCode.STREAM_IDENTIFIER_NOT_FOUND.getCode(), exception.getErrorCode());
    }
  }

  @Test
  public void test_onError_whenUnplugModem() throws TICCoreException, DataDictionaryException {
    TICPortDescriptor descriptor1 =
        new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD);
    TICPortDescriptor descriptor2 =
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO);
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

    this.unplugModem(descriptor1);
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

    this.unplugModem(descriptor2);
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
  public void test_onData_any() throws TICCoreException, DataDictionaryException {
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);

    this.plugModem(
        new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);

    this.plugModem(
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO));
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreFrame frame2 =
        this.createFrame(
            stream2.getIdentifier(), TICMode.HISTORIC, LocalDateTime.of(2017, 2, 14, 9, 37, 12, 0));
    stream2.notifyOnData(frame2);
    this.waitSubscriberNotification(subscriber.onDataCalls, 2);
    Assert.assertEquals(2, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
    Assert.assertTrue(frame2 == subscriber.onDataCalls.get(1).frame);
  }

  @Test
  public void test_onData_withIdentifier() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);

    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreFrame frame1 =
        this.createFrame(
            stream1.getIdentifier(), TICMode.STANDARD, LocalDateTime.of(2020, 3, 3, 12, 59, 30, 0));
    stream1.notifyOnData(frame1);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);

    this.plugModem(
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.MICHAUD));
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreFrame frame2 =
        this.createFrame(
            stream2.getIdentifier(), TICMode.HISTORIC, LocalDateTime.of(2017, 2, 14, 9, 37, 12, 0));
    stream2.notifyOnData(frame2);
    this.waitSubscriberNotification(subscriber.onDataCalls, 1);
    Assert.assertEquals(1, subscriber.onDataCalls.size());
    Assert.assertTrue(frame1 == subscriber.onDataCalls.get(0).frame);
  }

  @Test
  public void test_onError_any() throws TICCoreException, DataDictionaryException {
    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(subscriber);

    this.plugModem(
        new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreError error1 =
        new TICCoreError(
            stream1.getIdentifier(), TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");
    stream1.notifyOnError(error1);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);

    this.plugModem(
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO));
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreError error2 =
        new TICCoreError(
            stream2.getIdentifier(),
            TICCoreErrorCode.OTHER_REASON.getCode(),
            "Error reading stream COM4");
    stream2.notifyOnError(error2);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 2);
    Assert.assertEquals(2, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);
    Assert.assertTrue(error2 == subscriber.onErrorCalls.get(1).error);
  }

  @Test
  public void test_onError_withIdentifier() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));

    TICCoreSubscriberMock subscriber = new TICCoreSubscriberMock();
    this.ticCore.subscribe(identifier, subscriber);

    Assert.assertEquals(1, TICCoreStreamMock.streams.size());
    TICCoreStreamMock stream1 = TICCoreStreamMock.streams.get(0);
    TICCoreError error1 =
        new TICCoreError(
            stream1.getIdentifier(), TICCoreErrorCode.OTHER_REASON.getCode(), "Cannot read stream");
    stream1.notifyOnError(error1);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);

    this.plugModem(
        new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO));
    TICCoreStreamMock stream2 = TICCoreStreamMock.streams.get(1);
    TICCoreError error2 =
        new TICCoreError(
            stream2.getIdentifier(),
            TICCoreErrorCode.OTHER_REASON.getCode(),
            "Error reading stream COM4");
    stream2.notifyOnError(error2);
    this.waitSubscriberNotification(subscriber.onErrorCalls, 1);
    Assert.assertEquals(1, subscriber.onErrorCalls.size());
    Assert.assertTrue(error1 == subscriber.onErrorCalls.get(0).error);
  }

  @Test
  public void test_getIdentifiers() throws TICCoreException, DataDictionaryException {
    TICIdentifier identifier1 =
        this.plugModem(
            new TICPortDescriptor("1", "COM3", null, null, null, null, TICModemType.MICHAUD));
    TICIdentifier identifier2 =
        this.plugModem(
            new TICPortDescriptor("2", "COM4", null, null, null, null, TICModemType.TELEINFO));

    TICCoreSubscriberMock subscriber1 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber2 = new TICCoreSubscriberMock();
    TICCoreSubscriberMock subscriber3 = new TICCoreSubscriberMock();

    this.ticCore.subscribe(subscriber1);
    List<TICIdentifier> indentifierList1 = this.ticCore.getIndentifiers(subscriber1);
    Assert.assertNotNull(indentifierList1);
    Assert.assertEquals(2, indentifierList1.size());
    Assert.assertTrue(indentifierList1.contains(identifier1));
    Assert.assertTrue(indentifierList1.contains(identifier2));

    this.ticCore.subscribe(identifier1, subscriber2);
    List<TICIdentifier> indentifierList2 = this.ticCore.getIndentifiers(subscriber2);
    Assert.assertNotNull(indentifierList2);
    Assert.assertEquals(1, indentifierList2.size());
    Assert.assertTrue(indentifierList2.contains(identifier1));

    this.ticCore.subscribe(identifier2, subscriber3);
    List<TICIdentifier> indentifierList3 = this.ticCore.getIndentifiers(subscriber3);
    Assert.assertNotNull(indentifierList3);
    Assert.assertEquals(1, indentifierList3.size());
    Assert.assertTrue(indentifierList3.contains(identifier2));

    this.ticCore.unsubscribe(subscriber1);
    indentifierList1 = this.ticCore.getIndentifiers(subscriber1);
    Assert.assertNotNull(indentifierList1);
    Assert.assertEquals(0, indentifierList1.size());

    this.ticCore.unsubscribe(identifier1, subscriber2);
    indentifierList2 = this.ticCore.getIndentifiers(subscriber2);
    Assert.assertNotNull(indentifierList2);
    Assert.assertEquals(0, indentifierList2.size());

    this.ticCore.unsubscribe(identifier2, subscriber3);
    indentifierList3 = this.ticCore.getIndentifiers(subscriber3);
    Assert.assertNotNull(indentifierList3);
    Assert.assertEquals(0, indentifierList3.size());
  }

  private TICIdentifier plugModem(TICPortDescriptor descriptor) throws DataDictionaryException {
    this.ticPortFinder.addDescriptor(descriptor);
    this.waitPlugNotifierUpdate();

    return new TICIdentifier(descriptor.getPortId(), descriptor.getPortName(), null);
  }

  private void unplugModem(TICPortDescriptor descriptor) {
    this.ticPortFinder.removeDescriptor(descriptor);
    this.waitPlugNotifierUpdate();
  }

  private TICCoreFrame createFrame(
      TICIdentifier identifier, TICMode mode, LocalDateTime localDateTime)
      throws DataDictionaryException {
    DataDictionaryBase content = new DataDictionaryBase();
    if (mode == TICMode.STANDARD) {
      content.set("ADSC", identifier.getSerialNumber());
    } else {
      content.set("ADCO", identifier.getSerialNumber());
    }
    TICCoreFrame frame = new TICCoreFrame(identifier, mode, localDateTime, content);

    return frame;
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
