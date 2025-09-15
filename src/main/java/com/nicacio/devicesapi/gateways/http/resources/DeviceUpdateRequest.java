package com.nicacio.devicesapi.gateways.http.resources;

import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpdateRequest {
    private String name;

    private String brand;

    private DeviceStateEnum state;
}
