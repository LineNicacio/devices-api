package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.DeviceGateway;
import com.nicacio.devicesapi.usecases.UpdateDeviceUseCase;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import com.nicacio.devicesapi.usecases.exceptions.DeviceUpdateNotAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDeviceUseCaseImpl implements UpdateDeviceUseCase {

    private final DeviceGateway deviceGateway;

    @Override
    @CacheEvict(value = "devices", key = "#device.id")
    public Device execute(final Device device) {
        Device databaseDevice = deviceGateway.findById(device.getId())
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id " + device.getId()));

        if (isDeviceStateInUse(databaseDevice)) {
            if (isNameOrBrandBeingUpdated(device, databaseDevice)) {
                throw new DeviceUpdateNotAllowedException("Cannot update name or brand when device state is IN_USE");
            }
        }

        if (device.getName() != null) {
            databaseDevice.setName(device.getName());
        }
        if (device.getBrand() != null) {
            databaseDevice.setBrand(device.getBrand());
        }
        if (device.getState() != null) {
            databaseDevice.setState(device.getState());
        }

        return deviceGateway.update(databaseDevice);
    }

    private boolean isDeviceStateInUse(final Device device) {
        return DeviceStateEnum.IN_USE.equals(device.getState());
    }

    private boolean isNameBeingUpdated(final Device newDevice, final Device oldDevice) {
        return newDevice.getName() != null
                && !newDevice.getName().equals(oldDevice.getName());
    }

    private boolean isBrandBeingUpdated(final Device newDevice, final Device oldDevice) {
        return newDevice.getBrand() != null
                && !newDevice.getBrand().equals(oldDevice.getBrand());
    }

    private boolean isNameOrBrandBeingUpdated(final Device newDevice, final Device oldDevice) {
        return isNameBeingUpdated(newDevice, oldDevice) || isBrandBeingUpdated(newDevice, oldDevice);
    }
}