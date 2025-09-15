package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FindDeviceUseCaseImplTest {

    private DeviceGateway deviceGateway;
    private FindDeviceUseCaseImpl findDeviceUseCase;

    @BeforeEach
    void setUp() {
        deviceGateway = mock(DeviceGateway.class);
        findDeviceUseCase = new FindDeviceUseCaseImpl(deviceGateway);
    }

    @Test
    void shouldReturnDeviceByIdWhenDeviceExists() {
        // given
        String deviceId = "device-123";
        Device expectedDevice = buildDevice(deviceId, DeviceStateEnum.AVAILABLE);

        when(deviceGateway.findById(deviceId)).thenReturn(Optional.of(expectedDevice));

        // when
        Device result = findDeviceUseCase.byId(deviceId);

        // then
        assertThat(result).isEqualTo(expectedDevice);
        verify(deviceGateway, times(1)).findById(deviceId);
    }

    @Test
    void shouldThrowDeviceNotFoundExceptionWhenDeviceDoesNotExist() {
        // given
        String deviceId = "non-existent-id";

        when(deviceGateway.findById(deviceId)).thenReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> findDeviceUseCase.byId(deviceId))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id " + deviceId);

        verify(deviceGateway, times(1)).findById(deviceId);
    }

    @Test
    void shouldReturnAllDevicesPaged() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Device> devices = List.of(
                buildDevice("device-1", DeviceStateEnum.AVAILABLE),
                buildDevice("device-2", DeviceStateEnum.IN_USE)
        );
        Page<Device> devicePage = new PageImpl<>(devices, pageable, devices.size());

        when(deviceGateway.findAll(pageable)).thenReturn(devicePage);

        // when
        Page<Device> result = findDeviceUseCase.all(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactlyElementsOf(devices);
        verify(deviceGateway, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnDevicesByBrandPaged() {
        // given
        String brand = "TestBrand";
        Pageable pageable = PageRequest.of(0, 10);
        List<Device> devices = List.of(buildDevice("device-1", DeviceStateEnum.AVAILABLE));
        Page<Device> devicePage = new PageImpl<>(devices, pageable, devices.size());

        when(deviceGateway.findByBrand(brand, pageable)).thenReturn(devicePage);

        // when
        Page<Device> result = findDeviceUseCase.byBrand(brand, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getBrand()).isEqualTo(brand);
        verify(deviceGateway, times(1)).findByBrand(brand, pageable);
    }

    @Test
    void shouldReturnDevicesByStatePaged() {
        // given
        DeviceStateEnum state = DeviceStateEnum.IN_USE;
        Pageable pageable = PageRequest.of(0, 10);
        List<Device> devices = List.of(buildDevice("device-1", state));
        Page<Device> devicePage = new PageImpl<>(devices, pageable, devices.size());

        when(deviceGateway.findByState(state, pageable)).thenReturn(devicePage);

        // when
        Page<Device> result = findDeviceUseCase.byState(state, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getState()).isEqualTo(state);
        verify(deviceGateway, times(1)).findByState(state, pageable);
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
