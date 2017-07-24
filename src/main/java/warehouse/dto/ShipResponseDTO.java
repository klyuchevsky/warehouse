package warehouse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShipResponseDTO {

    private Date timestamp;
    private String status;
    private Map<String, Integer> notEnoughGoods;
    private Map<String, Map<Date, Integer>> goods;

    public static ShipResponseDTO createNotShippedResponse(Map<String, Integer> notEnoughGoods) {
        ShipResponseDTO dto = new ShipResponseDTO();
        dto.setTimestamp(new Date());
        dto.setStatus("not shipped");
        dto.setNotEnoughGoods(notEnoughGoods);
        return dto;
    }

    public static ShipResponseDTO createShippedResponse(Map<String, Map<Date, Integer>> goods) {
        ShipResponseDTO dto = new ShipResponseDTO();
        dto.setTimestamp(new Date());
        dto.setStatus("shipped");
        dto.setGoods(goods);
        return dto;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Integer> getNotEnoughGoods() {
        return notEnoughGoods;
    }

    public void setNotEnoughGoods(Map<String, Integer> notEnoughGoods) {
        this.notEnoughGoods = notEnoughGoods;
    }

    public Map<String, Map<Date, Integer>> getGoods() {
        return goods;
    }

    public void setGoods(Map<String, Map<Date, Integer>> goods) {
        this.goods = goods;
    }
}
