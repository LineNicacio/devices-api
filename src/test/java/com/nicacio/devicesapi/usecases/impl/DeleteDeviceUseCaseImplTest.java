package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.DeviceGateway;
import com.nicacio.devicesapi.usecases.exceptions.DeviceDeletionNotAllowedException;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteDeviceUseCaseImplTest {

    private DeviceGateway deviceGateway;
    private DeleteDeviceUseCaseImpl deleteDeviceUseCase;

    @BeforeEach
    void setUp() {
        deviceGateway = mock(DeviceGateway.class);
        deleteDeviceUseCase = new DeleteDeviceUseCaseImpl(deviceGateway);
    }

    @Test
    void shouldDeleteDeviceWhenDeviceExistsAndIsNotInUse() {
        // given
        String deviceId = "device-123";
        Device device = buildDevice(deviceId, DeviceStateEnum.AVAILABLE);

        when(deviceGateway.findById(deviceId)).thenReturn(Optional.of(device));
        doNothing().when(deviceGateway).delete(deviceId);

        // when
        deleteDeviceUseCase.execute(deviceId);

        // then
        verify(deviceGateway, times(1)).findById(deviceId);
        verify(deviceGateway, times(1)).delete(deviceId);
    }

    @Test
    void shouldThrowDeviceNotFoundExceptionWhenDeviceDoesNotExist() {
        // given
        String deviceId = "non-existent-id";

        when(deviceGateway.findById(deviceId)).thenReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> deleteDeviceUseCase.execute(deviceId))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id " + deviceId);

        verify(deviceGateway, times(1)).findById(deviceId);
        verify(deviceGateway, never()).delete(anyString());
    }

    @Test
    void shouldThrowDeviceDeletionNotAllowedExceptionWhenDeviceIsInUse() {
        // given
        String deviceId = "device-in-use";
        Device device = buildDevice(deviceId, DeviceStateEnum.IN_USE);

        when(deviceGateway.findById(deviceId)).thenReturn(Optional.of(device));

        // when - then
        assertThatThrownBy(() -> deleteDeviceUseCase.execute(deviceId))
                .isInstanceOf(DeviceDeletionNotAllowedException.class)
                .hasMessageContaining("In use devices cannot be deleted");

        verify(deviceGateway, times(1)).findById(deviceId);
        verify(deviceGateway, never()).delete(anyString());
    }

    private Device buildDevice(final String id, final DeviceStateEnum state) {
        return new Device(
                id,
                "Test Device",
                "TestBrand",
                state,
                Instant.parse("2025-09-14T23:00:00Z")
        );
    }
}