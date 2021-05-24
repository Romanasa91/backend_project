package twins.logic;


import org.springframework.stereotype.Component;
import twins.data.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class DataAccessControl {

    public DataAccessControl() {}

    public boolean isAllowed(UserRole userRole, LogicType key, ActionType value) {
        boolean rv;

        switch (userRole) {
            case ADMIN:
                rv = checkAdmin(key,value);
                break;
            case PLAYER:
                rv = checkPlayer(key, value);
                break;
            case MANAGER:
                rv = checkManager(key,value);
                break;
            default:
                rv = false;
                break;
        }
        return rv;
    }

    private boolean checkAdmin(LogicType key, ActionType value) {
        HashMap<LogicType,ArrayList<ActionType>> permissions = new HashMap<>();

        ArrayList<ActionType> userAction = new ArrayList<>(
                Arrays.asList(ActionType.DELETE_ALL, ActionType.GET_ALL));

        ArrayList<ActionType> operationAction = new ArrayList<>(
                Arrays.asList(ActionType.DELETE_ALL, ActionType.GET_ALL));

        ArrayList<ActionType> itemAction = new ArrayList<>(
                Arrays.asList(ActionType.DELETE_ALL));

        permissions.put(LogicType.USER, userAction);
        permissions.put(LogicType.OPERATION, operationAction);
        permissions.put(LogicType.ITEM, itemAction);

        if(permissions.containsKey(key)) {
            return permissions.get(key).contains(value);
        }
        return false;
    }

    private boolean checkManager(LogicType key, ActionType value) {
        HashMap<LogicType,ArrayList<ActionType>> permissions = new HashMap<>();

        ArrayList<ActionType> itemAction = new ArrayList<>(
                Arrays.asList(ActionType.CREATE, ActionType.UPDATE,
                ActionType.GET_SPECIFIC, ActionType.GET_ALL));

        permissions.put(LogicType.ITEM, itemAction);

        if(permissions.containsKey(key)) {
            return permissions.get(key).contains(value);
        }
        return false;
    }

    private boolean checkPlayer(LogicType key, ActionType value) {
        HashMap<LogicType,ArrayList<ActionType>> permissions = new HashMap<>();

        ArrayList<ActionType> operationAction = new ArrayList<>(
                Arrays.asList(ActionType.INVOKE, ActionType.INVOKE_ASYNCHRONOUS));

        ArrayList<ActionType> itemAction = new ArrayList<>(
                Arrays.asList(ActionType.GET_SPECIFIC, ActionType.GET_ALL));

        permissions.put(LogicType.OPERATION, operationAction);
        permissions.put(LogicType.ITEM, itemAction);

        if(permissions.containsKey(key)) {
            return permissions.get(key).contains(value);
        }
        return false;
    }
}
