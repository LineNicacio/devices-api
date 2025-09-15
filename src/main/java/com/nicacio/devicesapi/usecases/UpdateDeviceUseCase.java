package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.Device;

public interface UpdateDeviceUseCase {
    Device execute(Device device);
}
