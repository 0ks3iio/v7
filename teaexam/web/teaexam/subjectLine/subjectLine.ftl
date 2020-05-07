<div class="box box-default">
	<div class="box-body clearfix">
        <div class="filter">
			<div class="filter-item">
                  <span class="filter-name">年份：</span>
                  <div class="filter-content">
	                  <select class="form-control" id="year" name="year" onchange="queryExamList('');">
		                  <#list minYear..maxYear as item>
	                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
	                    </#list>
	                  </select>
                  </div>
              </div>
             <div class="filter-item">
                 <span class="filter-name">考试名称：</span>
                 <div class="filter-content">
	                <select class="form-control" id="examId" name="examId" onchange="queryLineList('');" style="width:200px;">
	                <#if examList?exists && examList?size gt 0>
	                   <#list examList as item>
	                        <option value="${item.id!}" <#if '${examId!}'=='${item.id!}'>selected</#if>>${item.examName!}</option>
	                   </#list>
	                <#else>
		               <option value="">---请选择考试---</option>
		            </#if>
	                </select>
                 </div>
             </div>	
			 <div class="filter-item filter-item-right">
				<button class="btn btn-blue" onclick="saveLine();">保存</button>
			 </div>
		</div>		
		<div class="clearfix">
			<div class="tree-wrap">
				<table class="table table-striped table-hover no-margin">
                    <thead>
						<tr>
							<th>科目</th>
						</tr>
					</thead>
				</table>
				<div class="tree-list" style="height:800px;margin-top: 0;">
				<#if subList?exists && subList?size gt 0>
				    <#list subList as item>
					<a <#if '${subjectId}'=='${item.id!}'>class="active"</#if> href="javascript:queryLineList('${item.id!}');">${item.subjectName!}</a>
					</#list>
				</#if>
				</div>
			</div>
			<div style="margin-left: 200px;">
				<table class="table table-bordered table-striped table-hover no-margin">
                    <thead>
						<tr>
							<th>分数线</th>
						</tr>
					</thead>
				</table>
				<form id="subForm">
				<div class="padding-10" style="border-right: 1px solid #ddd;border-bottom: 1px solid #ddd;">
					<div class="filter">
						<div class="filter-item">
		    				<span class="filter-name">满分：</span>
		    				<div class="filter-content">
		    					<input class="form-control" type="text" id="fullScore" name="fullScore" nullable="false" <#if fullScore?exists>value="${fullScore!}"</#if> vtype="number" min="0" max="999" maxLength="5" decimalLength="1"/>
		    				</div>
		    			</div>
					</div>
				    <table class="table table-bordered table-striped table-hover no-margin">
                        <thead>
							<tr>
							    <td width="200"><b>名称</b></td>
							    <#--td width="200"><b>分数上限</b></td-->
							    <td><b>分数下限</b></td>
								<#--td width="100"><b>操作</b></td-->
								<td width="17"></td>
							</tr>
						</thead>
					</table>
					<div style="height: 706px;overflow-y: scroll;">
						<table class="table table-bordered table-striped table-hover no-margin">
                            <#--tbody id="tb2">	
                            <#if lineList?exists && lineList?size gt 0>
                                <#list lineList as item>
							    <tr>
							    	<td><input type="text" class="form-control" nullable="false" id="lineList${item_index!}gradeName" name="lineList[${item_index!}].gradeName" value="${item.gradeName!}"></td>
							    	<td width="200"><input type="text" class="form-control" nullable="false" id="lineList${item_index!}maxScore" name="lineList[${item_index!}].maxScore" value="${item.maxScore!}"></td>
							    	<td width="200"><input type="text" class="form-control" nullable="false" id="lineList${item_index!}minScore" name="lineList[${item_index!}].minScore" value="${item.minScore!}"></td>
									<td width="100"><a class="color-blue" href="javascript:void(0);" onclick="delLineRow(this)">删除</a></td>
								</tr>
								</#list>	
							</#if>							
							</tbody>
							<tbody>
							    <tr>
							    	<td colspan="4" class="text-center"><a class="color-blue" href="javascript:addLineRow();">+新增</a></td>
								</tr>
							</tbody-->
							<tbody>
							    <tr>
							    	<td width="200">优秀</td>
							    	<td>
							    	    <input type="hidden" class="form-control" nullable="false" id="lineList0gradeCode" name="lineList[0].gradeCode" value="1">
							    	    <input type="text" class="form-control" nullable="false" id="lineList0minScore" name="lineList[0].minScore" <#if yxMinScore?exists>value="${yxMinScore?string('#.#')!}"</#if> vtype="number" min="0" max="999" maxLength="5" decimalLength="1">
							    	</td>
								</tr>
								<tr>
							    	<td width="200">合格</td>
							    	<td>
							    	    <input type="hidden" class="form-control" nullable="false" id="lineList1gradeCode" name="lineList[1].gradeCode" value="2">
							    	    <input type="text" class="form-control" nullable="false" id="lineList1minScore" name="lineList[1].minScore" <#if hgMinScore?exists>value="${hgMinScore?string('#.#')!}"</#if> vtype="number" min="0" max="999" maxLength="5" decimalLength="1"> 
							    	</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
function queryExamList(){
	var acadyear = $('#year').val();
     var url = "${request.contextPath}/teaexam/subjectLine/index/page?year="+acadyear;
     $('.model-div').load(url);
}

function queryLineList(subId){
     var acadyear = $('#year').val();
     var examId = $('#examId').val();
     var url = "${request.contextPath}/teaexam/subjectLine/index/page?examId="+examId+"&subjectId="+subId+"&year="+acadyear;
     $('.model-div').load(url);
}

var subIndex = 0;
<#if lineList?exists && lineList?size gt 0>
    subIndex = ${lineList?size}; 
</#if>

function addLineRow(){
    var html = '<tr><td><input type="text" class="form-control" nullable="false" id="lineList'+subIndex+'gradeName" name="lineList['+subIndex+'].gradeName"></td>'
    +'<td width="200"><input type="text" class="form-control" nullable="false" id="lineList'+subIndex+'maxScore" name="lineList['+subIndex+'].maxScore"></td>'
    +'<td width="200"><input type="text" class="form-control" nullable="false" id="lineList'+subIndex+'minScore" name="lineList['+subIndex+'].minScore"></td>'
    +'<td width="100"><a class="color-blue" href="javascript:void(0);" onclick="delLineRow(this)">删除</a></td></tr>';
    $('#tb2').append(html);
	subIndex++;
}

function delLineRow(obj){
    $(obj).parent().parent("tr").remove();
}

var isSubmit=false;
function saveLine(){
    if(isSubmit){
    	isSubmit = true;
		return;
	}
	var check = checkValue('#subForm');
	if(!check){
		isSubmit=false;
		return;
	}
	var subjectId = '${subjectId!}';
	if(subjectId == ''){
	    layerTipMsgWarn("提示","请选择一个科目!");
		return;
	}	
	var options = {
		url : '${request.contextPath}/teaexam/subjectLine/saveLine?subjectId='+subjectId,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(data.success,"失败",data.msg);
		 	}else{
		 		layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				queryLineList('${subjectId!}');
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);	
}
</script>