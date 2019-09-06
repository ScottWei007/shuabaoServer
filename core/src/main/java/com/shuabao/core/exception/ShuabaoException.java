package com.shuabao.core.exception;

import com.shuabao.core.base.ReturnCode;

/**
 * Created by Scott Wei on 4/7/2018.
 */
public class ShuabaoException extends Exception {
    private ReturnCode code = null;

    public ShuabaoException(ReturnCode code) {
        super();
        this.code = code;
     }

     public int getIndex() {
        return code.getIndex();
     }

     public String getMsg(int lang) {
        return code.getMsg(lang);
     }

    public ReturnCode getCode() {
        return code;
    }

    public ShuabaoException(String message) {
        super(message);
    }

    public ShuabaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShuabaoException(Throwable cause) {
        super(cause);
    }

    protected ShuabaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
