package com.nicacio.devicesapi.gateways.mongodb.repository;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
    Page<Device> findByBrandIgnoreCase(final String brand, final Pageable pageable);

    Page<Device> findByState(final DeviceStateEnum state, final Pageable pageable);
}
