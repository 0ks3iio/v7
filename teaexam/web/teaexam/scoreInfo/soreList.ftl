<div class="box box-default">
	<div class="box-body" id="scorediv">
<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<input type="hidden" id="type" value="0">
		<div class="filter-item">
		<#if lineState==1>
			<button class="btn btn-blue" onClick="doImport();">导入成绩</button>
			<button class="btn btn-blue" onClick="save();">保存</button>
		<#elseif lineState==0>
		   该科目没有维护分数线，请选设置分数线
		<#elseif lineState==-1>
		</#if>
		</div>
		<div class="filter-item filter-item-right">
			<span class="filter-name">考试名称：</span>
			<div class="filter-content">
				<select class="form-control" id="examId" name="examId" onchange="querySubList();" style="width:200px;">
					<#if examList?exists && examList?size gt 0>
					   <#list examList as item>
					      <option value="${item.id!}" <#if '${item.id!}'=='${examId!}'>selected="selected"</#if>>${item.examName!}</option>
					   </#list>
					<#else>
						<option value="">---请选择考试---</option>
					</#if>
				</select>
			</div>
		</div>	
		<div class="filter-item filter-item-right">
			<span class="filter-name">年份：</span>
            <div class="filter-content">
               <select name="year" id="year" class="form-control" onchange="queryExam()" style="width:135px">
                    <#list minYear..maxYear as item>
                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                    </#list>			                       
               </select>
           </div>
		</div>
	</div>	


	<div class="picker-table">
		<table class="table">
			<tbody>
				<tr>
					<th>科目：</th>
					<td>
						<div class="float-left mt3"></div>
						<div class="float-left mr10">
                            <select name="" id="subjectId" class="form-control" onchange="queryScoreList('${roomNo!}');">
							<#if subList?exists && subList?size gt 0>
							    <#list subList as sub>
								    <option value="${sub.id!}" <#if subId?exists && '${subId!}' == '${sub.id!}'>selected="selected"</#if>>${sub.subjectName!}
								    (
								    <#if sub.section == 1>
								     小学
								  <#elseif sub.section == 0>
						    	学前
								    <#elseif sub.section == 2>
								    初中
								    <#elseif sub.section == 3>
								    高中
								    </#if>
								    )</option>
								</#list>
							<#else>
							    <option value="">--请选择--</option>
						    </#if>
							</select>
						</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<th width="150" style="vertical-align: top;">考场：</th>
					<td>
						<div class="outter">
							<#if roomNoList?exists && roomNoList?size gt 0>
							    <#list roomNoList as item>
							          <a <#if roomNo?exists && roomNo == item>class="selected"</#if> href="javascript:void(0);" onClick="queryScoreList('${item!}');">${item!}</a>
							    </#list>
							</#if>
						</div>
					</td>
					<td width="78" style="vertical-align: top;">
						<div class="outter">
							<a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

    <form id="subForm">
    <table class="table table-bordered table-striped table-hover">
		<thead style="display: table-header-group;">
			<tr>
				<th>考生姓名</th>
				<th style="width:15%;">准考证号</th>
				<th>所在单位</th>
				<th>考场编号</th>
				<th>座位号</th>
				<th>考试科目</th>
				<th style="width:10%;">考试成绩</th>
			</tr>
		</thead>
		<tbody>
		<#if regList2?exists && regList2?size gt 0>
		    <#list regList2 as item>
			<tr>
			    <td>${item.teacherName!}</td>
			    <td>${item.cardNo!}</td>
			    <td>${item.schName!}</td>
			    <td>${item.roomNo!}</td>
			    <td>${item.seatNo!}</td>
			    <td>${item.subName!}</td>
			    <td>
			        <input type="text" class="form-control" vtype="number" min="0" max="${(maxMap[item.subjectInfoId])?default(999)}" maxLength="5" decimalLength="1" placeholder="请输入成绩" id="score${item_index!}" name="regList[${item_index!}].score" <#if item.score?exists>value="${item.score?string('#.#')!}</#if>">
			        <input type="hidden" name="regList[${item_index!}].id" value="${item.id!}">
			    </td>
			</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="7" align="center">暂无数据</td>
			</tr>
		</#if>
		</tbody>
	</table>
	</form>
</div></div></div>
<script>
function querySubList(){
	var acadyear = $('#year').val();
     var semester = $('#type').val();
     var examId = $('#examId').val();;
     var url = "${request.contextPath}/teaexam/scoreInfo/index/page?examId="+examId+"&year="+acadyear+"&type="+semester;
     $('.model-div').load(url);
}

function queryExam(){
	var acadyear = $('#year').val();
     var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/scoreInfo/index/page?year="+acadyear+"&type="+semester;
     $('.model-div').load(url); 
}

function queryScoreList(roomNo){
     var acadyear = $('#year').val();
     var semester = $('#type').val();
     var examId = $('#examId').val();;
     var subId = $('#subjectId').val();
     var url = "${request.contextPath}/teaexam/scoreInfo/index/page?examId="+examId+"&subId="+subId+"&roomNo="+roomNo+"&year="+acadyear+"&type="+semester;
     $('.model-div').load(url);
}

$(function(){
    $('.picker-more').click(function(){
		if($(this).children('span').text()=='展开'){
			$(this).children('span').text('折叠');
			$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
		}else{
			$(this).children('span').text('展开');
			$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
		};
		$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	});
})

var isSubmit=false;
function save(){
    var check = checkValue('#subForm');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    var subjectId = $('#subjectId').val();
	var options = {
		url : "${request.contextPath}/teaexam/scoreInfo/saveScore?subjectId="+subjectId,
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				queryScoreList('${roomNo!}');
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}

function doImport(){
    var subjectId = $('#subjectId').val();
    if(subjectId == ''){
        layerTipMsgWarn("提示","科目不能为空!");
		return;
    }
    var examId = $('#examId').val();
    
    $.ajax({
		url:'${request.contextPath}/teaexam/scoreInfo/getLimit',
		data:{'examId':examId,'subjectId':subjectId},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	 			var acadyear = $('#year').val();
                var semester = $('#type').val();
                var url = "${request.contextPath}/teaexam/scoreInfo/scoreInfoImport/main?examId="+examId+"&subjectId="+subjectId+"&year="+acadyear+"&type="+semester;
                $('#scorediv').load(url);
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 		}	
		}
	});
}
</script>