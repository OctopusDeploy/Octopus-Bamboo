package com.octopus.constants;

/**
 * Constants used by the Bamboo plugin
 */
public class OctoConstants {
    /**
     * The address of the local test Octopus Deploy instance
     */
    public static final String LOCAL_OCTOPUS_INSTANCE = "http://localhost:8065";
    /**
     * The name of the test profile
     */
    public static final String TEST_PROFILE = "test";
    /**
     * The header that holds the API key for the Octopus Deploy API
     */
    public static final String X_OCTOPUS_APIKEY = "X-Octopus-ApiKey";

    /**
     * The property key that defines the package id
     */
    public static final String PACKAGE_ID_PROP_KEY = "Octopus.Action.Package.PackageId";

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
    public static final String DETAILED_LOGGING = "detailedLogging";
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
     * The start of the http response range that indicates success
     */
    public static final int START_HTTP_OK_RANGE = 200;
    /**
     * The end of the http response range that indicates success
     */
    public static final int END_HTTP_OK_RANGE = 299;

    private OctoConstants() {

    }
}
