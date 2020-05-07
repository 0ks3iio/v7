<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<form id="subForm" method="post">
<input type="hidden" name="placeIds" id="placeIds">
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${itemName!}</h3>
	</div>
	<div class="box-body place-div">
		<#if newGkBuildingDtoList?exists && newGkBuildingDtoList?size gt 0>
		<#list newGkBuildingDtoList as item>
		      <h4 class="form-title<#if item_index gt 0> mt30</#if>">
				<b>${item.buildingName!}</b><a class="font-14 color-blue ml20 check-all" build-index="${item_index}" check-val="0" href="javascript:;">全选</a>
			  </h4>
				<#if item.foorNumList?exists && item.foorNumList?size gt 0>
			    <#list item.foorNumList as item2>
				   <p class="mt30">${item2!}楼</p>
				   <ul class="classroom clearfix ul-${item_index}">
				   <#if haveBuildingIdPalceList?exists && haveBuildingIdPalceList?size gt 0>
				      <#list haveBuildingIdPalceList as item3>
				           <#if '${item3.teachBuildingId!}' == '${item.buildingId!}' && '${item3.floorNumber?default(0)!}' == '${item2!}'>						
				               <li id="${item3.id!}" <#if hasPlaceIds?default('')?index_of(item3.id) == -1>onClick="changeColor('${item_index!}','${item2_index!}','${item3.id!}');"</#if> class="cheackli<#if checkMap?exists && '${checkMap[item3.id]!}' == '1'> selected</#if><#if hasPlaceIds?default('')?index_of(item3.id) != -1> disabled</#if>"><@htmlcomponent.cutOff4List str="${item3.placeName!}" length=4 /></li>
				           </#if>
				      </#list>
				   </#if>											  
				    </ul>
				</#list>
				</#if>									    
		</#list>
		</#if>
		<h4 class="form-title<#if newGkBuildingDtoList?exists && newGkBuildingDtoList?size gt 0> mt30</#if>">
			<b>其他楼</b><a class="font-14 color-blue ml20 check-all" build-index="other" check-val="0" href="javascript:;">全选</a>
		  </h4>
		<ul class="classroom mt30 clearfix ul-other">
			<#if noBuildingIdPalceList?exists && noBuildingIdPalceList?size gt 0>
                <#list noBuildingIdPalceList as item>
				   <li id="${item.id!}" <#if hasPlaceIds?default('')?index_of(item.id) == -1>onClick="changeColor('','','${item.id!}');"</#if> class="cheackli<#if checkMap?exists && '${checkMap[item.id]!}' == '1'> selected</#if><#if hasPlaceIds?default('')?index_of(item.id) != -1> disabled</#if>"><@htmlcomponent.cutOff4List str="${item.placeName!}" length=4/></li>
				</#list>
            </#if>
		</ul>														
	</div>
</div>
</form>
<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-blue" href="javascript:savePlaceArrang();">下一步</a>
	<span>
	共<b class="color-orange has-num">${classCount?default(0)}</b>个行政班<#if exCount gt 0>，额外还需要<b class="color-orange has-num">${exCount}</b>个教室</#if>，
	</span>
	<span>
		已选<b class="color-orange has-num" id="chooseNum">2</b>个教室
	</span>
</div>
<script>
$(function(){
	$("#chooseNum").html(calChooseNum());
	
	$('.check-all').off('click').on('click',function(){
		var cv = $(this).attr('check-val');
		var bi = $(this).attr('build-index');
		var txt = '取消';
		if(cv=='0'){
			$('.ul-'+bi).find('li:not(.disabled)').addClass('selected');
			$(this).attr('check-val','1');
		} else {
			txt = '全选';
			$(this).attr('check-val','0');
			$('.ul-'+bi).find('li:not(.disabled)').removeClass('selected');
		}
		$(this).html(txt);
		$("#chooseNum").html(calChooseNum());
	});
})

function calChooseNum(){
	var ll=$(".place-div").find("li.selected").length;
	if(!ll){
		ll=0;
	}
	return ll;
}

var isSavePlace=false;
function savePlaceArrang(){
	if(isSavePlace){
		return;
	}
	isSavePlace=true;
    var divideId = '${divideId!}';
    var arrayItemId = '${arrayItemId!}';
    var placeIds = '';//定义数组  
    //取出所有arrPlaceId的值，为数组赋值  
    $('.selected').each(function () {  
        //placeIds.push($(this).attr("id"));
        placeIds = placeIds + $(this).attr("id") + ",";
    });   
    if(placeIds.length == 0){
        layerTipMsgWarn("提示","请至少选择一个教室！");
        isSavePlace=false;
        return;
    }
    $('#placeIds').val(placeIds);
	var options = {
		url:"${request.contextPath}/newgkelective/${divideId!}/placeArrange/placeArrangeSave?arrayItemId="+arrayItemId,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		isSavePlace=false;
		 		return;
		 	}else{
		 		layer.closeAll();
				layer.msg(jsonO.msg, {offset: 't',time: 2000});
				if(arrayItemId == '' || arrayItemId == 'undefined'){
				    arrayItemId = jsonO.jobId;
				}
				//setTimeout(function(){
					url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/list?arrayItemId='+arrayItemId+"&arrayId=${arrayId!}";
                	$("#showList").load(url);
				//},500)
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		//error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}

function changeColor(b,f,placeId){
    if($('#'+placeId).hasClass("selected")){
    	 $('#'+placeId).removeClass();
    	 $('#'+placeId).addClass("cheackli");
    }else{
    	 $('#'+placeId).addClass("cheackli selected");
    }
    $("#chooseNum").html(calChooseNum());
}

function toBack(){
   <#--
   var divideId = '${divideId!}';
   url='${request.contextPath}/newgkelective/'+divideId+'/placeArrange/index/page?arrayId=${arrayId!}';
   $("#showList").load(url);-->
   <#if arrayId?default('')==''>
	   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${arrayItemId!}';
		$("#showList").load(url);
   <#else>
	   var url =  '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		$("#showList").load(url);
   </#if>
}

var pageTitle='教室方案';
<#if arrayItemId?default('')==''>
	pageTitle='新增教室方案';
</#if>
showBreadBack(toBack, false, pageTitle);
</script>