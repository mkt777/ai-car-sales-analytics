package com.coder.carsales.repository;

import com.coder.carsales.dto.MonthlyCountDto;
import com.coder.carsales.dto.YearlyCountDto;
import com.coder.carsales.dto.YearlyCountDto;
import com.coder.carsales.entity.CarSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSalesRepository extends JpaRepository<CarSales,Long> {

    boolean existsByCarNumber(String carNumber);

    @Query("""
        Select new com.coder.carsales.dto.YearlyCountDto(c.year,count(c))
        from CarSales c
        Group by c.year
        Order by c.year
""")
    List<YearlyCountDto> getYearlyCount();

    @Query("""
    SELECT new com.coder.carsales.dto.MonthlyCountDto(
        MONTH(c.dateOfPurchase),COUNT(c)
    )
    FROM CarSales c
    WHERE YEAR(c.dateOfPurchase) = :year
    GROUP BY MONTH(c.dateOfPurchase)
    ORDER BY MONTH(c.dateOfPurchase)
""")


    List<MonthlyCountDto> getMonthlyCount(@Param("year") int year);
}
