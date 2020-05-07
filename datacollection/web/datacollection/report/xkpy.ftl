<@dcm.reportData  reportCode=_reportCode dataId=_dataId>
<p />
<p align="center">
<span><$dc.unit.object.{unitId}@unitName$></span> 
<span><$dc.semester.currentSemester@acadyear$></span>学年
第 <span><$dc.semester.currentSemester@semester$></span>学期课任学科期末质量分析
</p>

<p align="center">
班級：<span><$dc.clazz.teachClass.{teacherId}@[0]classNameDynamic$></span>
任课老师：<span><$dc.teacher.object.{teacherId}@teacherName$></span>
</p>
<@dcm.saveButton />
<@dcm.listButton reportCode=_reportCode templatePath=_templatePath/>
<table align="center">
	<tr height="30">
	<@dcm.columnOne label="学科" columnId="xk" />
	<@dcm.columnOne label="年级" columnId="nj" />
	<@dcm.columnOne label="姓名" columnId="xm" />
	</tr>
	<@dcm.rowOne rowHeight="300" label="优生名单" type="input" columnId="ysmd" colspan=5/>
	<@dcm.rowOne rowHeight="300" label="基本情况" type="textarea" columnId="jbqk"  colspan=5/>
	<@dcm.rowOne rowHeight="300" label="培养措施" type="textarea" columnId="pycs"  colspan=5/>
	<@dcm.rowMulti label="内容记录" type="textarea" columnId="nrjl"  colspan=5/>
</table>
<table align="center">
<@dcm.rowMulti2 labels="内容记录,课堂表现" type="input" columnIds="nrjl,ktbx"/>
</table>
<p />
<p align="center">
班級：<span><$dc.clazz.teachClass.{teacherId}@[0]classNameDynamic$></span>
任课老师：<span><$dc.teacher.object.{teacherId}@teacherName$></span>
</p>
<table align="center">
	<@dcm.rowOne rowHeight="300" label="效果" type="textarea" columnId="xg"/>
	<@dcm.rowOne rowHeight="300" label="存在的问题及反思" type="textarea" columnId="czwt"/>
	<@dcm.rowOne rowHeight="300" label="备注" type="textarea" columnId="bz"/>
</table>
</div>
</@dcm.reportData>

