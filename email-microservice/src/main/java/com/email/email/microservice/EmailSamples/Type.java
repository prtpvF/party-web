package com.email.email.microservice.EmailSamples;

import com.email.email.microservice.util.TypeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

public enum Type  {
    DELETE_PARTY,
    DELETE_ACCOUNT,
    CREATE_PARTY,
    REGISTRATION,
    REQUEST_ANSWER,
    NEAR
}
