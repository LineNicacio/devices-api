package com.nicacio.devicesapi.gateways.http;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceGateway {
    Device save(Device device);

    Device update(Device device);

    Optional<Device> findById(String id);

    Page<Device> findAll(Pageable pageable);
}
