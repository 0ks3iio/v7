<div class="filter">
	<div class="filter-item">
        <div class="filter-content">
            <a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
            <a href="javascript:void(0);" class="btn btn-default" onclick="doAllImport();">导入</a>
        </div>
    </div>
</div>
<form id="scoreForm">
<input type="hidden" name="classType" value="${classType!}"/>
<input type="hidden" name="classId" value="${classId!}"/>
<input type="hidden" name="acadyear" value="${acadyear}"/>
<input type="hidden" name="subjectId" value="${subjectId!}"/>
<input type="hidden" name="semester" value="${semester}"/>
<input type="hidden" name="setId" value="${setId}"/>
<input type="hidden" name="gradeId" value="${gradeId!}"/>
<div style="overflow-x: auto;">
	<table class="table table-striped table-bordered table-hover text-center no-margin">
		<thead>
			<tr>
				<th rowspan="2" class="text-center" width="5%">序号</th>
				<th rowspan="2" class="text-center" width="8%">学号</th>
				<th rowspan="2" class="text-center" width="7%">姓名</th>
				<#assign sumScore = 0>
				<#if setList?exists && setList?size gt 0>
					<#list setList as set>
						<th colspan="${set.subSetList?size + 1}" class="text-center">${set.name}&nbsp; ${set.score!}</th>
						<#assign sumScore = sumScore + set.score!>
					</#list>
				</#if>
				<th rowspan="2" class="text-center">合计</th>
			</tr>
			<tr>
				<#if setList?exists && setList?size gt 0>
					<#list setList as set>
						<#if set.subSetList?exists && set.subSetList?size gt 0>
							<#list set.subSetList as subSet>
								<th class="text-center">${subSet.name!} ${subSet.score!}</th>
							</#list>
						</#if>
						<th class="text-center">分值</th>
					</#list>
				</#if>
			</tr>
		</thead>
		<tbody>
		<#if stulist?exists && stulist?size gt 0>
		<#assign i=0>
			<#list stulist as stu>
			<tr>
				<td>${stu_index + 1}</td>
				<td>${stu.studentCode}</td>
				<td>${stu.studentName}</td>
				<#if setList?exists && setList?size gt 0>
					<#list setList as set>
						<#if set.subSetList?exists && set.subSetList?size gt 0>
							<#list set.subSetList as subSet>
								<td>
									<input id="${subSet.id}_${stu.id}" onblur="countNum('${stu.id!}','${set.id!}','${set_index}','${stu_index}')" vtype="number" decimalLength="1" max="${subSet.score}" min="0" name="dtolist[${i}].score" type="text" nullable="false" class="form-control one_${set_index}${stu_index}" value="${infoMap[set.id+","+subSet.id+","+stu.id].score!}"/>
									<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
									<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
									<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
									<input type="hidden" name="dtolist[${i}].dailyId" value="${set.id!}"/>
									<input type="hidden" name="dtolist[${i}].subDailyId" value="${subSet.id!}"/>
									<input type="hidden" name="dtolist[${i}].totalScore" value="${subSet.score!}"/>
									<#assign i=i+1>
								</td>
							</#list>
						</#if>
						<td>
							<span class="color-blue" id="show_${stu_index}${set_index}">${infoMap[set.id+",,"+stu.id].score}</span>
						</td>
							<input type="hidden" class="set_${stu_index}" id="score_${stu_index}${set_index}" name="dtolist[${i}].score" value="${infoMap[set.id+",,"+stu.id].score}"/>
							<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
							<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
							<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
							<input type="hidden" name="dtolist[${i}].dailyId" value="${set.id!}"/>
							<input type="hidden" name="dtolist[${i}].subDailyId" value=""/>
							<input type="hidden" name="dtolist[${i}].totalScore" value="${set.score!}"/>
							<#assign i=i+1>
					</#list>
				</#if>
				<td>
					<span class="color-blue" id="count_${stu_index}">
					${infoMap[stu.id].score}
					</span>
					<input type="hidden" id="score_${stu_index}" name="dtolist[${i}].score" value="${infoMap[stu.id].score}"/>
					<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
					<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
					<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
					<input type="hidden" name="dtolist[${i}].dailyId" value=""/>
					<input type="hidden" name="dtolist[${i}].subDailyId" value=""/>
					<input type="hidden" name="dtolist[${i}].totalScore" value="${sumScore}"/>
					<#assign i=i+1>
				</td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<form>
<script>

function doAllImport(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var subjectId = $('#subjectId').val();
	var clsTypeId = $('#clsTypeId').val();
	var gradeId = $('#gradeId').val();
	var str = '?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&type=1"+"&subjectId="+subjectId+"&clsTypeId="+clsTypeId;
	var url='${request.contextPath}/exammanage/credit/import/register/head'+str;
	url=encodeURI(encodeURI(url));
	$('#model-div-37097').load(url);
}

function countNum(stuId,setId,setIndex,stuIndex){
	var oneArr = $('.one_'+setIndex+stuIndex);
	var a = 0;
	if(oneArr.length > 0){
		for(var i = 0;i<oneArr.length;i++){
			var one = oneArr[i];
			a = a + parseFloat(one.value);
		}
	}
	$('#show_'+stuIndex+setIndex).html(a);
	$('#score_'+stuIndex+setIndex).attr('value',a);
	
	var setArr = $('.set_'+stuIndex);
	var b = 0;
	if(setArr.length > 0){
		for(var i = 0;i<setArr.length;i++){
			var oneSet = setArr[i];
			b = b + parseFloat(oneSet.value);
		}
	}
	b = b.toFixed(1);
	$('#count_'+stuIndex).html(b);
	$('#score_'+stuIndex).attr('value',b);
}

var isSubmit=false;
function save(){
	if(isSubmit){
		return;
	}
	layer.closeAll();
	var check = checkValue('#listDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	isSubmit=true;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/credit/register/daily/save',
		data:{},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				<#if isAdmin>
				onClsClick('${classId}'+'_'+'${classType}');
				<#else>
				onSubClick('${subjectId!}');
				</#if>
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#scoreForm").ajaxSubmit(options);
} 

</script>