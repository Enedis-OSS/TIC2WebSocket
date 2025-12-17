package tic.frame.codec;

import tic.frame.TICFrame;
import tic.frame.TICMode;
import tic.frame.TICModeDetector;
import tic.frame.checksum.TICChecksum;
import tic.frame.checksum.TICChecksumOffset;
import tic.frame.delimiter.TICFrameDelimiter;
import tic.frame.delimiter.TICGroupDelimiter;
import tic.frame.delimiter.TICSeparator;
import tic.frame.group.TICGroup;

public class TICFrameCodec {

  private TICFrameCodec() {}

  public static TICFrame decode(byte[] frameBuffer) {
    TICMode mode = checkFrameBuffer(frameBuffer);
    TICFrame frame = new TICFrame(mode);
    StringBuffer groupBuffer = new StringBuffer();

    for (int i = 1; i < frameBuffer.length; i++) {
      if (groupBuffer.length() == 0) {
        if (frameBuffer[i] == TICGroupDelimiter.BEGIN.getValue()) {
          groupBuffer.append((char) frameBuffer[i]);
        }
      } else {
        groupBuffer.append((char) frameBuffer[i]);
        if (frameBuffer[i] == TICGroupDelimiter.END.getValue()) {
          TICGroup group = decodeGroup(groupBuffer.toString().getBytes(), mode);
          frame.addGroup(group);
          groupBuffer.setLength(0);
        }
      }
    }

    return frame;
  }

  public static byte[] encode(TICFrame frame) {
    if (frame == null) {
      throw new IllegalArgumentException("frame cannot be null");
    }
    StringBuffer frameBuffer = new StringBuffer();
    frameBuffer.append((char) TICFrameDelimiter.BEGIN.getValue());
    for (TICGroup group : frame.getGroupList()) {
      byte[] groupBuffer = encodeGroup(group, frame.getMode());
      frameBuffer.append(new String(groupBuffer));
    }
    frameBuffer.append((char) TICFrameDelimiter.END.getValue());
    return frameBuffer.toString().getBytes();
  }

  private static TICGroup decodeGroup(byte[] groupBuffer, TICMode mode) {
    boolean endLabelFound = false;
    StringBuffer label = new StringBuffer();
    StringBuffer value = new StringBuffer();
    byte separator = TICSeparator.getValueFromMode(mode);
    int beginChecksumOffset = TICChecksumOffset.getOffsetBegin();
    int checksumOffset = TICChecksumOffset.getOffsetChecksum(groupBuffer);
    int endValueOffset = checksumOffset - 1; // excluding separator before checksum

    for (int i = beginChecksumOffset; i < endValueOffset; i++) {
      if (!endLabelFound) {
        if (groupBuffer[i] == separator) {
          endLabelFound = true;
          continue;
        }
        label.append((char) groupBuffer[i]);
      } else {
        value.append((char) groupBuffer[i]);
      }
    }
    boolean isValid = TICChecksum.verifyChecksum(groupBuffer, mode);
    TICGroup group = new TICGroup(label.toString(), value.toString(), isValid);

    return group;
  }

  private static byte[] encodeGroup(TICGroup group, TICMode mode) {
    int checksum = 0;
    byte separator = TICSeparator.getValueFromMode(mode);
    StringBuffer groupBuffer = new StringBuffer();

    groupBuffer.append((char) TICGroupDelimiter.BEGIN.getValue());
    groupBuffer.append(group.getLabel());
    groupBuffer.append((char) separator);
    groupBuffer.append(group.getValue());
    groupBuffer.append((char) separator);
    groupBuffer.append((char) checksum);
    groupBuffer.append((char) TICGroupDelimiter.END.getValue());
    byte[] groupBufferBytes = groupBuffer.toString().getBytes();
    checksum = TICChecksum.computeChecksum(groupBufferBytes, mode);
    if (!group.isValid()) {
      checksum = (checksum + 1) & 0xFF;
    }
    int checksumOffset = TICChecksumOffset.getOffsetChecksum(groupBufferBytes);
    groupBuffer.setCharAt(checksumOffset, (char) checksum);

    return groupBuffer.toString().getBytes();
  }

  private static TICMode checkFrameBuffer(byte[] frameBuffer) {
    if (frameBuffer == null) {
      throw new IllegalArgumentException("frameBuffer cannot be null");
    }
    if (frameBuffer.length
        < TICFrameDelimiter.values().length + TICGroupDelimiter.values().length) {
      throw new IllegalArgumentException(
          "frameBuffer length must be >= "
              + (TICFrameDelimiter.values().length + TICGroupDelimiter.values().length)
              + " bytes");
    }
    if (frameBuffer[0] != TICFrameDelimiter.BEGIN.getValue()) {
      throw new IllegalArgumentException(
          "frameBuffer begin must be " + TICFrameDelimiter.BEGIN.getValue());
    }
    if (frameBuffer[frameBuffer.length - 1] != TICFrameDelimiter.END.getValue()) {
      throw new IllegalArgumentException(
          "frameBuffer end must be " + TICFrameDelimiter.END.getValue());
    }
    TICMode mode = TICModeDetector.findModeFromFrameBuffer(frameBuffer);
    if (mode == null) {
      throw new IllegalArgumentException("Unable to determine TIC Mode from frame buffer!");
    }

    return mode;
  }
}
