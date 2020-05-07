<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#-- <a href="javascript:toBack();" class="page-back-btn"><i class="fa fa-arrow-left"></i>返回</a>  -->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${itemName!}</h3>
	</div>
	<div class="box-body">
		<div class="explain">
			<p>
				请参考行政班数选择教室。选
				<input type="text" id="countNum" class="form-control input-sm inline-block number">
				个班
				<a class="btn btn-sm btn-blue" href="javascript:checkPlace();">确定</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="fa fa-exclamation-circle color-yellow"></span>
				<#--若教室在楼层内，请先给教室设置楼层。-->当前有${classCount!}个行政班，请至少安排${classCount!}个教室
			</p>
		</div>
		<form id="subForm" method="post">
		<div class="row">
			<div class="col-sm-6">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">可选教室 ${countPlace!}个</h3>
					</div>
				<div class="box-body js-container">
				<#if newGkBuildingDtoList?exists && newGkBuildingDtoList?size gt 0>
					<#list newGkBuildingDtoList as item>
					      <div class="building">
						  <h4 class="building-name">${item.buildingName!}</h4>
							<ul class="floor-list">	
							<#if item.foorNumList?exists && item.foorNumList?size gt 0>
							    <#list item.foorNumList as item2>
							        <li>
									   <span class="floor-name">${item2!}楼</span>
									   <ul class="classroom clearfix">
									   <#if haveBuildingIdPalceList?exists && haveBuildingIdPalceList?size gt 0>
									      <#list haveBuildingIdPalceList as item3>
									           <#if '${item3.teachBuildingId!}' == '${item.buildingId!}' && '${item3.floorNumber?default(0)!}' == '${item2!}'>						
									               <li id="${item3.id!}" onClick="changeColor('${item_index!}','${item2_index!}','${item3.id!}');" class="cheackli<#if checkMap?exists && '${checkMap[item3.id]!}' == '1'> selected</#if>"><@htmlcomponent.cutOff4List str="${item3.placeName!}" length=4 /></li>
									           </#if>
									      </#list>
									   </#if>											  
									    </ul>
								    </li>	
							    </#list>
							</#if>									    
																		
							</ul>
						</div>
					</#list>
				</#if>
				         <div class="building">
							<h4 class="building-name">其他楼</h4>
							<ul class="floor-list">
								<li>
									<ul class="classroom clearfix">
									<#if noBuildingIdPalceList?exists && noBuildingIdPalceList?size gt 0>
				                        <#list noBuildingIdPalceList as item>
										   <li id="${item.id!}" onClick="changeColor('','','${item.id!}');" class="cheackli<#if checkMap?exists && '${checkMap[item.id]!}' == '1'> selected</#if>"><@htmlcomponent.cutOff4List str="${item.placeName!}" length=4/></li>
										</#list>
				                    </#if>
									</ul>
								</li>
							</ul>														
						</div>							  							
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">已选教室&nbsp;&nbsp;<span id="chooseNum"></span></h3>
					</div>
					<div class="box-body js-container" id="yx">
					<#if newGkBuildingDtoList?exists && newGkBuildingDtoList?size gt 0>
				        <#list newGkBuildingDtoList as item>
						<div class="building" <#if !(checkMap?exists)>style="display:none;"</#if> id="hbuilding${item_index!}">
							    <h4 class="building-name">${item.buildingName!}</h4>
								<ul class="floor-list">
								<#if item.foorNumList?exists && item.foorNumList?size gt 0>
						            <#list item.foorNumList as item2>
						            <li class="tf" <#if !(checkMap?exists)>style="display:none;"</#if> id="hfoor${item_index!}${item2_index!}">
										<span class="floor-name">${item2!}楼</span>
										<ul class="classroom clearfix">
										    <#if haveBuildingIdPalceList?exists && haveBuildingIdPalceList?size gt 0>
								                <#list haveBuildingIdPalceList as item3>
								                    <#if '${item3.teachBuildingId!}' == '${item.buildingId!}' && '${item3.floorNumber?default(0)!}' == '${item2!}'>
								                        <li class="hli" <#if !(checkMap?exists && '${checkMap[item3.id]!}' == '1')>style="display:none;"</#if> id="h${item3.id!}"><@htmlcomponent.cutOff4List str="${item3.placeName!}" length=4/><a href="javascript:deletePlace('${item3.id!}');"><i class="fa fa-times-circle"></i></a></li>
								                    </#if>
								                </#list>
								            </#if>
										</ul>
									</li>
						            </#list>
						        </#if>												
								</ul>
							</div>
						</#list>
					</#if>
				        <div class="building" id="hbuilding" <#if !(checkMap?exists)>style="display:none;"</#if>>
				        <#if noBuildingIdPalceList?exists && noBuildingIdPalceList?size gt 0>
						    <h4 class="building-name">其他楼</h4>
						</#if>
							<ul class="floor-list">
					            <li>
									<ul class="classroom clearfix">
									    <#if noBuildingIdPalceList?exists && noBuildingIdPalceList?size gt 0>
							                <#list noBuildingIdPalceList as item>
							                   <li class="hli" <#if !(checkMap?exists && '${checkMap[item.id]!}' == '1')>style="display:none;"</#if> id="h${item.id!}"><@htmlcomponent.cutOff4List str="${item.placeName!}" length=4/><a href="javascript:deletePlace('${item.id!}');"><i class="fa fa-times-circle"></i></a></li>
							                </#list>
							            </#if>
									</ul>
								</li>											
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" name="placeIds" id="placeIds">
		</form>
		<div class="text-right"><a class="btn btn-blue" href="javascript:savePlaceArrang();">确定</a></div>
	</div>
