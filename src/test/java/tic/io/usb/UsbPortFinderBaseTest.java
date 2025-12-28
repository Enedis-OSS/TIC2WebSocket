package tic.io.usb;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class UsbPortFinderBaseTest {

  @Test
  public void test_getInstance() {
    // Given

    // When
    UsbPortFinderBase instance1 = UsbPortFinderBase.getInstance();
    UsbPortFinderBase instance2 = UsbPortFinderBase.getInstance();

    // Then
    Assert.assertNotNull(instance1);
    Assert.assertNotNull(instance2);
    Assert.assertSame(instance1, instance2);
  }

  @Test
  public void test_findAll() {
    // Given

    // When
    List<UsbPortDescriptor> descriptors = UsbPortFinderBase.getInstance().findAll();

    // Then
    Assert.assertNotNull(descriptors);
  }

  @Test
  public void test_findByProductId() {
    // Given
    short idProduct = 0;
    // When
    List<UsbPortDescriptor> descriptors =
        UsbPortFinderBase.getInstance().findByProductId(idProduct);

    // Then
    Assert.assertNotNull(descriptors);
  }

  @Test
  public void test_findByVendorId() {
    // Given
    short idVendor = 0;
    // When
    List<UsbPortDescriptor> descriptors = UsbPortFinderBase.getInstance().findByVendorId(idVendor);

    // Then
    Assert.assertNotNull(descriptors);
  }

  @Test
  public void test_indByProductIdAndVendorId() {
    // Given
    short idProduct = 0;
    short idVendor = 0;
    // When
    List<UsbPortDescriptor> descriptors =
        UsbPortFinderBase.getInstance().findByProductIdAndVendorId(idProduct, idVendor);

    // Then
    Assert.assertNotNull(descriptors);
  }
}
