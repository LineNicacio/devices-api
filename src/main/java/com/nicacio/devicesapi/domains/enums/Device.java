package com.nicacio.devicesapi.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.time.Instant;

@Data
@With
@AllArgsConstructor
public class Device {
    private String id;
    private String name;
    private String brand;
    private DeviceStateEnum state;
    private Instant createdAt;
}
