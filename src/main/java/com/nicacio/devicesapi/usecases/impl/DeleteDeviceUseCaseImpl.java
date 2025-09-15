package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.DeviceGateway;
import com.nicacio.devicesapi.usecases.DeleteDeviceUseCase;
import com.nicacio.devicesapi.usecases.exceptions.DeviceDeletionNotAllowedException;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteDeviceUseCaseImpl implements DeleteDeviceUseCase {

    private final DeviceGateway deviceGateway;

    @Override
    @CacheEvict(value = "devices", key = "#id")
    public void execute(final String id) {

        final Device device = deviceGateway.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id " + id));

        if (isDeviceStateInUse(device)) {
            throw new DeviceDeletionNotAllowedException("In use devices cannot be deleted");
        }

        deviceGateway.delete(id);
    }

    private boolean isDeviceStateInUse(final Device device) {
        return DeviceStateEnum.IN_USE.equals(device.getState());
    }
}
