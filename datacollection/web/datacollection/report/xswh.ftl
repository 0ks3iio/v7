<@dcm.reportData  reportCode=_reportCode dataId=_dataId>
<p />
<p align="center">
<@dcm.dcObjVal type="unit" id="{unitId}" columnId="unitName" /> 
<@dcm.dcObjVal type="student" id=_dataId columnId="boorish" />信息维护
</p><p />
<@dcm.saveButton />
<@dcm.listButton reportCode=_reportCode templatePath=_templatePath/>
<table align="center">
	<tr height="30">
	<@dcm.columnOne label="姓名" nullable=false columnId="student_name" />
	<@dcm.columnOne label="性别" columnId="sex" type="select" mcodeId="DM-XB" />
	<@dcm.columnOne label="身份证号" columnId="identity_card" type="input" />
	</tr>
	<tr height="30">
	<@dcm.columnOne label="独生子女" type="select" mcodeId="DM-BOOLEAN" columnId="is_singleton" />
	<@dcm.columnOne label="贫困家庭" type="select" mcodeId="DM-BOOLEAN" columnId="boorish" />
	<@dcm.columnOne label="本地生源" type="select" mcodeId="DM-BOOLEAN" columnId="is_local_source" />
	</tr>
</table>
<p />
</@dcm.reportData>

