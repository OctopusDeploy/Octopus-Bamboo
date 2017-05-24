package com.octopus.bamboo.plugins.task.promoterelease;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.struts.TextProvider;
import com.octopus.constants.OctoConstants;
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
@Named("promoteReleaseTaskConfigurator")
public class PromoteReleaseTaskConfigurator extends AbstractTaskConfigurator {

    @ComponentImport
    private final TextProvider textProvider;

    @Inject
    public PromoteReleaseTaskConfigurator(@NotNull final TextProvider textProvider) {
        this.textProvider = textProvider;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params,
                                                     @Nullable final TaskDefinition previousTaskDefinition) {
        checkNotNull(params);

        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(OctoConstants.SERVER_URL, params.getString(OctoConstants.SERVER_URL));
        config.put(OctoConstants.API_KEY, params.getString(OctoConstants.API_KEY));
        config.put(OctoConstants.PROJECT_NAME, params.getString(OctoConstants.PROJECT_NAME));
        config.put(OctoConstants.PROMOTE_FROM_NAME, params.getString(OctoConstants.PROMOTE_FROM_NAME));
        config.put(OctoConstants.PROMOTE_TO_NAME, params.getString(OctoConstants.PROMOTE_TO_NAME));
        config.put(OctoConstants.VERBOSE_LOGGING, params.getString(OctoConstants.VERBOSE_LOGGING));
        config.put(OctoConstants.SHOW_DEPLOYMENT_PROGRESS, params.getString(OctoConstants.SHOW_DEPLOYMENT_PROGRESS));
        config.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, params.getString(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        config.put(OctoConstants.TENANTS_NAME, params.getString(OctoConstants.TENANTS_NAME));
        config.put(OctoConstants.TENANT_TAGS_NAME, params.getString(OctoConstants.TENANT_TAGS_NAME));
        return config;
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        checkNotNull(context);
        checkNotNull(taskDefinition);

        super.populateContextForEdit(context, taskDefinition);

        context.put(OctoConstants.SERVER_URL, taskDefinition.getConfiguration().get(OctoConstants.SERVER_URL));
        context.put(OctoConstants.API_KEY, taskDefinition.getConfiguration().get(OctoConstants.API_KEY));
        context.put(OctoConstants.PROJECT_NAME, taskDefinition.getConfiguration().get(OctoConstants.PROJECT_NAME));
        context.put(OctoConstants.PROMOTE_FROM_NAME, taskDefinition.getConfiguration().get(OctoConstants.PROMOTE_FROM_NAME));
        context.put(OctoConstants.PROMOTE_TO_NAME, taskDefinition.getConfiguration().get(OctoConstants.PROMOTE_TO_NAME));
        context.put(OctoConstants.VERBOSE_LOGGING, taskDefinition.getConfiguration().get(OctoConstants.VERBOSE_LOGGING));
        context.put(OctoConstants.SHOW_DEPLOYMENT_PROGRESS, taskDefinition.getConfiguration().get(OctoConstants.SHOW_DEPLOYMENT_PROGRESS));
        context.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, taskDefinition.getConfiguration().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        context.put(OctoConstants.TENANTS_NAME, taskDefinition.getConfiguration().get(OctoConstants.TENANTS_NAME));
        context.put(OctoConstants.TENANT_TAGS_NAME, taskDefinition.getConfiguration().get(OctoConstants.TENANT_TAGS_NAME));
    }

    @Override
    public void validate(@NotNull final ActionParametersMap params,
                         @NotNull final ErrorCollection errorCollection) {
        checkNotNull(params);
        checkNotNull(errorCollection);

        super.validate(params, errorCollection);

        final String sayValue = params.getString(OctoConstants.SERVER_URL);
        if (StringUtils.isEmpty(sayValue)) {
            errorCollection.addError(OctoConstants.SERVER_URL, textProvider.getText(OctoConstants.SERVER_URL_ERROR_KEY));
        }

        final String apiKey = params.getString(OctoConstants.API_KEY);
        if (StringUtils.isEmpty(apiKey)) {
            errorCollection.addError(OctoConstants.API_KEY, textProvider.getText(OctoConstants.API_KEY_ERROR_KEY));
        }

        final String projectValue = params.getString(OctoConstants.PROJECT_NAME);
        if (StringUtils.isEmpty(projectValue)) {
            errorCollection.addError(OctoConstants.PROJECT_NAME, textProvider.getText(OctoConstants.PROJECT_NAME_ERROR_KEY));
        }

        final String promoteFrom = params.getString(OctoConstants.PROMOTE_FROM_NAME);
        if (StringUtils.isEmpty(promoteFrom)) {
            errorCollection.addError(OctoConstants.PROMOTE_FROM_NAME, textProvider.getText(OctoConstants.PROMOTE_FROM_NAME_ERROR_KEY));
        }

        final String promoteTo = params.getString(OctoConstants.PROMOTE_TO_NAME);
        if (StringUtils.isEmpty(promoteTo)) {
            errorCollection.addError(OctoConstants.PROMOTE_TO_NAME, textProvider.getText(OctoConstants.PROMOTE_TO_NAME_ERROR_KEY));
        }
    }
}
