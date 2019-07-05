package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.struts.TextProvider;
import com.octopus.bamboo.plugins.task.OverwriteMode;
import com.octopus.bamboo.plugins.task.OverwriteModes;
import com.octopus.constants.OctoConstants;
import com.octopus.services.impl.BaseConfigurator;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task configuration, where we save and validate the settings applied
 * to the plugin.
 */
@Component
@ExportAsService({com.atlassian.bamboo.task.TaskConfigurator.class})
@Named("pushTaskConfigurator")
public class PushTaskConfigurator extends BaseConfigurator {
    @ComponentImport
    private final TextProvider textProvider;

    @Inject
    public PushTaskConfigurator(@NotNull final TextProvider textProvider,
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
        config.put(OctoConstants.PUSH_PATTERN, params.getString(OctoConstants.PUSH_PATTERN));
        config.put(OctoConstants.FORCE, params.getString(OctoConstants.FORCE));
        config.put(OctoConstants.OVERWRITE_MODE, params.getString(OctoConstants.OVERWRITE_MODE));
        config.put(OctoConstants.VERBOSE_LOGGING, params.getString(OctoConstants.VERBOSE_LOGGING));
        config.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, params.getString(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        config.put(OctoConstants.OCTOPUS_CLI, params.getString(OctoConstants.OCTOPUS_CLI));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        context.put(OctoConstants.UI_CONFIG_BEAN, this.getUIConfigSupport());
        context.put(OctoConstants.OVERWRITE_MODES, OverwriteModes.getOverwriteModes());
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context,
                                       @NotNull final TaskDefinition taskDefinition) {
        checkNotNull(context);
        checkNotNull(taskDefinition);

        super.populateContextForEdit(context, taskDefinition);

        Map<String, String> configuration = taskDefinition.getConfiguration();

        context.put(OctoConstants.SERVER_URL, configuration.get(OctoConstants.SERVER_URL));
        context.put(OctoConstants.API_KEY, configuration.get(OctoConstants.API_KEY));
        context.put(OctoConstants.SPACE_NAME, configuration.get(OctoConstants.SPACE_NAME));
        context.put(OctoConstants.PUSH_PATTERN, configuration.get(OctoConstants.PUSH_PATTERN));

        String overwriteModeString = configuration.get(OctoConstants.OVERWRITE_MODE);

        if (StringUtils.isEmpty(overwriteModeString)) {
            final String forceUpload = configuration.get(OctoConstants.FORCE);
            final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));

            if (forceUploadBoolean) {
                overwriteModeString = OverwriteMode.OverwriteExisting.name();
            }
            else {
                overwriteModeString = OverwriteMode.FailIfExists.name();
            }
        }

        context.put(OctoConstants.OVERWRITE_MODE, overwriteModeString);
        context.put(OctoConstants.VERBOSE_LOGGING, configuration.get(OctoConstants.VERBOSE_LOGGING));
        context.put(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME, configuration.get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME));
        context.put(OctoConstants.OCTOPUS_CLI, configuration.get(OctoConstants.OCTOPUS_CLI));
        context.put(OctoConstants.UI_CONFIG_BEAN, this.getUIConfigSupport());
        context.put(OctoConstants.OVERWRITE_MODES, OverwriteModes.getOverwriteModes());
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

        final String pushPattern = params.getString(OctoConstants.PUSH_PATTERN);
        if (StringUtils.isEmpty(pushPattern)) {
            errorCollection.addError(OctoConstants.PUSH_PATTERN, textProvider.getText(OctoConstants.PUSH_PATTERN_ERROR_KEY));
        }

        final String octopusCli = params.getString(OctoConstants.OCTOPUS_CLI);
        if (StringUtils.isEmpty(octopusCli)) {
            errorCollection.addError(OctoConstants.OCTOPUS_CLI, textProvider.getText(OctoConstants.OCTOPUS_CLI_ERROR_KEY));
        }
    }
}
