package com.octopus.bamboo.plugins.task.pack;

import java.util.List;

public class OctopusMetadata {
    public String BuildEnvironment;
    public List<WorkItem> WorkItems;

    public OctopusMetadata() {
        BuildEnvironment = "Bamboo";
    }
}