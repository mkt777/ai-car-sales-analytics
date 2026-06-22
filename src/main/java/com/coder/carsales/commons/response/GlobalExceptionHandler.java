package com.coder.carsales.commons.response;

import com.coder.carsales.dto.UploadSalesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<UploadSalesResponse>> handleAllExceptions(Exception ex) {
        UploadSalesResponse response = new UploadSalesResponse(0,0,0);
        ApiResponse apiResponse = new ApiResponse<UploadSalesResponse>(
                false, ex.getMessage() , response, HttpStatus.BAD_REQUEST.value()
        ) ;

    return new ResponseEntity<ApiResponse<UploadSalesResponse>>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
