// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import java.util.List;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.MinMaxChecker;

/**
 * DataDictionary key descriptor Number min max
 * 
 * @param <T>
 */
public class KeyDescriptorListMinMaxSize<T> extends KeyDescriptorList<T>
{
	private MinMaxChecker minMaxChecker;

	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param mandatory
	 * @param itemClass
	 */
	public KeyDescriptorListMinMaxSize(String name, boolean mandatory, Class<T> itemClass)
	{
		this(name, mandatory, itemClass, null, null);
	}

	/**
	 * Constructor setting all attributes
	 * 
	 * @param name
	 * @param mandatory
	 * @param itemClass
	 * @param min
	 * @param max
	 */
	public KeyDescriptorListMinMaxSize(String name, boolean mandatory, Class<T> itemClass, Integer min, Integer max)
	{
		super(name, mandatory, itemClass);
		this.minMaxChecker = new MinMaxChecker(min, max);
	}

	@Override
	public List<T> convertValue(Object value) throws DataDictionaryException
	{
		List<T> convertedValue = super.convertValue(value);

		this.check(convertedValue);

		return convertedValue;
	}

	/**
	 * Get min
	 * 
	 * @return min
	 */
	public Integer getMin()
	{
		return this.minMaxChecker.getMin().intValue();
	}

	/**
	 * Set min
	 * 
	 * @param min
	 * @throws IllegalArgumentException
	 *             if min is greater than max
	 */
	public void setMin(Integer min)
	{
		this.minMaxChecker.setMin(min);
	}

	/**
	 * Get max
	 * 
	 * @return max
	 */
	public Integer getMax()
	{
		return this.minMaxChecker.getMax().intValue();
	}

	/**
	 * Set max
	 * 
	 * @param max
	 * @throws IllegalArgumentException
	 *             if max is smaller than min
	 */
	public void setMax(Integer max)
	{
		this.minMaxChecker.setMax(max);
	}

	private void check(List<T> list) throws DataDictionaryException
	{
		if (list != null)
		{
			if (!this.minMaxChecker.check(list.size()))
			{
				throw new DataDictionaryException("Key " + this.getName() + ": value size (" + list.size() + ") out of bound");
			}
		}
	}
}
