package org.bobpark.common.model;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Builder
public record SimplePageable(Long page,
                             Long size) {

    public SimplePageable {
        page = defaultIfNull(page, 0L);
        size = defaultIfNull(size, 20L);
    }

    public Pageable toPageable() {
        return PageRequest.of(page.intValue(), size.intValue());
    }
}
