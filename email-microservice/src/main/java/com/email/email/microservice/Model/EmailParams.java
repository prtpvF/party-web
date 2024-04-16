package com.email.email.microservice.Model;

import com.email.email.microservice.EmailSamples.Type;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailParams {
    private String email;
    private String subject;
    private Type type;
    private String body;
    private Integer partyId;
    private Integer requestId;
    private String message;
    private String city;
}
