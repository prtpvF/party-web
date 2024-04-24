package com.auth.authmicroservice.Model;

import com.auth.authmicroservice.util.TypeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public enum Type {
    DELETE_PARTY,
    DELETE_ACCOUNT,
    CREATE_PARTY,
    REGISTRATION,
    REQUEST_ANSWER,
    NEAR
}
