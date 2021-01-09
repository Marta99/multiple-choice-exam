package common.api.data;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonSerializable {
    String toJson() throws JsonProcessingException;
}
