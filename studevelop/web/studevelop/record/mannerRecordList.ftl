<div class="table-wrapper">
    <form id="mannReForm">
    <#if modelType == '1'>
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>学生</th>
					<th>学习态度</th>
					<th>交流与合作能力</th>
					<th>发现、提出问题能力</th>
                    <th>期中成绩</th>
                    <th>期末成绩</th>
                    <th>综合评定</th>
				</tr>
		</thead>
		<tbody>
			<#if mannerRecordList?exists && (mannerRecordList?size > 0)>
				<#list mannerRecordList as item>
					<tr>
						<td>${item.studentName!}</td>
						<td>
					        <select name="mannerRecordList[${item_index!}].manner" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-XXTD", '${item.manner!}', "1")}
						    </select>
						    <input type="hidden" name="mannerRecordList[${item_index!}].id" value="${item.id!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].studentId" value="${item.studentId!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].subjectId" value="${item.subjectId!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].acadyear" value="${item.acadyear!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].semester" value="${item.semester!}"/>
						    </td>
						<td>
						    <select name="mannerRecordList[${item_index!}].communication" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-NLQR", '${item.communication!}', "1")}
						    </select>
                        </td>
						<td>
						    <select name="mannerRecordList[${item_index!}].discovery" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-NLQR", '${item.discovery!}', "1")}
						    </select>
						</td>
						<td>${item.qzScore!}</td>
						<td>${item.qmScore!}</td>
						<td>${item.zpScore!}</td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	<#else>
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>科目</th>
					<th>学习态度</th>
					<th>交流与合作能力</th>
					<th>发现、提出问题能力</th>
                    <th>期中成绩</th>
                    <th>期末成绩</th>
                    <th>综合评定</th>
				</tr>
		</thead>
		<tbody>
			<#if mannerRecordList?exists && (mannerRecordList?size > 0)>
				<#list mannerRecordList as item>
					<tr>
						<td>${item.subjectName!}</td>
						<td>
					        <select name="mannerRecordList[${item_index!}].manner" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-XXTD", '${item.manner!}', "1")}
						    </select>
						    <input type="hidden" name="mannerRecordList[${item_index!}].id" value="${item.id!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].studentId" value="${item.studentId!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].subjectId" value="${item.subjectId!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].acadyear" value="${item.acadyear!}"/>
						    <input type="hidden" name="mannerRecordList[${item_index!}].semester" value="${item.semester!}"/>
						    </td>
						<td>
						    <select name="mannerRecordList[${item_index!}].communication" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-NLQR", '${item.communication!}', "1")}
						    </select>
                        </td>
						<td>
						    <select name="mannerRecordList[${item_index!}].discovery" id="" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-NLQR", '${item.discovery!}', "1")}
						    </select>
						</td>
						<td>${item.qzScore!}</td>
						<td>${item.qmScore!}</td>
						<td>${item.zpScore!}</td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	</#if>
	</form>
	<#if mannerRecordList?exists && (mannerRecordList?size > 0)>
	<div class="page-footer-btns text-center">
		<a class="btn btn-blue save-btn" id="saveBtn" onclick="save();">保存</a>
	</div>
	</#if>		
</div>
<script>
var isSubmit=false;
function save(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$('#saveBtn').addClass("disabled");		
	// 提交数据
	var ii = layer.load();	
	var options = {
	    url:'${request.contextPath}/studevelop/mannerRecord/save',
	    dataType : 'json',
	    type:'post',  
	    cache:false, 
	    success:function(data) {
	    	var jsonO = data;
	    	if(jsonO.success){
	    	    layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
			  	<#if modelType == '1'>
			  	searchList();
			  	<#else>
			  	searchList3();
			  	</#if>
	 		}
	 		else{	 		    
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 			$('#saveBtn').removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
	     },
	     error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
 	$('#mannReForm').ajaxSubmit(options);
}
</script>