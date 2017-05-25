# Octopus Deploy Bamboo Plugin

## SDK Installation
This plugin used the Atlassian SDK. You can find more information about how to install and run the SDK from
https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK

## Running Bamboo
Run the command `atlas-run` to start an instance of Bamboo with the plugin deployed.

The credentials are admin/admin.

## Error Codes
| Error Code | Description |
|------------|-------------|
| OCTOPUS-BAMBOO-INPUT-ERROR-0001 | No matching files could be found to push to Octopus Deploy. Check that the file pattern matches a file in the Bamboo working directory |
| OCTOPUS-BAMBOO-INPUT-ERROR-0002 | A required field was empty. |
| OCTOPUS-BAMBOO-INPUT-ERROR-0003 | The server capability that defines the path to the Octopus CLI has an incorrect path |