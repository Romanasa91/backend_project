package twins.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.boundaries.ItemBoundary;
import twins.boundaries.id.ItemId;
import twins.boundaries.id.UserId;
import twins.data.ItemEntity;
import twins.data.ItemHandler;
import twins.data.UserRole;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class ItemLogic implements ItemsServiceExtended {
    private final ItemHandler itemHandler;
    private String space;
    private final AtomicInteger id;
    private UsersServiceExtended usersService;
    private final LogicType logicType;
    private final DataAccessControl dac;


    @Autowired
    public ItemLogic(ItemHandler itemHandler, DataAccessControl dac) {
        this.itemHandler = itemHandler;
        this.id = new AtomicInteger();
        this.logicType = LogicType.ITEM;
        this.dac = dac;
    }

    @Autowired
    public void setUsersService(UsersServiceExtended usersService){this.usersService = usersService;}

    @Value("${spring.application.name:2021b.Roman}")
    public void setSpace(String space){
        this.space = space;
    }

    @Override
    @Transactional
    public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) throws Exception {
        UserRole role = usersService.findRole(userSpace,userEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.CREATE);
        if(!allowed){
            throw new Exception("Access Denied!");
        }
        if(!isValidItem(item))
            return null;
        item.setItemId(new ItemId(this.space, generateId()));
        item.setCreatedBy(new HashMap<String,UserId>(){{
            put("userId",new UserId(ItemLogic.this.space, userEmail));
        }});
        item.setCreatedTimestamp(new Date());
        itemHandler.save(convertToEntity(item));
        return item;
    }

    @Override
    @Transactional
    public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace,
                                   String itemId, ItemBoundary update) throws Exception {
        UserRole role = usersService.findRole(userSpace,userEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.UPDATE);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        ItemEntity entity = itemHandler.findById(new ItemId(itemSpace,itemId)
                .convertToString())
                .orElseThrow(RuntimeException::new);

        // cannot change: ID, CreatedBy, Date (set original data from DB).
        ItemBoundary boundaryFromDB = convertToBoundary(entity);
        update.setItemId(boundaryFromDB.getItemId());// id+space
        update.setCreatedBy(boundaryFromDB.getCreatedBy());
        update.setCreatedTimestamp(boundaryFromDB.getCreatedTimestamp());
        itemHandler.save(convertToEntity(update));
        return update;
    }


    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
        throw new RuntimeException("deprecated operation");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int page, int size) throws Exception {
        UserRole role;
        role = usersService.findRole(userSpace,userEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.GET_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        List<ItemBoundary> rv = new ArrayList<>();
        if(role.equals(UserRole.PLAYER)) {
            List<ItemEntity> itemsPageForPlayer = itemHandler.
                    findAllByActive(true,PageRequest.of(page, size, Sort.Direction.ASC,"type", "itemId"));
            for (ItemEntity entity : itemsPageForPlayer) {
                ItemBoundary boundary = this.convertToBoundary(entity);
                rv.add(boundary);
            }
        }
        else {
            List<ItemEntity> itemsPageForManager = itemHandler
                    .findAll(PageRequest.of(page, size, Sort.Direction.ASC,"type", "itemId")).toList();
            for (ItemEntity entity : itemsPageForManager) {
                ItemBoundary boundary = this.convertToBoundary(entity);
                rv.add(boundary);
            }
        }
        return rv;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemBoundary getSpecificItem(String userSpace, String userEmail,
                                        String itemSpace, String itemId) throws Exception {
        UserRole role = usersService.findRole(userSpace, userEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.GET_SPECIFIC);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        ItemEntity entity =  itemHandler
                .findById(new ItemId(itemSpace,itemId)
                .convertToString())
                .orElseThrow(RuntimeException::new);

        if(role.equals(UserRole.MANAGER)) {
            return convertToBoundary(entity);
        }
        else if(!entity.getActive()) {
            throw new Exception("Item not Found!");
        }
        return convertToBoundary(entity);
    }

    @Override
    @Transactional
    public void deleteAllItems(String adminSpace, String adminEmail) throws Exception {
        UserRole role =  usersService.findRole(adminSpace, adminEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.DELETE_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        itemHandler.deleteAll();
    }

    private ItemEntity convertToEntity(ItemBoundary boundary) {
        JsonConverter convert = new JsonConverter();
        ItemEntity rv = new ItemEntity();
        rv.setItemId(boundary.getItemId().convertToString());
        rv.setType(boundary.getType());
        rv.setName(boundary.getName());
        rv.setActive(boundary.getActive());
        rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
        rv.setCreatedBy(convert.marshall(boundary.getCreatedBy()));
        rv.setLocation(convert.marshall(boundary.getLocation()));
        rv.setItemAttributes(convert.marshall(boundary.getItemAttributes()));
        return rv;
    }

    private ItemBoundary convertToBoundary(ItemEntity entity) {
        JsonConverter convert = new JsonConverter();
        ItemBoundary rv = new ItemBoundary();
        rv.setItemId(covertToItemId(entity.getItemId()));
        rv.setType(entity.getType());
        rv.setName(entity.getName());
        rv.setActive(entity.getActive());
        rv.setCreatedTimestamp(entity.getCreatedTimestamp());
        rv.setCreatedBy(convert.unmarshall(entity.getCreatedBy(), HashMap.class));
        rv.setLocation(convert.unmarshall(entity.getLocation(), HashMap.class));
        rv.setItemAttributes(convert.unmarshall(entity.getItemAttributes(), HashMap.class));
        return rv;
    }

    private ItemId covertToItemId(String spaceId){
        String[] s = spaceId.split(ItemId.ID_SEPARATOR);
        if (s.length == 2) {
            String space = s[0];
            String id = s[1];
            return new ItemId(space,id);
        }
        return null;
    }

    private String generateId()
    {
        return String.valueOf(id.incrementAndGet());
    }

    private boolean isValidItem(ItemBoundary item){
        boolean rv = true;
        if(item.getName() == null) {
            System.err.println("Item name is not valid");
            rv = false;
        }
        if(item.getType() == null) {
            System.err.println("Type is not valid");
            rv = false;
        }
        return rv;
    }


}
