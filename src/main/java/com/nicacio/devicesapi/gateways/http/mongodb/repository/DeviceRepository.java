package com.nicacio.devicesapi.gateways.http.mongo.repository;

import com.nicacio.devicesapi.domains.enums.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
}
