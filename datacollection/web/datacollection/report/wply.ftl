<@dcm.reportData  reportCode=_reportCode dataId=_dataId>
<p />
<p align="center">
<span><$dc.unit.object.{unitId}@unitName$></span>物品领用登记表
</p>

<p align="center">
班級：<span><$dc.clazz.teachClass.{teacherId}@[0]classNameDynamic$></span>
任课老师：<span><$dc.teacher.object.{teacherId}@teacherName$></span>
</p>
<@dcm.saveButton />
<@dcm.listButton reportCode=_reportCode templatePath=_templatePath/>
<table align="center">
<@dcm.rowMulti2 labelWidth="150" labels="领用物品,领用时间,领用人,备注" type="input" columnIds="lywp,lysj,lyr,bz"/>
</table>
</@dcm.reportData>

