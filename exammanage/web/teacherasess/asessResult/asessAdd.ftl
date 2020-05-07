<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter-item">
				<label for="" class="filter-name">方案名称：</label>
				<div class="filter-content" >
					<input type="text" class="form-control" name="name" id="name" value="${teacherAsess.name!}" nullable="false" style="width:280px" maxLength="50"/>
				</div>
			</div>
            <div class="filter-item">
                <label for="" class="filter-name">年级：</label>
                <div class="filter-content" id="gradeCodesDiv">
                    <select name="gradeId" id="gradeId" oid="gradeCodes"  nullable="false" data-placeholder="请选择" class="multiselect form-control col-md-10 col-sm-10 col-xs-10 "  style="width:280px;" onChange="changeTea()">
                        <option value="">--- 请选择 ---</option>
					<#if gradeList?? && (gradeList?size>0)>
						<#list gradeList as item>
                            <option value="${item.id!}" <#if item.id==teacherAsess.gradeId?default('')>selected</#if>>${item.gradeName!}</option>
						</#list>
					</#if>
                    </select>
                </div>
            </div>
			<div class="filter-item" id="lkxzSelectDiv" >
               <label class="filter-name">本次考核方案：</label>
               <div class="filter-content">
            		<select multiple="multiple" name="convertId" id="lkxzSelect" nullable="false" data-placeholder="本次考核方案选择">
           			 </select>
           		</div>
           	</div>
			<div class="filter-item" id="lkxzSelectDivs">
               <label class="filter-name">原始参照方案：</label>
               <div class="filter-content">
            		<select multiple="multiple" name="referConvertId" id="lkxzSelects" nullable="false" data-placeholder="原始参照方案选择">
           			 </select>
           		</div>
           	</div>
           	<div class="filter-item">
				<label class="filter-name" style="width:228px;">“物化生政史地技”为选考：</label>
				<div class="filter-content" id="xuankaoTypeDivId">
					<input type="radio" name="xuankaoType" value="0">否 &nbsp;
					<input type="radio" name="xuankaoType" value="1">是 &nbsp;
				</div>
			</div>
		</div>
     </div>
</form>
</div>	
<div class="layer-footer" style="margin-bottom: -45px;">
	<a href="javascript:" class="btn btn-blue" id="arrange-commit" onclick="save('2');">确定并对比</a>
	<a href="javascript:" class="btn btn-white" id="arrange-commit" onclick="save('1');">确定</a>
	<a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
	<!--<a href="javascript:" class="btn btn-lightblue" id="arrange-commits">确定并对比</a>-->
</div>
<script>

$('#lkxzSelect').chosen({
	width:'280px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	max_selected_options:1 //当select为多选时，最多选择个数
});

$('#lkxzSelects').chosen({
	width:'280px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	max_selected_options:1 //当select为多选时，最多选择个数
});

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
function changeTea() {
		var gradeId = $("#gradeId").val();
		var lkxzSelect=$("#lkxzSelect");
		var lkxzSelects=$("#lkxzSelects");
		$.ajax({
			url:"${request.contextPath}/teacherasess/asess/convertList",
			data:{"gradeId":gradeId},
			dataType: "json",
			success: function(data){
				lkxzSelect.html("");
				lkxzSelect.chosen("destroy");
				lkxzSelects.html("");
				lkxzSelects.chosen("destroy");
				if(data.length==0){
					lkxzSelect.append("");
					lkxzSelects.append("");
				}else{
					for(var i = 0; i < data.length; i ++){
						lkxzSelect.append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
						lkxzSelects.append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
					}
				}
				$(lkxzSelect).chosen({
					width:'280px',
					results_height:'100px',
					multi_container_height:'100px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				$(lkxzSelects).chosen({
					width:'280px',
					results_height:'100px',
					multi_container_height:'100px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				//changeSubject();
			}
		});
	}


$(function(){
		//初始化多选控件
		initChosenMore("#myDiv");
	});
 
var isSubmit=false;
function save(type){
	if(isSubmit){
			return;
		}	
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    
	//考试名称问题
	var name=$("#name").val();
	
	var reg=/^[\u4E00-\u9FA5A-Za-z0-9-]+$/;
	if(!reg.test(name)){
		showMsgError("方案名称只能由数字或者英文或者中文汉字或者-组成！");
		return;
	}
	
	var convertId=$("#lkxzSelect").val();
	if(convertId==null||convertId==""){
		showMsgError("请选择本次考核方案！");
		return;
	}
	var referConvertId=$("#lkxzSelects").val();
	if(referConvertId==null||referConvertId==""){
		showMsgError("请选择原始参考方案！");
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
	
	isSubmit=true;
	var options = {
		url : "${request.contextPath}/teacherasess/asess/save?type="+type,
		dataType : 'json',
		success : function(data){
			isSubmit=false;
 			var jsonO = data;
	 		if(!jsonO.success){
	 			$("#arrange-commit").removeClass("disabled");
				layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
				return;
	 		}
	 		else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	changeAcadyear();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
		
};
</script>