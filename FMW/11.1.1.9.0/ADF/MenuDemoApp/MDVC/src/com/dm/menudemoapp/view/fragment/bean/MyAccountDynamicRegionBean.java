package com.dm.menudemoapp.view.fragment.bean;

import javax.faces.event.ActionEvent;

import oracle.adf.controller.TaskFlowId;

public class MyAccountDynamicRegionBean {
    
    private String taskFlowId =
        "/WEB-INF/com/dm/menudemoapp/taskflows/home-flow.xml#home-flow";

    public MyAccountDynamicRegionBean() {
        super();
    }

    public TaskFlowId getDynamicTaskFlowId() {
        return TaskFlowId.parse(taskFlowId);
    }

    public void setTaskFlowId(String taskFlowId) {
        this.taskFlowId = taskFlowId;
    }

    public String getTaskFlowId() {
        return taskFlowId;
    }
}
