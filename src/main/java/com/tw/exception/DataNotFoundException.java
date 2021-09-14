package com.tw.exception;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: 数据未查询到异常
 * @Date: 2021-08-31
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String msg) {
        super(msg);
    }
}
