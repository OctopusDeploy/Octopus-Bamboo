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

# Error Codes
| Error Code | Description |
|------------|-------------|
| OCTOPUS-BAMBOO-ERROR-0001 | An interaction with the Octopus Deploy API failed with a status code of 401, which usually indicates an issue with the API key |
| OCTOPUS-BAMBOO-ERROR-0002 | An interaction with the Octopus Deploy API failed with a status code outside of the 200 range, which indicates an error with the request |
| OCTOPUS-BAMBOO-ERROR-0003 | No matching files could be found to push to Octopus Deploy. Check that the file pattern matches a file in the Bamboo working directory |
| OCTOPUS-BAMBOO-ERROR-0004 | The API key setting was not found when running the package push |
| OCTOPUS-BAMBOO-ERROR-0005 | A file that already exists was attempted to be pushed again without the force option enabled |
| OCTOPUS-BAMBOO-ERROR-0006 | The list of packages was not found while attempting to match a step package with a version |
| OCTOPUS-BAMBOO-ERROR-0007 | The release could not be deployed |
| OCTOPUS-BAMBOO-INPUT-ERROR-0001 | The project name could not be found |
| OCTOPUS-BAMBOO-INPUT-ERROR-0002 | The channel name could not be found |
| OCTOPUS-BAMBOO-INPUT-ERROR-0003 | The default channel could not be found |
| OCTOPUS-BAMBOO-INPUT-ERROR-0004 | The release could not be created |
| OCTOPUS-BAMBOO-INPUT-ERROR-0005 | The environment name could not be found |
| OCTOPUS-BAMBOO-INPUT-ERROR-0006 | The release version could not be found |