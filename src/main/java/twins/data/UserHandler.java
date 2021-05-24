package twins.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserHandler extends PagingAndSortingRepository<UserEntity, String> {}