</div>						


<script>
$(function(){
	$('.js-container').each(function(){
		$(this).css({
			overflow: 'auto',
			height: $(window).innerHeight()-$(this).offset().top - 98
		})
	})
	$("#chooseNum").html(calChooseNum()+"个");
})

function calChooseNum(){
	var ll=$("#yx").find("li").length;
	if(ll==0){
		return 0;
	}
	var num=0;
	$("#yx").find(".hli").each(function(){
		
		if($(this).css("display")=="none"){
            //隐藏的
        }else{
            //显示的
            num=num+1;
        }
	});
	return num;
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
    var classStr = $('#'+placeId).attr("class");
    if($('#'+placeId).hasClass("selected")){
    	 $('#'+placeId).removeClass();
    	 $('#'+placeId).addClass("cheackli");
    	 $('#h'+placeId).attr("style","display:none");
    }else{
    	 $('#'+placeId).addClass("cheackli selected");
    	 $('#hbuilding'+b).attr("style","");
       	 $('#hfoor'+b+f).attr("style","");
         $('#h'+placeId).attr("style","");
    }
    $("#chooseNum").html(calChooseNum()+"个");
}

function deletePlace(placeId){
   $('#h'+placeId).attr("style","display:none");
   $('#'+placeId).removeClass();
   $('#'+placeId).addClass("cheackli");
   $("#chooseNum").html(calChooseNum()+"个");
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
function checkPlace(){
   var countNum = $('#countNum').val();
   $('.hli').attr("style","display:none");
   var arrPrePrice = [];//定义数组  
    //取出所有housename的值，为数组赋值  
    $('.cheackli').each(function () {  
        arrPrePrice.push($(this).attr("id"))  
    });  
    if(countNum > arrPrePrice.length){
        layerTipMsgWarn("提示","数量不能大于教室总数！");
        return;
    }
     $('.cheackli').attr("class","cheackli");
    //判断数组中是否存在str  
    for(var i=0;i<arrPrePrice.length;i++){  
        if(i>(countNum-1)){
           continue;
        } 
        $('#'+arrPrePrice[i]).addClass("cheackli selected");
        $('#h'+arrPrePrice[i]).attr("style","");
        $('#yx').find('.tf').attr("style","");
        $('#yx').find('.building').attr("style","");
    } 
    $("#chooseNum").html(calChooseNum()+"个");
}
</script>