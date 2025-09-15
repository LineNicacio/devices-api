package com.nicacio.devicesapi.usecases;

import com.nicacio.devicesapi.domains.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FindDeviceUseCase {
    Device byId(String id);

    Page<Device> all(Pageable pageable);
}
