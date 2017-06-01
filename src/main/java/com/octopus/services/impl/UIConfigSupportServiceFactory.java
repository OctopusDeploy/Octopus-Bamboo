package com.octopus.services.impl;

import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import org.osgi.framework.BundleContext;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of OptionalService for the UIConfigSupport class.
 * <p>
 * See https://bitbucket.org/bwoskow/optional-service-sample-plugin for more details
 */
public class UIConfigSupportServiceFactory extends OptionalService<UIConfigSupport> {
    public UIConfigSupportServiceFactory(@NotNull final BundleContext bundleContext) {
        super(checkNotNull(bundleContext), UIConfigSupport.class);
    }
}
