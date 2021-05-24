package twins.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemHandler extends PagingAndSortingRepository<ItemEntity, String> {

    List<ItemEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);

}