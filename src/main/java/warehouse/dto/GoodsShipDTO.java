package warehouse.dto;

import java.util.Map;

public class GoodsShipDTO {

    private Map<String, Integer> goods;

    public Map<String, Integer> getGoods() {
        return goods;
    }

    public void setGoods(Map<String, Integer> goods) {
        this.goods = goods;
    }
}
