// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a mutable array of bytes with utility methods for manipulation, searching, and
 * slicing.
 *
 * <p>Implements the {@link List} interface for bytes, and provides additional methods for
 * byte-level operations, such as splitting, slicing, and computing checksums. Supports conversion
 * from integers and arrays, and offers options for greedy, contiguous, and pattern-based
 * operations.
 *
 * @author Enedis Smarties team
 */
public class BytesArray implements List<Byte> {
  /** Default option for byte array operations. */
  public static final int DEFAULT_OPTIONS = 0x0000;

  /** Greedy option for slice/split operations. */
  public static final int GREEDY = 0x0001;

  /** Contiguous option for slice/split operations. */
  public static final int CONTIGUOUS = 0x0002;

  /** Option to remove pattern bytes from results. */
  public static final int REMOVE_PATTERNS = 0x0004;

  /**
   * Convert int to byte[]
   *
   * @param value
   * @param numberOfByte
   * @return byte[]
   * @throws NumberFormatException
   */
  public static byte[] intToArrayOfByte(int value, int numberOfByte) throws NumberFormatException {
    if (countBytes(value) > numberOfByte) {
      throw new NumberFormatException(
          "Value " + value + " can't be converted in a byte[" + numberOfByte + "]");
    }

    byte[] byteArray = new byte[numberOfByte];

    for (int i = 0; i < byteArray.length; i++) {
      if (i < Integer.BYTES) {
        byteArray[byteArray.length - i - 1] = (byte) (value >> (i * 8));
      }
    }

    return byteArray;
  }

  /**
   * Conver int to BytesArray
   *
   * @param value
   * @param numberOfByte
   * @return BytesArray
   * @throws Exception
   */
  public static BytesArray valueOf(int value, int numberOfByte) throws Exception {
    return new BytesArray(intToArrayOfByte(value, numberOfByte));
  }

  private static int countBytes(int value) {
    int tmp = Math.abs(value);
    int count = 0;

    for (int i = 0; i < Integer.BYTES; i++) {
      tmp = tmp >> 8;
      count++;
      if (tmp == 0) {
        break;
      }
    }
    return count;
  }

  protected byte[] buffer;

  /** Constructs an empty BytesArray. */
  public BytesArray() {
    this.buffer = new byte[0];
  }

  /**
   * Constructs a BytesArray from a given byte array.
   *
   * @param bytesArray the source array of bytes (copied)
   */
  public BytesArray(byte[] bytesArray) {
    this.buffer = bytesArray.clone();
  }

  @Override
  public int size() {
    return this.buffer.length;
  }

  @Override
  public boolean isEmpty() {
    return (0 == this.buffer.length) ? true : false;
  }

