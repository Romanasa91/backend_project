package twins.logic;

import twins.boundaries.OperationBoundary;
import java.util.List;

public interface OperationsService {

    Object invokeOperation(OperationBoundary operation) throws Exception;
    OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) throws Exception;
    void deleteAllOperations(String adminSpace, String adminEmail) throws Exception;

    @Deprecated
    List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail);
}
