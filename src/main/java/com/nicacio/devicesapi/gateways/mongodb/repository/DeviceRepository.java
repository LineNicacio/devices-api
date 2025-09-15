package com.nicacio.devicesapi.gateways.mongodb.repository;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
}
