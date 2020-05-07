<#if libraryList?exists&&libraryList?size gt 0>
<#if businessType! !="multimodel">
<#list libraryList as component>
<label id="library-${component.id!}" class="choice" onclick="chooseComponent('${component.id!}','${component.name!}');">
    <input type="checkbox" name="${component.id!}" id="${component.id!}" <#if businessId! ==component.id!>checked="checked"</#if>/>
    <span class="choice-name">${component.name!}</span>
</label>
</#list>
<#else>
<#list libraryList as component>
<label id="library-${component.id!}" class="choice" onclick="chooseComponent('${component.id!}','${component.favoriteName!}');">
    <input type="checkbox" name="${component.id!}" id="${component.id!}" <#if businessId! ==component.id!>checked="checked"</#if>/>
    <span class="choice-name">${component.favoriteName!}</span>
</label>
</#list>
</#if>
<#else>
<li><a href="javacript:void(0);" ><label class="pos-rel js-kind" ><span class="lbl">无可用的组件</span></label></a></li>
 </#if>
</ul>
<script type="text/javascript">
	var isSubmit=false;
	//选择组件
	function chooseComponent(id,name){
		if($("#library-"+id).children('input').prop('checked')){
			$("#businessId").val(id);
			$("#businessName").val(name);
			$("#library-"+id).siblings().find('input').prop('checked',false)
		}else{
			$("#businessId").val('');
			$("#businessName").val('');
		}
	};
	
	function addComponent2Report(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var businessId=$("#businessId").val();
		var width=$("#width").val();
		var height=$("#height").val();
 		var type=$("#type").val();
		if(businessId ==""){
			showLayerTips4Confirm('warn',"请先选择组件");
			isSubmit = false;
			return;
		}
		if(type=="6" ){
			 if(width ==""){
				showLayerTips4Confirm('warn',"请先选择窗口宽度");
				isSubmit = false;
				return;
			 }
			 <#if businessType! =="chart">
			 if(height ==""){
				showLayerTips4Confirm('warn',"请先选择窗口高度");
				isSubmit = false;
				return;
			 }
			 </#if>
		 }
		 
		 var options = {
			url:"${request.contextPath}/bigdata/data/analyse/component/save",
			dataType : 'json',
			success : function(data){
				layer.closeAll();
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.message);
		 			isSubmit = false;
		 		}else{
		 			showLayerTips('success',data.message,'t');
		 			$('.page-content').load('${request.contextPath}/bigdata/data/analyse/design?type='+type+'&reportId='+$("#reportId").val());
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#component-param-form").ajaxSubmit(options);
	}
		
    $(document).ready(function(){

	});
</script>