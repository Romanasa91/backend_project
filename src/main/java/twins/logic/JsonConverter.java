package twins.logic;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    private final ObjectMapper jackson;

    public JsonConverter() {
        this.jackson = new ObjectMapper();
    }

    // Boundary -> Entity
    public String marshall(Object value) {
        try {
            return this.jackson
                    .writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Entity -> Boundary
    public <T> T unmarshall(String json, Class<T> requiredType) {
        try {
            return this.jackson
                    .readValue(json, requiredType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
