package by.intexsoft.diplom.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCodeDto {

        private String username;
        private String code;
}
