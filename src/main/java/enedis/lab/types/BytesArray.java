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

/** Bytes array */
public class BytesArray implements List<Byte> {
  /** Default option */
  public static final int DEFAULT_OPTIONS = 0x0000;

  /** Greedy option */
  public static final int GREEDY = 0x0001;

  /** Contiguous option */
  public static final int CONTIGUOUS = 0x0002;

  /** Remove patterns option */
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

  /** Default constructor */
  public BytesArray() {
    this.buffer = new byte[0];
  }

  /**
   * Constructor from array of byte
   *
   * @param bytesArray
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

      // Si l'index est le premier
      if (0 == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 1, this.buffer.length);
      }

      // Sinon, si l'index est le dernier
      else if ((this.buffer.length - 1) == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length - 1);

        bytesArray[bytesArray.length - 1] = this.buffer[this.buffer.length - 1];
      }

      // Sinon, si l'index est au milieu du tableau
      else {
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
      // Obtenir l'index de la première occurence de l'élément
      int index = this.indexOf(element);

      // Si un tel élément a été trouvé,
      if (index >= 0) {
        this.removeIndex(index);
      }

      // Sinon,
      else {
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
    } else {
      // Aucune action
    }

    return element;
  }

  /**
   * Remove the buffer from an index to other
   *
   * @param fromIndex
   * @param toIndex
   * @return new length of buffer
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

      // Cas ou on ajoute en début:
      if (0 == index) {
        for (int i = 0; i < collectionArray.length; i++) {
          bytesArray[i] = ((Byte) collectionArray[i]).byteValue();
        }

        System.arraycopy(this.buffer, 0, bytesArray, collectionArray.length, this.buffer.length);
      }

      // Cas où on ajoute à la fin:
      else if (this.buffer.length == index) {
        System.arraycopy(this.buffer, 0, bytesArray, 0, this.buffer.length);

        int j = index;
        for (int i = 0; i < collectionArray.length; i++) {
          bytesArray[j] = ((Byte) collectionArray[i]).byteValue();
          j++;
        }
      }

      // Cas où on ajoute au milieu:
      else {
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
   * Get the buffer element of the given index
   *
   * @param element
   * @param offset
   * @return the element
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
    } else {
      // Aucune action
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
    } else {
      // Aucune action
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
   * Get bytes
   *
   * @return bytes
   */
  public byte[] getBytes() {
    return this.buffer;
  }

  /**
   * Copy
   *
   * @return copied BytesArray
   */
  public BytesArray copy() {
    return new BytesArray(this.buffer);
  }

  /**
   * Get index of element
   *
   * @param element
   * @return index of element
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
    } else {
      // Aucune action
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

    // Si le motif de séparation existe, copier chaque segment dans l'élément associé du tableau
    if (false == patternsIndexesList.isEmpty()) {
      int begin_index = 0;
      int end_index;
      int segment_length;
      int nb_segments = patternsIndexesList.size() + 1;

      for (int i = 0; i < nb_segments; i++) {
        end_index =
            (i < patternsIndexesList.size()) ? patternsIndexesList.get(i) : this.buffer.length;

        segment_length = end_index - begin_index;

        // Si la taille du segment est non nulle,
        if (segment_length > 0) {
          byte[] segment = new byte[segment_length];

          System.arraycopy(this.buffer, begin_index, segment, 0, segment_length);

          bytesArraysList.add(new BytesArray(segment));
        }

        // Sinon,
        else {
          bytesArraysList.add(new BytesArray());
        }

        begin_index = end_index + 1;
      }
    }

    // Sinon, copier dans l'unique élément du tableau l'intégralité des données
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

    // ... Si l'appel de la fonction est "gourmand" (greedy), il n'y aura au mieux qu'un élément à
    // retourner qui
    // correspondra au plus grand segment [begin_pattern, end_pattern] du tableau
    // NB le paramètre 'occurences' est alors non signifiant
    if ((options & GREEDY) != 0) {
      bytesArraysList = this.sliceGreedy(beginPattern, endPattern, options);
    }

    // ... Si l'appel de la fonction est "non gourmand" (not greedy), les éléments du tableau à
    // retourner
    // correspondront aux segments [begin_pattern, end_pattern]
    // les plus rationnels, dans la limite des n premières occurences demandées
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

      // copie des données existantes
      System.arraycopy(this.buffer, 0, newBytesArray, 0, this.buffer.length);

      // copie des nouvelles données
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

      // Cas ou on ajoute au début:
      if (0 == index) {
        System.arraycopy(bytesArray, 0, newBytesArray, 0, bytesArray.length);

        // copie des données existantes
        System.arraycopy(this.buffer, 0, newBytesArray, bytesArray.length, this.buffer.length);
      }

      // Cas où on ajoute à la fin:
      else if (this.buffer.length == index) {
        // copie des données existantes
        System.arraycopy(this.buffer, 0, newBytesArray, 0, this.buffer.length);

        // copie des nouvelles données
        System.arraycopy(bytesArray, 0, newBytesArray, this.buffer.length, bytesArray.length);
      }

      // Cas où on ajoute au milieu:
      else {
        // copie des données existantes (premier segment)
        System.arraycopy(this.buffer, 0, newBytesArray, 0, index);

        // copie des nouvelles données
        System.arraycopy(bytesArray, 0, newBytesArray, index, bytesArray.length);

        // copie des données existantes (deuxième segment)
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

    // Si la contiguité des segments est requise,
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
      // Traiter un motif DEBUT
      if (this.buffer[i] == beginPattern.byteValue()) {
        if ((-1 == endIndex) || (this.buffer[i - 1] == endPattern.byteValue())) {
          beginIndex = i;
        }
      }

      // Traiter un motif FIN
      else if (this.buffer[i] == endPattern.byteValue()) {
        // Si nous ne sommes pas au dernier élément du tableau
        if (i < (this.buffer.length - 1)) {
          // Si le prochain caractère est le motif de début de trame
          if (this.buffer[i + 1] == beginPattern.byteValue()) {
            endIndex = i;

            this.slicePart(bytesArraysList, beginIndex, endIndex, options);

            // Si un nombre d'occurences est spécifié et si le nombre de segments l'a déjà atteint,
            // interrompre le traitement
            if ((occurences > 0) && (bytesArraysList.size() >= occurences)) {
              break;
            }
          }
        }

        // Sinon, si nous sommes au dernier élément du tableau
        else {
          endIndex = i;

          this.slicePart(bytesArraysList, beginIndex, endIndex, options);

          // Si un nombre d'occurences est spécifié et si le nombre de segments l'a déjà atteint,
          // interrompre le traitement
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

        // Si un nombre d'occurences est spécifié et si le nombre de segments l'a déjà atteint,
        // interrompre le traitement
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

    // Si l'index est le premier du tableau
    if (0 == index) {
      System.arraycopy(this.buffer, 1, bytesArray, 0, bytesArray.length);
    }

    // Sinon, si l'index est le dernier
    else if ((this.buffer.length - 1) == index) {
      System.arraycopy(this.buffer, 0, bytesArray, 0, bytesArray.length);
    }

    // Sinon, si l'index est au milieu du tableau
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
