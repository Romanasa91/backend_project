package twins.logic;

import twins.boundaries.ItemBoundary;
import java.util.List;

public interface ItemsService {

    ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) throws Exception;
    ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace,
                            String itemsId, ItemBoundary update) throws Exception;
    ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace,
                                 String itemsId) throws Exception;
    void deleteAllItems(String adminSpace, String adminEmail) throws Exception;

    @Deprecated
    List<ItemBoundary> getAllItems(String userSpace, String userEmail);
}
