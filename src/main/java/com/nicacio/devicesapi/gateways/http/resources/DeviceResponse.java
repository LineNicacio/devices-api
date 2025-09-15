package com.nicacio.devicesapi.gateways.http.resources;

import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.time.OffsetDateTime;

@Getter
@With
@AllArgsConstructor
public class DeviceResponse {
    private final String id;
    private final String name;
    private final String brand;
    private final DeviceStateEnum state;
    private final OffsetDateTime createdAt;
}

