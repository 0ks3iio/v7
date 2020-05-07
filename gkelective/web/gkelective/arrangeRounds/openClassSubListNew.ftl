<form id="myform">
<a href="javascript:" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
    <div class="box-header">
        <h4 class="box-title">
        	${dto.gsaEnt.arrangeName!}第${dto.gkRounds.orderId!}轮
        </h4>
    </div>
    <div class="box-body">
    	 <div class="filter filter-f16">
            <div class="filter-item">
                <span class="filter-name"><font style="color:red;">*</font>每班容纳数：</span>
                <div class="filter-content">
                	<input type="text" class="form-control pull-left" <#if (dto.gkRounds.step?default(1) >2)>readonly="true"</#if>  nullable="false"  name="gkRounds.minNum" id="minNum" min="1" max="999" vtype="int" maxlength="3" value="<#if dto.gkRounds.minNum?exists>${dto.gkRounds.minNum}<#else>40</#if>">
					<span class="pull-left"> - &nbsp;</span>
					<input type="text" class="form-control pull-left" <#if (dto.gkRounds.step?default(1) >2)>readonly="true"</#if>  nullable="false"  name="gkRounds.maxNum" id="maxNum" min="1" max="999" vtype="int" maxlength="3" value="<#if dto.gkRounds.maxNum?exists>${dto.gkRounds.maxNum}<#else>50</#if>">
                </div>
            </div>
        </div>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th style="width:20%;">科目</th>
                    <th style="width:20%;">教学方式
                    <span class="fa fa-question-circle color-grey" data-toggle="tooltip" data-placement="top" title="" <#if dto.gkRounds.openClassType=="1">data-original-title="开课按照组合科目走行政班，其他走教学班，不开课则不开设这个${PCKC!}"><#else>data-original-title="走班按照教学班安排，不走班则按照行政班安排${PCKC!}"></#if></span></th>
                	<th style="width:20%;">是否需要考勤：</th>
                </tr>
            </thead>
            <tbody>
            <#if (dto.gkRounds.step?default(1) <=2)>
	              <#list dto.gksubList as item>
	                <tr>
	                    <td>
	                    <input type="hidden" name="gksubList[${item_index}].id" value="${item.id!}">
	                    <input type="hidden" name="gksubList[${item_index}].roundsId" value="${item.roundsId!}">
	                    <input type="hidden" name="gksubList[${item_index}].subjectId" value="${item.subjectId!}">
	                    ${item.subjectName!}
	                    </td>
	                    <td>
	                        <label class="pos-rel">
								<input type="radio" class="wp form-control form-radio" name="gksubList[${item_index}].teachModel" value="1"  <#if item.teachModel?default(1) == 1>checked</#if>>
								<span class="lbl"> <#if dto.gkRounds.openClassType=="1">开课<#else>走班</#if></span>
							</label>
							<label class="pos-rel">
								<input type="radio" class="wp form-control form-radio" name="gksubList[${item_index}].teachModel" value="0"  <#if item.teachModel?default(1) == 0>checked</#if>>
								<span class="lbl"> <#if dto.gkRounds.openClassType=="1">不开课<#else>不走班</#if></span>
							</label>
	                    </td>
	                    
	                    <td>
	                    	<label class="pos-rel">
								<input type="radio" class="wp form-control form-radio" name="gksubList[${item_index}].punchCard" value="1"  <#if item.punchCard?default(0) == 1>checked</#if>>
								<span class="lbl">是</span>
							</label>
							<label class="pos-rel">
								<input type="radio" class="wp form-control form-radio" name="gksubList[${item_index}].punchCard" value="0"  <#if item.punchCard?default(0) == 0>checked</#if>>
								<span class="lbl">否</span>
							</label>
	                    </td>
	                </tr>
	              </#list>
	         <#else>
	         	<#list dto.gksubList as item>
	              	<tr>
	                    <td>${item.subjectName!}</td>
	                    <td>
	                    <#if (item.teachModel?default(1) == 1)>
	                    	<#if dto.gkRounds.openClassType=="1">开课<#else>走班</#if>
	                    <#elseif (item.teachModel?default(1) == 0)>
	                    	<#if dto.gkRounds.openClassType=="1">不开课<#else>不走班</#if>
	                    </#if>
	                    </td>
	                    <td>
	                    	<#if (item.punchCard?default(0) == 0)>否<#else>是</#if>
	                    </td>
	                </tr>
	              </#list>
	         </#if>
            </tbody>
        </table>
        
        
        <div>
        	<em>温馨提示：开班进行中不能修改（历史轮次也不能修改哦！）。</em>
    		<#if (dto.gkRounds.step?default(1) <4)>
        		<a  href="javascript:" class="btn btn-blue pull-right jsocs-save" ><#if isAddorUp=="0">保存<#else>修改</#if></a>
           	</#if>
        </div>
     
    </div>
</div>
</form>
<script>
	$(function(){
		// #############提示工具#############
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
		
		$('.jsocs-save').on('click',function(){
			doGoClassSave();
		});
		
		$('.gotoLcIndex').on('click',function(){
			var url =  contextPath+'/gkelective/${arrangeId!}/arrangeRounds/index/page';
			$("#showList").load(url);
		});
	});

	var isSubmit=false;
	function doGoClassSave(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var checkVal = checkValue('#myform');
		if(!checkVal){
		 	isSubmit=false;
		 	return;
		}
		var minnum = $("#minNum").val();
		var maxNum = $("#maxNum").val();
		if(parseFloat(minnum) > parseFloat(maxNum)){
			flag = false;
			layer.tips('人数下限值不能超过上限值!', $("#minNum"), {
				tipsMore: true,
				tips: 4
			});
			return;
		}
		
		// 提交数据
		var ii = layer.load();
		var options = {
			url : '${request.contextPath}/gkelective/${arrangeId!}/goClass/save?roundsId=${roundsId!}',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					layerTipMsg(data.success,"成功",data.msg);
					var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangeRounds/index/page';
				  	//var url =  '${request.contextPath}/gkelective/${arrangeId!}/openClasSub/list/page?roundsId=${roundsId!}';
					$("#showList").load(url);
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myform").ajaxSubmit(options);
	}
</script>