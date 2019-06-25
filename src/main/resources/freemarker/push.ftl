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

[@ui.bambooSection title='Package']
    [@ww.textarea labelKey="octopus.pushPattern.key" name="pushPattern" required='true' class="long-field"/]
    <div class="description">[@s.text name='octopus.pushPattern.help' /]</div>
    [@ww.checkbox labelKey="octopus.force.key" name="force" required='false'/]
    <div class="description">[@s.text name='octopus.force.help' /]</div>
[/@ui.bambooSection]

[@ui.bambooSection title='Advanced']
    [@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
    [@ww.textfield labelKey="octopus.additionalArgs.key" name="additionalArgs" required='false' class="long-field"/]
    <div class="description">[@s.text name='octopus.additionalArgs.help' /]</div>
[/@ui.bambooSection]
