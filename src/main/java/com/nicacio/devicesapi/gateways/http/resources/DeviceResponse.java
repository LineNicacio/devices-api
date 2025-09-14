package com.nicacio.devicesapi.gateways.http.resources;

import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private DeviceStateEnum state;
}
