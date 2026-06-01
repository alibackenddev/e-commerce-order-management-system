package uz.pdp.dto.page_dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class PageableDto {
    private long offest;
    private int pageNumber;
    private int pageSize;
    private boolean paged;

}
