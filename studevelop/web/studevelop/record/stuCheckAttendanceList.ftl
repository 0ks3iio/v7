<title>考勤总计List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<form name="checkAttForm" id="checkAttForm" action="" method="post">
	<table class="table table-bordered table-striped table-hover no-margin" id="showGradeTabble">
		<thead>
			<tr>
				<th>学生姓名</th>
				<th>事假天数</th>
				<th>病假天数</th>
				<th>旷课节数</th>
				<th>迟到节数</th>
				<th>早退节数</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<#if lastList?exists && (lastList?size > 0)>
				<#list lastList as item>
					<tr>
						<td>
						<input type="hidden" name="checkAttList[${item_index}].id" value="${item.id!}"/>
						<input type="hidden" name="checkAttList[${item_index}].studentId" value="${item.studentId!}"/>
						<input type="hidden" name="checkAttList[${item_index}].acadyear" value="${item.acadyear!}"/>
						<input type="hidden" name="checkAttList[${item_index}].semester" value="${item.semester!}"/>
						${item.studentName!}
						</td>
						<td>
							<input type="text"  class="table-input " name="checkAttList[${item_index}].businessVacation" id="businessVacation${item_index}" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype = "number" nullable="false"  value="${item.businessVacation!}" min="0" max="999" maxLength="5">
						</td>
						<td>
							<input type="text"  class="table-input " name="checkAttList[${item_index}].illnessVacation" id="illnessVacation${item_index}" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype = "number" nullable="false"  value="${item.illnessVacation!}" min="0" max="999"  maxLength="5">
						</td>
						<td>
							<input type="text"  class="table-input " name="checkAttList[${item_index}].wasteVacation" id="wasteVacation${item_index}"  vtype ="int" nullable="false"  value="${item.wasteVacation?default(0)}"  maxLength="3">
						</td>
						<td>
							<input type="text"  class="table-input " name="checkAttList[${item_index}].late" id="late${item_index}"  vtype = "int" nullable="false"  value="${item.late?default(0)}"  maxLength="3">
						</td>
						<td>
							<input type="text"  class="table-input " name="checkAttList[${item_index}].leaveEarly" id="leaveEarly${item_index}"  vtype = "int" nullable="false"  value="${item.leaveEarly?default(0)}"  maxLength="3">
						</td>
						<td>
							<input type="text"  name="checkAttList[${item_index}].remark" id="remark${item_index}" value="${item.remark!}"  maxLength="200">
						</td>
					</tr>
				</#list>
			 </#if>
		</tbody>
	</table>		
</form>
</div>
<script>
	 var isSubmit=false;
    function saveAllAtt(){
        if(isSubmit){
            return false;
        }
        isSubmit = true;
        var check = checkValue('#checkAttForm');
        if(!check){
            isSubmit=false;
            return;
        }
        var options = {
            url:'${request.contextPath}/studevelop/checkAttendance/saveAllAtt',
            dataType : 'json',
            clearForm : false,
            resetForm : false,
            type : 'post',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }else{
                    layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
                }
                isSubmit = false;
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $('#checkAttForm').ajaxSubmit(options);
    }
</script>