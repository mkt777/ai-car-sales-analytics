package com.coder.carsales.service;

import com.coder.carsales.dto.MonthlyCountDto;
import com.coder.carsales.dto.UploadSalesResponse;
import com.coder.carsales.dto.YearlyCountDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarSalesService {

    UploadSalesResponse uploadCsv(MultipartFile file) throws IOException;

    List<YearlyCountDto>  getYearlyCarsCount();

    List<MonthlyCountDto> getMonthlyCountByYear(int year);

}
