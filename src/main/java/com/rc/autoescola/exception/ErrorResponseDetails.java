package com.rc.autoescola.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDetails {
    private String fields;
    private String message;
    private int status;
    private String developerMessage;
}
