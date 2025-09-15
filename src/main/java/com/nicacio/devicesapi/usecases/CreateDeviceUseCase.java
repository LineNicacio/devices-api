package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.Device;

public interface CreateDeviceUseCase {
    Device execute(final Device device);
}
