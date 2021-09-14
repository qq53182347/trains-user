package com.tw.controller.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ErrorDTO implements Serializable {

    private static final long ssserialVersionUID = -1L;

    private int code ;
    private String message;

}
