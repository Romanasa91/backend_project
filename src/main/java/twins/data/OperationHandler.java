package twins.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OperationHandler extends PagingAndSortingRepository<OperationEntity, String> {}