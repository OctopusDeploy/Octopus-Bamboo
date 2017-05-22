package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.google.common.base.Optional;
import com.octopus.api.RestAPI;
import com.octopus.domain.*;
import com.octopus.services.FeignService;
import com.octopus.services.LookupService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final List<Release> releases = restAPI.getReleases().getItems();

        CollectionUtils.filter(releases, new Predicate<Release>() {
            @Override
            public boolean evaluate(final Release item) {
                return releaseVersion.equals(item.getVersion())
                        && project.getId().equals(item.getProjectId());
            }
        });

        return Optional.fromNullable(releases.isEmpty() ? null : releases.get(0));
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

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final PagedChannels channels = restAPI.getProjectChannels(project.getId());

        CollectionUtils.filter(channels.getItems(), new Predicate<Channel>() {
            @Override
            public boolean evaluate(final Channel item) {
                return channelName.equals(item.getName());
            }
        });

        return Optional.fromNullable(channels.getItems().isEmpty() ? null : channels.getItems().get(0).getId());
    }

    public Optional<String> getDefaultChannel(@NotNull final TaskContext taskContext,
                                              @NotNull final Project project) {
        checkNotNull(taskContext);
        checkNotNull(project);

        final RestAPI restAPI = feignService.createClient(taskContext, true);
        final PagedChannels channels = restAPI.getProjectChannels(project.getId());

        CollectionUtils.filter(channels.getItems(), new Predicate<Channel>() {
            @Override
            public boolean evaluate(final Channel item) {
                return item.getIsDefault();
            }
        });

        return Optional.fromNullable(channels.getItems().isEmpty() ? null : channels.getItems().get(0).getId());
    }
}
