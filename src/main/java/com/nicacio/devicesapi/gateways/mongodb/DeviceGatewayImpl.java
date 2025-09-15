package com.nicacio.devicesapi.gateways.mongodb;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.gateways.mongodb.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeviceGatewayImpl implements DeviceGateway {

    private final DeviceRepository repository;

    @Override
    public Device save(final Device device) {
        return repository.save(device.withCreatedAt(Instant.now()));
    }
}
