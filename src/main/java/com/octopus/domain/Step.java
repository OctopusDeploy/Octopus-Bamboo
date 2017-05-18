package com.octopus.domain;

import java.util.List;
import java.util.Map;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/DeploymentSteps
 */
public class Step {
    private String condition;

    private String name;

    private String startTrigger;

    private List<Action> actions;

    private String id;

    private String requiresPackagesToBeAcquired;

    private Map<String, String> properties;

    public String getCondition() {
        return condition;
    }

    public void setCondition(final String condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStartTrigger() {
        return startTrigger;
    }

    public void setStartTrigger(final String startTrigger) {
        this.startTrigger = startTrigger;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(final List<Action> actions) {
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getRequiresPackagesToBeAcquired() {
        return requiresPackagesToBeAcquired;
    }

    public void setRequiresPackagesToBeAcquired(final String requiresPackagesToBeAcquired) {
        this.requiresPackagesToBeAcquired = requiresPackagesToBeAcquired;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

}