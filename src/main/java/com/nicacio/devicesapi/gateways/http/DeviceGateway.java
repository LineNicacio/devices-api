package com.nicacio.devicesapi.gateways.http;

import com.nicacio.devicesapi.domains.Device;

import java.util.Optional;

public interface DeviceGateway {
    Device save(Device device);

    Device update(Device device);

    Optional<Device> findById(String id);
}
