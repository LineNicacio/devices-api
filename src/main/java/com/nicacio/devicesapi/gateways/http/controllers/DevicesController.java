package com.nicacio.devicesapi.gateways.http.controllers;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.mappers.DeviceMapper;
import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.gateways.http.resources.DeviceResponse;
import com.nicacio.devicesapi.gateways.http.resources.DeviceUpdateRequest;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import com.nicacio.devicesapi.usecases.UpdateDeviceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/devices")
@RestController
public class DevicesController {

    private final DeviceMapper deviceMapper;
    private final CreateDeviceUseCase createDeviceUseCase;
    private final UpdateDeviceUseCase updateDeviceUseCase;

    @PostMapping()
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody @Valid final DeviceRequest deviceRequest) {

        final Device createdDevice = createDeviceUseCase.execute(deviceMapper.toDomain(deviceRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceMapper.toResponse(createdDevice));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable String id,
            @RequestBody @Valid DeviceUpdateRequest deviceUpdateRequest) {

        final Device updatedDevice = updateDeviceUseCase.execute(deviceMapper.toDomain(deviceUpdateRequest).withId(id));

        return ResponseEntity.ok(deviceMapper.toResponse(updatedDevice));
    }

}
