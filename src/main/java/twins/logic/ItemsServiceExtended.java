package twins.logic;

import twins.boundaries.ItemBoundary;

import java.util.List;

public interface ItemsServiceExtended extends ItemsService {
    List<ItemBoundary> getAllItems(String userSpace, String userEmail, int page, int size) throws Exception;
}
