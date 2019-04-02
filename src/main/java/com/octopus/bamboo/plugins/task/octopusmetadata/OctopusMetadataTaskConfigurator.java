package com.octopus.bamboo.plugins.task.octopusmetadata;

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

@Component
@ExportAsService({com.atlassian.bamboo.task.TaskConfigurator.class})
@Named("octopusMetadataTaskConfigurator")
public class OctopusMetadataTaskConfigurator extends BaseConfigurator {
    @ComponentImport
    private final TextProvider textProvider;

    @Inject
    public OctopusMetadataTaskConfigurator(@NotNull final TextProvider textProvider,
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
        config.put(OctoConstants.PACK_ID_NAME, params.getString(OctoConstants.PACK_ID_NAME));
        config.put(OctoConstants.PACK_VERSION_NAME, params.getString(OctoConstants.PACK_VERSION_NAME));
        config.put(OctoConstants.COMMENT_PARSER_NAME, params.getString(OctoConstants.COMMENT_PARSER_NAME));
        config.put(OctoConstants.FORCE, params.getString(OctoConstants.FORCE));
        config.put(OctoConstants.VERBOSE_LOGGING, params.getString(OctoConstants.VERBOSE_LOGGING));
        config.put(OctoConstants.OCTOPUS_CLI, params.getString(OctoConstants.OCTOPUS_CLI));
        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        context.put(OctoConstants.COMMENT_PARSERS_LIST, CommentParserFactory.getParsers());
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
        context.put(OctoConstants.PACK_ID_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_ID_NAME));
        context.put(OctoConstants.PACK_VERSION_NAME, taskDefinition.getConfiguration().get(OctoConstants.PACK_VERSION_NAME));
        context.put(OctoConstants.COMMENT_PARSER_NAME, taskDefinition.getConfiguration().get(OctoConstants.COMMENT_PARSER_NAME));
        context.put(OctoConstants.FORCE, taskDefinition.getConfiguration().get(OctoConstants.FORCE));
        context.put(OctoConstants.VERBOSE_LOGGING, taskDefinition.getConfiguration().get(OctoConstants.VERBOSE_LOGGING));
        context.put(OctoConstants.OCTOPUS_CLI, taskDefinition.getConfiguration().get(OctoConstants.OCTOPUS_CLI));
        context.put(OctoConstants.UI_CONFIG_BEAN, this.getUIConfigSupport());
        context.put(OctoConstants.COMMENT_PARSERS_LIST, CommentParserFactory.getParsers());
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

        final String octopusCli = params.getString(OctoConstants.OCTOPUS_CLI);
        if (StringUtils.isEmpty(octopusCli)) {
            errorCollection.addError(OctoConstants.OCTOPUS_CLI, textProvider.getText(OctoConstants.OCTOPUS_CLI_ERROR_KEY));
        }
    }
}
