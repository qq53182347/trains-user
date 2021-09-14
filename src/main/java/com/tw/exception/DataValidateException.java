package com.tw.exception;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 数据验证异常
 * @Date: 2021-08-31
 */
public class DataValidateException extends RuntimeException {
    public DataValidateException(String msg) {
        super(msg);
    }
}
