<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
		    <ul class="nav nav-tabs" role="tablist">
		    	<#if clazzList?exists && clazzList?size gt 0>
		        <li role="presentation" class="active"><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">我的班级</a></li>
		        <li role="presentation"  ><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(2)">全校班级</a></li>
		        <#else>
		        <li role="presentation"  class="active"><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(2)">全校班级</a></li>
		        </#if>
		        
		    </ul>
		</div>
		<div class="tab-content" id="itemShowDivId">
		</div>
	</div>
</div>
<script>
	$(function(){
		<#if clazzList?exists && clazzList?size gt 0>
		itemShowList(1);
		<#else>
		itemShowList(2);
		</#if>
	});
	function itemShowList(tabType){
		var url = '';
		if(tabType == 1){
	        url =  '${request.contextPath}/eclasscard/gate/attance/myClassList/page';
		}else if(tabType == 2){
	        url =  '${request.contextPath}/eclasscard/gate/attance/schoolList/page';
		}
        $("#itemShowDivId").load(url);
	}
</script>