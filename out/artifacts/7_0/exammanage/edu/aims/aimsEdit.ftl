<style>
.layui-layer .chosen-container-multi .chosen-choices{overflow:auto;height:80px!important;}
.layui-layer .chosen-container .chosen-results{max-height:180px;}
</style>

<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<input type="hidden" name="examId" id="examId" value="${aims.examId!}">
	<input type="hidden" name="id" id="id" value="${aims.id!}">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter-item">
                <label for="" class="filter-name">开启填报志愿：</label>
                <div class="filter-content" id="isOpenDivId">
                    <input type="radio" name="isOpen" <#if aims.isOpen == "1">checked="checked"</#if> value="1" />是 &nbsp;
                    <input type="radio" name="isOpen" <#if aims.isOpen == "0">checked="checked"</#if> value="0" />否
                </div>
            </div>
			<div class="filter-item">
				<label for="" class="filter-name">填报开始时间：</label>
				<div class="filter-content">
				    <div class="input-group"  style="width: 300px">
						<input class="form-control date-picker" vtype="data" type="text" nullable="true" name="startTime" id="examStartDate" placeholder="填报开始时间" value="${(aims.startTime?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
				    </div>
				 </div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">填报结束时间：</label>
				<div class="filter-content">
				    <div class="input-group"  style="width: 300px">
					<input class="form-control date-picker" vtype="data" type="text" nullable="true" name="endTime" id="examEndDate" placeholder="填报结束时间" value="${(aims.endTime?string('yyyy-MM-dd'))!}">
					<span class="input-group-addon">
						<i class="fa fa-calendar bigger-110"></i>
					</span>
				    </div>
				</div>
			</div>
			<div class="filter-item" id="lkxzSelectDiv">
               <label class="filter-name">可填报的学校：</label>
               <div class="filter-content">
            		<select multiple="multiple" name="schoolIds" id="lkxzSelect" data-placeholder="可填报的学校" style="width:150px;">
                        <#if unitList?? && (unitList?size>0)>
							<#list unitList as item>
								<option value="${item.id!}" <#if tbMap?? && tbMap[item.id]??>selected</#if>>${item.unitName?default('')}</option>
							</#list>
						<#else>
							<option value="">暂无数据</option>
						</#if>
           			 </select>
           		</div>
            <div>
	     </div>  
     </div>
</form>
</div>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

$('#lkxzSelect').chosen({
	width:'350px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});


$(function(){
	//初始化日期控件
	var viewContent={
		'format' : 'yyyy-mm-dd',
		'minView' : '2'
	};
	initCalendarData("#myDiv",".date-picker",viewContent);
	//初始化多选控件
	initChosenMore("#myDiv");
	$('.date-picker').next().on("click", function(){
		$(this).prev().focus();
	});
});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var examStartDate = $('#examStartDate').val();
	var examEndDate = $('#examEndDate').val();
	if(examStartDate>examEndDate){
		layerTipMsg(false,"提示","开始时间不能大于结束时间！");
		isSubmit=false;
		return;
	}
	var schoolArr = $("#lkxzSelect").val();
	if(schoolArr && schoolArr.length > 0){
	}else{
		layerTipMsg(false,"提示","可填报的学校不能为空！");
		isSubmit=false;
		return;
	}
	var options = {
		url : "${request.contextPath}/exammanage/edu/aims/save",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	changeExam();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
});
</script>