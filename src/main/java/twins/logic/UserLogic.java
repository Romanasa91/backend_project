package twins.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.boundaries.UserBoundary;
import twins.boundaries.id.UserId;
import twins.data.UserEntity;
import twins.data.UserHandler;
import twins.data.UserRole;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserLogic implements UsersServiceExtended{
    private final UserHandler userHandler;
    private String space;
    private final LogicType logicType;
    private final DataAccessControl dac;


    @Autowired
    public UserLogic(UserHandler userHandler, DataAccessControl dac) {
        super();
        this.userHandler = userHandler;
        this.logicType = LogicType.USER;
        this.dac = dac;
    }

    @Value("${spring.application.name:2021b.Roman}")
    public void setSpace(String space){
        this.space = space;
    }


    @PostConstruct
    public void createAdmin()
    {
        UserId id = new UserId(this.space,"admin@admin.com");
        UserBoundary adminBoundary = new UserBoundary();
        adminBoundary.setUserId(id);
        adminBoundary.setRole("ADMIN");
        adminBoundary.setUsername("admin");
        adminBoundary.setAvatar("ADMIN");
        userHandler.save(convertToEntity(adminBoundary));
    }

    @PostConstruct
    public void createManager()
    {
        UserId id = new UserId(this.space,"manager@manager.com");
        UserBoundary adminBoundary = new UserBoundary();
        adminBoundary.setUserId(id);
        adminBoundary.setRole("MANAGER");
        adminBoundary.setUsername("manager");
        adminBoundary.setAvatar("MANAGER");
        userHandler.save(convertToEntity(adminBoundary));
    }

    @Override
    @Transactional
    public UserBoundary createUser(UserBoundary user) {
        if(!isValidUser(user))
            return null;
        user.setRole(user.getRole().toUpperCase());
        user.setUserId(
                new UserId(this.space, user.getUserId().getEmail()));
        return convertToBoundary(userHandler.save(convertToEntity(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public UserBoundary login(String userSpace, String userEmail) {
        String id = new UserId(userSpace, userEmail).convertToString();
        UserEntity userEntity = userHandler.findById(id)
                .orElseThrow(RuntimeException::new);
        convertToBoundary(userEntity);
        return convertToBoundary(userEntity);
    }

    @Override
    @Transactional
    public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
        String id = new UserId(userSpace, userEmail).convertToString();
        UserEntity entity = userHandler.findById(id)
                .orElseThrow(RuntimeException::new);
        // user ID cannot be changed
        update.setUserId(convertToBoundary(entity).getUserId());
        userHandler.save(convertToEntity(update));
        return update;
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
        throw new RuntimeException("deprecated operation");
    }

    public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int page, int size) throws Exception {
        UserRole role = findRole(adminSpace, adminEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.GET_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        Page<UserEntity> usersPage = userHandler
                .findAll(PageRequest.of(page,size, Sort.Direction.ASC,"role","username"));
        List<UserEntity> content = usersPage.getContent();
        List<UserBoundary> rv = new ArrayList<>();

        for (UserEntity entity : content) {
            UserBoundary boundary = this.convertToBoundary(entity);
            rv.add(boundary);
        }
        return rv;
    }

    @Override
    @Transactional
    public void deleteAllUsers(String adminSpace, String adminEmail) throws Exception {
        UserRole role = findRole(adminSpace, adminEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.DELETE_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }
        userHandler.deleteAll();
    }


    public UserRole findRole(String userSpace, String userEmail) throws Exception {
        String id = new UserId(userSpace, userEmail).convertToString();
        if(!userHandler.findById(id).isPresent()) {
            throw new Exception("No such user!");
        }
        return userHandler.findById(id).get().getRole();
    }

    private UserEntity convertToEntity(UserBoundary boundary) {
        UserEntity rv = new UserEntity();
        rv.setUserId(boundary.getUserId().convertToString());
        rv.setRole(UserRole.valueOf(boundary.getRole().toUpperCase()));
        rv.setUsername(boundary.getUsername());
        rv.setAvatar(boundary.getAvatar());
        return rv;
    }

    private UserBoundary convertToBoundary(UserEntity entity) {
        UserBoundary rv = new UserBoundary();
        rv.setUserId(covertToUserId(entity.getUserId()));
        rv.setRole(entity.getRole().toString());
        rv.setUsername(entity.getUsername());
        rv.setAvatar(entity.getAvatar());
        return rv;
    }

    private UserId covertToUserId(String id){
        String[] s = id.split(UserId.ID_SEPARATOR);
        if (s.length == 2) {
            String space = s[0];
            String email = s[1];
            return new UserId(space,email);
        }
        return null;
    }

    private boolean isValidEmail(String email)
    {
        Pattern p = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(email);
        return matcher.find();
    }

    private boolean isValidRole(String role)
    {
        for (UserRole r: UserRole.values()) {
            if(r.toString().equals(role.toUpperCase()))
                return true;
        }
        return false;
    }

    private boolean isValidUser(UserBoundary user) {
        boolean rv = true;
        if(!isValidEmail(user.getUserId().getEmail())) {
            System.err.println("Email is not valid");
            rv = false;
        }
        if(!isValidRole(user.getRole())){
            System.err.println("Role is not valid");
            rv = false;
        }
        if(user.getUsername() == null) {
            System.err.println("Name is not valid");
            rv = false;
        }
        if(user.getAvatar() == null || user.getAvatar().equals("")) {
            System.err.println("Avatar is not valid");
            rv = false;
        }
        return rv;
    }
}
