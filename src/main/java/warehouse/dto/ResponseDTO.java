package warehouse.dto;

import java.util.Date;

public class ResponseDTO {

    private Date timestamp;

    public ResponseDTO(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
