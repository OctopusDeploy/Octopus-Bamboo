package com.octopus.services.impl;

import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The base class for configurators. It takes care of getting an instance of UIConfigSupport
 * if available (which it is not when the add-on is run as in a remote agent).
 * <p>
 * See https://bitbucket.org/bwoskow/optional-service-sample-plugin for more details
 */
abstract public class BaseConfigurator extends AbstractTaskConfigurator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConfigurator.class);

    private final ApplicationContext applicationContext;
    private UIConfigSupport uiConfigSupport;

    public BaseConfigurator(@NotNull final ApplicationContext applicationContext) {
        this.applicationContext = checkNotNull(applicationContext, "applicationContext");
    }

    public synchronized UIConfigSupport getUIConfigSupport() {
        if (uiConfigSupport == null) {
            initUIConfigSupport();
        }
        return uiConfigSupport;
    }

    private void initUIConfigSupport() {
        try {
            Class<?> fooServiceFactoryClass = getUIConfigSupportServiceFactoryClass();
            if (fooServiceFactoryClass != null) {
                uiConfigSupport = ((UIConfigSupportServiceFactory) applicationContext
                        .getAutowireCapableBeanFactory()
                        .createBean(fooServiceFactoryClass,
                                AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR,
                                false)).getService();
            }
        } catch (final Exception ex) {
            LOGGER.debug("Could not create UIConfigSupport. This is expected when running on a remote agent.", ex);
        }
    }

    /**
     * If the necessary GreenHopper class is found on the classpath, GreenHopper exists.
     * In that case return the {@link UIConfigSupportServiceFactory} class, otherwise return null.
     */
    private Class<?> getUIConfigSupportServiceFactoryClass() {
        try {
            // Check to see if the class we depend on (from GreenHopper) is available
            getClass().getClassLoader().loadClass("com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport");
            // If we get to this point in the code, GreenHopper is available. Let's load the service factory
            return getClass().getClassLoader().loadClass("com.octopus.services.impl.UIConfigSupportServiceFactory");
        } catch (Exception e) {
            LOGGER.info("The necessary UIConfigSupport class is unavailable.");
            return null;
        }
    }
}
