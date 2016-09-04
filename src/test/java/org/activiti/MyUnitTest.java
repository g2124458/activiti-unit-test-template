package org.activiti;

import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class MyUnitTest
{

    private static ConcurrentHashMap<String, Integer> taskCounter = new ConcurrentHashMap<String, Integer>();
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    public static synchronized void incrementTaskCounter(String taskName)
    {
        boolean isTaskExecuted = taskCounter.containsKey(taskName);
        String exceptionMessage = "Task '" + taskName + "' had been already executed!";
        assertFalse(exceptionMessage, isTaskExecuted);

        System.out.println("Incrementing task counter for '" + taskName + "'");
        taskCounter.put(taskName, 1);
    }

    @Test
    @Deployment(resources = {"org/activiti/test/Process2.bpmn20.xml"})
    public void test()
    {
        System.out.println("Creating instance of Proccess2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        params.put("param3", "value3");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                                                      .startProcessInstanceByKey("Process2", params);

        assertNotNull(processInstance);

        activitiRule.getProcessEngine().getProcessEngineConfiguration().setAsyncExecutorActivate(true);
        activitiRule.getProcessEngine().getProcessEngineConfiguration().setAsyncExecutorEnabled(true);
        JobExecutor jobExecutor = activitiRule.getProcessEngine().getProcessEngineConfiguration().getJobExecutor();
        jobExecutor.start();

        long jobCount = activitiRule.getManagementService().createJobQuery().count();
        System.out.println("CreateJobQuery count: " + jobCount);

    }


}
