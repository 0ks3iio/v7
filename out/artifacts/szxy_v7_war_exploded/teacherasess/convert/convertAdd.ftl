<div class="" id="myDiv">
	<form id="parmForm">
	<div class="box box-default">
		<div class="box-body">
			<div class="form-horizontal" role="form">
				<div class="form-group">
					<label class="col-sm-2 control-title no-padding-right"><span class="form-title">新增方案</span></label>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">方案名称：</label>
					<div class="col-sm-3">
						<input type="text" name="convertName" id="convertName" class="form-control" nullable="false" maxLength="50">
					</div>
					<div class="col-sm-4 control-tips"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">年级：</label>
					<div class="col-sm-3">
						<select name="gradeId" id="gradeId" class="form-control" onChange="showAcadyearList()">
		                    <#list grades as grade>
			                     <option value="${grade.id!}">${grade.gradeName!}</option>
		                    </#list>
						</select>
					</div>
					<div class="col-sm-4 control-tips"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">“物化生政史地技”为选考：</label>
					<div class="col-sm-3" id="xuankaoTypeDivId">
						<input type="radio" name="xuankaoType" value="0">否 &nbsp;
						<input type="radio" name="xuankaoType" value="1">是 &nbsp;
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">所选考试：</label>
					<div class="col-sm-8">
						<p class="color-grey mt10 font-12"><span class="color-red">*</span> 所有考试占比之和需是100%</p>
						<table class="table table-bordered table-striped table-hover no-margin">
							<thead>
								<tr>
									<th width="20%">学年</th>
									<th width="40%">考试名称</th>
									<th width="25%">占比</th>
									<th width="15%">操作</th>
								</tr>
							</thead>
							<tbody>
								<tr>
								    <td colspan="4" class="text-center">
								    	<a class="color-blue js-addRow1" href="javascript:;" onclick="addRow1(this)">+新增</a>
								    </td>
								</tr>
							</tbody>
						</table>
						<div class="mt10" id="setDivId" style="display:none"><a class="btn btn-white js-set" href="javascript:;">两个考试组合比例设置</a><span class="color-grey font-12 ml10 mt10">需满3门考试(所选考试等内容有所修改时，请重新设置组合比例)</span></div>
					</div>
				</div>
				<!-- 组合比例设置 -->
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"></label>
					<div class="col-sm-9">
						<button  type="button" class="btn btn-blue" onclick="save('2')">确定并统计</button>
						<button  type="button" class="btn btn-white" onclick="save('1')">确定</button>
						<button  type="button" class="btn btn-white" onclick="$('.back').click();" >取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="layer layer-set" id="layerSetDiv">
		<div class="layer-content">
			<p>当学生有一门无成绩，只有两门考试成绩时，按以下比例计算折算分。</p>
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
					<tr>
						<th width="25%">两两组合</th>
						<th width="45%">考试名称</th>
						<th width="30%">占比</th>
					</tr>
				</thead>
				<form >
				<tbody>
					<tr>
						<td rowspan="2">组合1</td>
						<input type="hidden" name="groupList[0].groupName" value="组合1">
						<td><span id="groupList0_examName1"></span></td>
						<#-- 所选考试有新增修改删除操作时 置空  通过判断该值来确定某些行为-->
						<input type="hidden" id="groupList0_examId1" name="groupList[0].examId1">
						<td><input type="text" id="groupList0_scale1" style="width: 60px;" name="groupList[0].scale1" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
					<tr>
						<td><span id="groupList0_examName2"></span></td>
						<input type="hidden" id="groupList0_examId2" name="groupList[0].examId2">
						<td><input type="text" id="groupList0_scale2" style="width: 60px;" name="groupList[0].scale2" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
					<tr>
						<td rowspan="2">组合2</td>
						<input type="hidden" name="groupList[1].groupName" value="组合2">
						<td><span id="groupList1_examName1"></span></td>
						<input type="hidden" id="groupList1_examId1" name="groupList[1].examId1">
						<td><input type="text" id="groupList1_scale1" style="width: 60px;" name="groupList[1].scale1" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
					<tr>
						<td><span id="groupList1_examName2"></span></td>
						<input type="hidden" id="groupList1_examId2" name="groupList[1].examId2">
						<td><input type="text" id="groupList1_scale2" style="width: 60px;" name="groupList[1].scale2" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
					<tr>
						<td rowspan="2">组合3</td>
						<input type="hidden" name="groupList[2].groupName" value="组合3">
						<td><span id="groupList2_examName1"></span></td>
						<input type="hidden" id="groupList2_examId1" name="groupList[2].examId1">
						<td><input type="text" id="groupList2_scale1" style="width: 60px;" name="groupList[2].scale1" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
					<tr>
						<td><span id="groupList2_examName2"></span></td>
						<input type="hidden" id="groupList2_examId2" name="groupList[2].examId2">
						<td><input type="text" id="groupList2_scale2" style="width: 60px;" name="groupList[2].scale2" class="form-control inline-block number groupScaleClass" nullable="false" vtype="int" min="0" max="100">%</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	</form>
