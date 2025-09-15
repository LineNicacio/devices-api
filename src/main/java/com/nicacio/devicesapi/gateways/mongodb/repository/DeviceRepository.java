package com.nicacio.devicesapi.gateways.mongodb.repository;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
    Page<Device> findByBrandIgnoreCase(final String brand, final Pageable pageable);
}
