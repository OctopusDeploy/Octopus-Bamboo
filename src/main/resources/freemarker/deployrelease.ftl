<style type="text/css" xmlns="http://www.w3.org/1999/html">
  h3, h3:first-child {
    margin-top: 20px;
  }
</style>

[@ui.bambooSection title='Octopus CLI']
    [#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='octopuscli'/][/#assign]
    [@ww.select cssClass="builderSelectWidget" labelKey='octopus.cli.key' name='octopusCli'
        list=uiConfigBean.getExecutableLabels('octopuscli') extraUtility=addExecutableLink required='true' /]
    <div class="description">[@s.text name='octopus.cli.help' /]</div>
[/@ui.bambooSection]

[@ui.bambooSection title='Octopus connection']
    [@ww.textfield labelKey="octopus.serverUrl.key" name="serverUrl" required='true'/]
    <div class="description">[@s.text name='octopus.serverUrl.help' /]</div>
    [@ww.password labelKey="octopus.apiKey.key" name="apiKey" required='true' showPassword='true'/]
    <div class="description">[@s.text name='octopus.apiKey.help' /]</div>
    [@ww.textfield labelKey="octopus.spaceName.key" name="spaceName" /]
    <div class="description">[@s.text name='octopus.spaceName.help' /]</div>
[/@ui.bambooSection]

[@ui.bambooSection title='Deployment']
    [@ww.textfield labelKey="octopus.projectName.key" name="projectName" required='true'/]
    <div class="description">[@s.text name='octopus.projectName.help' /]</div>
    [@ww.textfield labelKey="octopus.environmentName.key" name="environmentName" required='true'/]
    <div class="description">[@s.text name='octopus.environmentNameMandatory.help' /]</div>
    [@ww.textfield labelKey="octopus.releaseVersion.key" name="releaseVersion" required='true'/]
    <div class="description">[@s.text name='octopus.deployReleaseVersion.help' /]</div>
    [@ww.textfield labelKey="octopus.tenants.key" name="tenants" required='false'/]
    <div class="description">[@s.text name='octopus.tenants.help' /]</div>
    <div class="description">[@s.text name='octopus.tenants.help2' /]</div>
    [@ww.textfield labelKey="octopus.tenantTags.key" name="tenantTags" required='false'/]
    <div class="description">[@s.text name='octopus.tenantTags.help' /]</div>
    <div class="description">[@s.text name='octopus.tenantTags.help2' /]</div>

    [@ui.bambooSection title='Progress']
        [@ww.checkbox labelKey="octopus.showProgress.key" name="showProgress" required='false'/]
        <div class="description">[@s.text name='octopus.showProgress.help' /]</div>

        [@ui.bambooSection dependsOn="showProgress" showOn="true"]
            [@ww.textfield labelKey="octopus.deploymentTimeout.key" name="deploymentTimeout" required='false'/]
            <div class="description">[@s.text name='octopus.deploymentTimeout.help' /]</div>
            [@ww.checkbox labelKey="octopus.cancelOnTimeout.key" name="cancelOnTimeout" required='false'/]
            <div class="description">[@s.text name='octopus.cancelOnTimeout.help' /]</div>
        [/@ui.bambooSection]
    [/@ui.bambooSection]
[/@ui.bambooSection]

[@ui.bambooSection title='Advanced']
    [@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
    [@ww.textfield labelKey="octopus.additionalArgs.key" name="additionalArgs" required='false' class="long-field"/]
    <div class="description">[@s.text name='octopus.additionalArgs.help' /]</div>
[/@ui.bambooSection]
