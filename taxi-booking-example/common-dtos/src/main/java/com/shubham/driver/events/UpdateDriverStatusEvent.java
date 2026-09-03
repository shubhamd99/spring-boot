package com.shubham.driver.events;

import com.shubham.enums.CommonStatus;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpdateDriverStatusEvent {
    private UUID driverId;
    private CommonStatus driverStatus;
}
