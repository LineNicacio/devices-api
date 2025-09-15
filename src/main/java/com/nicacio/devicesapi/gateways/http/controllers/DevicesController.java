package com.nicacio.devicesapi.gateways.http.controllers;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.mappers.DeviceMapper;
import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.gateways.http.resources.DeviceResponse;
import com.nicacio.devicesapi.gateways.http.resources.DeviceUpdateRequest;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import com.nicacio.devicesapi.usecases.FindDeviceUseCase;
import com.nicacio.devicesapi.usecases.UpdateDeviceUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/devices")
@RestController
public class DevicesController {

    private final DeviceMapper deviceMapper;
    private final FindDeviceUseCase findDeviceUseCase;
    private final CreateDeviceUseCase createDeviceUseCase;
    private final UpdateDeviceUseCase updateDeviceUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> findById(@PathVariable String id) {
        final Device device = findDeviceUseCase.byId(id);
        return ResponseEntity.ok(deviceMapper.toResponse(device));
    }

    @GetMapping
    public ResponseEntity<Page<DeviceResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Device> pageResult = findDeviceUseCase.all(pageable);
        Page<DeviceResponse> responsePage = pageResult.map(deviceMapper::toResponse);


        return ResponseEntity.ok(responsePage);
    }

    @PostMapping()
    public ResponseEntity<DeviceResponse> create(@RequestBody @Valid final DeviceRequest deviceRequest) {

        final Device createdDevice = createDeviceUseCase.execute(deviceMapper.toDomain(deviceRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceMapper.toResponse(createdDevice));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponse> update(
            @PathVariable String id,
            @RequestBody @Valid DeviceUpdateRequest deviceUpdateRequest) {

        final Device updatedDevice = updateDeviceUseCase.execute(deviceMapper.toDomain(deviceUpdateRequest).withId(id));

        return ResponseEntity.ok(deviceMapper.toResponse(updatedDevice));
    }

}
