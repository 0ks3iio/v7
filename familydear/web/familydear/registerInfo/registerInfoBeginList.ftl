<#if index == '2'>
<div class="filter" id="searchType">
	<div class="filter-item">
		<span class="filter-name">年度：</span>
        <div class="filter-content">
            <select name="year" id="year" class="form-control" onchange="searchEndList()" style="width:135px">
                <#list minYear..maxYear as item>
                    <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                </#list>			                       
            </select>
        </div>
	</div>
	<div class="filter-item">
			<span class="filter-name">访亲轮次名称 ：</span>
			<div class="filter-content">
				<input type="text" class="typeahead scrollable form-control"  autocomplete="off" data-provide="typeahead" id="activityName" value="${activityName!}">
			</div>
		</div>
	<div class="filter-item">
			 <button type="button" class="btn btn-default" onClick="searchEndList();">
				 <i class="fa fa-search"></i>
			 </button>
	    </div>
</div>	
</#if>	
<div id="a1" class="tab-pane active">
<div>
	<div class="explain-default explain-blue no-margin" id="activityContent">
<#if famDearActivities?exists && famDearActivities?size gt 0>
    <#list famDearActivities as active>
		<div>
			<h4><b>${active.title!}</b></h4>
			<#--<div>访亲需求：</div>-->
			<#--<div class="color-grey">${active.require!}</div>-->
			<div class="mt10">文件内容：</div>
            <table class="color-grey" style="width:100%">
                <tr>
                    <td class="content" id="content2" style="word-break:break-all; word-wrap:break-word;">${active.fileContent!}</td>
                </tr>
            </table>
			<#--<div class="color-grey">${active.fileContent!}</div>-->
			<div class="text-right">
				<a class="color-blue js-more-infor" href="#" id="${active.id!}">折叠</a>
			</div>
		</div>
			<div class="row">
				<#if arrangeList?exists && arrangeList?size gt 0>
				    <#list arrangeList as arrange>
				        <#if arrange.activityId == active.id>
							<#if arrange.canLowTime>
							<#--<div><a class="btn btn-white btn-sm disabled" href="#">已结束</a></div>-->
							<#else>
				            <div class="col-sm-4"> 
								<div class="box-default box-primary mt20">
									<div class="box-body pos-rel">
										<#--<p class="font-16"><b>${arrange.rural!}</b></p>-->
										<div class="color-grey mb10">
											<div>批次：${arrange.batchType!}</div>
											<div>活动时间：${(arrange.startTime?string('yyyy.MM.dd'))?if_exists}~${(arrange.endTime?string('yyyy.MM.dd'))?if_exists}</div>
											<div>报名时间：${(arrange.applyTime?string('yyyy.MM.dd'))?if_exists}~${(arrange.applyEndTime?string('yyyy.MM.dd'))?if_exists}</div>
											<div>干部人数：${arrange.peopleNumber!}人</div>
											<div id='${arrange.id!}rural' value="${arrange.rural!}">结亲村：${arrange.rural!}</div>
										</div>
										
										<#assign a = 0>
										<#if familyDearRegisters?exists && familyDearRegisters?size gt 0>
										    <#list familyDearRegisters as register>
										        <#if register.activityId == active.id && register.arrangeId == arrange.id>
										            <#if register.status == 1>
										               <#if index=='1'>    
										                  <div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${active.id!}','${arrange.id!}','${register.id!}','0');">取消报名</a></div>						               
										                  <span class="label-extra label-extra-green">审核中</span>
				                                       <#else>
				                                          <div><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></div>
				                                       </#if>
										     
										            <#elseif register.status == 2>
										                <div class="color-blue mt16 font-16">报名成功</div>
										            <#elseif register.status == -1>		
										                <#if index=='1'>				                
										                <div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${active.id!}','${arrange.id!}','${register.id!}','1');">重新报名</a><span class="color-red ml10"><i class="fa fa-exclamation-circle color-yellow"></i> ${register.remark!}</span></div>
														<span class="label-extra label-extra-red">未通过</span>
										                <#else>
										                      <div><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></div>
														      <span class="label-extra label-extra-red">未通过</span>
										                </#if>
										            </#if>
										            <#assign a = a + 1>
										        </#if>						        
										    </#list>
										</#if>
										<#if a == 0>
										     <#if index=='1'>
										     	<#if arrange.canApply>   
													<#if arrange.canFull>
										     			<div><a class="btn btn-white btn-sm disabled" href="#">已报满</a></div>
										     		<#else>   
										     			<div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${active.id!}','${arrange.id!}','','1');">我要报名</a></div>
										     		</#if>
										     	<#else>
										     		<#if arrange.canLowTime>
										     			<#--<div><a class="btn btn-white btn-sm disabled" href="#">已结束</a></div>-->
										     		<#else>
										     			<div><a class="btn btn-white btn-sm disabled" href="#">未开始</a></div>
										     		</#if>
										     	</#if>
										     <#else>
										     <#--<div><a class="btn btn-white btn-sm disabled" href="#">已结束</a></div>-->
										     </#if>
										</#if>
										
									</div>
								</div>
						   </div>
							</#if>
				            
						
						</#if>
					</#list>
				</#if>
			</div>
	</#list>
	<#else>
			<tr>
				<td  align="center">暂无数据</td>
			</tr>
	</#if>
	</div>
