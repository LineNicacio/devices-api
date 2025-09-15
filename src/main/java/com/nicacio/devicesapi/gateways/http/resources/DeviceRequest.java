package com.nicacio.devicesapi.gateways.http.resources;

import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    @NotBlank(message = "The 'name' field must not be blank")
    private String name;

    @NotBlank(message = "The 'brand' field must not be blank")
    private String brand;

    @NotNull(message = "The 'state' field is required")
    private DeviceStateEnum state;
}
