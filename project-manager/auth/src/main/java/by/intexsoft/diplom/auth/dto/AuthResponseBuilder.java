package by.intexsoft.diplom.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseBuilder {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("scope")
        private String scope;

        @JsonProperty("expires_in")
        private Integer expiresIn;

        @JsonProperty("refresh_expires_in")
        private Integer refreshExpiresIn;

        @JsonProperty("not-before-policy")
        private String notBeforePolicy;

        @JsonProperty("session_state")
        private String sessionState;

        @Override
        public String toString() {
                return "AuthResponseBuilder{" +
                        "accessToken='" + accessToken + '\'' +
                        ", refreshToken='" + refreshToken + '\'' +
                        ", tokenType='" + tokenType + '\'' +
                        ", scope='" + scope + '\'' +
                        ", expiresIn=" + expiresIn +
                        ", refreshExpiresIn=" + refreshExpiresIn +
                        ", notBeforePolicy='" + notBeforePolicy + '\'' +
                        ", sessionState='" + sessionState + '\'' +
                        '}';
        }
}