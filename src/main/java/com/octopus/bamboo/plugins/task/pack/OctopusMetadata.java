package com.octopus.bamboo.plugins.task.pack;

import java.util.List;

public class OctopusMetadata {
    public String BuildServerType;
    public List<WorkItem> WorkItems;

    public OctopusMetadata() {
        BuildServerType = "Bamboo";
    }
}