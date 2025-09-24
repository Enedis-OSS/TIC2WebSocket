// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
// 
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package enedis.lab.codec;

/**
 * Exception thrown during codec operations
 * 
 * This exception is used to signal errors occurring
 * during data encoding and decoding processes.
 */
public class CodecException extends Exception {

	private static final long serialVersionUID = 6029680699870915485L;

	private Object data;

	/**
	 * Creates a new CodecException with no detail message
	 */
	public CodecException() {
		super();
	}

	/**
	 * Creates a new CodecException with the specified detail message and cause
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link Throwable#getMessage()} method)
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link Throwable#getCause()} method)
	 */
	public CodecException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new CodecException with the specified detail message
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link Throwable#getMessage()} method)
	 */
	public CodecException(String message) {
		super(message);
	}

	/**
	 * Creates a new CodecException with the specified detail message and data
	 *
	 * @param message the detail message (which is saved for later retrieval
	 *                by the {@link Throwable#getMessage()} method)
	 * @param data    additional data object associated with this exception
	 */
	public CodecException(String message, Object data) {
		super(message);
		this.data = data;
	}

	/**
	 * Creates a new CodecException with the specified cause
	 *
	 * @param cause the cause (which is saved for later retrieval by the
	 *              {@link Throwable#getCause()} method)
	 */
	public CodecException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates and throws a CodecException for invalid value scenarios
	 *
	 * @param info additional information about the invalid value
	 * @throws CodecException always thrown with message "Invalid value : " + info
	 */
	public static void raiseInvalidValue(String info) throws CodecException {
		throw new CodecException("Invalid value : " + info);
	}

	/**
	 * Creates and throws a CodecException for missing value scenarios
	 *
	 * @param value the name or identifier of the missing value
	 * @throws CodecException always thrown with message "Missing value : " + value
	 */
	public static void raiseMissingValue(String value) throws CodecException {
		throw new CodecException("Missing value : " + value);
	}

	/**
	 * Creates and throws a CodecException for data inconsistency scenarios
	 *
	 * @param info additional information about the inconsistency
	 * @throws CodecException always thrown with message "Inconsistency : " + info
	 */
	public static void raiseInconsistency(String info) throws CodecException {
		throw new CodecException("Inconsistency : " + info);
	}

	/**
	 * Returns the additional data object associated with this exception
	 *
	 * @return the data object, or null if no data was provided
	 */
	public Object getData() {
		return this.data;
	}

}
