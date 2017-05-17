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
     * The configuration key for the Octopus Server URL
     */
    public static final String SERVER_URL = "serverUrl";
    /**
     * The configuration key for the Octopus Server API key
     */
    public static final String API_KEY = "apiKey";
    /**
     * The configuration key for the ant pattern used to find files to upload
     */
    public static final String PUSH_PATTERN = "pushPattern";
    /**
     * The configuration key for the force upload option
     */
    public static final String FORCE = "force";

    private OctoConstants() {

    }
}
