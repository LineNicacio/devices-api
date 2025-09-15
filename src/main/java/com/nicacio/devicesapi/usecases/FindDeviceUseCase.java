package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FindDeviceUseCase {
    Device byId(final String id);

    Page<Device> all(final Pageable pageable);

    Page<Device> byBrand(final String brand, Pageable pageable);
}
