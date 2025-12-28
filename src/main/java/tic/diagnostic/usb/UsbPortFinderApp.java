package tic.diagnostic.usb;

import java.util.List;
import tic.io.usb.UsbPortDescriptor;
import tic.io.usb.UsbPortFinderBase;
import tic.io.usb.UsbPortJsonEncoder;

public class UsbPortFinderApp {

  /**
   * Program writing the USB port descriptor list (JSON format) on the output stream
   *
   * @param args not used
   */
  public static void main(String[] args) {
    List<UsbPortDescriptor> descriptors = UsbPortFinderBase.getInstance().findAll();
    System.out.println(UsbPortJsonEncoder.encode(descriptors));
  }
}
