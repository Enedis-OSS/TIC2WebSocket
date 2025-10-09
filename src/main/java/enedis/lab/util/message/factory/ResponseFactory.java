// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Response;

/** Request factory */
public class ResponseFactory extends AbstractMessageFactory<Response> {
  /** Default constructor */
  public ResponseFactory() {
    super(Response.class);
  }
}
