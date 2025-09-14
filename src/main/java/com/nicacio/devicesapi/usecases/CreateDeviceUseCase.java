package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.enums.Device;

public interface CreateDeviceUseCase {
    Device execute(Device device);
}
