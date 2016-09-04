package org.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Map;

public class DummyActivity implements JavaDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        String activityName = execution.getCurrentActivityName();
        MyUnitTest.incrementTaskCounter(activityName);

        System.out.println("----- [ Start: " + activityName + " ] -----");
        Map<String, Object> vars = execution.getVariables();
        for (Map.Entry<String, Object> stringObjectEntry : vars.entrySet())
        {
            System.out.println("[Parameter in " + activityName + " ] " + stringObjectEntry.getKey() + " = " + stringObjectEntry
                    .getValue()
                    .toString());
        }

        System.out.println("----- [ End: " + activityName + " ] -----");
    }
}
