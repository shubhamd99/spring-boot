package com.shubham.cab.events;

import com.shubham.cab.enums.CabTypes;
import lombok.*;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CabEvent {
    private UUID driverId;
    private CabTypes cabType;
    private String registrationNumber;
}
