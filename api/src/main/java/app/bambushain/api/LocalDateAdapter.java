package app.bambushain.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    @Inject
    public LocalDateAdapter() {
    }

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.toString());
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString());
    }
}