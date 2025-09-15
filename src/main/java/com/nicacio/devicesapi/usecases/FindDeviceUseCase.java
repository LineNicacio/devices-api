package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.Device;

public interface FindDeviceUseCase {
    Device byId(String id);
}
