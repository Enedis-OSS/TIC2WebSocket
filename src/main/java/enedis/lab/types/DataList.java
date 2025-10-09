// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.json.JSONArray;

/**
 * Interface extending Java standard List to provide JSON serialization mechanism
 *
 * @param <E> the type of elements in this list
 */
public interface DataList<E> extends List<E> {
  /**
   * Write this data list to a File
   *
   * @param file the output file
   * @param indentFactor JSON indentation factor
   * @throws IOException if writing file fails
   */
  public void toFile(File file, int indentFactor) throws IOException;

  /**
   * Write this data list to a Stream
   *
   * @param stream the output stream
   * @param indentFactor JSON indentation factor
   * @throws IOException if writing stream fails
   */
  public void toStream(OutputStream stream, int indentFactor) throws IOException;

  @Override
  public String toString();

  /**
   * Convert this data list to a String
   *
   * @param indentFactor JSON indentation factor
   * @return JSON string representation
   */
  public String toString(int indentFactor);

  /**
   * Convert this data list to JSON array
   *
   * @return JSON array
   */
  public JSONArray toJSON();
}
