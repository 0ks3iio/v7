<div class="filter">
	<div class="filter-item">
        <div class="filter-content">
            <a href="javascript:void(0);" class="btn btn-blue" onclick="save();">保存</a>
            <a href="javascript:void(0);" class="btn btn-default" onclick="doAllImport();">导入</a>
            <#if isAdmin>
            <a href="javascript:" class="btn btn-white js-addExam" onclick="moduleSet();">管理模块成绩</a>
            </#if>
            <a href="javascript:" class="btn btn-default" onclick="usualSet();">管理平时成绩</a>
        </div>
    </div>
</div>
<form id="scoreForm">
<input type="hidden" name="classType" value="${classType}"/>
<input type="hidden" name="classId" value="${classId}"/>
<input type="hidden" name="acadyear" value="${acadyear}"/>
<input type="hidden" name="subjectId" value="${subjectId}"/>
<input type="hidden" name="semester" value="${semester}"/>
<input type="hidden" name="setId" value="${set.id}"/>
<input type="hidden" name="gradeId" value="${gradeId!}"/>
<div style="overflow-x: auto;">
  <table class="table table-striped table-bordered table-hover text-center no-margin">
    <thead>
      <tr>
        <th rowspan="2" class="text-center">序号</th>
        <th rowspan="2" class="text-center">学号</th>
        <th rowspan="2" class="text-center">姓名</th>
        <#if usualSetList?exists && usualSetList?size gt 0>
        <th colspan="${usualSetList?size + 2}" class="text-center">平时成绩&nbsp;${set.usualScore}</th>
        </#if>
        <#if moudleSet?exists>
        <th colspan="2" class="text-center">模块成绩&nbsp;${set.moduleScore}</th>
        </#if>
        <#if hasStat == '1'>
        <th colspan="2" class="text-center">补考</th>
        </#if>
      </tr>
      <tr>
      	<#if usualSetList?exists && usualSetList?size gt 0>
      	<#list usualSetList as usualSet>
        <th class="text-center" width="100">${usualSet.name}</th>
      	</#list>
        <th class="text-center">平均分</th>
        <th class="text-center">折分</th>
        </#if>
        <#if moudleSet?exists>
        <th class="text-center" width="100">成绩</th>
        <th class="text-center">折分</th>
        </#if>
        <#if hasStat == '1'>
        	<th class="text-center" width="100">成绩</th>
        	<th class="text-center">折分</th>
        </#if>
        <#--
        <th class="text-center" width="100">成绩</th>
        <th class="text-center">折分</th>
        -->
      </tr>
    </thead>
    <tbody>
    <#assign i=0>
    <#if stulist?exists && stulist?size gt 0>
    	<#list stulist as stu>
    	<tr>
	        <td>${stu_index + 1}</td>
	        <td>${stu.studentCode}</td>
			<td>${stu.studentName}</td>
			<#if usualSetList?exists && usualSetList?size gt 0>
	      	<#list usualSetList as usualSet>
	        <td>
	            <input id="${usualSet.id}_${stu.id}_11" vtype="number" onblur="countNum1('${stu_index}')" decimalLength="1" max="${subFullMark}" min="0" name="dtolist[${i}].score" type="text" nullable="false" class="form-control oneUsual_${stu_index}" value="${infoMap["1,"+usualSet.id+",1,"+stu.id].score!}"/>
				<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="1"/>
				<input type="hidden" name="dtolist[${i}].examType" value="1"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value="${usualSet.id!}"/>
				<#assign i=i+1>
	        </td>
	      	</#list>
	        <td>
	        	<span class="color-blue" id="usualAvgTxt_${stu_index}">${infoMap["1,,2,"+stu.id].score?string('0.#')}</span>
	        	<input type="hidden" id="usualAvg_${stu_index}" name="dtolist[${i}].score" value="${infoMap["1,,2,"+stu.id].score?string('0.#')}"/>
	        	<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="2"/>
				<input type="hidden" name="dtolist[${i}].examType" value="1"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value=""/>
				<#assign i=i+1>
	        </td>
	        <td>
	        	<span class="color-blue" id="usualConTxt_${stu_index}">${infoMap["1,,3,"+stu.id].score?string('0.#')}</span>
	        	<input type="hidden" id="usualCon_${stu_index}" name="dtolist[${i}].score" value="${infoMap["1,,3,"+stu.id].score?string('0.#')}"/>
	        	<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="3"/>
				<input type="hidden" name="dtolist[${i}].examType" value="1"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value=""/>
				<#assign i=i+1>
	        </td>
	        </#if>
			<#assign setId = "" />
	        <#if moudleSet?exists>
				<#assign setId = moudleSet.id />
	        <td>
	          <input id="${moudleSet.id}_${stu.id}_21" vtype="number" onblur="countNum2('${stu_index}')" decimalLength="1" max="${subFullMark}" min="0" name="dtolist[${i}].score" type="text" nullable="false" class="form-control moudleSet_${stu_index}" value="${infoMap["2,"+moudleSet.id+",1,"+stu.id].score!}"/>
	          	<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="1"/>
				<input type="hidden" name="dtolist[${i}].examType" value="2"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value="${moudleSet.id!}"/>
	          <#assign i=i+1>
	        </td>
	        <td>
	        	<span class="color-blue" id="moudleConTxt_${stu_index}">${infoMap["2,,3,"+stu.id].score?string('0.#')}</span>
	        	<input type="hidden" id="moudleCon_${stu_index}" name="dtolist[${i}].score" value="${infoMap["2,,3,"+stu.id].score?string('0.#')}"/>
	        	<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="3"/>
				<input type="hidden" name="dtolist[${i}].examType" value="2"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value=""/>
				<#assign i=i+1>
	        </td>
	        </#if>
	        <#if hasStat == '1'>

	        <td>
				<#if infoMap["3,"+setId+",1,"+stu.id]?exists>
					<span class="color-blue">${infoMap["3,"+setId+",1,"+stu.id].score!}</span>
					<input type="hidden" name="dtolist[${i}].score" value="${infoMap["3,"+setId+",1,"+stu.id].score!}"/>
					<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
					<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
					<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
					<input type="hidden" name="dtolist[${i}].scoreType" value="1"/>
					<input type="hidden" name="dtolist[${i}].examType" value="3"/>
					<input type="hidden" name="dtolist[${i}].examSetId" value="${setId!}"/>
					<#assign i=i+1>
				  </#if>
	        </td>
	        <td>
	        	<#if infoMap["3,"+setId+",3,"+stu.id]?exists>
	        	<span class="color-blue" id="bkMoudleConTxt_${stu_index}">${infoMap["3,"+setId+",3,"+stu.id].score?string('0.#')}</span>
	        	<input type="hidden" id="bkMoudleCon_${stu_index}" name="dtolist[${i}].score" value="${infoMap["3,"+setId+",3,"+stu.id].score?string('0.#')}"/>
	        	<input type="hidden" name="dtolist[${i}].studentId" value="${stu.id!}"/>
				<input type="hidden" name="dtolist[${i}].studentCode" value="${stu.studentCode!}"/>
				<input type="hidden" name="dtolist[${i}].studentName" value="${stu.studentName!}"/>
				<input type="hidden" name="dtolist[${i}].scoreType" value="3"/>
				<input type="hidden" name="dtolist[${i}].examType" value="3"/>
				<input type="hidden" name="dtolist[${i}].examSetId" value="${setId!}"/>
				<#assign i=i+1>
	        	</#if>
	        </td>
	        </#if>
	      </tr>
	      </#list>
    </#if>
    </tbody>
  </table>
