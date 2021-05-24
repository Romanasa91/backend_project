package twins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import twins.boundaries.OperationBoundary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import twins.logic.OperationsServiceExtended;

@RestController
public class OperationController {
    private final OperationsServiceExtended operationService;

    @Autowired
    public OperationController(OperationsServiceExtended operationService) {
        this.operationService = operationService;
    }

    @RequestMapping(
            path="/twins/operations",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationBoundary storeOperation (
            @RequestBody OperationBoundary input) throws Exception {
        this.operationService.invokeOperation(input);
        return input;
    }

    @RequestMapping(
            path="/twins/operations/async",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationBoundary storeOperationId (
            @RequestBody OperationBoundary input){
        return this.operationService.sendAndForget(input);
    }
}
