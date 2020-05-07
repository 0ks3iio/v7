<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<input type="hidden" id="type" value="${type!}" />
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" style="width:188px;" onchange="findImportIndex()">
						<#if acadyearList?exists && acadyearList?size gt 0>
					     	<#list acadyearList as item>
					     		<option value="${item!}" <#if acadyear?default('')== item >selected</#if> >${item!}学年</option>
					     	</#list>
					     <#else>
					     	<option value="">--未设置--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" style="width:188px;"  onchange="findImportIndex()">
						${mcodeSetting.getMcodeSelect('DM-XQ',semester?default('0'),'0')}
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:" class="btn btn-blue" onclick="toBackTeachClass()">返回</a>
			</div>
		</div>
		<div id="importContentDiv">
		</div>
	</div>
</div>
<script>
	$(function(){
		findImportIndex()
	})
	function findImportIndex(){
		var url="";
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		if(acadyear==""){
			layer.tips("学年不能为空", "#acadyear", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(semester==""){
			layer.tips("学期不能为空", "#semester", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		var parmUrl="acadyear="+acadyear+"&semester="+semester;
		<#if type?default('')=='1'>
			url='${request.contextPath}/basedata/teachclass/classImport/index/page?'+parmUrl;
		<#elseif type?default('')=='2'>
			url='${request.contextPath}/basedata/teachclass/studentImport/index/page?'+parmUrl;
		</#if>
		if(url!=""){
			$("#importContentDiv").load(url);
		}
	}
	function toBackTeachClass(){
		var url =  '${request.contextPath}/basedata/teachclass/indexItem/page?showTabType=${showTabType!}';
		$("#indexContent").load(url);
	}
</script>