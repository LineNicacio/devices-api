package com.nicacio.devicesapi.usecases.impl;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.http.DeviceGateway;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import com.nicacio.devicesapi.usecases.exceptions.DeviceUpdateNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateDeviceUseCaseImplTest {
    private DeviceGateway deviceGateway;
    private UpdateDeviceUseCaseImpl updateDeviceUseCase;

    @BeforeEach
    void setUp() {
        deviceGateway = mock(DeviceGateway.class);
        updateDeviceUseCase = new UpdateDeviceUseCaseImpl(deviceGateway);
    }

    @Test
    void shouldUpdateDeviceSuccessfully() {
        // given
        Device existingDevice = buildDevice("device-123", "Old Name", "OldBrand", DeviceStateEnum.AVAILABLE);
        Device updatedInfo = buildDevice("device-123", "New Name", "New Brand", DeviceStateEnum.AVAILABLE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));
        when(deviceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Device result = updateDeviceUseCase.execute(updatedInfo);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("device-123");
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getBrand()).isEqualTo("New Brand");
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);

        verify(deviceGateway).findById("device-123");
        verify(deviceGateway).update(result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeviceDoesNotExist() {
        // given
        Device updatedInfo = buildDevice("device-999", "New Name", "NewBrand", DeviceStateEnum.AVAILABLE);
        when(deviceGateway.findById("device-999")).thenReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> updateDeviceUseCase.execute(updatedInfo))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id device-999");

        verify(deviceGateway).findById("device-999");
        verify(deviceGateway, never()).update(any());
    }

    @Test
    void shouldThrowUpdateNotAllowedExceptionWhenNameUpdatedAndStateInUse() {
        // given
        Device existingDevice = buildDevice("device-123", "Old Name", "OldBrand", DeviceStateEnum.IN_USE);
        Device updatedInfo = buildDevice("device-123", "New Name", null, DeviceStateEnum.IN_USE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));

        // when - then
        assertThatThrownBy(() -> updateDeviceUseCase.execute(updatedInfo))
                .isInstanceOf(DeviceUpdateNotAllowedException.class)
                .hasMessageContaining("Cannot update name or brand when device state is IN_USE");

        verify(deviceGateway).findById("device-123");
        verify(deviceGateway, never()).update(any());
    }

    @Test
    void shouldThrowUpdateNotAllowedExceptionWhenBrandUpdatedAndStateInUse() {
        // given
        Device existingDevice = buildDevice("device-123", "Old Name", "OldBrand", DeviceStateEnum.IN_USE);
        Device updatedInfo = buildDevice("device-123", null, "NewBrand", DeviceStateEnum.IN_USE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));

        // when - then
        assertThatThrownBy(() -> updateDeviceUseCase.execute(updatedInfo))
                .isInstanceOf(DeviceUpdateNotAllowedException.class)
                .hasMessageContaining("Cannot update name or brand when device state is IN_USE");

        verify(deviceGateway).findById("device-123");
        verify(deviceGateway, never()).update(any());
    }

    @Test
    void shouldUpdateOnlyState() {
        // given
        Device existingDevice = buildDevice("device-123", "Old Name", "OldBrand", DeviceStateEnum.AVAILABLE);
        Device updatedInfo = buildDevice("device-123", null, null, DeviceStateEnum.IN_USE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));
        when(deviceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Device result = updateDeviceUseCase.execute(updatedInfo);

        // then
        assertThat(result.getName()).isEqualTo("Old Name");
        assertThat(result.getBrand()).isEqualTo("OldBrand");
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.IN_USE);
    }

    @Test
    void shouldDoNothingIfAllFieldsNullExceptId() {
        // given
        Device existingDevice = buildDevice("device-123", "Name", "Brand", DeviceStateEnum.AVAILABLE);
        Device updatedInfo = new Device("device-123", null, null, null, null);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));
        when(deviceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Device result = updateDeviceUseCase.execute(updatedInfo);

        // then
        assertThat(result.getName()).isEqualTo("Name");
        assertThat(result.getBrand()).isEqualTo("Brand");
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
    }

    @Test
    void shouldDoNotUpdateFieldsIfNoChanges() {
        // given
        Device existingDevice = buildDevice("device-123", "Name", "Brand", DeviceStateEnum.AVAILABLE);
        Device updatedInfo = buildDevice("device-123", "Name", "Brand", DeviceStateEnum.AVAILABLE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));
        when(deviceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Device result = updateDeviceUseCase.execute(updatedInfo);

        // then
        assertThat(result.getName()).isEqualTo("Name");
        assertThat(result.getBrand()).isEqualTo("Brand");
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
    }

    @Test
    void shouldAllowStateUpdateWhenNameAndBrandAreNotUpdatedEvenIfStateInUse() {
        // given
        Device existingDevice = buildDevice("device-123", "Name", "Brand", DeviceStateEnum.IN_USE);
        Device updatedInfo = buildDevice("device-123", null, null, DeviceStateEnum.AVAILABLE);

        when(deviceGateway.findById("device-123")).thenReturn(Optional.of(existingDevice));
        when(deviceGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Device result = updateDeviceUseCase.execute(updatedInfo);

        // then
        assertThat(result.getState()).isEqualTo(DeviceStateEnum.AVAILABLE);
        assertThat(result.getName()).isEqualTo("Name");  // name unchanged
        assertThat(result.getBrand()).isEqualTo("Brand"); // brand unchanged

        verify(deviceGateway).update(result);
    }

    private Device buildDevice(String id, String name, String brand, DeviceStateEnum state) {
        return new Device(
                id,
                name,
                brand,
                state,
                Instant.parse("2025-09-14T23:00:00Z")
        );
    }
}
