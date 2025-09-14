package com.nicacio.devicesapi.gateways.http;

import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/devices")
@RestController
public class DevicesController {

    private final CreateDeviceUseCase createDeviceUseCase;

    @PostMapping(value = "/")
    public ResponseEntity createDevice(@RequestBody DeviceRequest deviceRequest) {
        createDeviceUseCase.execute();

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }



}
