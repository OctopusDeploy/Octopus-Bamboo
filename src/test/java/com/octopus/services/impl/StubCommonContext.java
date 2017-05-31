package com.octopus.services.impl;

import com.atlassian.bamboo.Key;
import com.atlassian.bamboo.ResultKey;
import com.atlassian.bamboo.chains.ChainStorageTag;
import com.atlassian.bamboo.credentials.CredentialsData;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.repository.RepositoryDefinition;
import com.atlassian.bamboo.task.RuntimeTaskContext;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.runtime.RuntimeTaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildKey;
import com.atlassian.bamboo.v2.build.CommonContext;
import com.atlassian.bamboo.v2.build.CurrentResult;
import com.atlassian.bamboo.v2.build.trigger.TriggerReason;
import com.atlassian.bamboo.variable.VariableContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * A stub common context
 */
public class StubCommonContext implements CommonContext {
    @NotNull
    public BuildKey getBuildKey() {
        return null;
    }

    public long getEntityId() {
        return 0;
    }

    public Key getEntityKey() {
        return null;
    }

    public ResultKey getResultKey() {
        return new ResultKey() {

            @NotNull
            public String getKey() {
                return "key";
            }

            @NotNull
            public Key getEntityKey() {
                return new Key() {

                    @NotNull
                    public String getKey() {
                        return "entity key";
                    }
                };
            }

            public int getResultNumber() {
                return 0;
            }
        };
    }

    public String getDisplayName() {
        return null;
    }

    public CurrentResult getCurrentResult() {
        return null;
    }

    @NotNull
    public ErrorCollection getErrorCollection() {
        return null;
    }

    @NotNull
    public List<TaskDefinition> getTaskDefinitions() {
        return null;
    }

    @NotNull
    public List<RuntimeTaskDefinition> getRuntimeTaskDefinitions() {
        return null;
    }

    @NotNull
    public RuntimeTaskContext getRuntimeTaskContext() {
        return null;
    }

    @NotNull
    public VariableContext getVariableContext() {
        return null;
    }

    @NotNull
    public TriggerReason getTriggerReason() {
        return null;
    }

    @NotNull
    public Map<Long, RepositoryDefinition> getRepositoryDefinitionMap() {
        return null;
    }

    @NotNull
    public List<RepositoryDefinition> getRepositoryDefinitions() {
        return null;
    }

    @NotNull
    public Iterable<CredentialsData> getSharedCredentials() {
        return null;
    }

    @NotNull
    public Map<PlanKey, ChainStorageTag> getChainStorageTags() {
        return null;
    }
}
