package com.withus.withus.domain.notice.dto;

import java.util.Objects;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
public record PageableDto(
    int page,
    int size,
    String sortBy
) {

  public static PageableDto getsPageableDto(int page, int size, String sortBy) {

    return PageableDto.builder()
        .page(page)
        .size(size)
        .sortBy(sortBy)
        .build();
  }

  public Pageable toPageable() {
    if(Objects.isNull(sortBy)) {

      return PageRequest.of(page-1, size);
    }
    else {

      return PageRequest.of(page-1, size, Sort.by(sortBy).descending());
    }

  }

}
