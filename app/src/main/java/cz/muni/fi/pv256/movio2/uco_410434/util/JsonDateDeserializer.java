package cz.muni.fi.pv256.movio2.uco_410434.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class JsonDateDeserializer implements JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String strValue = json.getAsString();
        if (strValue == null || strValue.isEmpty()) {
            return DateTime.now();
        }
        return DateTime.parse(strValue);
    }
}
