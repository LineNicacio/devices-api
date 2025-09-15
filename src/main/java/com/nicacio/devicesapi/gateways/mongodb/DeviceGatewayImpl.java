package com.nicacio.devicesapi.gateways.mongodb;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.gateways.mongodb.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceGatewayImpl implements DeviceGateway {

    private final DeviceRepository repository;

    @Override
    public void delete(final String id) {
        repository.deleteById(id);
    }

    @Override
    public Device save(final Device device) {
        return repository.save(device.withCreatedAt(Instant.now()));
    }

    @Override
    public Device update(final Device device) {
        return repository.save(device);
    }

    @Override
    public Optional<Device> findById(final String id) {
        return repository.findById(id);
    }

    @Override
    public Page<Device> findByBrand(final String brand, final Pageable pageable) {
        return repository.findByBrandIgnoreCase(brand, pageable);
    }

    @Override
    public Page<Device> findByState(final DeviceStateEnum state, final Pageable pageable) {
        return repository.findByState(state, pageable);
    }

    @Override
    public Page<Device> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }
}
