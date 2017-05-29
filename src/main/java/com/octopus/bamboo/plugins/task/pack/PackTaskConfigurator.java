package com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
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
import java.util.Arrays;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task configuration, where we save and validate the settings applied
 * to the plugin.
 */
@Component
@ExportAsService(AbstractTaskConfigurator.class)
@Named("packTaskConfigurator")
public class PackTaskConfigurator extends AbstractTaskConfigurator {
    @ComponentImport
    private final TextProvider textProvider;
    @ComponentImport
    private final UIConfigSupport uiConfigSupport;

    @Inject
    public PackTaskConfigurator(@NotNull final TextProvider textProvider,
                                @NotNull final UIConfigSupport uiConfigSupport) {
        checkNotNull(uiConfigSupport, "uiConfigSupport cannot be null");
        checkNotNull(textProvider, "textProvider cannot be null");

        this.textProvider = textProvider;
        this.uiConfigSupport = uiConfigSupport;
    }

    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params,
                                                     @Nullable final TaskDefinition previousTaskDefinition) {
        checkNotNull(params);

        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(OctoConstants.SERVER_URL, params.getString(OctoConstants.SERVER_URL));
        config.put(OctoConstants.API_KEY, params.getString(OctoConstants.API_KEY));
        config.put(OctoConstants.PACK_BASE_PATH_NAME, params.getString(OctoConstants.PACK_BASE_PATH_NAME));
        config.put(OctoConstants.PACK_FORMAT_NAME, params.getString(OctoConstants.PACK_FORMAT_NAME));
        config.put(OctoConstants.PACK_ID_NAME, params.getString(OctoConstants.PACK_ID_NAME));
        config.put(OctoConstants.PACK_VERSION_NAME, params.getString(OctoConstants.PACK_VERSION_NAME));
        config.put(OctoConstants.PACK_INCLUDE_NAME, params.getString(OctoConstants.PACK_INCLUDE_NAME));
        config.put(OctoConstants.PACK_OUT_FOLDER_NAME, params.getString(OctoConstants.PACK_OUT_FOLDER_NAME));
        config.put(OctoConstants.PACK_OVERWRITE_NAME, params.getString(OctoConstants.PACK_OVERWRITE_NAME));
        config.put(OctoConstants.VERBOSE_LOGGING, params.getString(OctoConstants.VERBOSE_LOGGING));
        config.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, params.getString(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        config.put(OctoConstants.OCTOPUS_CLI, params.getString(OctoConstants.OCTOPUS_CLI));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        context.put(OctoConstants.UI_CONFIG_BEAN, this.uiConfigSupport);
        context.put(OctoConstants.PACK_FORMAT_NAME, OctoConstants.PACK_ZIP_FORMAT);
        context.put(OctoConstants.PACK_FORMATS_LIST, Arrays.asList(OctoConstants.PACK_ZIP_FORMAT, OctoConstants.PACK_NUGET_FORMAT));
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        checkNotNull(context);
        checkNotNull(taskDefinition);

        super.populateContextForEdit(context, taskDefinition);

        context.put(OctoConstants.SERVER_URL, taskDefinition.getConfiguration().get(OctoConstants.SERVER_URL));
        context.put(OctoConstants.API_KEY, taskDefinition.getConfiguration().get(OctoConstants.API_KEY));
        context.put(OctoConstants.PACK_BASE_PATH_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_BASE_PATH_NAME));
        context.put(OctoConstants.PACK_FORMAT_NAME, StringUtils.defaultIfBlank(
                taskDefinition.getConfiguration().get(OctoConstants.PACK_FORMAT_NAME),
                OctoConstants.PACK_ZIP_FORMAT));
        context.put(OctoConstants.PACK_ID_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_ID_NAME));
        context.put(OctoConstants.PACK_VERSION_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_VERSION_NAME));
        context.put(OctoConstants.PACK_INCLUDE_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_INCLUDE_NAME));
        context.put(OctoConstants.PACK_OUT_FOLDER_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_OUT_FOLDER_NAME));
        context.put(OctoConstants.PACK_OVERWRITE_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_OVERWRITE_NAME));
        context.put(OctoConstants.VERBOSE_LOGGING, taskDefinition.getConfiguration().get(OctoConstants.VERBOSE_LOGGING));
        context.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, taskDefinition.getConfiguration().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        context.put(OctoConstants.OCTOPUS_CLI, taskDefinition.getConfiguration().get(OctoConstants.OCTOPUS_CLI));
        context.put(OctoConstants.UI_CONFIG_BEAN, this.uiConfigSupport);
        context.put(OctoConstants.PACK_FORMATS_LIST, Arrays.asList(OctoConstants.PACK_ZIP_FORMAT, OctoConstants.PACK_NUGET_FORMAT));
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

        final String apiKeyValue = params.getString(OctoConstants.API_KEY);
        if (StringUtils.isEmpty(apiKeyValue)) {
            errorCollection.addError(OctoConstants.API_KEY, textProvider.getText(OctoConstants.API_KEY_ERROR_KEY));
        }

        final String pushPattern = params.getString(OctoConstants.PACK_ID_NAME);
        if (StringUtils.isEmpty(pushPattern)) {
            errorCollection.addError(OctoConstants.PACK_ID_NAME, textProvider.getText(OctoConstants.PACK_ID_ERROR_KEY));
        }

        final String octopusCli = params.getString(OctoConstants.OCTOPUS_CLI);
        if (StringUtils.isEmpty(octopusCli)) {
            errorCollection.addError(OctoConstants.OCTOPUS_CLI, textProvider.getText(OctoConstants.OCTOPUS_CLI_ERROR_KEY));
        }

        final String packFormat = params.getString(OctoConstants.PACK_FORMAT_NAME);
        if (StringUtils.isEmpty(packFormat)) {
            errorCollection.addError(OctoConstants.PACK_FORMAT_NAME, textProvider.getText(OctoConstants.PACK_FORMAT_ERROR_KEY));
        }
    }
}
