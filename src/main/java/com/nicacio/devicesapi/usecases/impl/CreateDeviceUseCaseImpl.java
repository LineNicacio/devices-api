package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreateDeviceUseCaseImpl implements CreateDeviceUseCase {

    private final DeviceGateway deviceGateway;

    @Override
    public Device execute(final Device device) {
        return deviceGateway.save(device);
    }
}