</div><!-- /.page-content -->
<script>
	$(function(){
		showAcadyearList();
		showBreadBack(goBlack,true,"新增方案");
		$('.js-set').on('click', function(){
			if(!$("#groupList0_examId1").val()){//不存在值时 
				$(".groupScaleClass").each(function(index, element) {
				    $(this).val("");
				});
				var examIds=new Array();
				var examNames=new Array();
				$('.examTr option:selected').each(function(index, element) {
					examIds[index]=$(this).val();
					examNames[index]=$(this).text();
				});
				$("#groupList0_examId1").val(examIds[0]);
				$("#groupList0_examId2").val(examIds[1]);
				$("#groupList1_examId1").val(examIds[0]);
				$("#groupList1_examId2").val(examIds[2]);
				$("#groupList2_examId1").val(examIds[1]);
				$("#groupList2_examId2").val(examIds[2]);
				
				$("#groupList0_examName1").html(examNames[0]);
				$("#groupList0_examName2").html(examNames[1]);
				$("#groupList1_examName1").html(examNames[0]);
				$("#groupList1_examName2").html(examNames[2]);
				$("#groupList2_examName1").html(examNames[1]);
				$("#groupList2_examName2").html(examNames[2]);
				
			}
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '组合比例设置',
				area: '420px',
				btn: ['确定', '取消'],
				content: $('.layer-set'),
				yes:function(index,layerDiv){
					var check = checkValue('#layerSetDiv');
				    if(!check){
				        return;
				    }
                    layer.closeAll();
            	},
	            btn2:function(index){
	            	resetGroup();
	                layer.close(index);
	            }
			});
		});
	});
	
