<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="entityId" class="form-control" 
			  <#if entity?exists> value="${entity.id!}" </#if> />
		   <input type="text" nullable="false" maxLength="50" id="metadataId" class="form-control" 
			  <#if metadataId?exists> value="${metadataId!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-8" >
			  <input type="text" disabled="true" nullable="false" maxLength="50" id="type" class="form-control" 
			  <#if type?exists> value="${type!}" </#if> />
			</div>
		</div>
		<div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>元数据字段：</label>
            <div class="col-sm-8">
                <select id="columnId" name="columnId" class="form-control" nullable="false" >
                	<#if columnList?exists&&columnList?size gt 0>
						<#list columnList as item>
		                    <option <#if entity?exists && entity.entityColumnName! == item.columnName!>selected="selected"</#if> value="${item.id!}">${item.name!}</omption>
		               </#list>
					</#if>     
                </select>
            </div>
        </div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>属性名：</label>
			<div class="col-sm-8" >
			  <input type="text" nullable="false" maxLength="50" id="entityName" class="form-control" 
			  <#if entity?exists> value="${entity.entityName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据库字段长度：</label>
			<div class="col-sm-8" >
			  <input type="text" nullable="false" maxLength="50" id="entityLength" class="form-control"  vtype="number"
			  <#if entity?exists> value="${entity.entityLength!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">字段关联的外键：</label>
			<div class="col-sm-8" >
			  <input type="text"  maxLength="100" id="relationColumn" class="form-control" 
			  <#if entity?exists> value="${entity.relationColumn!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">说明：</label>
			<div class="col-sm-8" >
			  <input type="text"  maxLength="50" id="entityComment" class="form-control" 
			  <#if entity?exists> value="${entity.entityComment!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">微代码：</label>
			<div class="col-sm-8" >
			  <input type="text"  maxLength="50" id="mcodeId" class="form-control" 
			  <#if entity?exists> value="${entity.mcodeId!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>字段类别：</label>
			<div class="col-sm-8" >
			  <select name="columnProp" id="columnProp" class="form-control inteEdit" onChange="showInterfaceEdit()">
					<option value="0" <#if entity?exists &&entity.columnProp== 0>selected</#if>>默认</option>
					<option value="1" <#if entity?exists &&entity.columnProp== 1>selected</#if>>主键</option>
					<option value="2" <#if entity?exists &&entity.columnProp== 2>selected</#if>>删除字段</option>
					<option value="3" <#if entity?exists &&entity.columnProp== 3>selected</#if>>单位类型</option>
			  </select>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否有默认值：</label>
			<div class="col-sm-8" id= "isDefault">
			  <#if entity?exists>
			    <label><input type="radio" class="wp isShow" value="1"  
				<#if entity?exists &&entity.isDefault== 1>   checked="checked" name="isDefault"</#if>><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" 
			    <#if entity?exists &&entity.isDefault== 0>   checked="checked" name="isDefault"</#if>><span class="lbl"> 否</span></label>
			  <#else>
			    <label><input type="radio" class="wp isShow" value="1" checked="checked" name="isDefault"><span class="lbl"> 是</span></label>
			    <label><input type="radio" class="wp isShow"  value="0" ><span class="lbl"> 否</span></label>
			  </#if>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否非空：</label>
			<div class="col-sm-8" id= "mandatory">
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
			<div class="col-sm-8" id= "isUsing">
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
			<div class="col-sm-8" id= "isSensitive">
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