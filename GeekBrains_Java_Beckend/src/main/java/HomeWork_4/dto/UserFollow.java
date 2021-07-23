package HomeWork_4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFollow {
    @JsonProperty("status")
    public Boolean status;

}
