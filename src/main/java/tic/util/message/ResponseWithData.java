package tic.util.message;

import java.time.LocalDateTime;

public abstract class ResponseWithData<T> extends Response {

    /**
     * Constructor setting parameters to specific values
     *
     * @param name
     * @param dateTime
     * @param errorCode
     * @param errorMessage
     * @param data
     */
    public ResponseWithData(
        String name, LocalDateTime dateTime, Number errorCode, String errorMessage, T data) {
        super(name, dateTime, errorCode, errorMessage);
        this.setData(data);
    }
    
    /**
     * Get data
     *
     * @return the data
     */
    public abstract T getData();
    
    /**
     * Set data
     *
     * @param data
     */
    public abstract void setData(T data);
}
