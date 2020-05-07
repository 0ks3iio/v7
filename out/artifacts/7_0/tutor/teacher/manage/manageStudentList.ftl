<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th><label><span class="lbl" > 序号</span></label></th>
				<th>姓名</th>
				<th>学号</th>
				<th>行政班</th>
				<th>班主任</th>
				<th>最近登记类型</th>
				<th>最近登记时间</th>
				<th>导师记录</th>
			</tr>
		</thead>
		<tbody>
		   <#if ShowStuList?exists && ShowStuList?size gt 0>
              <#list ShowStuList as stu>
                  <tr>
					<td class="cbx-td"><label><span class="lbl"> ${stu_index+1!}</span></label></td>
					<td>${stu.student.studentName!}</td>
					<td>${stu.student.studentCode!}</td>
					<td>${stu.gcName!}</td>
					<td>${stu.classMaster!}</td>                    
					<td>${mcodeSetting.getMcode("DM-DSJL-LX","${stu.tutorRecord.recordType!}")}</td>
					<td>${stu.tutorRecord.creationTime!}</td>
					<td>
					  	<a href="javascript:void(0);" onclick="showRecordIndex('${stu.student.id!}');">查看</a>
					</td>
					<input type="hidden"  class="studentId"  value="${stu.student.id!}"/>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="88" align="center">
					暂无学生
					</td>
			   <tr>
               <#--  "暂无学生" -->
           </#if>   
		</tbody>
	</table>
</div>
