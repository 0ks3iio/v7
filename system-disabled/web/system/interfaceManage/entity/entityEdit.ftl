<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="entityId" class="form-control" 
			  <#if entity?exists> value="${entity.id!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-9" >
			  <input type="text" disabled="true" nullable="false" maxLength="50" id="type" class="form-control" 
			  <#if type?exists> value="${type!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">属性名：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="entityName" class="form-control" 
			  <#if entity?exists> value="${entity.entityName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">数据库字段：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="entityColumnName" class="form-control" 
			  <#if entity?exists> value="${entity.entityColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">数据库字段类型：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="entityType" class="form-control" 
			  <#if entity?exists> value="${entity.entityType!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">字段名称：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="displayName" class="form-control" 
			  <#if entity?exists> value="${entity.displayName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">说明：</label>
			<div class="col-sm-9" >
			  <input type="text"  maxLength="50" id="entityComment" class="form-control" 
			  <#if entity?exists> value="${entity.entityComment!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">微代码：</label>
			<div class="col-sm-9" >
			  <input type="text"  maxLength="50" id="mcodeId" class="form-control" 
			  <#if entity?exists> value="${entity.mcodeId!}" </#if> />
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否为空：</label>
			<div class="col-sm-9" id= "mandatory">
			  <#if entity?exists>
			    <label><input type="radio" class="wp isShow" value="1"  
				<#if entity?exists &&entity.mandatory== 1>   checked="checked" name="mandatory"</#if>><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" 
			    <#if entity?exists &&entity.mandatory== 0>   checked="checked" name="mandatory"</#if>><span class="lbl"> 否</span></label>
			  <#else>
			    <label><input type="radio" class="wp isShow" value="1" checked="checked" name="mandatory"><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" ><span class="lbl"> 否</span></label>
			  </#if>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否启用：</label>
			<div class="col-sm-9" id= "isUsing">
			  <#if entity?exists>
			    <label><input type="radio"  class="wp isShow" value="1" 
				<#if entity?exists &&entity.isUsing== 1>   checked="checked" name="isUsing"</#if>><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" 
			    <#if entity?exists &&entity.isUsing== 0>   checked="checked" name="isUsing"</#if>><span class="lbl"> 否</span></label> 
			  <#else>
			    <label><input type="radio"  class="wp isShow"  value="1" checked="checked" name="isUsing"><span class="lbl"> 是</span></label>
			    <label><input type="radio"  class="wp isShow"  value="0" ><span class="lbl"> 否</span></label>
			  </#if>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否敏感字段：</label>
			<div class="col-sm-9" id= "isSensitive">
			  <#if entity?exists>
			    <label><input type="radio"  class="wp isShow" value="1" 
				<#if entity?exists &&entity.isSensitive== 1>   checked="checked" name="isSensitive"</#if>><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" 
			    <#if entity?exists &&entity.isSensitive== 0>   checked="checked" name="isSensitive"</#if>><span class="lbl"> 否</span></label> 
			  <#else>
			    <label><input type="radio"  class="wp isShow" value="1"  ><span class="lbl"> 是</span></label>
			    <label><input type="radio"  class="wp isShow"  value="0" checked="checked" name="isSensitive"><span class="lbl"> 否</span></label>
			  </#if>
			</div>
		</div>
	</div>
	<script>
	$(document).ready(function() {
		$(".isShow").change(function() {
		    var checked = $(this).attr("checked")=="checked"?true:false;
            if(checked){
                $(this).attr("checked",false);
                $(this).removeAttr("name");
            }else{
            	var that = $(this).parent().siblings().find('input');
            	var name = that.attr("name");
                $(this).attr("checked",true);
                $(this).attr("name",name);
                that.removeAttr("name");
                that.attr("checked",false);
            }
        });
    });
	</script>
</div>