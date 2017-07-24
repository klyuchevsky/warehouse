package warehouse.dto;

import warehouse.domain.History;

import java.util.Date;

public class HistoryDTO {
    private String operation;
    private Date timestamp;
    private Integer quantity;

    public static HistoryDTO from(History history) {
        HistoryDTO dto = new HistoryDTO();
        dto.setOperation(history.getOperation());
        dto.setTimestamp(history.getTimestamp());
        dto.setQuantity(history.getAmount());
        return dto;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}