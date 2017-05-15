package com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.struts.TextProvider;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task configuration, where we save and validate the settings applied
 * to the plugin.
 */
@Component
@ExportAsService({com.atlassian.bamboo.task.TaskConfigurator.class})
@Named("taskConfigurator")
public class TaskConfigurator extends AbstractTaskConfigurator {
    private static final String SERVER_URL = "serverUrl";
    private static final String SERVER_URL_ERROR_KEY = "octopus.serverUrl.error";
    @ComponentImport
    private final TextProvider textProvider;

    @Inject
    public TaskConfigurator(@NotNull final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params,
                                                     @Nullable final TaskDefinition previousTaskDefinition) {
        checkNotNull(params);
        checkNotNull(previousTaskDefinition);

        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(SERVER_URL, params.getString(SERVER_URL));
        return config;
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        checkNotNull(context);
        checkNotNull(taskDefinition);

        super.populateContextForEdit(context, taskDefinition);

        context.put(SERVER_URL, taskDefinition.getConfiguration().get(SERVER_URL));
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params,
                         @NotNull final ErrorCollection errorCollection) {
        checkNotNull(params);
        checkNotNull(errorCollection);

        super.validate(params, errorCollection);

        final String sayValue = params.getString(SERVER_URL);
        if (StringUtils.isEmpty(sayValue)) {
            errorCollection.addError(SERVER_URL, textProvider.getText(SERVER_URL_ERROR_KEY));
        }
    }
}
