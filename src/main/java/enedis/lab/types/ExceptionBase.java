// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types;

/**
 * Exception base
 */
@SuppressWarnings("serial")
public class ExceptionBase extends Exception
{
	protected int		code;
	protected String	info;

	/**
	 * Constructor
	 * 
	 * @param code
	 * @param info
	 */
	public ExceptionBase(int code, String info)
	{
		this.setErrorCode(code);
		this.setErrorInfo(info);
	}

	@Override
	public String getMessage()
	{
		return this.info + " (" + this.code + ")";
	}

	/**
	 * Get error code
	 * 
	 * @return error code
	 */
	public int getErrorCode()
	{
		return this.code;
	}

	/**
	 * Set error code
	 * 
	 * @param errorCode
	 */
	public void setErrorCode(int errorCode)
	{
		this.code = errorCode;
	}

	/**
	 * Get error info
	 * 
	 * @return error info
	 */
	public String getErrorInfo()
	{
		return this.info;
	}

	/**
	 * Set error info
	 * 
	 * @param errorInfo
	 */
	public void setErrorInfo(String errorInfo)
	{
		this.info = errorInfo;
	}

}
