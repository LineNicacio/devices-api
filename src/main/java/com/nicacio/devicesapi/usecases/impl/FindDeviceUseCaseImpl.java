package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.usecases.FindDeviceUseCase;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FindDeviceUseCaseImpl implements FindDeviceUseCase {

    private final DeviceGateway deviceGateway;

    @Override
    public Device byId(final String id) {
        return deviceGateway.findById(id).orElseThrow(() ->
                new DeviceNotFoundException("Device not found with id " + id));
    }

    @Override
    public Page<Device> all(final Pageable pageable) {
        return deviceGateway.findAll(pageable);
    }

    @Override
    public Page<Device> byBrand(final String brand, final Pageable pageable) {
        return deviceGateway.findByBrand(brand, pageable);
    }
}
