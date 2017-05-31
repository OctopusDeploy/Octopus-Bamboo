package com.octopus.constants;

/**
 * Constants used by the Bamboo plugin
 */
public class OctoConstants {
    public static final String PUSH_COMMAND = "push";

    /**
     * The address of the local test Octopus Deploy instance
     */
    public static final String LOCAL_OCTOPUS_INSTANCE = "http://localhost:8065";
    /**
     * The name of the test profile
     */
    public static final String TEST_PROFILE = "test";

    /**
     * The configuration key for the Octopus CLI instance
     */
    public static final String OCTOPUS_CLI = "octopusCli";
    /**
     * The configuration key for the Octopus Server URL
     */
    public static final String SERVER_URL = "serverUrl";
    /**
     * The configuration key for the Octopus Server API key
     */
    public static final String API_KEY = "apiKey";
    /**
     * The configuration key for the Feign logging level
     */
    public static final String VERBOSE_LOGGING = "verboseLogging";
    /**
     * The configuration key for the ant pattern used to find files to upload
     */
    public static final String PUSH_PATTERN = "pushPattern";
    /**
     * The configuration key for the force upload option
     */
    public static final String FORCE = "force";
    /**
     * The configuration key for the release version option
     */
    public static final String RELEASE_VERSION = "releaseVersion";
    /**
     * The configuration key for the project name option
     */
    public static final String PROJECT_NAME = "projectName";
    /**
     * The configuration key for the channel name option
     */
    public static final String CHANNEL_NAME = "channelName";
    /**
     * The configuration key for the environment name option
     */
    public static final String ENVIRONMENT_NAME = "environmentName";
    /**
     * The configuration key for the ignore existing release option
     */
    public static final String IGNORE_EXISTING_RELEASE_NAME = "ignoreExisting";
    /**
     * The configuration key for the promote from option
     */
    public static final String PROMOTE_FROM_NAME = "promoteFrom";
    /**
     * The configuration key for the additional command line arguments
     */
    public static final String ADDITIONAL_COMMAND_LINE_ARGS_NAME = "additionalArgs";
    /**
     * The configuration key for the promote to option
     */
    public static final String PROMOTE_TO_NAME = "promoteTo";
    /**
     * The configuration key for the show progress option
     */
    public static final String SHOW_DEPLOYMENT_PROGRESS = "showProgress";
    /**
     * The configuration key for the tenants option
     */
    public static final String TENANTS_NAME = "tenants";
    /**
     * The configuration key for the tenant tags option
     */
    public static final String TENANT_TAGS_NAME = "tenantTags";
    /**
     * The configuration key for the pack id
     */
    public static final String PACK_ID_NAME = "packId";
    /**
     * The configuration key for the pack format
     */
    public static final String PACK_FORMAT_NAME = "packFormat";
    /**
     * The configuration key for the pack format
     */
    public static final String PACK_VERSION_NAME = "packVersion";
    /**
     * The configuration key for the pack out folder
     */
    public static final String PACK_OUT_FOLDER_NAME = "packOutFolder";
    /**
     * The configuration key for the pack base folder
     */
    public static final String PACK_BASE_PATH_NAME = "packBasePath";
    /**
     * The configuration key for the pack include pattern
     */
    public static final String PACK_INCLUDE_NAME = "packInclude";
    /**
     * The configuration key for the pack overwrite option
     */
    public static final String PACK_OVERWRITE_NAME = "packOverwrite";
    /**
     * The name of the capability that links to the octopus cli
     */
    public static final String OCTOPUS_CLI_CAPABILITY = "system.builder.octopuscli";
    /**
     * UIConfigSupport bean used to display lists of executables
     */
    public static final String UI_CONFIG_BEAN = "uiConfigBean";
    /**
     * Collection of pack formats
     */
    public static final String PACK_FORMATS_LIST = "formats";
    /**
     * ZIP pack format
     */
    public static final String PACK_ZIP_FORMAT = "zip";
    /**
     * NUGET pack format
     */
    public static final String PACK_NUGET_FORMAT = "nuget";

    public static final String API_KEY_ERROR_KEY = "octopus.apiKey.error";
    public static final String SERVER_URL_ERROR_KEY = "octopus.serverUrl.error";
    public static final String PUSH_PATTERN_ERROR_KEY = "octopus.pushPattern.error";
    public static final String OCTOPUS_CLI_ERROR_KEY = "octopus.cli.error";
    public static final String PROJECT_NAME_ERROR_KEY = "octopus.projectName.error";
    public static final String RELEASE_VERSION_ERROR_KEY = "octopus.releaseVersion.error";
    public static final String ENVIRONMENT_NAME_ERROR_KEY = "octopus.environmentName.error";
    public static final String PACK_ID_ERROR_KEY = "octopus.packId.error";
    public static final String PACK_FORMAT_ERROR_KEY = "octopus.packFormat.error";

    public static final String PROMOTE_FROM_NAME_ERROR_KEY = "octopus.promoteFrom.error";
    public static final String PROMOTE_TO_NAME_ERROR_KEY = "octopus.promoteTo.error";

    private OctoConstants() {

    }
}
