package com.nicacio.devicesapi.gateways.http.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.http.advice.ExceptionControllerAdvice;
import com.nicacio.devicesapi.gateways.http.mappers.DeviceMapper;
import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.gateways.http.resources.DeviceResponse;
import com.nicacio.devicesapi.gateways.http.resources.DeviceUpdateRequest;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import com.nicacio.devicesapi.usecases.FindDeviceUseCase;
import com.nicacio.devicesapi.usecases.UpdateDeviceUseCase;
import com.nicacio.devicesapi.usecases.exceptions.DeviceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DevicesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private DeviceMapper deviceMapper;

    @Mock
    private FindDeviceUseCase findDeviceUseCase;

    @Mock
    private CreateDeviceUseCase createDeviceUseCase;

    @Mock
    private UpdateDeviceUseCase updateDeviceUseCase;

    @InjectMocks
    private DevicesController devicesController;

    private DeviceRequest validRequest;
    private DeviceUpdateRequest validUpdateRequest;
    private Device domainDevice;
    private Device domainDeviceWithId;
    private Device createdDevice;
    private Device updatedDevice;
    private DeviceResponse response;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(devicesController)
                .setControllerAdvice(ExceptionControllerAdvice.class)
                .build();
        validRequest = buildRequest();
        domainDevice = buildDevice(null);
        createdDevice = buildDevice("abc123");
        validUpdateRequest = buildUpdateRequest();
        domainDeviceWithId = buildDevice("abc123");
        updatedDevice = buildDevice("abc123").withName("Updated Device");
        response = buildResponse("abc123");
    }

    @Test
    void shouldCreateSuccessfully() throws Exception {
        // given
        when(deviceMapper.toDomain(validRequest)).thenReturn(domainDevice);
        when(createDeviceUseCase.execute(domainDevice)).thenReturn(createdDevice);
        when(deviceMapper.toResponse(createdDevice)).thenReturn(response);

        // when - then
        mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.name").value("Test Device"))
                .andExpect(jsonPath("$.brand").value("TestBrand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        //given
        DeviceRequest invalidRequest = buildRequest().withName("");

        // when - then
        mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenBrandIsMissing() throws Exception {
        // given
        DeviceRequest invalidRequest = buildRequest().withBrand("");

        // when - then
        mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenStateIsInvalid() throws Exception {
        // given
        DeviceRequest invalidRequest = buildRequest().withState(null);

        // when - then
        mockMvc.perform(post("/api/v1/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnPagedDevicesSuccessfully() throws Exception {
        // given
        Device device1 = buildDevice("id1");
        Device device2 = buildDevice("id2");

        Page<Device> devicePage = new PageImpl<>(
                List.of(device1, device2),
                PageRequest.of(0, 10),
                2
        );

        DeviceResponse response1 = buildResponse("id1");
        DeviceResponse response2 = buildResponse("id2");

        when(findDeviceUseCase.all(any(Pageable.class))).thenReturn(devicePage);

        when(deviceMapper.toResponse(device1)).thenReturn(response1);
        when(deviceMapper.toResponse(device2)).thenReturn(response2);

        mockMvc.perform(get("/api/v1/devices")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("id1"))
                .andExpect(jsonPath("$.content[1].id").value("id2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldUpdateSuccessfully() throws Exception {
        // given
        when(deviceMapper.toDomain(validUpdateRequest)).thenReturn(domainDeviceWithId.withId("abc123"));
        when(updateDeviceUseCase.execute(domainDeviceWithId.withId("abc123"))).thenReturn(updatedDevice);
        when(deviceMapper.toResponse(updatedDevice)).thenReturn(response.withName("Updated Device").withBrand("UpdatedBrand"));

        // when - then
        mockMvc.perform(patch("/api/v1/devices/{id}", "abc123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.name").value("Updated Device"))
                .andExpect(jsonPath("$.brand").value("UpdatedBrand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturnDeviceByIdSuccessfully() throws Exception {
        // given
        String deviceId = "abc123";
        when(findDeviceUseCase.byId(deviceId)).thenReturn(createdDevice);
        when(deviceMapper.toResponse(createdDevice)).thenReturn(response);

        // when - then
        mockMvc.perform(get("/api/v1/devices/{id}", deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.name").value("Test Device"))
                .andExpect(jsonPath("$.brand").value("TestBrand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturnNotFoundWhenDeviceDoesNotExist() throws Exception {
        // given
        String deviceId = "non-existent-id";
        when(findDeviceUseCase.byId(deviceId))
                .thenThrow(new DeviceNotFoundException("Device not found with id " + deviceId));

        // when - then
        mockMvc.perform(get("/api/v1/devices/{id}", deviceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Device not found with id " + deviceId));
    }

    @Test
    void shouldReturnDevicesByBrand() throws Exception {
        // given
        String brand = "TestBrand";
        Pageable pageable = PageRequest.of(0, 10);

        Device device1 = buildDevice("id1").withBrand(brand);
        Device device2 = buildDevice("id2").withBrand(brand);

        List<Device> devices = List.of(device1, device2);
        Page<Device> devicePage = new PageImpl<>(devices, pageable, devices.size());

        DeviceResponse response1 = buildResponse("id1").withBrand(brand);
        DeviceResponse response2 = buildResponse("id2").withBrand(brand);

        when(findDeviceUseCase.byBrand(eq(brand), any(Pageable.class))).thenReturn(devicePage);
        when(deviceMapper.toResponse(device1)).thenReturn(response1);
        when(deviceMapper.toResponse(device2)).thenReturn(response2);

        // when - then
        mockMvc.perform(get("/api/v1/devices/brand/{brand}", brand)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("id1"))
                .andExpect(jsonPath("$.content[0].brand").value(brand))
                .andExpect(jsonPath("$.content[1].id").value("id2"))
                .andExpect(jsonPath("$.content[1].brand").value(brand))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldReturnDevicesByState() throws Exception {
        // given
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;
        Pageable pageable = PageRequest.of(0, 10);

        List<Device> devices = List.of(buildDevice("device-1"), buildDevice("device-2"));
        Page<Device> page = new PageImpl<>(devices, pageable, devices.size());

        List<DeviceResponse> deviceResponses = devices.stream()
                .map(device -> buildResponse(device.getId()))
                .toList();
        Page<DeviceResponse> responsePage = new PageImpl<>(deviceResponses, pageable, deviceResponses.size());

        when(findDeviceUseCase.byState(state, pageable)).thenReturn(page);
        when(deviceMapper.toResponse(any(Device.class))).thenAnswer(invocation -> {
            Device d = invocation.getArgument(0);
            return buildResponse(d.getId());
        });

        // when - then
        mockMvc.perform(get("/api/v1/devices/state/" + state)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(devices.size()))
                .andExpect(jsonPath("$.content[0].id").value("device-1"))
                .andExpect(jsonPath("$.content[1].id").value("device-2"));
    }

    private DeviceRequest buildRequest() {
        return new DeviceRequest(
                "Test Device",
                "TestBrand",
                DeviceStateEnum.AVAILABLE
        );
    }

    private Device buildDevice(final String id) {
        return new Device(
                id,
                "Test Device",
                "TestBrand",
                DeviceStateEnum.AVAILABLE,
                Instant.parse("2025-09-14T23:00:00Z")
        );
    }

    private DeviceResponse buildResponse(final String id) {
        return new DeviceResponse(
                id,
                "Test Device",
                "TestBrand",
                DeviceStateEnum.AVAILABLE,
                OffsetDateTime.ofInstant(Instant.parse("2025-09-14T23:00:00Z"), ZoneOffset.UTC)
        );
    }

    private DeviceUpdateRequest buildUpdateRequest() {
        return new DeviceUpdateRequest(
                "Updated Device",
                "UpdatedBrand",
                DeviceStateEnum.AVAILABLE
        );
    }
}
