package com.nicacio.devicesapi.gateways.http.mappers;

import com.nicacio.devicesapi.domains.Device;
import com.nicacio.devicesapi.gateways.http.resources.DeviceRequest;
import com.nicacio.devicesapi.gateways.http.resources.DeviceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Named("instantToOffset")
    static OffsetDateTime instantToOffset(Instant instant) {
        return instant != null ? instant.truncatedTo(ChronoUnit.MILLIS).atOffset(ZoneOffset.UTC) : null;
    }

    Device toDomain(DeviceRequest request);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "instantToOffset")
    DeviceResponse toResponse(Device device);
}
