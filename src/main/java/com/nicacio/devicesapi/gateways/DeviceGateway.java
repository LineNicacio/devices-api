package com.nicacio.devicesapi.gateways;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceGateway {
    Device save(final Device device);

    Device update(final Device device);

    Optional<Device> findById(final String id);

    void delete(final String id);

    Page<Device> findByBrand(final String brand, final Pageable pageable);

    Page<Device> findByState(final DeviceStateEnum state, final Pageable pageable);

    Page<Device> findAll(final Pageable pageable);
}
