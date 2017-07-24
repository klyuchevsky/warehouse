package warehouse.services;

import warehouse.domain.Goods;
import warehouse.domain.GoodsLoad;
import warehouse.domain.History;

import java.util.List;

public interface HistoryService {

    void recordLoads(List<GoodsLoad> loads);

    void recordShip(Goods goods, Integer count);

    List<History> getByArticle(String article);

}
