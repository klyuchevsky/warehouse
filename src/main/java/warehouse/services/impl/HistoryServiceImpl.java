package warehouse.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.domain.Goods;
import warehouse.domain.GoodsLoad;
import warehouse.domain.History;
import warehouse.repositories.HistoryRepository;
import warehouse.services.HistoryService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    @Transactional
    public void recordLoads(List<GoodsLoad> loads) {
        List<History> records = loads.stream()
                .map(load -> {
                    History history = new History();
                    history.setGoods(load.getGoods());
                    history.setTimestamp(new Date());
                    history.setOperation(History.LOAD_OPERATION);
                    history.setAmount(load.getAmount());
                    return history;
                }).collect(Collectors.toList());
        historyRepository.save(records);
    }

    @Override
    @Transactional
    public void recordShip(Goods goods, Integer count) {
        History history = new History();
        history.setGoods(goods);
        history.setTimestamp(new Date());
        history.setOperation(History.SHIP_OPERATION);
        history.setAmount(count);
        historyRepository.save(history);
    }

    @Override
    @Transactional
    public List<History> getByArticle(String article) {
        return historyRepository.getByGoodsArticle(article);
    }

}
