package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.github.zafarkhaja.semver.Version;
import com.google.common.base.Optional;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.domain.*;
import com.octopus.domain.Package;
import com.octopus.services.FeignService;
import com.octopus.services.LookupService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * Implementation of the lookup service
 */
@Component
public class LookupServiceImpl implements LookupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LookupServiceImpl.class);
    private final FeignService feignService;

    @Inject
    public LookupServiceImpl(final FeignService feignService) {
        this.feignService = feignService;
    }

    public Optional<Project> getProject(@NotNull final TaskContext taskContext, @NotNull final String projectName) {
        checkNotNull(taskContext);
        checkArgument(StringUtils.isNotBlank(projectName));

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final List<Project> projects = restAPI.getProjects();

        CollectionUtils.filter(projects, new Predicate<Project>() {
            @Override
            public boolean evaluate(final Project item) {
                return projectName.equals(item.getName());
            }
        });

        return Optional.fromNullable(projects.isEmpty() ? null : projects.get(0));
    }

    @Override
    public Optional<Release> getRelease(@NotNull final TaskContext taskContext,
                                        @NotNull final String releaseVersion,
                                        @NotNull final Project project) {
        checkNotNull(taskContext);
        checkNotNull(project);
        checkArgument(StringUtils.isNotBlank(releaseVersion));

        final List<Release> matching = new ArrayList<>();

        final RestAPI restAPI = feignService.createClient(taskContext, true);

        int count = 0;

        while (true) {
            final PagedResult<Release> releases = restAPI.getReleases(count);
            count += releases.getItems().size();
            matching.addAll(releases.getItems());
            CollectionUtils.filter(matching, new Predicate<Release>() {
                @Override
                public boolean evaluate(final Release item) {
                    return releaseVersion.equals(item.getVersion())
                            && project.getId().equals(item.getProjectId());
                }
            });

            if (count >= releases.getTotalResults()) {
                break;
            }
        }

        return Optional.fromNullable(matching.isEmpty() ? null : matching.get(0));
    }

    @Override
    public Optional<Environment> getEnvironment(@NotNull final TaskContext taskContext, @NotNull final String environmentName) {
        checkNotNull(taskContext);
        checkArgument(StringUtils.isNotBlank(environmentName));

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final List<Environment> environments = restAPI.getEnvironments();

        CollectionUtils.filter(environments, new Predicate<Environment>() {
            @Override
            public boolean evaluate(final Environment item) {
                return environmentName.equals(item.getName());
            }
        });

        return Optional.fromNullable(environments.isEmpty() ? null : environments.get(0));
    }

    public Optional<String> getChannel(@NotNull final TaskContext taskContext,
                                       @NotNull final Project project,
                                       @NotNull final String channelName) {
        checkNotNull(taskContext);
        checkNotNull(project);
        checkArgument(StringUtils.isNotBlank(channelName));

        final List<Channel> matching = new ArrayList<>();

        final RestAPI restAPI = feignService.createClient(taskContext, true);

        int count = 0;

        while (true) {
            final PagedResult<Channel> channels = restAPI.getProjectChannels(project.getId(), count);
            count += channels.getItems().size();
            matching.addAll(channels.getItems());
            CollectionUtils.filter(matching, new Predicate<Channel>() {
                @Override
                public boolean evaluate(final Channel item) {
                    return channelName.equals(item.getName());
                }
            });

            if (count >= channels.getTotalResults()) {
                break;
            }
        }

        return Optional.fromNullable(matching.isEmpty() ? null : matching.get(0).getId());
    }

    public Optional<String> getDefaultChannel(@NotNull final TaskContext taskContext,
                                              @NotNull final Project project) {
        checkNotNull(taskContext);
        checkNotNull(project);

        final List<Channel> matching = new ArrayList<>();

        final RestAPI restAPI = feignService.createClient(taskContext, true);

        int count = 0;
        while (true) {
            final PagedResult<Channel> channels = restAPI.getProjectChannels(project.getId(), count);
            count += channels.getItems().size();
            matching.addAll(channels.getItems());
            CollectionUtils.filter(matching, new Predicate<Channel>() {
                @Override
                public boolean evaluate(final Channel item) {
                    return item.getIsDefault();
                }
            });

            if (count >= channels.getTotalResults()) {
                break;
            }
        }

        return Optional.fromNullable(matching.isEmpty() ? null : matching.get(0).getId());
    }

    @Override
    public void populateSelectedPackages(@NotNull final TaskContext taskContext,
                                         @NotNull final Release release,
                                         @NotNull final Project project) {
        checkNotNull(taskContext);
        checkNotNull(release);
        checkNotNull(project);

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final DeploymentProcess deploymentProcess = restAPI.getDeploymentProcess(project.getDeploymentProcessId());

        int count = 0;
        while (true) {
            final PagedResult<Package> pagedPackages = restAPI.getPackages(count);
            count += pagedPackages.getItems().size();

            if (deploymentProcess.getSteps() != null) {
                for (final Step step : deploymentProcess.getSteps()) {
                    if (step.getActions() != null) {
                        for (final Action action : step.getActions()) {
                            if (action.getProperties() != null && action.getProperties().containsKey(OctoConstants.PACKAGE_ID_PROP_KEY)) {
                                final String packageId = action.getProperties().get(OctoConstants.PACKAGE_ID_PROP_KEY);

                                checkState(pagedPackages.getItems() != null, "OCTOPUS-BAMBOO-ERROR-0006: Failed to find a list of packages.");

                                /*
                                    Get packages that have the matching id
                                 */
                                final List<Package> matchingPackages = ListUtils.select(pagedPackages.getItems(), new Predicate<Package>() {
                                    @Override
                                    public boolean evaluate(final Package item) {
                                        return packageId.equals(item.getPackageId());
                                    }
                                });

                                /*
                                    Sort the list by semver versions
                                 */
                                matchingPackages.sort(new Comparator<Package>() {
                                    @Override
                                    public int compare(final Package o1, final Package o2) {
                                        final Version version1 = Version.valueOf(o1.getVersion());
                                        final Version version2 = Version.valueOf(o2.getVersion());

                                        return version1.compareTo(version2);
                                    }
                                });

                                /*
                                    Set the highest package version
                                 */
                                if (!matchingPackages.isEmpty()) {
                                    final SelectedPackages selectedPackages = new SelectedPackages();
                                    selectedPackages.setStepName(step.getName());
                                    selectedPackages.setVersion(matchingPackages.get(0).getVersion());
                                    release.getSelectedPackages().add(selectedPackages);
                                }
                            }
                        }
                    }
                }
            }

            if (count >= pagedPackages.getTotalResults()) {
                break;
            }
        }

    }
}
