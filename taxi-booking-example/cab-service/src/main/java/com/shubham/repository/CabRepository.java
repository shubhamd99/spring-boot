package com.shubham.repository;

import com.shubham.entity.Cab;
import com.shubham.enums.CommonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CabRepository extends JpaRepository<Cab, UUID> {
    boolean existsByRegistrationNumberAndCabStatus(String registrationNumber, CommonStatus cabStatus);
}
