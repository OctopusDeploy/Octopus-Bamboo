package com.octopus.domain;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/PackageFromBuiltInFeeds
 */
public class Package {
    private String description;

    private String published;

    private String fileExtension;

    private String packageSizeBytes;

    private String title;

    private String summary;

    private String feedId;

    private String hash;

    private String releaseNotes;

    private String id;

    private String nuGetFeedId;

    private String nuGetPackageId;

    private String version;

    private String packageId;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(final String published) {
        this.published = published;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(final String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getPackageSizeBytes() {
        return packageSizeBytes;
    }

    public void setPackageSizeBytes(final String packageSizeBytes) {
        this.packageSizeBytes = packageSizeBytes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(final String feedId) {
        this.feedId = feedId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(final String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNuGetFeedId() {
        return nuGetFeedId;
    }

    public void setNuGetFeedId(final String nuGetFeedId) {
        this.nuGetFeedId = nuGetFeedId;
    }

    public String getNuGetPackageId() {
        return nuGetPackageId;
    }

    public void setNuGetPackageId(final String nuGetPackageId) {
        this.nuGetPackageId = nuGetPackageId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(final String packageId) {
        this.packageId = packageId;
    }
}
