// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.types.datadictionary;

import enedis.lab.types.DataDictionaryException;
import enedis.lab.util.MinMaxChecker;

/**
 * DataDictionary key descriptor Number min max
 */
public class KeyDescriptorNumberMinMax extends KeyDescriptorNumber
{
	private MinMaxChecker minMaxChecker;

	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param mandatory
	 */
	public KeyDescriptorNumberMinMax(String name, boolean mandatory)
	{
		this(name, mandatory, null, null);
	}

	/**
	 * Constructor setting all attributes
	 * 
	 * @param name
	 * @param mandatory
	 * @param min
	 * @param max
	 */
	public KeyDescriptorNumberMinMax(String name, boolean mandatory, Number min, Number max)
	{
		super(name, mandatory);
		this.minMaxChecker = new MinMaxChecker(min, max);
	}

	@Override
	public Number convertValue(Object value) throws DataDictionaryException
	{
		Number convertedValue = super.convertValue(value);

		this.check(convertedValue);

		return convertedValue;
	}

	/**
	 * Get min
	 * 
	 * @return min
	 */
	public Number getMin()
	{
		return this.minMaxChecker.getMin();
	}

	/**
	 * Set min
	 * 
	 * @param min
	 * @throws IllegalArgumentException
	 *             if min is greater than max
	 */
	public void setMin(Number min)
	{
		this.minMaxChecker.setMin(min);
	}

	/**
	 * Get max
	 * 
	 * @return max
	 */
	public Number getMax()
	{
		return this.minMaxChecker.getMax();
	}

	/**
	 * Set max
	 * 
	 * @param max
	 * @throws IllegalArgumentException
	 *             if max is smaller than min
	 */
	public void setMax(Number max)
	{
		this.minMaxChecker.setMax(max);
	}

	private void check(Number convertedValue) throws DataDictionaryException
	{
		if (convertedValue != null)
		{
			if (!this.minMaxChecker.check(convertedValue))
			{
				throw new DataDictionaryException("Key " + this.getName() + ": value (" + convertedValue + ") out of bound");
			}
		}
	}
}
