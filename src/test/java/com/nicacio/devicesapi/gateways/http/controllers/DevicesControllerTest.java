package com.nicacio.devicesapi.gateways.http.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.domains.enums.DeviceStateEnum;
import com.nicacio.devicesapi.gateways.http.mappers.DeviceMapper;
import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.gateways.http.resources.DeviceResponse;
import com.nicacio.devicesapi.usecases.CreateDeviceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DevicesControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private DeviceMapper deviceMapper;

    @Mock
    private CreateDeviceUseCase createDeviceUseCase;

    @InjectMocks
    private DevicesController devicesController;

    private DeviceRequest validRequest;
    private Device domainDevice;
    private Device createdDevice;
    private DeviceResponse response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(devicesController)
                .build();
        validRequest = buildRequest();
        domainDevice = buildDevice(null);
        createdDevice = buildDevice("abc123");
        response = buildResponse("abc123");
    }

    @Test
    void shouldCreateDeviceSuccessfully() throws Exception {
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
}
