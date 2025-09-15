package com.nicacio.devicesapi.domains;

import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private String id;
    private String name;
    private String brand;
    private DeviceStateEnum state;
    private Instant createdAt;
}
