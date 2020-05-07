<div class="filter header_filters">
	<div class="filter-item block">
        <span class="filter-name">学年：</span>
		<div class="filter-content">
			<select class="form-control" id="acadyear" onChange="doChangeDate2();">
			<#if (acadyearList?size>0)>
				<#list acadyearList as item>
				<option value="${item!}" <#if item==acadyear>selected="selected"</#if>>${item!}学年</option>
				</#list>
			</#if>
			</select>
		</div>
    </div>
    <div class="filter-item block">
    	<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select class="form-control" id="semester" onChange="doChangeDate2();">
			 ${mcodeSetting.getMcodeSelect("DM-XQ", semester, "0")}
			</select>
		</div>
    </div>
    <div class="filter-item block">
        <span class="filter-name">考试：</span>
        <div style="float:left;width:50%">
		   <select class="form-control" multiple vtype="selectMore" name="examId" id="examId"  data-placeholder="选择考试" >
		   </select>
        </div>
    </div>
    <p class="no-margin text-center" style="height: 25px"><em id="t"></em></p>
</div>

    <#-- 确定和取消按钮 -->
<div class="layer-footer">
    <button class="btn btn-lightblue" id="result-commit">确定</button>
    <button class="btn btn-grey" id="result-close">取消</button>
</div>

<script type="text/javascript">
	var examId='${examId!}';
	$(function(){
		//初始化多选控件
		initChosenMore(".header_filters");
		doChangeDate2();
	});	
	function doChangeDate2(){
		var s=false;
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/hierarchy/findList2',
		    data: {'searchAcadyear':acadyear,'searchSemester':semester,'isgkExamType':'1'},  
		    type:'post',  
		    success:function(data) {
		    	$("#examId").html("");
		    	$("#examId").chosen("destroy");
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		if(jsonO[index].id!=examId){
			    			var htmlOption="<option ";
		    				htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    				$("#examId").append(htmlOption);
			    		}
			    		
			    	});
			    }
		    	$("#examId").chosen({
					width:'100%',
					results_height:'50px',
					multi_container_height:'50px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
		    	
		    }
		});
	}

	
// 取消按钮操作功能
$("#result-close").on("click", function(){
    doLayerOk("#result-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
// 确定按钮操作功能
var isSubmit=false;
$("#result-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var i = 0;
	var copyExamIds="";
	$('#examId option:selected').each(function(){
		if(i==0){
			copyExamIds=copyExamIds+$(this).val();
		}else{
			copyExamIds=copyExamIds+","+$(this).val();
		}
		i++;
	});
	if(i==0){
		layer.alert('没有要复用的对象！',{icon:7});
		isSubmit=false;
		return;
	}
	$.ajax({
		    url:'${request.contextPath}/scoremanage/hierarchy/copySetSave',
		    data: {'copyExamIds':copyExamIds,'examId':examId},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.success){
		    		layer.closeAll();
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
					searchList();
		 		}else{
	 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 				isSubmit=false;
				}
		    }
		});
	
 });    

</script>
