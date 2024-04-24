package com.email.email.microservice.Model;

import com.email.email.microservice.EmailSamples.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.core.serializer.Deserializer;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailParams implements Serializable  {
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
