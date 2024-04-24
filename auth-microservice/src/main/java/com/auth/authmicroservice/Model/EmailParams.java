package com.auth.authmicroservice.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailParams  {
    private String email;
    private String subject;
    private Type type;
    private String body;
    private Integer partyId;
    private Integer requestId;
    private String message;
    private String city;

    @Override
    public String toString() {
        return "EmailParams{" +
                "email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", partyId=" + partyId +
                ", requestId=" + requestId +
                ", message='" + message + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
