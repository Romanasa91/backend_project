package twins.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.boundaries.OperationBoundary;
import twins.boundaries.id.OperationId;
import twins.boundaries.id.UserId;
import twins.data.OperationEntity;
import twins.data.OperationHandler;
import twins.data.UserRole;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OperationLogic implements OperationsServiceExtended{
    private final OperationHandler operationHandler;
    private String space;
    private final AtomicInteger id;
    private UsersServiceExtended usersService;
    private final LogicType logicType;
    private final DataAccessControl dac;
    private final ObjectMapper jackson;
    private JmsTemplate jmsTemplate;


    @Autowired
    public OperationLogic(OperationHandler operationHandler, DataAccessControl dac) {
        this.operationHandler = operationHandler;
        this.id = new AtomicInteger();
        this.logicType = LogicType.OPERATION;
        this.dac = dac;
        this.jackson = new ObjectMapper();
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Autowired
    public void setUsersService(UsersServiceExtended usersService){this.usersService = usersService;}

    @Value("${spring.application.name:2021b.Roman}")
    public void setSpace(String space){
        this.space = space;
    }


    @Override
    @Transactional
    public Object invokeOperation(OperationBoundary operation) throws Exception {
       return invokeAsynchronousOperation(operation);
    }

    @Override
    @Transactional
    public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) throws Exception {
        UserId id = operation.getInvokedBy().get("userId");
        UserRole role =  usersService.findRole(id.getSpace(),id.getEmail());
        boolean allowed = dac.isAllowed(role, logicType, ActionType.INVOKE_ASYNCHRONOUS);
        if(!allowed){
            throw new Exception("Access Denied!");
        }
        if(isNullOperation(operation))
            return null;

        if(operation.getOperationId() == null)
        {
            operation.setOperationId(
                    new OperationId(space,generateId()));
        }

        operation.setCreatedTimestamp(new Date());
        operationHandler.save(convertToEntity(operation));
        return operation;
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
        throw new RuntimeException("deprecated operation");
    }

    public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int page, int size) throws Exception {
        UserRole role =  usersService.findRole(adminSpace, adminEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.GET_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }

        Page<OperationEntity> operationsPage = operationHandler
                .findAll(PageRequest.of(page,size, Sort.Direction.ASC,"type","operationId"));
        List<OperationEntity> content = operationsPage.getContent();
        List<OperationBoundary> rv = new ArrayList<>();

        for (OperationEntity entity : content) {
            OperationBoundary boundary = this.convertToBoundary(entity);
            rv.add(boundary);
        }
        return rv;
    }

    @Override
    @Transactional
    public void deleteAllOperations(String adminSpace, String adminEmail) throws Exception {
        UserRole role =  usersService.findRole(adminSpace, adminEmail);
        boolean allowed = dac.isAllowed(role, logicType, ActionType.DELETE_ALL);
        if(!allowed){
            throw new Exception("Access Denied!");
        }
        operationHandler.deleteAll();
    }

    @Override
    @Transactional
    public OperationBoundary sendAndForget(OperationBoundary operation) {
        try {
            operation.setOperationId(new OperationId(space,generateId()));
            String json = this.jackson
                    .writeValueAsString(operation);

            // send the json to the MOM
            this.jmsTemplate
                    .send("Operations",
                            // lambda expression that implements functionality of sending a text message to the MOM
                            session->session.createTextMessage(json));
            return operation; // respond to client without waiting for message processing
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OperationEntity convertToEntity(OperationBoundary boundary) {
        JsonConverter convert = new JsonConverter();
        OperationEntity rv = new OperationEntity();
        rv.setOperationId(boundary.getOperationId().convertToString());
        rv.setType(boundary.getType());
        rv.setItem(convert.marshall(boundary.getItem()));
        rv.setInvokedBy(convert.marshall(boundary.getInvokedBy()));
        rv.setOperationAttributes(convert.marshall(boundary.getOperationAttributes()));
        rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
        return rv;
    }

    private OperationBoundary convertToBoundary(OperationEntity entity) {
        JsonConverter convert = new JsonConverter();
        OperationBoundary rv = new OperationBoundary();
        rv.setOperationId(covertToOperationId(entity.getOperationId()));
        rv.setType(entity.getType());
        rv.setItem(convert.unmarshall(entity.getItem(),HashMap.class));
        rv.setInvokedBy(convert.unmarshall(entity.getInvokedBy(), HashMap.class));
        rv.setOperationAttributes(convert.unmarshall(entity.getOperationAttributes(), HashMap.class));
        rv.setCreatedTimestamp(entity.getCreatedTimestamp());
        return rv;
    }

    private OperationId covertToOperationId(String spaceId){
        String[] s = spaceId.split(OperationId.ID_SEPARATOR);
        if (s.length == 2) {
            String space = s[0];
            String id = s[1];
            return new OperationId(space,id);
        }
        return null;
    }

    private String generateId()
    {
        return String.valueOf(id.incrementAndGet());
    }

    private boolean isNullOperation(OperationBoundary operation){
        boolean rv = false;
        if(operation.getType() == null) {
            System.err.println("Type is null");
            rv = true;
        }
        return rv;
    }
}



