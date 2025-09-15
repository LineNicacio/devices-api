package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.DeviceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CreateDeviceUseCaseImplTest {
    private DeviceGateway deviceGateway;
    private CreateDeviceUseCaseImpl createDeviceUseCase;

    @BeforeEach
    void setUp() {
        deviceGateway = mock(DeviceGateway.class);
        createDeviceUseCase = new CreateDeviceUseCaseImpl(deviceGateway);
    }

    @Test
    void shouldCallDeviceGatewayAndReturnCreatedDevice() {
        // given
        Device inputDevice = buildDevice(null);
        Device savedDevice = buildDevice("device-123");

        when(deviceGateway.save(inputDevice)).thenReturn(savedDevice);

        // when
        Device result = createDeviceUseCase.execute(inputDevice);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("device-123");
        assertThat(result.getName()).isEqualTo("Test Device");
        assertThat(result.getBrand()).isEqualTo("TestBrand");
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
        assertThat(result.getCreatedAt()).isEqualTo(Instant.parse("2025-09-14T23:00:00Z"));

        verify(deviceGateway, times(1)).save(inputDevice);
    }

    private Device buildDevice(String id) {
        return new Device(
                id,
                "Test Device",
                "TestBrand",
                DeviceStateEnum.AVAILABLE,
                Instant.parse("2025-09-14T23:00:00Z")
        );
    }
}