package com.octopus.services.impl;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An optional service that may or may not exist. This class is completely generic
 * and can be overridden to be used for any optional service.
 * <p>
 * See https://bitbucket.org/bwoskow/optional-service-sample-plugin
 */
public abstract class OptionalService<T> implements InitializingBean, DisposableBean {
    private final Class<T> type;
    private final ServiceTracker tracker;

    public OptionalService(final BundleContext bundleContext, final Class<T> type) {
        this.type = checkNotNull(type, "type");
        this.tracker = new ServiceTracker(bundleContext, type.getName(), null);
    }

    /**
     * Returns the service (of type {@code T}) if it exists, or {@code null} if not
     *
     * @return the service (of type {@code T}) if it exists, or {@code null} if not
     */
    protected final T getService() {
        return type.cast(tracker.getService());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tracker.open();
    }

    @Override
    public final void destroy() {
        tracker.close();
    }
}
