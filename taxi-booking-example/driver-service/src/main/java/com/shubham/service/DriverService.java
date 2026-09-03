package com.shubham.service;

import com.shubham.cab.events.CabEvent;
import com.shubham.driver.dto.DriverDTO;
import com.shubham.entity.Driver;
import com.shubham.enums.CommonStatus;
import com.shubham.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private KafkaTemplate<String, CabEvent> kafkaTemplate;

    @Autowired
    public DriverService(DriverRepository driverRepository, KafkaTemplate<String, CabEvent> kafkaTemplate) {
        this.driverRepository = driverRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(UUID id) {
        return driverRepository.findById(id);
    }

    public String saveDriver(DriverDTO driverDTO) {
        Driver driver = driverRepository.save(
                Driver.builder()
                        .driverName(driverDTO.getDriverName())
                        .driverEmail(driverDTO.getDriverEmail())
                        .driverLocation(driverDTO.getDriverLocation())
                        .driverStatus(CommonStatus.PENDING)
                        .build()
        );
        CabEvent cabEvent = CabEvent.builder()
                .driverId(driver.getDriverId())
                .cabType(driverDTO.getCabDTO().getCabType())
                .registrationNumber(driverDTO.getCabDTO().getRegistrationNumber())
                .build();
        kafkaTemplate.send("add-cab-event", cabEvent);
        return "Driver details: "+driver.getDriverId()+" processed.";
    }

    public void deleteDriver(UUID id) {
        driverRepository.deleteById(id);
    }
    @Transactional
    public int updateDriveStatus(UUID driverId, CommonStatus status){
        return driverRepository.updateDriverStatus(driverId, status);
    }
}
