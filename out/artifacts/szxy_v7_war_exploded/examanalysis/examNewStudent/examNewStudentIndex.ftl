<script src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<#if !isStu?default(false)>
	<div class="pos-abs" style="width: 230px;">
		<div class="box box-default">
			<div class="box-body">
				<div class="accordionContainer">
					<div class="panel-group" id="accordion">
	                    <div class="panel panel-default">
	                        <@treemacro.gradeClassStudentForSchoolInsetTree height="500" click="onTreeClick"/>
	                    </div>
	                </div>
				</div>
			</div>
		</div>
	</div>
</#if>
<div class="row" <#if !isStu?default(false)>style="margin-left: 240px;"</#if>>
	<div class="col-xs-12">
		<div class="box box-default">
		    <div class="box-body">
		    	<div class="filter">
					<div class="filter-item">
						<span class="filter-name">学年：</span>
						<div class="filter-content">
							<select class="form-control" id="acadyear" name="acadyear" onChange="showList()">
								<#if acadyearList?exists && (acadyearList?size>0)>
				                    <#list acadyearList as item>
					                     <option value="${item!}" <#if semester.acadyear?default(' ')==item>selected</#if>>${item!}</option>
				                    </#list>
			                    <#else>
				                    <option value="">未设置</option>
			                     </#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学期：</span>
						<div class="filter-content">
							<select class="form-control" id="semester" name="semester" onChange="showList()">
								<option value="1" <#if semester.semester?default(' ')==1>selected</#if>>第一学期</option>
								<option value="2" <#if semester.semester?default(' ')==2>selected</#if>>第二学期</option>
								<option value="3" >一学年</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<div class="btn-group" role="group">
							<a type="button"  class="btn btn-blue" href="javascript:;" id="tableId" onclick="changeType('1')">报表</a>
							<a type="button" class="btn btn-white" href="javascript:;" id="echartsId" onclick="changeType('2')">图表</a>
						</div>
					</div>
					<div class="filter-item">
						 <label class="pos-rel labelchose-w">
							<input name="gradeId" id="showAll" type="checkbox"  class="wp"  onclick="changeAll()"/>
							<span class="lbl">展示所有学年下的考试</span>
					    </label>
					</div>
					<div class="filter-item filter-item-right">
						<button id="exportId" class="btn btn-white" onclick="doExport();" disabled="disabled">导出</button>
					</div>
				</div>
			<input type="hidden" id="studentId" value="${studentId!}">
			<input type="hidden" id="studentName" >
			<input type="hidden" id="type" name="type">
			<div id="showListDiv"></div>
			<div id="showRadarDiv"></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	<#if isStu?default(false)>
		$(function(){
			$("#exportId").attr("disabled", false);
			showList();
		});
	</#if>
	function onTreeClick(event, treeId, treeNode, clickFlag){
	  if(treeNode.type == "student"){
		  var id = treeNode.id;
	  	  $('#studentId').attr('value',id);
	  	  $('#studentName').attr('value',treeNode.name);
	  	  var type = $('#type').val();
	  	  if(!type || type=="1"){
	  	  	  $("#exportId").attr("disabled", false);
	  	  }
	  	  showList();
	  }
	}
	function changeAll(){
		if(!$("#showAll").attr("checked")){
			$("#showAll").attr("checked",true);
		}else{
			$("#showAll").attr("checked",false);
		}
		showList();
	}
	function changeType(type){
		$('#type').attr('value',type);
		var studentId =$('#studentId').val();
		if(type && type=='1'){
			if(!$("#tableId").hasClass("btn-blue")){
				$("#tableId").addClass("btn-blue").removeClass("btn-white");
				$("#echartsId").removeClass("btn-blue").addClass("btn-white");
				$("#exportId").show();
				$("#showRadarDiv").hide();
				if(studentId){
					$("#exportId").attr("disabled", false);
				}
			}
		}else if(type && type=='2'){
			if(!$("#echartsId").hasClass("btn-blue")){
				$("#echartsId").addClass("btn-blue").removeClass("btn-white");
				$("#tableId").addClass("btn-white").removeClass("btn-blue");
				$("#showRadarDiv").show();
				$("#exportId").hide();
			}
		}
		showList();
	}
	function showList(){
	 	var studentId =$('#studentId').val();
	    var acadyear = $('#acadyear').val();
	    var semester = $('#semester').val();
	    var type = $('#type').val();
	    var conType = '';
	    if(!type){
	    	type="1";
	    	$('#type').attr('value',type);
	    }else if(type=='2'){
	    	conType='1';
	    }
	    var showAll='';
	    if($("#showAll").attr("checked")){
	    	showAll='1';
	    }
	    if(studentId){
	  	    $("#showListDiv").load("${request.contextPath}/examanalysis/examNewStudent/List/page?acadyear="+acadyear+
	  	    	"&semester="+semester+"&studentId="+studentId+"&type="+type+"&conType="+conType+"&showAll="+showAll);
	    }
	    showRadar();
	}
	function showRadar(toChange){
	    var type = $('#type').val();
	   	if(type && type=='2'){//只有图表时 加载雷达图
		 	var studentId =$('#studentId').val();
		    var acadyear = $('#acadyear').val();
		    var semester = $('#semester').val();
		    var examId='';
		    if(toChange && $("#examId") && $("#examId").val()){//toChange有值说明 需要取考试id
		    	examId=$("#examId").val();
		    }
		    if(studentId){
		  	    $("#showRadarDiv").load("${request.contextPath!}/examanalysis/examNewStudent/radar/page?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId+"&examId="+examId);
		    }
	    }
	}
</script>
