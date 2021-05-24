package twins.logic;

import twins.boundaries.UserBoundary;

import java.util.List;

public interface UsersService {

    UserBoundary createUser(UserBoundary user);
    UserBoundary login(String userSpace, String userEmail);
    UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update);
    void deleteAllUsers(String adminSpace, String adminEmail) throws Exception;

    @Deprecated
    List<UserBoundary> getAllUsers(String adminSpace, String adminEmail);
}
