package twins.logic;

import twins.boundaries.OperationBoundary;

import java.util.List;

public interface OperationsServiceExtended extends OperationsService {
    List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail,
                                             int page, int size) throws Exception;
    OperationBoundary sendAndForget(OperationBoundary operation);

}
