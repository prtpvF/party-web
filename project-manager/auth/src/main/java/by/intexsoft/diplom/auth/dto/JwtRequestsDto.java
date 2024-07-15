package by.intexsoft.diplom.auth.dto;

import lombok.Data;

@Data
public class JwtRequestsDto {
    private String username;
    private String sessionId;
}