var trMax = 0;
var trIndex=0;
var acadHtml='';
var examHtml='';
var acad = '';
var isSaveSubmit=false;
function save(type){
		if(isSaveSubmit){
			return;
		}
		
		var xuankaoType=$('input:radio[name="xuankaoType"]:checked').val();
	    if(!xuankaoType){
			layer.tips('不能为空',$("#xuankaoTypeDivId"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		
		var check = checkValue('#myDiv');
	    if(!check){
	        isSaveSubmit=false;
	        return;
	    }
		isSaveSubmit = true;
		var options = {
			url : "${request.contextPath}/teacherasess/convert/save?type="+type,
			data : {},
			dataType : 'json',
			success : function(data){
				$("#btn-saveParm").removeClass("disabled");
	 			if(!data.success){
	 				isSaveSubmit = false;
	 				layerTipMsg(data.success,"失败",data.msg);
	 			}else{
	 			if('2' == type){
	 				layer.msg("保存成功,统计中", {
						offset: 't',
						time: 2000
					});
	 			}else{
	 				layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
	 			}
					//goBlack();
					$('.back').click();
	 			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){} 
		};
		$("#parmForm").ajaxSubmit(options);
}

function addRow1(obj){
	if(trIndex == 3){
		layer.msg("最多有3门考试", {
			offset: 't',
			time: 2000
		});
		return;
	}
	if(trIndex==2){
		$("#setDivId").show();
	}
	$("#groupList0_examId1").val("");//置空
	
	$tr=$(obj).parent().parent();
	var $html='<tr><td><select name="examDtos['+trMax+'].acadyear" id="acadyear'+trMax+'" class="form-control acadTr" onChange="showExamList1('+trMax+')">'+acadHtml+'</select></td>'
			  +'<td><select name="examDtos['+trMax+'].examId" id="examId'+trMax+'" onchange="resetGroup();" class="form-control examTr" nullable="false">'+examHtml+'</select></td>'
			  +'<td><input type="text" name="examDtos['+trMax+'].scale" id="scale'+trMax+'" class="form-control inline-block number" nullable="false" vtype="int" min="0" max="100">%</td>'
			  +'<td><a class="color-blue" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';
	$tr.before($html);
	trIndex++;
	trMax++;
	if(trIndex == 3){
		$(".js-addRow1").addClass("disabled");
	}
}
function resetGroup(){
	$("#groupList0_examId1").val("");//置空
}
function deleRow(obj){
	$tr=$(obj).parent().parent();
	if(trIndex == 1){
		layer.msg("至少有1门考试", {
			offset: 't',
			time: 2000
		});
		return;
	}
	if(trIndex==3){
		$("#setDivId").hide();
	}
	resetGroup();//置空
	$tr.remove();
	trIndex--;
	if(trIndex < 3){
		$(".js-addRow1").removeClass("disabled");
	}
}

function changeTable(){
	var acadArr = $('.acadTr');
	var examArr = $('.examTr');
	if(acadArr && acadArr.length > 0){
		acadArr.html(acadHtml);
	}
	if(examArr && examArr.length > 0){
		examArr.html(examHtml);
	}
	resetGroup();//置空
}

function showExamList1(index){
	var gradeId = $('#gradeId').val();
	var acadyear = $('#acadyear'+index).val();
	var examObj = $('#examId'+index);
	
	resetGroup();//置空
	$.ajax({
		url:"${request.contextPath}/teacherasess/convert/queryExam",
		data:{gradeId:gradeId,acadyear:acadyear},
		dataType: "json",
		success: function(json){
			examObj.html('');
			var data = json.list;
			if(data.length==0){
				examObj.html("<option value='' >没有考试信息</option>");
			}else{
				var eHtml = '';
				for(var i = 0; i < data.length; i ++){
					eHtml = eHtml + "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
				}
				examObj.html(eHtml);
			}
		}
	});
}

function showExamList(){
	examHtml = '';
	var gradeId = $('#gradeId').val();
	
	resetGroup();//置空
	if(acad && acad != ''){
		$.ajax({
			url:"${request.contextPath}/teacherasess/convert/queryExam",
			data:{gradeId:gradeId,acadyear:acad},
			dataType: "json",
			success: function(json){
				var data = json.list;
				if(data.length==0){
					examHtml = "<option value='' >没有考试信息</option>";
				}else{
					for(var i = 0; i < data.length; i ++){
						examHtml = examHtml + "<option value='"+data[i].id+"' >"+data[i].name+"</option>";
					}
				}
				changeTable();
			}
		});
	}
}

function showAcadyearList(){
	var gradeId = $('#gradeId').val();
	if(!gradeId || gradeId == ''){
		return false;
	}
	resetGroup();//置空
	acadHtml = '';
	$.ajax({
		url:"${request.contextPath}/teacherasess/convert/queryAcadyear",
		data:{gradeId:gradeId},
		dataType: "json",
		success: function(json){
			var data = json.list;
			for(var i = 0; i < data.length; i ++){
				if(i == 0){
					acad = data[i];
				}
				acadHtml = acadHtml + "<option value='"+data[i]+"'>"+data[i]+"</option>";
			}
			showExamList();
		}
	});
}

function goBlack(){
	var url = '${request.contextPath}/teacherasess/convert/index/page';
	$('.model-div').load(url);
}

</script>
