package com.shubham.driver.dto;

import com.shubham.cab.dto.CabDTO;
import lombok.Data;

@Data
public class DriverDTO {
    private String driverName;
    private String driverEmail;
    private String driverLocation;
    private CabDTO cabDTO;
}
