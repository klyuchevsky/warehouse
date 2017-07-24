package warehouse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import warehouse.dto.*;
import warehouse.services.HistoryService;
import warehouse.services.WarehouseService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private HistoryService historyService;

    @Transactional
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseDTO load(@RequestBody GoodsLoadDTO content) {
        warehouseService.createLoads(content.getGoods());
        warehouseService.calculateTotalReminder(content.getGoods(), true);
        return new ResponseDTO(new Date());
    }

    @Transactional
    @RequestMapping(value = "ship", method = RequestMethod.POST)
    public ShipResponseDTO ship(@RequestBody GoodsShipDTO content) {
        Map<String, Integer> lack = warehouseService.checkReminders(content.getGoods());
        if (!lack.isEmpty()) {
            return ShipResponseDTO.createNotShippedResponse(lack);
        } else {
            Map<String, Map<Date, Integer>> result = warehouseService.ship(content.getGoods());
            warehouseService.calculateTotalReminder(content.getGoods(), false);
            return ShipResponseDTO.createShippedResponse(result);
        }
    }

    @Transactional
    @RequestMapping(value = "stock", method = RequestMethod.GET)
    public Map<String, Integer> stock() {
        return warehouseService.stock();
    }

    @Transactional
    @RequestMapping(value = "history", method = RequestMethod.GET)
    public List<HistoryDTO> history(@RequestParam String article) {
        return historyService.getByArticle(article).stream()
                .map(HistoryDTO::from)
                .collect(Collectors.toList());
    }

}