</div>
<div class="layer layer-see"></div>
<script>
var isSubmit=false;
function doRegister(activityId,arrangeId,id,status){
    if(isSubmit){
    	isSubmit = true;
		return;
	}
    var msg = "";
    if(status == '0'){
        msg = "确定取消报名吗？";
    }else if(status == '1'){
    	var con=$("#"+arrangeId+"rural").attr("value");
        msg = "该批次结亲村为"+con+"，您是否确认报名？";
    }
    var index = layer.confirm(msg, {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:"${request.contextPath}/familydear/registerInfo/doRegister",
		        data:{activityId:activityId,arrangeId:arrangeId,id:id,status:status},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"报名失败！",data.msg);
		 		      layer.closeAll();
		 		      layerTipMsg(data.success,"报名失败！",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
		 		      if(status == '0'){
					      layerTipMsg(data.success,"取消报名成功！",data.msg);
					  }else if(status == '1'){
					  	layerTipMsg(data.success,"报名成功！",data.msg);
					  }
                      var url = "${request.contextPath}/familydear/registerInfo/beginRegister?index=1";
                      $('#bmTblDiv').load(url);
    		       }
		        }
	        });
	})

}

$(function () {
    $("#activityContent").find("table[class='color-grey']").each(function () {
		var id = $(this).parent().find("a[class='color-blue js-more-infor']").attr("id");
		if(id){
            var isShow = $.cookie(id);
            if(isShow=="true"){
                $(this).parent().find("a[class='color-blue js-more-infor']").text("折叠");
                $(this).show();
            }else if((isShow=="false")){
                $(this).parent().find("a[class='color-blue js-more-infor']").text("展开");
                $(this).hide();
            }
		}
    })
    // var isShow = $.cookie("isShow");
    // if(isShow=="true"){
     //    $(".js-more-infor").text("折叠").parent().siblings(".color-grey").show();
	// }else if((isShow=="false")){
     //    $(".js-more-infor").text("展开").parent().siblings(".color-grey").hide();
	// }
})

$(".js-more-infor").click(function(){
    var id = $(this).attr("id");
	if($(this).text() === "展开"){
        $.cookie(id, true);
		$(this).text("折叠").parent().siblings(".color-grey").show();
	}else{
        $.cookie(id, false);
		$(this).text("展开").parent().siblings(".color-grey").hide();
	}
})

function searchEndList(){
    var year = $('#year').val();
    var activityName = $('#activityName').val();
    var url = "${request.contextPath}/familydear/registerInfo/beginRegister?index=2&year="+year+"&activityName="+activityName;
    $('#bmTblDiv').load(url);
}

</script>