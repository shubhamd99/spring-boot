package com.shubham.cab.dto;

import com.shubham.cab.enums.CabTypes;
import lombok.Data;

@Data
public class CabDTO {
    private CabTypes cabType;
    private String registrationNumber;
}
