package by.intexsoft.diplom.publicapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedDataDto<T> {

        private List<T> data;

        private Long count;
}
