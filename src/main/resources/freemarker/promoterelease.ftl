[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='octopuscli'/][/#assign]
[@ww.select cssClass="builderSelectWidget" labelKey='octopus.cli.key' name='octopusCli'
list=uiConfigBean.getExecutableLabels('octopuscli') extraUtility=addExecutableLink required='true' /]
<div class="description">[@s.text name='octopus.cli.help' /]</div>
[@ww.textfield labelKey="octopus.serverUrl.key" name="serverUrl" required='true'/]
<div class="description">[@s.text name='octopus.serverUrl.help' /]</div>
[@ww.password labelKey="octopus.apiKey.key" name="apiKey" required='true' showPassword='true'/]
<div class="description">[@s.text name='octopus.apiKey.help' /]</div>
[@ww.textfield labelKey="octopus.spaceName.key" name="spaceName" /]
<div class="description">[@s.text name='octopus.spaceName.help' /]</div>
[@ww.textfield labelKey="octopus.projectName.key" name="projectName" required='true'/]
<div class="description">[@s.text name='octopus.projectName.help' /]</div>
[@ww.textfield labelKey="octopus.promoteFrom.key" name="promoteFrom" required='true'/]
<div class="description">[@s.text name='octopus.promoteFrom.help' /]</div>
[@ww.textfield labelKey="octopus.promoteTo.key" name="promoteTo" required='true'/]
<div class="description">[@s.text name='octopus.promoteTo.help' /]</div>
[@ww.textfield labelKey="octopus.tenants.key" name="tenants" required='false'/]
<div class="description">[@s.text name='octopus.tenants.help' /]</div>
<div class="description">[@s.text name='octopus.tenants.help2' /]</div>
[@ww.textfield labelKey="octopus.tenantTags.key" name="tenantTags" required='false'/]
<div class="description">[@s.text name='octopus.tenantTags.help' /]</div>
<div class="description">[@s.text name='octopus.tenantTags.help2' /]</div>
[@ww.checkbox labelKey="octopus.showProgress.key" name="showProgress" required='false'/]
<div class="description">[@s.text name='octopus.showProgress.help' /]</div>
[@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
[@ww.textfield labelKey="octopus.additionalArgs.key" name="additionalArgs" required='false'/]
<div class="description">[@s.text name='octopus.additionalArgs.help' /]</div>