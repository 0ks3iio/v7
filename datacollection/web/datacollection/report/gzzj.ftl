<@dcm.reportData  reportCode=_reportCode dataId=_dataId>
<p />
<p align="center">
<@dcm.dcObjVal type="unit" id="{unitId}" columnId="unitName" />
<@dcm.dcObjVal type="unit" id="{unitId}" columnId="unitClass" />
2017上半年度<@dcm.dcDataVal reportCode=_reportCode dataId=_dataId columnId="xm" />工作总结
</p><p />
<@dcm.saveButton />
<@dcm.listButton reportCode=_reportCode templatePath=_templatePath/>
<table align="center">
	<tr height="30">
	<@dcm.columnOne label="姓名" nullable=false columnId="xm" />
	<@dcm.columnOne label="性别" columnId="xb" type="select" mcodeId="DM-XB" />
	<@dcm.columnOne label="部门" columnId="bm" />
	</tr>
	<tr height="30">
	<@dcm.columnOne label="入司时间" columnId="rssj" />
	<@dcm.columnOne label="岗位" columnId="gw" colspan=3 />
	</tr>
	<@dcm.rowOne rowHeight="300" label="参与代表性项目" type="input" columnId="xmmc" colspan=5 />
	<@dcm.rowOne rowHeight="100" label="工作量" type="textarea" columnId="gzl"  colspan=5 />
	<@dcm.rowOne rowHeight="300" label="工作突出方面" type="textarea" columnId="gztc" colspan=5 />
	<@dcm.rowOne rowHeight="300" label="待改进" type="textarea" columnId="dgj" colspan=5 />
	<@dcm.rowOne rowHeight="300" label="下期工作重点" type="textarea" columnId="xqgzzd" colspan=5 />
	<@dcm.rowOne rowHeight="100" label="签名" type="input" columnId="qm"  colspan=5 />
</table>
<p />
<table align="center">
<@dcm.rowMulti2 labelWidth="150" labels="指标类型,内容,A,B+,B,CD" type="input" columnIds="zblx,nr,ca,cbp,cb,ccd"/>
</table>
</@dcm.reportData>

