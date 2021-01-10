package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class LocationAPI implements Cloneable, Serializable, JsonSerializable {
    @JsonProperty("port") private int port;
    @JsonProperty("host") private String host;
    @JsonProperty("bind_key") private String bindKey;

    public LocationAPI(@JsonProperty("port") int port,
                       @JsonProperty("host") String host,
                       @JsonProperty("bind_key") String bindKey) {
        this.port = port;
        this.host = host;
        this.bindKey = bindKey;
    }

    @Override
    protected LocationAPI clone() {
        return new LocationAPI(port, host, bindKey);
    }

    @Override
    public String toString() {
        return "LocationAPI{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", bindKey='" + bindKey + '\'' +
                '}';
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getBindKey() {
        return bindKey;
    }
}
