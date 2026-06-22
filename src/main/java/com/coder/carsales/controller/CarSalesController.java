package com.coder.carsales.controller;

import com.coder.carsales.commons.response.ApiResponse;
import com.coder.carsales.dto.MonthlyCountDto;
import com.coder.carsales.dto.UploadSalesResponse;
import com.coder.carsales.dto.YearlyCountDto;
import com.coder.carsales.service.CarSalesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/car-sales")


public class CarSalesController {

    private final CarSalesService salesService ;

    public CarSalesController(CarSalesService salesService){
        this.salesService = salesService;
    }
    @PostMapping("/upload-csv")
    public ResponseEntity<ApiResponse<UploadSalesResponse>> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {

            UploadSalesResponse response =
                    new UploadSalesResponse(0, 0, 0);

            ApiResponse<UploadSalesResponse> apiResponse =
                    new ApiResponse<>(
                            false,
                            "File is Empty",
                            response,
                            HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        UploadSalesResponse response = salesService.uploadCsv(file);

        ApiResponse<UploadSalesResponse> apiResponse = getApiResponse(response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);



    }
    private static ApiResponse<UploadSalesResponse> getApiResponse(UploadSalesResponse response) {

        String message ;
        boolean success ;

        if(response.getFailedCount() == 0){
            message = "All records uploaded successfully";
            success = true;
        }
        else if (response.getSuccessCount() == 0){
            message = "All records fails to upload";
            success = false;
        }else{
            message = "Uploaded with some errors"+response.getFailedCount()+ "row failed";
            success = false;
        }
        return new ApiResponse<UploadSalesResponse>(success,message,response,HttpStatus.OK.value());
    }
    @GetMapping("/yearly-count")
    public ResponseEntity<?> yearlyCount(){
        List<YearlyCountDto> carsCount = salesService.getYearlyCarsCount() ;
        ApiResponse<List<YearlyCountDto>> response = new ApiResponse<List<YearlyCountDto>>(
                true,
                "Yearly Count",
                carsCount,HttpStatus.OK.value()
        );
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-count")
    public ResponseEntity<ApiResponse<List<MonthlyCountDto>>> monthlyCount(
            @RequestParam int year) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Monthly Data Read Successfully",
                        salesService.getMonthlyCountByYear(year),
                        HttpStatus.OK.value()
                )
        );
    }
}
