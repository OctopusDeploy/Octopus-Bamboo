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
[@ww.textfield labelKey="octopus.packId.key" name="packId" required='true'/]
<div class="description">[@s.text name='octopus.packId.help' /]</div>
[@ww.textfield labelKey="octopus.packVersion.key" name="packVersion" required='false'/]
<div class="description">[@s.text name='octopus.packVersion.help' /]</div>
[@ww.checkbox labelKey="octopus.force.key" name="force" required='false'/]
<div class="description">[@s.text name='octopus.force.help' /]</div>
[@ww.select cssClass="builderSelectWidget" labelKey='octopus.commentParser.key' name='commentParser'
list="commentParsers" required='false' /]
<div class="description">[@s.text name='octopus.commentParser.help' /]</div>
[@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
