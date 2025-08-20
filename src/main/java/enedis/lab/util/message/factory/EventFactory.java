// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.util.message.factory;

import enedis.lab.util.message.Event;

/**
 * Request factory
 */
public class EventFactory extends AbstractMessageFactory<Event>
{
	/**
	 * Default constructor
	 */
	public EventFactory()
	{
		super(Event.class);
	}
}
