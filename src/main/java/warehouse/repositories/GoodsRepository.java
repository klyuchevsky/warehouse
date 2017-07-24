package warehouse.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import warehouse.domain.Goods;
import warehouse.domain.GoodsLoad;

import java.util.Optional;

@Repository
public interface GoodsRepository extends CrudRepository<Goods, Long> {

    Optional<Goods> getByArticle(String article);

}
