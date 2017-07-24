package warehouse.services;

import java.util.Date;
import java.util.Map;

public interface WarehouseService {

    void createLoads(Map<String, Integer> goods);

    Map<String, Map<Date, Integer>> ship(Map<String, Integer> goods);

    void calculateTotalReminder(Map<String, Integer> goods, boolean increase);

    Map<String, Integer> checkReminders(Map<String, Integer> ship);

    Map<String, Integer> stock();

}