</div>
</form>
<script>
function doAllImport(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var subjectId = $('#subjectId').val();
	var clsTypeId = $('#clsTypeId').val();
	var gradeId = $('#gradeId').val();
	var str = '?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&type=2"+"&subjectId="+subjectId+"&clsTypeId="+clsTypeId;
	var url='${request.contextPath}/exammanage/credit/import/register/head'+str;
	url=encodeURI(encodeURI(url));
	$('#model-div-37097').load(url);
}


var markFull = parseFloat('${subFullMark}');
var usualScore = parseFloat('${set.usualScore}');
var moduleScore = parseFloat('${set.moduleScore}');
function countNum1(stuIndex){
	var oneArr = $('.oneUsual_'+stuIndex);
	var a = 0;
	if(oneArr.length > 0){
		for(var i = 0;i<oneArr.length;i++){
			var one = oneArr[i];
			a = a + parseFloat(one.value);
		}
	}
	var avgAA = a/oneArr.length;
	var avgA = avgAA.toFixed(1);  
	$('#usualAvgTxt_'+stuIndex).html(avgA);
	$('#usualAvg_'+stuIndex).attr('value',avgA);
	var conBB = avgA*usualScore/markFull;
	var conB = conBB.toFixed(1);
	$('#usualConTxt_'+stuIndex).html(conB);
	$('#usualCon_'+stuIndex).attr('value',conB);
}

function countNum2(stuIndex){
	var oneArr = $('.moudleSet_'+stuIndex);
	var a = 0;
	if(oneArr.length > 0){
		for(var i = 0;i<oneArr.length;i++){
			var one = oneArr[i];
			a = a + parseFloat(one.value);
		}
	}
	var conBB = a*moduleScore/markFull;
	var conB = conBB.toFixed(1);
	$('#moudleConTxt_'+stuIndex).html(conB);
	$('#moudleCon_'+stuIndex).attr('value',conB);
}

function countNum3(stuIndex){
	var oneArr = $('.bk_'+stuIndex);
	var a = 0;
	if(oneArr.length > 0){
		for(var i = 0;i<oneArr.length;i++){
			var one = oneArr[i];
			a = a + parseFloat(one.value);
		}
	}
	var conCC = a*moduleScore/markFull;
	var conC = conCC.toFixed(1);
	$('#bkMoudleConTxt_'+stuIndex).html(conC);
	$('#bkMoudleCon_'+stuIndex).attr('value',conC);
}
function usualSet(){
	var clsTypeId=$("#clsTypeId").val();
	var subjectId='${subjectId!}';
	var url = "${request.contextPath}/exammanage/credit/register/exam/usualSet?gradeId=${gradeId!}&acadyear=${acadyear!}&semester=${semester!}&clsTypeId="+clsTypeId+"&subjectId="+subjectId;
    indexDiv = layerDivUrl(url,{title: "平时成绩设置",width:450,height:400});
}

function moduleSet(){
   	var url = "${request.contextPath}/exammanage/credit/register/exam/moduleSet?gradeId=${gradeId!}&acadyear=${acadyear!}&semester=${semester!}";
    indexDiv = layerDivUrl(url,{title: "模块成绩设置",width:450,height:200});
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
		url : '${request.contextPath}/exammanage/credit/register/exam/save',
		data:{},
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				//layerTipMsg(data.success,"成功",data.msg);
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				//onClsClick('${classId}'+'_'+'${classType}');
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
