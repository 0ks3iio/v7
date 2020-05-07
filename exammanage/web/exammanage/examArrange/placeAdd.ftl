<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<input type="hidden" name="examId" id="examId" value="${examInfo.id!}">
		<div class="layer-addexaRoom">
			<div class="layer-body">
				<div class="filter clearfix">
				<#if isgk?exists && isgk == '1'>
				<#else>
					<div class="filter-item block">
						<label for="" class="filter-name">考生总数：</label>
						<div class="filter-content">
							<input type="text"  class="form-control" style="width:60px;" id="allnum" name="allnum" readOnly="readOnly" value="${allnum?default(0)}"/>
							<span class="lbl">人</span>
						</div>
					</div>
				</#if>
					<div class="filter-item block">
						<label for="" class="filter-name" >平均容纳：</label>
						<div class="filter-content">
							<input type="text" class="form-control" id="avgCount" name="avgCount" <#if isgk?exists && isgk == '1'><#else>placeholder="回车计算场地"</#if> maxLength="4">
							<span class="lbl">人</span>
						</div>
					</div>
					<#if isgk?exists && isgk == '1'>
				<#else>
					<div class="filter-item block">
						<label for="" class="filter-name">需考场数：</label>
						<span class="filter-name" id="placeNum">最少0间</span>
					</div>
					</#if>
					<div class="filter-item block">
						<span class="filter-name">添加考场：</span>
						<@popup.selectPlaceByBuild clickId="placeName" id="placeIds" name="placeName" handler="placeCallBack()">
							 <textarea  id="placeName" class="form-control" readOnly="readOnly"></textarea>
							 <input type="hidden" class="form-control" id="placeIds" name="placeIds" value=""/>
						</@popup.selectPlaceByBuild>
					</div>
					<p class="no-margin text-center"><em id="chooseText">已选0间</em></p>
				</div>
			</div>
		</div>
</form>
</div>	
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

function placeCallBack(){
	var placeIds=$('#placeIds').val();
	if(placeIds==""){
		$("#chooseText").html("已选0间");
	}else{
		var placeArr=placeIds.split(",");
		var selectCount=placeArr.length;
		$("#chooseText").html("已选"+selectCount+"间");
	}
	
}
$("#avgCount").bind("keyup",function(event){
	if(event.keyCode ==13){
   		doChangeAvg();
 	 }
});

$("#avgCount").blur(function(){
   	doChangeAvg();
 	
});

function doChangeAvg(){
	var avgCount=$("#avgCount").val();
	avgCount = avgCount.replace(/(^\s+)|(\s+$)/g,"");
	$("#avgCount").val(avgCount);
	if(avgCount==""){
		layer.tips("请输入正整数", "#avgCount", {
				tipsMore: true,
				tips:3		
			});
		return;
	}
	if (!/^\d+$/.test(avgCount)) {
		layer.tips("请输入正整数", "#avgCount", {
				tipsMore: true,
				tips:3		
			});
	}else{
		if(avgCount<=0){
			layer.tips("请输入正整数", "#avgCount", {
				tipsMore: true,
				tips:3		
			});
		}else{
		<#if isgk?exists && isgk == '1'>
				<#else>
			var allnum=$("#allnum").val();
			var s1=parseInt(allnum);
			var s2=parseInt(avgCount);
			var dd=Math.ceil(s1/s2);
			$("#placeNum").html("最少"+dd+"间");
			</#if>
		}
	}
}

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	var avgCount=$("#avgCount").val();
	avgCount = avgCount.replace(/(^\s+)|(\s+$)/g,"");
	$("#avgCount").val(avgCount);
	if (!/^\d+$/.test(avgCount)) {
		layer.tips("请输入正整数", "#avgCount", {
				tipsMore: true,
				tips:3		
			});
		return;
	}else{
		if(avgCount<=0){
			layer.tips("请输入正整数", "#avgCount", {
				tipsMore: true,
				tips:3		
			});
			return;
		}else{
		
		}
	}
	if(isSubmit){
        return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var options = {
		url : "${request.contextPath}/exammanage/examArrange/placeSave",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			//layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                layer.tips(jsonO.msg, $("#chooseText"), {
                    tipsMore: true,
                    tips:3
                });
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	itemShowList('2');
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