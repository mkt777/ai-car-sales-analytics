package com.coder.carsales.service ;
import com.coder.carsales.dto.MonthlyCountDto;
import com.coder.carsales.dto.UploadSalesResponse;
import com.coder.carsales.dto.YearlyCountDto;
import com.coder.carsales.entity.CarSales;
import com.coder.carsales.repository.CarSalesRepository ;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarSalesServiceImpl implements CarSalesService {

    private final CarSalesRepository repository;

    public CarSalesServiceImpl(CarSalesRepository repository) {
        this.repository = repository;
    }


    public UploadSalesResponse uploadCsv(MultipartFile file) {

        List<CarSales> cars = new ArrayList<>();

        int totalRecords = 0;
        int successCount = 0;
        int failCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader() // Header
                    .setSkipHeaderRecord(true) // Skip(Not Treated as Data)
                    .setIgnoreHeaderCase(true) // Case insensitive
                    .setTrim(true) // naam ke aage sapaces hoti hai usse hata rahe hai
                    .build();

            CSVParser csvParser = csvFormat.parse(reader);

            for (CSVRecord record : csvParser) {

                totalRecords++;

                try {

                    String carNumber = record.get("Car Number");

                    if (repository.existsByCarNumber(carNumber)) {
                        failCount++;
                        System.out.println("Duplicate Car Number " + carNumber + " already exists.");
                        continue;
                    }

                    CarSales carSales = new CarSales();

                    carSales.setCarNumber(record.get("Car Number"));
                    carSales.setBrand(record.get("Brand"));
                    carSales.setModel(record.get("Model"));
                    carSales.setColor(record.get("Color"));
                    carSales.setYear(Integer.parseInt(record.get("Year")));
                    carSales.setDateOfPurchase(LocalDate.parse(record.get("Date of Purchase"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    carSales.setTimeOfPurchase(LocalTime.parse(record.get("Time of Purchase")));
                    carSales.setPrice(Long.parseLong(record.get("Price (Rs)")));
                    carSales.setMileage(Double.parseDouble(record.get("Mileage (km/l)")));
                    carSales.setEngine(Integer.parseInt(record.get("Engine (cc)")));
                    carSales.setFuelType(record.get("Fuel Type"));
                    carSales.setPaymentMode(record.get("Payment Mode"));
                    carSales.setState(record.get("State"));
                    carSales.setCity(record.get("City"));
                    carSales.setCustomerName(record.get("Customer Name"));
                    carSales.setContactNumber(record.get("Contact Number"));
                    carSales.setEmail(record.get("Email"));
                    carSales.setWarrantyPeriod(
                            Integer.parseInt(record.get("Warranty Period (years)")));

                    cars.add(carSales);
                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    System.out.println("Error in record " + totalRecords + " : " + e.getMessage());
                }
            }

            if (!cars.isEmpty()) {
                repository.saveAll(cars);
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to parse CSV: " + e.getMessage(), e);
        }

        return new UploadSalesResponse(
                totalRecords,
                successCount,
                failCount
        );
    }

    @Override
    public List<YearlyCountDto> getYearlyCarsCount() {
        return repository.getYearlyCount();
    }



    @Override
    public List<MonthlyCountDto> getMonthlyCountByYear(int year) {

        List<MonthlyCountDto> data = repository.getMonthlyCount(year);

        Map<Integer, Long> map = data.stream()
                .collect(Collectors.toMap(
                        MonthlyCountDto::month,
                        MonthlyCountDto::count
                ));

        List<MonthlyCountDto> result = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            result.add(new MonthlyCountDto(
                    i,
                    map.getOrDefault(i, 0L)
            ));
        }

        return result;
    }
}
