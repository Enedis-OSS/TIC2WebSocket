// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

import enedis.lab.mock.FunctionCall;

@SuppressWarnings("javadoc")
public class TICCoreSubscriberOnDataCall extends FunctionCall
{
	public TICCoreFrame frame;

	public TICCoreSubscriberOnDataCall(TICCoreFrame frame)
	{
		super();
		this.frame = frame;
	}

}
