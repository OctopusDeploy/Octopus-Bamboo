package com.octopus.bamboo.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * Condition that is satisfied when the active profile is not test
 */
public class NotIntegrationTestCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !Arrays.asList(context.getEnvironment().getActiveProfiles())
                .contains("test");
    }
}