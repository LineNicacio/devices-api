package com.nicacio.devicesapi.usecases.exceptions;

public class DeviceUpdateNotAllowedException extends RuntimeException {
    public DeviceUpdateNotAllowedException(String message) {
        super(message);
    }
}
