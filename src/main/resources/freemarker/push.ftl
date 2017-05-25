[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='octopuscli'/][/#assign]
[@ww.select cssClass="builderSelectWidget" labelKey='octopus.cli.key' name='octopusCli'
list=uiConfigBean.getExecutableLabels('octopuscli') extraUtility=addExecutableLink required='true' /]
<div class="description">[@s.text name='octopus.cli.help' /]</div>
[@ww.textfield labelKey="octopus.serverUrl.key" name="serverUrl" required='true'/]
<div class="description">[@s.text name='octopus.serverUrl.help' /]</div>
[@ww.password labelKey="octopus.apiKey.key" name="apiKey" required='true' showPassword='true'/]
<div class="description">[@s.text name='octopus.apiKey.help' /]</div>
[@ww.textarea labelKey="octopus.pushPattern.key" name="pushPattern" required='true'/]
<div class="description">[@s.text name='octopus.pushPattern.help' /]</div>
[@ww.checkbox labelKey="octopus.force.key" name="force" required='false'/]
<div class="description">[@s.text name='octopus.force.help' /]</div>
[@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
[@ww.textfield labelKey="octopus.additionalArgs.key" name="additionalArgs" required='false'/]
<div class="description">[@s.text name='octopus.additionalArgs.help' /]</div>