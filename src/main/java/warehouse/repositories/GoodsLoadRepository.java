package warehouse.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.domain.Goods;
import warehouse.domain.GoodsLoad;

import java.util.List;

@Repository
public interface GoodsLoadRepository extends CrudRepository<GoodsLoad, Long> {

    List<GoodsLoad> getAllByGoodsArticleOrderByReceived(String article);

}
