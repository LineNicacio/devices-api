package com.nicacio.devicesapi.gateways.http;

import com.nicacio.devicesapi.domains.Device;

public interface DeviceGateway {
    Device save(Device device);
}
