package warehouse.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.domain.Goods;
import warehouse.domain.GoodsReminder;

import java.util.Optional;

@Repository
public interface GoodsReminderRepository extends CrudRepository<GoodsReminder, Long> {

    Optional<GoodsReminder> getByGoodsArticle(String article);

}
