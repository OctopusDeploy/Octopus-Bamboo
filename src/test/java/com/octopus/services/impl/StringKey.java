package com.octopus.services.impl;

import com.atlassian.bamboo.Key;
import org.jetbrains.annotations.NotNull;

/**
 * A dummy implementation of the key interface
 */
public class StringKey implements Key {
    @NotNull
    @Override
    public String getKey() {
        return "key";
    }
}
