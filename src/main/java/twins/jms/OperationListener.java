package twins.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import twins.boundaries.OperationBoundary;
import twins.logic.OperationsServiceExtended;

@Component
public class OperationListener {
    private final ObjectMapper jackson;
    private final OperationsServiceExtended operationLogic;


    @Autowired
    public OperationListener(OperationsServiceExtended operationLogic) {
        this.operationLogic = operationLogic;
        this.jackson = new ObjectMapper();
    }

    @JmsListener(destination = "Operations") // invoked by MOM
    public void handleOperation (String json) {
        try{
            Thread.sleep(30000);
            OperationBoundary boundary = this.jackson
                    .readValue(json, OperationBoundary.class);

            this.operationLogic
                    .invokeAsynchronousOperation(boundary);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
