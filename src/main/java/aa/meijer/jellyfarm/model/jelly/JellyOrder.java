package aa.meijer.jellyfarm.model.jelly;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JellyOrder {
    @JsonProperty("string")
    private String string;

}
