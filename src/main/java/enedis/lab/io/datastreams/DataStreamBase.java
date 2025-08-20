// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.io.datastreams;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enedis.lab.io.channels.Channel;
import enedis.lab.io.channels.ChannelListener;
import enedis.lab.io.channels.ChannelStatus;
import enedis.lab.types.DataDictionary;
import enedis.lab.util.task.NotifierBase;

/**
 * DataStream Base
 */
public abstract class DataStreamBase implements DataStream, ChannelListener
{
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTANTS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// TYPES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// STATIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// ATTRIBUTES
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected NotifierBase<DataStreamListener>	notifier;
	protected DataStreamConfiguration			configuration;
	private DataStreamStatus					status;
	protected Channel							channel;
	protected Logger							logger;

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// CONSTRUCTORS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor
	 *
	 * @param configuration
	 *            configuration used to set the stream
	 * @throws DataStreamException
	 */
	public DataStreamBase(DataStreamConfiguration configuration) throws DataStreamException
	{
		this.init(configuration);
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// ChannelListener
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onStatusChanged(String channelName, ChannelStatus channelStatus)
	{
		DataStreamStatus status_cpy = this.status;

		switch (channelStatus)
		{
			case STOPPED:
			{
				if (DataStreamStatus.CLOSED != this.status)
				{
					this.setStatus(DataStreamStatus.ERROR);
				}
				break;
			}
			case STARTED:
			{
				if (DataStreamStatus.ERROR == this.status)
				{
					this.setStatus(DataStreamStatus.OPENED);
				}
				break;
			}
			case ERROR:
			{
				this.setStatus(DataStreamStatus.ERROR);
				break;
			}

			default:
			{

			}
		}

		if (!this.notifier.getSubscribers().isEmpty() && (status_cpy != this.status))
		{
			this.notifyOnStatusChanged(this.status);
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// INTERFACE
	/// DataStream
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void open() throws DataStreamException
	{
		String channelName = this.configuration.getChannelName();

		if (null != channelName)
		{
			if (null != this.channel)
			{
				this.channel.subscribe(this);
				this.setStatus(DataStreamStatus.OPENED);
				this.logger.info("Stream " + this.getName() + " start");
			}
			else
			{
				this.setStatus(DataStreamStatus.ERROR);
				DataStreamException.raiseChannelError(channelName, "channel does not exist");
			}
		}
	}

	@Override
	public void close() throws DataStreamException
	{
		String channelName = this.configuration.getChannelName();

		if (null != channelName)
		{
			if (null != this.channel)
			{
				this.channel.unsubscribe(this);
				this.setStatus(DataStreamStatus.CLOSED);
				this.logger.info("Service " + this.getName() + " stop");
			}
			else
			{
				this.setStatus(DataStreamStatus.ERROR);
				DataStreamException.raiseChannelError(channelName, "channel does not exist");
			}
		}
	}

	@Override
	public void setup(DataStreamConfiguration configuration) throws DataStreamException
	{
		if (this.status == DataStreamStatus.OPENED)
		{
			DataStreamException.raiseInternalError("Cannot setup stream " + this.getName() + " (already open)");
		}
		this.configuration = configuration;
	}

	@Override
	public void subscribe(DataStreamListener listener)
	{
		this.notifier.subscribe(listener);
	}

	@Override
	public void unsubscribe(DataStreamListener listener)
	{
		this.notifier.unsubscribe(listener);
	}

	@Override
	public boolean hasSubscriber(DataStreamListener subscriber)
	{
		return this.notifier.hasSubscriber(subscriber);
	}

	@Override
	public Collection<DataStreamListener> getSubscribers()
	{
		return this.notifier.getSubscribers();
	}

	@Override
	public String getName()
	{
		return this.configuration.getName();
	}

	@Override
	public DataStreamConfiguration getConfiguration()
	{
		return (DataStreamConfiguration) this.configuration.clone();
	}

	@Override
	public DataStreamDirection getDirection()
	{
		return this.configuration.getDirection();
	}

	@Override
	public DataStreamType getType()
	{
		return this.configuration.getType();
	}

	@Override
	public DataStreamStatus getStatus()
	{
		return this.status;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PUBLIC METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Set the channel reference
	 *
	 * @param channel
	 */
	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PROTECTED METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Notify onDataReceived listeners
	 *
	 * @param data
	 *            new data received
	 */
	protected void notifyOnDataReceived(DataDictionary data)
	{
		if (data != null)
		{
			for (DataStreamListener listener : this.notifier.getSubscribers())
			{
				listener.onDataReceived(this.getName(), data);
			}
		}
	}

	/**
	 * Notify onDataSent listeners
	 *
	 * @param data
	 *            new data sent
	 */
	protected void notifyOnDataSent(DataDictionary data)
	{
		if (data != null)
		{
			for (DataStreamListener listener : this.notifier.getSubscribers())
			{
				listener.onDataSent(this.getName(), data);
			}
		}
	}

	/**
	 * Notify onStatusChanged listeners
	 *
	 * @param newStatus
	 *            new status
	 */
	protected void notifyOnStatusChanged(DataStreamStatus newStatus)
	{
		if (newStatus != null)
		{
			for (DataStreamListener listener : this.notifier.getSubscribers())
			{
				listener.onStatusChanged(this.getName(), newStatus);
			}
		}
	}

	/**
	 * Notify onErrorDetected listeners
	 *
	 * @param errorCode
	 *            the error code
	 * @param errorMessage
	 *            the error message
	 * @param data
	 *            the data associated with the error
	 */
	protected void notifyOnErrorDetected(int errorCode, String errorMessage, DataDictionary data)
	{
		for (DataStreamListener listener : this.notifier.getSubscribers())
		{
			listener.onErrorDetected(this.getName(), errorCode, errorMessage, data);
		}
	}

	/**
	 * Set the channel status
	 *
	 * @param status
	 *            channel status
	 */
	protected void setStatus(DataStreamStatus status)
	{
		this.setStatus(status, true);
	}

	/**
	 * Set the channel status. If 'notify' argument is false, no channel listener will be notified about a new status,
	 * otherwise, the will
	 *
	 * @param status
	 *            channel status
	 * @param notify
	 *            if 'true', the channel's listener will be notified, else it will not
	 */
	protected void setStatus(DataStreamStatus status, boolean notify)
	{
		if (status != this.status)
		{
			this.status = status;

			if (true == notify)
			{
				this.notifyOnStatusChanged(this.status);
			}
		}
	}

	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///
	/// PRIVATE METHODS
	///
	/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void init(DataStreamConfiguration configuration) throws DataStreamException
	{
		this.notifier = new NotifierBase<DataStreamListener>();
		this.status = DataStreamStatus.UNKNOWN;
		this.configuration = configuration;
		this.logger = LogManager.getLogger(this.getClass());
		this.setup(configuration);
	}
}
