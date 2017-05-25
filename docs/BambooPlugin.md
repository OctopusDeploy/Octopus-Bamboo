The Octopus Deploy add-on for Bamboo allows packages to be uploaded to an Octopus Deploy server, as well as creating, deploying and promoting releases to your [environments](https://octopus.com/docs/key-concepts/environments). The plugin does this by running the Octopus Deploy Command Line tool.

## Getting Started

The plugin relies on a local copy of the Octopus Deploy Command Line tool being available to the build agent. The command line tool can be downloaded from the [Octopus Deploy Download](https://octopus.com/docs/api-and-integration/octo.exe-command-line) page.

Note that while the command line tool package is largely self contained, some Linux distributions require additional libraries to be installed before .NET Core applications will run. These packages are documented at the [Get started with .NET Core](https://www.microsoft.com/net/core) website.

To verify that the command line tool can be run, execute it from a terminal. When run with no arguments, the `Octo` executable will display a list of available commands.

## Installing the Add-on

Following the instructions at [Installing add-ons](https://confluence.atlassian.com/display/UPM/Installing+add-ons) to install the Octopus Deploy Bamboo add-on.

TODO: Include some instructions about finding the Octopus Deploy add-on here (i.e. what do you search for).

## A Typical Workflow for Pushing Packages

There are 3 typical steps that are required to push a package to the Octopus Deploy:

1. Build the application with Bamboo.
2. Create an archive with a tool like tar or 7zip following the Octopus Deploy [versioning requirements](https://octopus.com/docs/packaging-applications/versioning-in-octopus-deploy).
3. Push the package to Octopus Deploy using the add-on.

Let's take a look at the steps that you could use to push a Java WAR file built with Maven to Octopus Deploy.

### Build the Application
We'll assume that there is already a Bamboo build plan in place that produces a WAR file using Maven. The [Bamboo Documentation](https://confluence.atlassian.com/bamboo/maven-289277038.html) describes how this is done.

### Create the Package
With the WAR file built, we need to add it to an archive that follows Octopus Deploy [versioning requirements](https://octopus.com/docs/packaging-applications/versioning-in-octopus-deploy). In this example we will stick to a simple Major.Minor.Patch semver
format.

Creating the archive is done with a [Bamboo Script task](https://confluence.atlassian.com/bamboo/script-289277046.html) that calls an archive tool to generate an appropriately named archive file. Octopus Deploy supports a number of package types. See [Supported Packages in Octopus Deploy](https://octopus.com/docs/packaging-applications/supported-packages) for more information.

For Bamboo build agents hosts on a Linux server, the script body will call `tar` to create the archive. Note that we first enter the `target` directory and then create the archive. This is required to ensure the resulting gzip file does not include a `target` directory.

```
cd target
tar -czf ../myapplication.0.0.${bamboo.buildNumber}.tar.gz *.war
```

For Bamboo build agents hosted on a Windows server, you may want to use a tool like [7Zip](http://www.7-zip.org/download.html) to build the archive, in which case the following command can be used. Note the leading `./` before the path to the war file is significant, as it removes the `target` directory from the resulting zip file. See [this discussion](https://stackoverflow.com/questions/10753667/compressing-only-files-using-7z-without-preserving-the-path) on StackOverflow for more information.

```
"C:\Program Files\7-Zip\7z.exe" a -tzip myapplication.0.0.%bamboo_buildNumber%.zip ./target/*.war
```

In both cases we have used the variable `bamboo.buildNumber` to generate a package whose filename includes the build number as the semver patch version e.g. `myapplication.0.0.23.tar.gz`. It is highly recommended that all packages and releases across Bamboo and Octopus Deploy reference a unique version tied to a specific build to make management and auditing easier.

### Push the Packages

Pushing the package to Octopus Deploy is done with the `Octopus Deploy: Push Packages` task. There are 4 fields that are required to be configured in this task.


#### Octopus CLI
The `Octopus CLI` field references a [Bamboo capability](https://confluence.atlassian.com/bamboo/capability-289277445.html) that defines the path to the Octopus Deploy Command Line tool.

Click the `Add new executable` link to specify the location of the command line tool. The `Executable label` can be anything you want, and the `Path` is the full path to the command line tool executable file.

![Add new executable](https://github.com/OctopusDeploy/Octopus-Bamboo/blob/master/docs/Executable.PNG?raw=true)

#### Octopus URL

The `Octopus URL` field defines the URL of the Octopus Deploy server that the package will be pushed to. This URL must include the scheme `http:\\` or `https:\\`, and also the port if it is not the default of `80` or `443`.

#### API key

The `API key` field defines the API key that is used to authenticate with the Octopus Deploy server. See [How to create an API key](https://octopus.com/docs/how-to/how-to-create-an-api-key) for more information.

#### Package paths

The `Package paths` field defines the [Ant paths](https://ant.apache.org/manual/dirtasks.html) that are used to match packages to be pushed to Octopus Deploy. The Ant path `**/*${bamboo.buildNumber}.tar.gz` mathes the gzip package created with `tar`, while the path `**/*${bamboo.buildNumber}.zip` matches the zip file created with `7z`.

Note that it is recommended that the package paths defined here are specific to the build. While the Ant path `**/*.tar.gz` or `**/*.zip` do match the packages, they also match any old packages that might have been created in previous builds and not cleaned up. This means these less specific paths can result in old packages being uploaded, which is usually not the desired result.

![Push Package](https://github.com/OctopusDeploy/Octopus-Bamboo/blob/master/docs/PushPackage.PNG?raw=true)
