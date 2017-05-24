# Octopus-Bamboo
Octopus Deploy plugin for Bamboo

# SDK Installation
https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK

# Testing
Integration testing can be done in two modes: one with a live connection to a local instance of Octopus Deploy,
and a second where all API calls a mocked. The spring profile "test" is used to configure a mock API server
that will always successfully respond to an API call.

Run integration test against a mock Octopus Deploy REST API with the command:
```
atlas-integration-test --jvmargs "-Dspring.profiles.active=test"
```

To run the integration tests against a local copy of Octopus Deploy (http://localhost:8065)
with a valid API key use the following command:
```
atlas-integration-test --jvmargs "-DapiKey=OctopusDeployAPIKey"
```

To run the integration tests against a local copy of Octopus Deploy (http://localhost:8065)
without an API key (and therefor failing all interactions, which is a condition that
has been accounted for in the tests), use the following command:
```
atlas-integration-test
```

# Running Bamboo
Run the command `atlas-run` to start an instance of Bamboo with the plugin deployed.

The credentials are admin/admin.

# Error Codes
| Error Code | Description |
|------------|-------------|
| OCTOPUS-BAMBOO-INPUT-ERROR-0001 | No matching files could be found to push to Octopus Deploy. Check that the file pattern matches a file in the Bamboo working directory |
| OCTOPUS-BAMBOO-INPUT-ERROR-0002 | A required field was empty. |