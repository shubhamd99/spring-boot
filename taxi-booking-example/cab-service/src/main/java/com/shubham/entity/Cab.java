package com.shubham.entity;

import com.shubham.cab.enums.CabTypes;
import com.shubham.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "cab")
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cab_id")
    private Integer id;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "cab_type")
    private CabTypes cabType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cab_status")
    private CommonStatus cabStatus;

}
