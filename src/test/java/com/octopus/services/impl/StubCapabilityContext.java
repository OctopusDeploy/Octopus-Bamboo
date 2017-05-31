package com.octopus.services.impl;

import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.ReadOnlyCapabilitySet;
import com.octopus.constants.OctoTestConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A stub set of capabilities
 */
public class StubCapabilityContext implements CapabilityContext {
    private String octoPath;

    @Nullable
    @Override
    public ReadOnlyCapabilitySet getCapabilitySet() {
        return null;
    }

    @Override
    public void setCapabilitySet(@Nullable final ReadOnlyCapabilitySet readOnlyCapabilitySet) {

    }

    @Override
    public void clearCapabilitySet() {

    }

    @Nullable
    @Override
    public String getCapabilityValue(@NotNull final String s) {
        return octoPath;
    }

    public String getOctoPath() {
        return octoPath;
    }

    public void setOctoPath(String octoPath) {
        this.octoPath = octoPath;
    }
}
