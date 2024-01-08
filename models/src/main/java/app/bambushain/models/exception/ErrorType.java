package app.bambushain.models.exception;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

@Getter
@JsonAdapter(ErrorType.Adapter.class)
public enum ErrorType {
    Crypto("crypto"),
    Database("database"),
    ExistsAlready("existsAlready"),
    InsufficientRights("insufficientRights"),
    InvalidData("invalidData"),
    Io("Io"),
    Mailing("mailing"),
    Network("network"),
    NotFound("notFound"),
    Serialization("serialization"),
    Unauthorized("unauthorized"),
    Unknown("unknown"),
    Validation("validation");

    private final String value;

    ErrorType(String value) {
        this.value = value;
    }

    public static ErrorType fromValue(String value) {
        for (ErrorType b : ErrorType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static class Adapter extends TypeAdapter<ErrorType> {
        @Override
        public void write(final JsonWriter jsonWriter, final ErrorType enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public ErrorType read(final JsonReader jsonReader) throws IOException {
            val value = jsonReader.nextString();

            return ErrorType.fromValue(value);
        }
    }
}
