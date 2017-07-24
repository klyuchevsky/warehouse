package warehouse.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import warehouse.domain.Goods;
import warehouse.domain.GoodsLoad;
import warehouse.domain.GoodsReminder;
import warehouse.repositories.GoodsLoadRepository;
import warehouse.repositories.GoodsReminderRepository;
import warehouse.repositories.GoodsRepository;
import warehouse.services.HistoryService;
import warehouse.services.WarehouseService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsLoadRepository goodsLoadRepository;

    @Autowired
    private GoodsReminderRepository goodsReminderRepository;

    @Autowired
    private HistoryService historyService;

    @Override
    @Transactional
    public void createLoads(Map<String, Integer> goods) {
        Date current = new Date();
        List<GoodsLoad> loads = goods.entrySet().stream()
                .map(entry -> createLoad(entry.getKey(), entry.getValue(), current))
                .collect(Collectors.toList());
        goodsLoadRepository.save(loads);
        historyService.recordLoads(loads);
    }

    @Override
    @Transactional
    public Map<String, Map<Date, Integer>> ship(Map<String, Integer> goods) {
        Map<String, Map<Date, Integer>> result = new LinkedHashMap<>();
        goods.entrySet().forEach(entry -> {
            Map<Date, Integer> shipped = shipWithArticle(entry);
            result.put(entry.getKey(), shipped);
        });
        return result;
    }

    @Override
    @Transactional
    public void calculateTotalReminder(Map<String, Integer> goods, boolean increase) {
        List<GoodsReminder> reminders = goods.entrySet().stream()
                .map(stringIntegerEntry -> updateReminder(stringIntegerEntry.getKey(), stringIntegerEntry.getValue(), increase))
                .collect(Collectors.toList());
        goodsReminderRepository.save(reminders);
    }

    @Override
    @Transactional
    public Map<String, Integer> checkReminders(Map<String, Integer> ship) {
        Map<String, Integer> result = new LinkedHashMap<>();
        ship.entrySet().forEach(entry -> {
            Integer reminder = goodsReminderRepository.getByGoodsArticle(entry.getKey())
                    .map(GoodsReminder::getRemainder)
                    .orElse(0);
            if (reminder < entry.getValue()) {
                result.put(entry.getKey(), entry.getValue() - reminder);
            }
        });

        return result;
    }

    @Override
    @Transactional
    public Map<String, Integer> stock() {
        Map<String, Integer> result = new LinkedHashMap<>();
        goodsReminderRepository.findAll()
                .forEach(reminder -> result.put(reminder.getGoods().getArticle(), reminder.getRemainder()));
        return result;
    }

    private GoodsLoad createLoad(String article, Integer amount, Date date) {
        Goods goods = goodsRepository.getByArticle(article)
                .orElseThrow(() -> new IllegalStateException(MessageFormat.format("Goods with article {0} not found", article)));

        GoodsLoad load = new GoodsLoad();
        load.setGoods(goods);
        load.setAmount(amount);
        load.setRemainder(amount);
        load.setReceived(date);
        return load;
    }

    private GoodsReminder updateReminder(String article, Integer amount, boolean increase) {
        GoodsReminder reminder = goodsReminderRepository.getByGoodsArticle(article)
                .orElseGet(() -> {
                    GoodsReminder goodsReminder = new GoodsReminder();
                    Goods goods = goodsRepository.getByArticle(article)
                            .orElseThrow(() -> new IllegalStateException(MessageFormat.format("Goods with article {0} not found", article)));
                    goodsReminder.setGoods(goods);
                    return goodsReminder;
                });

        Integer val = Optional.ofNullable(reminder.getRemainder()).orElse(0);
        Integer newVal = increase ? val + amount : val - amount;
        reminder.setRemainder(newVal);

        return reminder;
    }

    private Map<Date, Integer> shipWithArticle(Map.Entry<String, Integer> goods) {
        String article = goods.getKey();
        List<GoodsLoad> loads = goodsLoadRepository.getAllByGoodsArticleOrderByReceived(article);

        Map<Date, Integer> shipped = new LinkedHashMap<>();
        List<GoodsLoad> toUpdate = new ArrayList<>();
        List<GoodsLoad> toDelete = new ArrayList<>();
        int required = goods.getValue();
        while (required > 0) {
            for (GoodsLoad load : loads) {
                if (required == 0) {
                    break;
                }
                int count;
                if (required >= load.getRemainder()) {
                    count = load.getRemainder();
                    required = required - count;
                    load.setRemainder(0);
                    shipped.put(load.getReceived(), count);
                    toDelete.add(load);
                } else {
                    int remainder = load.getRemainder() - required;
                    load.setRemainder(remainder);
                    count = required;
                    shipped.put(load.getReceived(), count);
                    toUpdate.add(load);
                    required = 0;
                    break;
                }
            }
        }

        goodsLoadRepository.save(toUpdate);
        goodsLoadRepository.delete(toDelete);
        historyService.recordShip(loads.get(0).getGoods(), goods.getValue());

        return shipped;
    }

}
