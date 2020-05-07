<form id="metadataForm">
<div class="form-horizontal form-made">
        <input type="hidden" name="typeId"  <#if interfaceType?exists> value="${interfaceType.id!}" </#if> />
        <input type="hidden" name="oldType" <#if interfaceType?exists> value="${interfaceType.type!}" </#if> />
        <div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>类别：</label>
			<div class="col-sm-8" >
		        <select name="classify"  nullable="false" id="classifyId" class="form-control" onChange="doChange();" <#if interfaceType?exists> disabled="true" </#if> >
					<option value="1" <#if interfaceType?exists &&interfaceType.classify== 1>selected</#if>>获取接口类型</option>
				    <option value="2" <#if interfaceType?exists &&interfaceType.classify== 2>selected</#if>>获取结果类型</option>
				    <option value="3" <#if interfaceType?exists &&interfaceType.classify== 3>selected</#if>>获取公用类型</option>
				</select>
			</div>
		</div>
		<#---
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>数据源：</label>
            <div class="col-sm-8">
                <select id="databaseIdSelect" name="dbId" class="form-control" nullable="false" onchange="changeDb(this.value)">
                	<option value="" dbType="">请选择数据源</omption>
                	<#if dbList?exists&&dbList?size gt 0>
						<#list dbList as db>
		                    <option <#if interfaceType.dbId! == db.id!>selected="selected"</#if> value="${db.id!}" dbType="${db.type!}">${db.name!}</omption>
		               </#list>
					</#if>     
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
            <div class="col-sm-8">
                <input type="text" name="name" id="name" class="form-control" nullable="false" maxlength="50" value="${metadata.name!}">
            </div>
        </div>
        -->
        <div class="form-group layerTableSee " style="display:none">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>元数据表：</label>
            <div class="col-sm-8">
                <select id="metadataId" name="metadataId" class="form-control" nullable="false" style="height:34px" <#if interfaceType?exists> disabled="true" </#if>>
                	<#if metadataList?exists&&metadataList?size gt 0>
						<#list metadataList as item>
		                    <option <#if interfaceType?exists && interfaceType.metadataId! == item.id!>selected="selected"</#if> value="${item.id!}">${item.tableName!}</omption>
		               </#list>
					</#if>     
                </select>
            </div>
        </div>
        <div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>类型名称：</label>
			<div class="col-sm-8" >
			  <input type="text" style="height:34px" nullable="false" maxLength="50" id="typeName" class="form-control" <#if interfaceType?exists> value="${interfaceType.typeName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>类型：</label>
			<div class="col-sm-8" >
			  <input type="text" style="height:34px" onkeyup="this.value=this.value.replace(/[^a-zA-Z]/g,'')" nullable="false" maxLength="50" id="type" class="form-control" <#if interfaceType?exists> value="${interfaceType.type!}" </#if>  />
			</div>
		</div>
		<script>
		$(document).ready(function() {
			<#if interfaceType?exists>
		        doChange();
		    </#if>
		 });
		function doChange(){
		       var classify = $("#classifyId").val();
		       if(classify == '1'){
		         $('.layerTableSee').hide();
		       }else{
		         $('.layerTableSee').show();
		       }
		    }
		</script>
</div>
</form>



