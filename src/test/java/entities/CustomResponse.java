package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties (ignoreUnknown = true)
public class CustomResponse {

    private String tag_id;
    private String name_tag;
    private String id;

}
