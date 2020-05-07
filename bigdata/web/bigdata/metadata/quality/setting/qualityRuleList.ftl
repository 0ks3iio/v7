<div class="filter-made mb-10">
	<div class="filter-item">
		 <span class="filter-name">规则类型：</span>
		<div class="form-group">
            <input type="hidden" id="dimCode" value=""/>
            <select id="dimCode" class="form-control" name="dimCode" onChange="dimChange(this.value);">
            	<option value="all">全部</option>
      			<#list dimList as dim>
                <option value="${dim.code!}" <#if dimCode! == dim.code!>selected="selected"</#if>>${dim.name!}</option>
                </#list>           
        	</select>
        </div>
	</div>
	<div class="filter-item filter-item-right clearfix">
        <button class="btn btn-lightblue" onclick="editQualityRule();">新增规则</button>
	</div>
</div>
<#if  ruleList?exists &&ruleList?size gt 0>
<table class="tables">
	<thead>
		<tr>
			<th>规则名称</th>
			<th>规则类型</th>
			<th>所属维度</th>
			<th>计算规则</th>
			<th >操作</th>
		</tr>
	</thead>
	<tbody>
          	<#list ruleList as rule>
				<tr>
					<td>${rule.ruleName!}</td>
					<td><#if rule.ruleType! ==1>表规则<#elseif rule.ruleType! ==2>字段规则<#elseif rule.ruleType! ==3>任务规则</#if></td>
					<td>${rule.dimName!}</td>
					<td>${mcodeSetting.getMcode("DM-BG-JSGZ","${rule.computerType!}")}</td>
					<td><a class="look-over" href="javascript:void(0);" onclick="editQualityRule('${rule.id!}')">
					编辑</a><span class="tables-line">|</span>
					<a class="remove" href="javascript:void(0);" onclick="deleteQualityRule('${rule.id!}','${rule.ruleName!?html?js_string}')">
					删除</a>
					</td>
				</tr>
      	    </#list>         
	</tbody>
</table>
<#else>
<div class="no-data-common">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
		<p class="color-999">
			暂无规则
		</p>
	</div>
</div>
 </#if>
<div class="layer layer-form layer-quality-rule-edit" id="qualityRuleEditDiv">
</div>
<script>
	function dimChange(dimCode){
		$("#dimCode").val(dimCode);
		showList('rule',dimCode);
	}
	
	function deleteQualityRule(id,name){
		showConfirmTips('prompt',"提示","您确定要删除"+name+"吗？",function(){
			$.ajax({
		            url:'${request.contextPath}/bigdata/setting/quality/deleteRule',
		            data:{
		              'id':id
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
		            	layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.msg);
				 		}else{
				 			showLayerTips('success',data.msg,'t');
						  	showList('rule');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}

	function editQualityRule(id) {
    	    $.ajax({
	            url: '${request.contextPath}/bigdata/setting/quality/ruleEdit',
	            type: 'POST',
	            data: {
					id:id,
					dimCode:$("#dimCode").val()
				},
	            dataType: 'html',
	            beforeSend: function(){
			      	$('#qualityRuleEditDiv').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
			    },
	            success: function (response) {
	                $('#qualityRuleEditDiv').html(response);
	            }
            });
            var ruleSubmit = false;
            layer.open({
            	type: 1,
            	shade: .6,
            	title: '质量规则编辑',
            	btn: ['保存','取消'],
            	yes:function(index, layero){
               		if(ruleSubmit){
               			showLayerTips('warn','数据保存中,请不要重复点击','t');
						return;						
					}
			  		ruleSubmit = true;
   		       		if($('#ruleName').val()== '') {
                    	layer.tips("规则名称不能为空!", "#ruleName", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                    	ruleSubmit = false;
                    	return;
                	}             
                	
                	if($('#orderId').val()== '') {
                    	layer.tips("排序号不能为空!", "#orderId", {
	                        tipsMore: true,
	                        tips: 3
	                    });
                    	ruleSubmit = false;
                    	return;
                	}

					var  options = {
					url : "${request.contextPath}/bigdata/setting/quality/saveRule",
					dataType : 'json',
					success : function(data){
					 	if(!data.success){
					 		showLayerTips4Confirm('error',data.msg);
					 		dimSubmit = false;
					 	}else{
					 		showLayerTips('success',"保存成功",'t');
					 		showList('rule');
					 		layer.close(index);
			    		}
					},
					clearForm : false,
					resetForm : false,
					type : 'post',
					error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
				};
				$("#qualityRuleSubmitForm").ajaxSubmit(options);
            },
            end:function(){
               	$('#qualityRuleEditDiv').empty();
             },
            area: ['600px','520px'],
            content: $('.layer-quality-rule-edit')
        });
       // $('.chosen-container').width('100%');
	}
</script>