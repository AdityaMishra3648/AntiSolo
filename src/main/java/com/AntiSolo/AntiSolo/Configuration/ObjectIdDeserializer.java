package com.AntiSolo.AntiSolo.Configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;
import org.springframework.boot.jackson.JsonComponent;
import java.io.IOException;

@JsonComponent
public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new ObjectId(jsonParser.getValueAsString()); // Convert String to ObjectId
    }
}
