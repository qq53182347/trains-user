package com.tw.config;

import com.tw.controller.dto.ErrorDTO;
import com.tw.exception.DataNotFoundException;
import com.tw.exception.DataValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuguanghui
 * @version V1.0
 * @Description: Controller统一异常处理
 * @Date: 2021-08-31
 */
@ControllerAdvice
public class AllControllerAdvice {
    private static Logger logger = LoggerFactory.getLogger(AllControllerAdvice.class);



    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> handleDataNotFoundException(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build());
    }


    @ExceptionHandler(DataValidateException.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> handleDataValidateException(DataValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build());
    }

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDTO> errorHandler(Exception ex) {
        ex.printStackTrace();
        logger.error("接口出现严重异常：{}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorDTO.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());
    }

}