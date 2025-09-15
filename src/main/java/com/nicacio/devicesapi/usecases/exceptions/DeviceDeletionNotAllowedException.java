package com.nicacio.devicesapi.usecases.exceptions;

public class DeviceDeletionNotAllowedException extends RuntimeException {
    public DeviceDeletionNotAllowedException(String message) {
        super(message);
    }
}
