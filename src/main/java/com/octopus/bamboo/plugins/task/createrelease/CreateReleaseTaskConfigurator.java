package com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.struts.TextProvider;
import com.octopus.constants.OctoConstants;
import com.octopus.services.impl.BaseConfigurator;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
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
@Named("createReleaseTaskConfigurator")
public class CreateReleaseTaskConfigurator extends BaseConfigurator {

    @ComponentImport
    private final TextProvider textProvider;

    @Inject
    public CreateReleaseTaskConfigurator(@NotNull final TextProvider textProvider,
                                         @NotNull final ApplicationContext applicationContext) {
        super(applicationContext);
        this.textProvider = checkNotNull(textProvider, "textProvider cannot be null");
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params,
                                                     @Nullable final TaskDefinition previousTaskDefinition) {
        checkNotNull(params);

        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(OctoConstants.SERVER_URL, params.getString(OctoConstants.SERVER_URL));
        config.put(OctoConstants.API_KEY, params.getString(OctoConstants.API_KEY));
        config.put(OctoConstants.SPACE_NAME, params.getString(OctoConstants.SPACE_NAME));
        config.put(OctoConstants.PROJECT_NAME, params.getString(OctoConstants.PROJECT_NAME));
        config.put(OctoConstants.CHANNEL_NAME, params.getString(OctoConstants.CHANNEL_NAME));
        config.put(OctoConstants.GIT_REF, params.getString(OctoConstants.GIT_REF));
        config.put(OctoConstants.GIT_COMMIT, params.getString(OctoConstants.GIT_COMMIT));
        config.put(OctoConstants.VERBOSE_LOGGING, params.getString(OctoConstants.VERBOSE_LOGGING));
        config.put(OctoConstants.RELEASE_VERSION, params.getString(OctoConstants.RELEASE_VERSION));
        config.put(OctoConstants.ENVIRONMENT_NAME, params.getString(OctoConstants.ENVIRONMENT_NAME));
        config.put(OctoConstants.IGNORE_EXISTING_RELEASE_NAME, params.getString(OctoConstants.IGNORE_EXISTING_RELEASE_NAME));
        config.put(OctoConstants.SHOW_DEPLOYMENT_PROGRESS, params.getString(OctoConstants.SHOW_DEPLOYMENT_PROGRESS));
        config.put(OctoConstants.DEPLOYMENT_TIMEOUT, params.getString(OctoConstants.DEPLOYMENT_TIMEOUT));
        config.put(OctoConstants.CANCEL_DEPLOYMENT_ON_TIMEOUT, params.getString(OctoConstants.CANCEL_DEPLOYMENT_ON_TIMEOUT));
        config.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, params.getString(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        config.put(OctoConstants.TENANTS_NAME, params.getString(OctoConstants.TENANTS_NAME));
        config.put(OctoConstants.TENANT_TAGS_NAME, params.getString(OctoConstants.TENANT_TAGS_NAME));
        config.put(OctoConstants.OCTOPUS_CLI, params.getString(OctoConstants.OCTOPUS_CLI));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        context.put(OctoConstants.UI_CONFIG_BEAN, this.getUIConfigSupport());
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        checkNotNull(context);
        checkNotNull(taskDefinition);

        super.populateContextForEdit(context, taskDefinition);

        context.put(OctoConstants.SERVER_URL, taskDefinition.getConfiguration().get(OctoConstants.SERVER_URL));
        context.put(OctoConstants.API_KEY, taskDefinition.getConfiguration().get(OctoConstants.API_KEY));
        context.put(OctoConstants.SPACE_NAME, taskDefinition.getConfiguration().get(OctoConstants.SPACE_NAME));
        context.put(OctoConstants.PROJECT_NAME, taskDefinition.getConfiguration().get(OctoConstants.PROJECT_NAME));
        context.put(OctoConstants.CHANNEL_NAME, taskDefinition.getConfiguration().get(OctoConstants.CHANNEL_NAME));
        context.put(OctoConstants.VERBOSE_LOGGING, taskDefinition.getConfiguration().get(OctoConstants.VERBOSE_LOGGING));
        context.put(OctoConstants.RELEASE_VERSION, taskDefinition.getConfiguration().get(OctoConstants.RELEASE_VERSION));
        context.put(OctoConstants.ENVIRONMENT_NAME, taskDefinition.getConfiguration().get(OctoConstants.ENVIRONMENT_NAME));
        context.put(OctoConstants.IGNORE_EXISTING_RELEASE_NAME, taskDefinition.getConfiguration().get(OctoConstants.IGNORE_EXISTING_RELEASE_NAME));
        context.put(OctoConstants.GIT_COMMIT, taskDefinition.getConfiguration().get(OctoConstants.GIT_COMMIT));
        context.put(OctoConstants.GIT_REF, taskDefinition.getConfiguration().get(OctoConstants.GIT_REF));
        context.put(OctoConstants.SHOW_DEPLOYMENT_PROGRESS, taskDefinition.getConfiguration().get(OctoConstants.SHOW_DEPLOYMENT_PROGRESS));
        context.put(OctoConstants.DEPLOYMENT_TIMEOUT, taskDefinition.getConfiguration().get(OctoConstants.DEPLOYMENT_TIMEOUT));
        context.put(OctoConstants.CANCEL_DEPLOYMENT_ON_TIMEOUT, taskDefinition.getConfiguration().get(OctoConstants.CANCEL_DEPLOYMENT_ON_TIMEOUT));
        context.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, taskDefinition.getConfiguration().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        context.put(OctoConstants.TENANTS_NAME, taskDefinition.getConfiguration().get(OctoConstants.TENANTS_NAME));
        context.put(OctoConstants.TENANT_TAGS_NAME, taskDefinition.getConfiguration().get(OctoConstants.TENANT_TAGS_NAME));
        context.put(OctoConstants.OCTOPUS_CLI, taskDefinition.getConfiguration().get(OctoConstants.OCTOPUS_CLI));
        context.put(OctoConstants.UI_CONFIG_BEAN, this.getUIConfigSupport());
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

        final String octopusCli = params.getString(OctoConstants.OCTOPUS_CLI);
        if (StringUtils.isEmpty(octopusCli)) {
            errorCollection.addError(OctoConstants.OCTOPUS_CLI, textProvider.getText(OctoConstants.OCTOPUS_CLI_ERROR_KEY));
        }
    }
}
