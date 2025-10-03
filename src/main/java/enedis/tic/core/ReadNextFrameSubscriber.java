// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.tic.core;

public class ReadNextFrameSubscriber implements TICCoreSubscriber
{
	private TICCoreFrame	frame;
	private TICCoreError	error;

	public ReadNextFrameSubscriber()
	{
		super();
		this.clear();
	}

	@Override
	public void onData(TICCoreFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void onError(TICCoreError error)
	{
		this.error = error;
	}

	public TICCoreFrame getData()
	{
		return this.frame;
	}

	public TICCoreError getError()
	{
		return this.error;
	}

	public void clear()
	{
		this.frame = null;
		this.error = null;
	}

}
