[#assign addExecutableLink][@ui.displayAddExecutableInline executableKey='octopuscli'/][/#assign]
[@ww.select cssClass="builderSelectWidget" labelKey='octopus.cli.key' name='octopusCli'
list=uiConfigBean.getExecutableLabels('octopuscli') extraUtility=addExecutableLink required='true' /]
<div class="description">[@s.text name='octopus.cli.help' /]</div>
[@ww.textfield labelKey="octopus.packId.key" name="packId" required='true'/]
<div class="description">[@s.text name='octopus.packId.help' /]</div>
[@ww.textfield labelKey="octopus.packVersion.key" name="packVersion" required='false'/]
<div class="description">[@s.text name='octopus.packVersion.help' /]</div>
[@ww.radio labelKey="octopus.packFormat.key" name="packFormat" required='true' list='formats'/]
<div class="description">[@s.text name='octopus.packFormat.help' /]</div>
[@ww.textfield labelKey="octopus.packBasePath.key" name="packBasePath" required='false'/]
<div class="description">[@s.text name='octopus.packBasePath.help' /]</div>
[@ww.textarea labelKey="octopus.packInclude.key" name="packInclude" required='false'/]
<div class="description">[@s.text name='octopus.packInclude.help' /]</div>
[@ww.textfield labelKey="octopus.packOutFolder.key" name="packOutFolder" required='false'/]
<div class="description">[@s.text name='octopus.packOutFolder.help' /]</div>
[@ww.checkbox labelKey="octopus.packOverwrite.key" name="packOverwrite" required='false'/]
<div class="description">[@s.text name='octopus.packOverwrite.help' /]</div>
[@ww.checkbox labelKey="octopus.verboseLogging.key" name="verboseLogging" required='false'/]
[@ww.textfield labelKey="octopus.additionalArgs.key" name="additionalArgs" required='false'/]
<div class="description">[@s.text name='octopus.additionalArgs.help' /]</div>