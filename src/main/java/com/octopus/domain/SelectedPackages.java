package com.octopus.domain;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/ActionTemplateUsages
 */
public class SelectedPackages {
    private String stepName;

    private String version;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(final String stepName) {
        this.stepName = stepName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }
}
