package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Token {

    @JsonProperty("token")
    private String uuid;

    public Token() {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return uuid;
    }

}