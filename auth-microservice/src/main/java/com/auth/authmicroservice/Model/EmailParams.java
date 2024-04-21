package com.auth.authmicroservice.Model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailParams implements Serializable {
    private String email;
    private String subject;
    private Type type;
    private String body;
    private Integer partyId;
    private Integer requestId;
    private String message;
    private String city;
}
