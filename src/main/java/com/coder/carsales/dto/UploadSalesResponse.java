package com.coder.carsales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadSalesResponse {
    private int totalRecord ;
    private int successCount ;
    private int failedCount ;

}
