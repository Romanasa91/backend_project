package twins.logic;

import twins.boundaries.UserBoundary;
import twins.data.UserRole;

import java.util.List;

public interface UsersServiceExtended extends UsersService{
    List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int page, int size) throws Exception;
    UserRole findRole(String userSpace, String userEmail) throws Exception;
}
