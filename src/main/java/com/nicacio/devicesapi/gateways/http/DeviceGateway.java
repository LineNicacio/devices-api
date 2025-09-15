package com.nicacio.devicesapi.gateways.http;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceGateway {
    Device save(final Device device);

    Device update(final Device device);

    Optional<Device> findById(final String id);

    Page<Device> findByBrand(final String brand, Pageable pageable);

    Page<Device> findAll(final Pageable pageable);
}
