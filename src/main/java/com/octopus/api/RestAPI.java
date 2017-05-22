package com.octopus.api;

import com.octopus.domain.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

import java.io.File;
import java.util.List;

/**
 * A small subset of the Octopus Delpoy REST API that this plugin will
 * use to interact with an Octopus Deploy server.
 */
public interface RestAPI {
    /**
     * https://octopus.com/docs/packaging-applications/package-repositories/pushing-packages-to-the-built-in-repository
     *
     * @param replace Replace an existing package
     * @param file    The file to be uploaded
     */
    @RequestLine("POST /packages/raw?replace={replace}")
    @Headers("Content-Type: multipart/form-data")
    Response packagesRaw(@Param("replace") Boolean replace,
                         @Param("file") File file);

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Projects
     *
     * @return The details of the projects
     */
    @RequestLine("GET /projects/all")
    List<Project> getProjects();

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Channels
     *
     * @return The channels of the project
     */
    @RequestLine("GET /projects/{projectId}/channels")
    PagedChannels getProjectChannels(@Param("projectId") String projectId);

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Releases
     *
     * @param ignoreChannelRules This is undocumented?
     * @param release            Details of the new release
     * @return Details of the created release
     */
    @RequestLine("POST /releases?ignoreChannelRules={ignoreChannelRules}")
    @Headers("Content-Type: application/json")
    Release createRelease(@Param("ignoreChannelRules") Boolean ignoreChannelRules, Release release);

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/DeploymentProcesses
     *
     * @param deploymentProcessesId The deployment process ID
     * @return The details of the deployment process
     */
    @RequestLine("GET /deploymentprocesses/{deploymentProcessesId}")
    DeploymentProcess getDeploymentProcess(@Param("deploymentProcessesId") String deploymentProcessesId);

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/PackageFromBuiltInFeeds
     *
     * @return Pages results of package details
     */
    @RequestLine("GET /packages")
    PagedPackages getPackages();

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Releases
     * @return Pages results of release details
     */
    @RequestLine("GET /releases")
    PagedReleases getReleases();

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Environments
     *
     * @return All the environments
     */
    @RequestLine("GET /environments/all")
    List<Environment> getEnvironments();

    /**
     * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments
     *
     * @return The created deployment
     */
    @RequestLine("POST /deployments")
    Deployment createDeployment(Deployment deployment);
}