  @Override
  public boolean contains(Object element) {
    int index = this.indexOf(element);
    return (index >= 0) ? true : false;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Iterator iterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Byte[] toArray() {
    Byte[] bytesArray = new Byte[this.buffer.length];

    for (int i = 0; i < this.buffer.length; i++) {
      bytesArray[i] = this.buffer[i];
    }

    return bytesArray;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Byte[] toArray(Object[] a) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean add(Byte element) {
    byte byte_element = element;
    byte[] bytesArray = new byte[this.buffer.length + 1];

    System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length);
    bytesArray[bytesArray.length - 1] = byte_element;
    this.buffer = bytesArray;

    return true;
  }

  @Override
  public void add(int index, Byte element) {
    if ((index < 0) || (index >= this.buffer.length)) {
      this.add(element);
    } else {
      byte[] bytesArray = new byte[this.buffer.length + 1];

      if (0 == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 1, this.buffer.length);
      } else if ((this.buffer.length - 1) == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length - 1);

        bytesArray[bytesArray.length - 1] = this.buffer[this.buffer.length - 1];
      } else {
        System.arraycopy(this.buffer, 0, bytesArray, 0, index - 1);

        System.arraycopy(
            this.buffer,
            index, // src , src_pos
            bytesArray,
            index + 1, // dest, dest_pos
            this.buffer.length - index - 1); // size
      }

      bytesArray[index] = element;
      this.buffer = bytesArray;
    }
  }

  @Override
  public boolean remove(Object element) {
    boolean success = true;

    if ((null != element) && (element instanceof Number)) {
      int index = this.indexOf(element);

      if (index >= 0) {
        this.removeIndex(index);
      } else {
        success = false;
      }
    } else {
      success = false;
    }

    return success;
  }

  @Override
  public Byte remove(int index) {
    Byte element = null;

    if ((index >= 0) && (index < this.buffer.length)) {
      element = new Byte(this.buffer[index]);
      this.removeIndex(index);
    }

    return element;
  }

  /**
   * Removes a range of bytes from the buffer, from {@code fromIndex} to {@code toIndex}
   * (inclusive).
   *
   * @param fromIndex the starting index (inclusive)
   * @param toIndex the ending index (inclusive)
   * @return the number of bytes removed, or 0 if indices are invalid
   */
  public int remove(int fromIndex, int toIndex) {
    int length = 0;
    if ((fromIndex >= 0) && (toIndex < this.buffer.length) && (toIndex > fromIndex)) {
      this.removeSubList(fromIndex, toIndex);
      length = toIndex - fromIndex + 1;
    }
    return length;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends Byte> collection) {
    boolean hasChanged;

    if ((null != collection) && (collection.size() > 0)) {
      Object[] collectionArray = collection.toArray();
      byte[] bytesArray = new byte[this.buffer.length + collection.size()];

      System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length);

      int j = this.buffer.length;
      for (int i = 0; i < collectionArray.length; i++) {
        bytesArray[j] = ((Byte) collectionArray[i]).byteValue();
        j++;
      }

      this.buffer = bytesArray;

      hasChanged = true;
    } else {
      hasChanged = false;
    }

    return hasChanged;
  }

  @Override
  public boolean addAll(int index, Collection<? extends Byte> collection) {
    boolean hasChanged;

    if ((null != collection)
        && (collection.size() > 0)
        && (index >= 0)
        && (index <= this.buffer.length)) {
      Object[] collectionArray = collection.toArray();
      byte[] bytesArray = new byte[this.buffer.length + collection.size()];

      if (0 == index) {
        for (int i = 0; i < collectionArray.length; i++) {
          bytesArray[i] = ((Byte) collectionArray[i]).byteValue();
        }

        System.arraycopy(this.buffer, 0, bytesArray, collectionArray.length, this.buffer.length);
      } else if (this.buffer.length == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length);

        int j = index;
        for (int i = 0; i < collectionArray.length; i++) {
          bytesArray[j] = ((Byte) collectionArray[i]).byteValue();
          j++;
        }
      } else {
        System.arraycopy(this.buffer, 0, bytesArray, 0, index);

        int j = index;
        for (int i = 0; i < collectionArray.length; i++) {
          bytesArray[j] = ((Byte) collectionArray[i]).byteValue();
          j++;
        }

        System.arraycopy(
            this.buffer,
            index,
            bytesArray,
            index + collectionArray.length,
            this.buffer.length - index);
      }

      this.buffer = bytesArray;

      hasChanged = true;
    } else {
      hasChanged = false;
    }

    return hasChanged;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void clear() {
    this.buffer = new byte[0];
  }

  @Override
  public Byte get(int index) {
    Byte element = null;

    if ((index >= 0) && (index < this.buffer.length)) {
      element = new Byte(this.buffer[index]);
    } else {

    }

    return element;
  }

  @Override
  public Byte set(int index, Byte element) {
    Byte previous_element = null;

    if ((index >= 0) && (index < this.buffer.length)) {
      previous_element = new Byte(this.buffer[index]);
      this.buffer[index] = element;
    } else {
      previous_element = new Byte(this.buffer[this.buffer.length - 1]);
      this.buffer[this.buffer.length - 1] = element;
    }

    return new Byte(previous_element);
  }

  @Override
  public int indexOf(Object element) {
    return this.indexOf(element, 0);
  }

  /**
   * Returns the index of the first occurrence of the specified element, starting from the given
   * offset.
   *
   * @param element the element to search for (must be a Number)
   * @param offset the index to start searching from
   * @return the index of the element, or -1 if not found
   */
  public int indexOf(Object element, int offset) {
    int index = -1;
    if ((null != element)
        && (element instanceof Number)
        && offset >= 0
        && offset < this.buffer.length) {
      Byte byte_element = ((Number) element).byteValue();
      for (int i = offset; i < this.buffer.length; i++) {
        if (this.buffer[i] == byte_element) {
          index = i;
          break;
        }
      }
    }
    return index;
  }

  @Override
  public int lastIndexOf(Object element) {
    int index = -1;

    if ((null != element) && (element instanceof Number)) {
      Byte byte_element = ((Number) element).byteValue();

      for (int i = this.buffer.length - 1; i >= 0; i--) {
        if (this.buffer[i] == byte_element) {
          index = i;
          break;
        }
      }
    }

    return index;
  }

  @Override
  public ListIterator<Byte> listIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ListIterator<Byte> listIterator(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BytesArray subList(int fromIndex, int toIndex) {
    BytesArray subBytesArray = new BytesArray(new byte[0]);

    if (fromIndex >= 0) {
      if ((toIndex > fromIndex) && (toIndex < this.buffer.length)) {
        int size = (toIndex - fromIndex) + 1;
        byte[] buffer = new byte[size];

        System.arraycopy(this.buffer, fromIndex, buffer, 0, size);

        subBytesArray = new BytesArray(buffer);
      } else if ((toIndex < 0) || (toIndex >= this.buffer.length)) {
        int size = this.buffer.length - fromIndex;
        byte[] buffer = new byte[size];

        System.arraycopy(this.buffer, fromIndex, buffer, 0, size);

        subBytesArray = new BytesArray(buffer);
      } else {

      }
    }

    return subBytesArray;
  }

  /**
   * Returns a copy of the underlying byte array.
   *
   * @return a copy of the bytes in this BytesArray
   */
  public byte[] getBytes() {
    return this.buffer;
  }

  /**
   * Returns a deep copy of this BytesArray.
   *
   * @return a new BytesArray with the same contents
   */
  public BytesArray copy() {
    return new BytesArray(this.buffer);
  }

  /**
   * Returns a list of all indices where the specified element occurs in the buffer.
   *
   * @param element the element to search for (must be a Number)
   * @return a list of indices where the element is found
   */
  public List<Integer> indexesOf(Object element) {
    List<Integer> indexesList = new ArrayList<Integer>();
    if ((null != element) && (element instanceof Number)) {
      Byte byte_element = ((Number) element).byteValue();
      for (int i = 0; i < this.buffer.length; i++) {
        if (this.buffer[i] == byte_element) {
          indexesList.add(i);
        }
      }
    }
    return indexesList;
  }

  /**
   * @param value
   * @return true if the BytesArray starts with the given value, else return false
   */
  public boolean startsWith(byte value) {
    boolean retVal;

    if ((this.buffer.length > 0) && (this.buffer[0] == value)) {
      retVal = true;
    } else {
      retVal = false;
    }

    return retVal;
  }

  /**
   * @param value
   * @return true if the BytesArray ends with the given value, else return false
   */
  public boolean endsWith(byte value) {
    boolean retVal;

    if ((this.buffer.length > 0) && (this.buffer[this.buffer.length - 1] == value)) {
      retVal = true;
    } else {
      retVal = false;
    }

    return retVal;
  }

  /**
   * Get element count
   *
   * @param element
   * @return element count
   */
  public int count(Object element) {
    return this.indexesOf(element).size();
  }

  /**
   * Split
   *
   * @param pattern
   * @return split BytesArray
   */
  public List<BytesArray> split(Number pattern) {
    List<Integer> patternsIndexesList = this.indexesOf(pattern.byteValue());
    List<BytesArray> bytesArraysList = new ArrayList<BytesArray>();

    // If the split pattern exists, copy each segment into the corresponding element
    // of the array
    if (false == patternsIndexesList.isEmpty()) {
      int begin_index = 0;
      int end_index;
      int segment_length;
      int nb_segments = patternsIndexesList.size() + 1;

      for (int i = 0; i < nb_segments; i++) {
        end_index =
            (i < patternsIndexesList.size()) ? patternsIndexesList.get(i) : this.buffer.length;

        segment_length = end_index - begin_index;

        // If the segment size is not zero
        if (segment_length > 0) {
          byte[] segment = new byte[segment_length];

          System.arraycopy(this.buffer, begin_index, segment, 0, segment_length);

          bytesArraysList.add(new BytesArray(segment));
        } else {
          bytesArraysList.add(new BytesArray());
        }

        begin_index = end_index + 1;
      }
    }

    // Otherwise, copy all data into the single element of the array
    else {
      bytesArraysList.add(new BytesArray(this.buffer));
    }

    return bytesArraysList;
  }

  /**
   * Extract parts in the ByteArray according specific 'begin' and 'end' patterns.<br>
   * NB : 'begin' and 'end' patterns shall be different. Use split() method if they don't.<br>
   * <br>
   * Example: <br>
   * <br>
   * BytesArray myArray(new byte[12] {42,14,20,98,34,47,14,61,17,98,110,25}); <br>
   * List<BytesArray> mySlicedArray = myArray.slice(14,98);<br>
   * mySlicedArray.get(0) contains {14,20,98} <br>
   * mySlicedArray.get(1) contains {14,61,17,98} <br>
   *
   * @param beginPattern
   * @param endPattern
   * @return slices list
   */
  public List<BytesArray> slice(Number beginPattern, Number endPattern) {
    return this.slice(beginPattern, endPattern, -1, 0);
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param options
   * @return slices list
   */
  public List<BytesArray> slice(Number beginPattern, Number endPattern, int options) {
    return this.slice(beginPattern, endPattern, -1, options);
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param occurences
   * @param options
   * @return slices list
   */
  public List<BytesArray> slice(
      Number beginPattern, Number endPattern, int occurences, int options) {
    List<BytesArray> bytesArraysList;

    // ... If the function call is "greedy", there will be at most one element to
    // return,
    // which will correspond to the largest segment [begin_pattern, end_pattern] of
    // the array.
    // Note: the 'occurences' parameter is then not significant.
    if ((options & GREEDY) != 0) {
      bytesArraysList = this.sliceGreedy(beginPattern, endPattern, options);
    }

    // ... If the function call is "not greedy", the elements to return will
    // correspond
    // to the most rational segments [begin_pattern, end_pattern], within the limit
    // of the first n occurrences requested.
    else {
      bytesArraysList = this.sliceNotGreedy(beginPattern, endPattern, occurences, options);
    }

    return bytesArraysList;
  }

  /**
   * Add all byte
   *
   * @param bytesArray
   * @return true if changed has been applied
   */
  public boolean addAll(byte[] bytesArray) {
    boolean hasChanged = false;

    if (bytesArray != null && bytesArray.length > 0) {
      byte[] newBytesArray = new byte[this.buffer.length + bytesArray.length];

      // copy existing data
      System.arraycopy(this.buffer, 0, newBytesArray, 0, this.buffer.length);

      // copy new data
      System.arraycopy(bytesArray, 0, newBytesArray, this.buffer.length, bytesArray.length);

      this.buffer = newBytesArray;
      hasChanged = true;
    } else {
      hasChanged = false;
    }

    return hasChanged;
  }

  /**
   * Add all byte at index
   *
   * @param index
   * @param bytesArray
   * @return true if changed has been applied
   */
  public boolean addAll(int index, byte[] bytesArray) {
    boolean hasChanged = false;

    if ((index >= 0) && (index <= this.buffer.length)) {
      byte[] newBytesArray = new byte[this.buffer.length + bytesArray.length];

      if (0 == index) {
        System.arraycopy(bytesArray, 0, newBytesArray, 0, bytesArray.length);
        System.arraycopy(this.buffer, 0, newBytesArray, bytesArray.length, this.buffer.length);
      } else if (this.buffer.length == index) {
        System.arraycopy(this.buffer, 0, newBytesArray, 0, this.buffer.length);
        System.arraycopy(bytesArray, 0, newBytesArray, this.buffer.length, bytesArray.length);
      } else {
        System.arraycopy(this.buffer, 0, newBytesArray, 0, index);
        System.arraycopy(bytesArray, 0, newBytesArray, index, bytesArray.length);
        System.arraycopy(
            this.buffer,
            index,
            newBytesArray,
            index + bytesArray.length,
            this.buffer.length - index);
      }

      this.buffer = newBytesArray;
      hasChanged = true;
    } else {
      hasChanged = false;
    }

    return hasChanged;
  }

  /**
   * Compute the BytesArray checksum on 8bits (Byte)
   *
   * @return the computed checksum or 'null' if the BytesArray is empty
   */
  public Byte checksum8() {
    Byte checksum = null;

    if (this.buffer.length > 0) {
      byte checksum_8 = 0;

      for (int i = 0; i < this.buffer.length; i++) {
        checksum_8 += this.buffer[i];
      }

      checksum = new Byte(checksum_8);
    } else {

    }

    return checksum;
  }

  /**
   * Compute the BytesArray checksum on 16 bits (Short)
   *
   * @return the computed checksum or 'null' if the BytesArray is empty
   */
  public Short checksum16() {
    Short checksum = null;

    if (this.buffer.length > 0) {
      short checksum_16 = 0;

      for (int i = 0; i < this.buffer.length; i++) {
        checksum_16 += this.buffer[i];
      }

      checksum = new Short(checksum_16);
    } else {

    }

    return checksum;
  }

  /**
   * Compute the BytesArray checksum on 32 bits (Integer)
   *
   * @return the computed checksum or 'null' if the BytesArray is empty
   */
  public Integer checksum32() {
    Integer checksum = null;

    if (this.buffer.length > 0) {
      int checksum_32 = 0;

      for (int i = 0; i < this.buffer.length; i++) {
        checksum_32 += this.buffer[i];
      }

      checksum = new Integer(checksum_32);
    } else {

    }

    return checksum;
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param options
   * @return
   */
  private List<BytesArray> sliceGreedy(Number beginPattern, Number endPattern, int options) {
    List<BytesArray> bytesArraysList = new ArrayList<BytesArray>();

    int beginIndex = this.indexOf(beginPattern.byteValue());

    if (beginIndex >= 0) {
      int endIndex = this.lastIndexOf(endPattern.byteValue());

      if (endIndex > 0) {
        if ((options & REMOVE_PATTERNS) != 0) {
          beginIndex++;
          endIndex--;
        }

        int size = endIndex - beginIndex + 1;

        if (size > 0) {
          byte[] bytesBuffer = new byte[size];

          System.arraycopy(this.buffer, beginIndex, bytesBuffer, 0, size);

          bytesArraysList.add(new BytesArray(bytesBuffer));
        } else {
          bytesArraysList.add(new BytesArray(new byte[0]));
        }
      }
    } else {

    }

    return bytesArraysList;
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param occurences
   * @param options
   * @return
   */
  private List<BytesArray> sliceNotGreedy(
      Number beginPattern, Number endPattern, int occurences, int options) {
    List<BytesArray> bytesArraysList = new ArrayList<BytesArray>();

    // If segment contiguity is required,
    if ((options & CONTIGUOUS) != 0) {
      bytesArraysList = this.sliceContiguous(beginPattern, endPattern, occurences, options);
    } else {
      bytesArraysList = this.sliceNotContiguous(beginPattern, endPattern, occurences, options);
    }

    return bytesArraysList;
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param occurences
   * @param options
   * @return
   */
  private List<BytesArray> sliceContiguous(
      Number beginPattern, Number endPattern, int occurences, int options) {
    List<BytesArray> bytesArraysList = new ArrayList<BytesArray>();

    int beginIndex = -1;
    int endIndex = -1;

    for (int i = 0; i < this.buffer.length; i++) {
      // Handle a BEGIN pattern
      if (this.buffer[i] == beginPattern.byteValue()) {
        if ((-1 == endIndex) || (this.buffer[i - 1] == endPattern.byteValue())) {
          beginIndex = i;
        }
      }

      // Handle a END pattern
      else if (this.buffer[i] == endPattern.byteValue()) {
        // If we are not at the last element of the array
        if (i < (this.buffer.length - 1)) {
          // If the next character is the start pattern
          if (this.buffer[i + 1] == beginPattern.byteValue()) {
            endIndex = i;

            this.slicePart(bytesArraysList, beginIndex, endIndex, options);

            // If a number of occurrences is specified and the number of segments has
            // already been reached,
            // stop processing
            if ((occurences > 0) && (bytesArraysList.size() >= occurences)) {
              break;
            }
          }
        }

        // Otherwise, if we are at the last element of the array
        else {
          endIndex = i;

          this.slicePart(bytesArraysList, beginIndex, endIndex, options);

          // If a number of occurrences is specified and the number of segments has
          // already been reached,
          // stop processing
          if ((occurences > 0) && (bytesArraysList.size() >= occurences)) {
            break;
          }
        }
      } else {

      }
    }

    return bytesArraysList;
  }

  /**
   * @param beginPattern
   * @param endPattern
   * @param occurences
   * @param options
   * @return
   */
  private List<BytesArray> sliceNotContiguous(
      Number beginPattern, Number endPattern, int occurences, int options) {
    List<BytesArray> bytesArraysList = new ArrayList<BytesArray>();

    int beginIndex = -1;
    int endIndex = -1;

    for (int i = 0; i < this.buffer.length; i++) {
      if (this.buffer[i] == beginPattern.byteValue()) {
        beginIndex = i;
      } else if (this.buffer[i] == endPattern.byteValue()) {
        endIndex = i;

        this.slicePart(bytesArraysList, beginIndex, endIndex, options);

        // If a number of occurrences is specified and the number of segments has
        // already been reached,
        // stop processing
        if ((occurences > 0) && (bytesArraysList.size() >= occurences)) {
          break;
        }
      } else {

      }
    }

    return bytesArraysList;
  }

  /**
   * @param bytesArraysList
   * @param beginIndex
   * @param endIndex
   * @param options
   */
  private void slicePart(
      List<BytesArray> bytesArraysList, int beginIndex, int endIndex, int options) {
    if (beginIndex >= 0) {
      if ((options & REMOVE_PATTERNS) != 0) {
        beginIndex++;
        endIndex--;
      }

      BytesArray part = this.subList(beginIndex, endIndex);

      if (null != part) {
        bytesArraysList.add(part);
      }
    }
  }

  /**
   * @param index
   */
  private void removeIndex(int index) {
    byte[] bytesArray = new byte[this.buffer.length - 1];

    // If the index is the first in the array
    if (0 == index) {
      System.arraycopy(this.buffer, 1, bytesArray, 0, bytesArray.length);
    }

    // Else, if the index is the last
    else if ((this.buffer.length - 1) == index) {
      System.arraycopy(this.buffer, 0, bytesArray, 0, bytesArray.length);
    }

    // Else, if the index is in the middle of the array
    else {
      System.arraycopy(this.buffer, 0, bytesArray, 0, index);

      System.arraycopy(
          this.buffer,
          index + 1, // src , src_pos
          bytesArray,
          index, // dest, dest_pos
          bytesArray.length - index); // size
    }

    this.buffer = bytesArray;
  }

  private void removeSubList(int fromIndex, int toIndex) {
    byte[] bytesArray = new byte[this.buffer.length - (toIndex - fromIndex + 1)];

    if (fromIndex > 0) {
      System.arraycopy(this.buffer, 0, bytesArray, 0, fromIndex);
    }
    if (toIndex + 1 < this.buffer.length) {
      System.arraycopy(
          this.buffer, toIndex + 1, bytesArray, fromIndex, bytesArray.length - fromIndex);
    }

    this.buffer = bytesArray;
  }
}
