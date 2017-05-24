package com.octopus.services;

import com.google.common.base.Optional;
import com.octopus.domain.Release;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Defines the actions that can be taken against the API
 */
public interface OctopusClient {
    /**
     * Pushes a package to the Octopus Deploy server
     *
     * @param logger           Logging service
     * @param buildLogger      Feign logging servce
     * @param host             Octopus Deploy host
     * @param apiKey           Octopus Deploy API key
     * @param pattern          Ant pattern for matching files to upload
     * @param workingDirectory Working directory containing files to upload
     * @param forceUpload      Force upload to Octopus Deploy
     * @param verboseLogging   Enable verbose logging from feign
     * @return true on success, false otherwise
     */
    boolean pushPackage(@NotNull LoggerService logger,
                        @NotNull feign.Logger buildLogger,
                        @NotNull String host,
                        @NotNull String apiKey,
                        @NotNull String pattern,
                        @NotNull File workingDirectory,
                        boolean forceUpload,
                        boolean verboseLogging);

    /**
     * Creates a release in Octopus Deploy
     *
     * @param logger         Logging service
     * @param buildLogger    Feign logging servce
     * @param host           Octopus Deploy host
     * @param apiKey         Octopus Deploy API key
     * @param projectName    The name of the project
     * @param channelName    The optional name of the channel
     * @param releaseVersion The release version
     * @param ignoreExisting True if this action is skipped because of an existing release
     * @param verboseLogging Enable verbose fiegn logging
     * @return release object on success, empty optional on failure
     */
    Optional<Release> createRelease(@NotNull LoggerService logger,
                                    @NotNull feign.Logger buildLogger,
                                    @NotNull String host,
                                    @NotNull String apiKey,
                                    @NotNull String projectName,
                                    String channelName,
                                    @NotNull String releaseVersion,
                                    boolean ignoreExisting,
                                    boolean verboseLogging);

    /**
     * Deploys a release in Octopus Deploy
     *
     * @param logger          Logging service
     * @param buildLogger     Feign logging servce
     * @param host            Octopus Deploy host
     * @param apiKey          Octopus Deploy API key
     * @param projectName     The project name
     * @param environmentName The environment name
     * @param releaseVersion  The release version
     * @param verboseLogging  Enables detailed logging in feign
     * @return true on success, false otherwise
     */
    boolean deployRelease(@NotNull LoggerService logger,
                          @NotNull feign.Logger buildLogger,
                          @NotNull String host,
                          @NotNull String apiKey,
                          @NotNull String projectName,
                          @NotNull String environmentName,
                          @NotNull String releaseVersion,
                          boolean verboseLogging);

    //void promoteRelease();
}
