# Octopus-Bamboo
Octopus Deploy plugin for Bamboo

# SDK Installation
https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK

# Testing
Run integration test against a mock Octopus Deploy REST API with the command:
```
atlas-integration-test --jvmargs "-Dspring.profiles.active=test"
```

To run the integration tests against a local copy of Octopus Deploy (http://localhost:8065), 
use the following command:
```
atlas-integration-test --jvmargs "-DapiKey=OctopusDeployAPIKey"
```

To run the integration tests against a local copy of Octopus Deploy (http://localhost:8065)
without an API key (and therefor failing all interactions, which is a condition that
has been accounted for in the tests), use the following command:
```
atlas-integration-test
```

# Error Codes
OCTOPUS-BAMBOO-ERROR-0001   An interaction with the Octopus Deploy API failed with a status code of 401, which usually indicates an issue with the API key
OCTOPUS-BAMBOO-ERROR-0002   An interaction with the Octopus Deploy API failed with a status code outside of the 200 range, which indicates an error with the request
OCTOPUS-BAMBOO-ERROR-0003   No matching files could be found to push to Octopus Deploy. Check that the file pattern matches a file in the Bamboo working directory
OCTOPUS-BAMBOO-ERROR-0004   The API key setting was not found when running the package push
OCTOPUS-BAMBOO-ERROR-0005   A file that already exists was attempted to be pushed again without the force option enabled

# Test Plan
Test with invalid pattern - must see OCTOPUS-BAMBOO-ERROR-0003
Test with invalid API key - must see OCTOPUS-BAMBOO-ERROR-0001
Disable force and rerun build - must see OCTOPUS-BAMBOO-ERROR-0005