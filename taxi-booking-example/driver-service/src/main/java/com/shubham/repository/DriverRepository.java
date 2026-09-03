package com.shubham.repository;

import com.shubham.entity.Driver;
import com.shubham.enums.CommonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
    @Modifying
    @Query("UPDATE Driver d SET d.driverStatus = :status WHERE d.driverId = :driverId")
    int updateDriverStatus(@Param("driverId") UUID driverId, @Param("status") CommonStatus status);
}