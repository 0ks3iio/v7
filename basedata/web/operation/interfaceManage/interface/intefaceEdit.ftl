<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="interfaceId" class="form-control" 
			  <#if interface?exists> value="${interface.id!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">调用类型：</label>
			<div class="col-sm-9" >
			  <select name="dataType" id="dataType" class="form-control inteEdit" onChange="showInterfaceEdit()">
					<option value="1" <#if interface?exists &&interface.dataType== 1>selected</#if>>获取基础接口</option>
					<option value="2" <#if interface?exists &&interface.dataType== 2>selected</#if>>获取业务接口</option>
					<option value="3" <#if interface?exists &&interface.dataType== 3>selected</#if>>推送基础接口</option>
			  </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">接口名称：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="typeName" class="form-control" 
			  <#if interface?exists> value="${interface.typeName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">接口类型：</label>
			<div class="col-sm-9" >
			  <select name="type" id="type" class="form-control">
			     <#if interTypeList?exists && (interTypeList?size>0)>
	                <#list interTypeList as item>
	                  <option value="${item.type!}" <#if interface?exists &&interface.type?default('a')==item.type?default('b')>selected</#if>>
	                     ${item.type!}</option>
	                </#list>
                </#if>
			  </select>
			</div>
		</div>
		<div class="form-group">   
			<label class="col-sm-3 control-label no-padding-right">uri：</label>
			<div class="col-sm-9">
				<input type="text" nullable="false" maxLength="50" id="uri" class="form-control"  
			    <#if interface?exists> value="${interface.uri!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">方法类型：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="methodType" class="form-control" 
			  <#if interface?exists> value="${interface.methodType!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">数据库表名：</label>
			<div class="col-sm-9" >
			  <input type="text"  maxLength="50" id="tableName" class="form-control" 
			  <#if interface?exists> value="${interface.tableName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">栏目名：</label>
			<div class="col-sm-9" >
			  <input type="text" maxLength="50" id="fpkColumnName" class="form-control" 
			  <#if interface?exists> value="${interface.fpkColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">基础表单位字段：</label>
			<div class="col-sm-9" >
			  <input type="text" maxLength="50" id="unitColumnName" class="form-control" 
			  <#if interface?exists> value="${interface.unitColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">返回结果类型：</label>
			<div class="col-sm-9" >
			  <select name="resultType" id="resultType" class="form-control">
			     <#if resultTypeList?exists && (resultTypeList?size>0)>
	                <#list resultTypeList as item>
	                  <option value="${item.type!}" <#if interface?exists && interface.resultType?default('a')==item.type?default('b')>selected</#if>>
	                      ${item.type!}</option> 
	                </#list>
                </#if>
			  </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">返回结果名称：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="resultTypeName" class="form-control" 
			  <#if interface?exists> value="${interface.resultTypeName!}" </#if> />
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否启用：</label>
			<div class="col-sm-9" id= "isUsing">
			  <label><input type="radio"  class="wp isShow" value="1" 
				<#if interface?exists &&interface.isUsing== 1>   checked="checked" name="isUsing"</#if>><span class="lbl"> 是</span></label>
			  <label><input type="radio" class="wp isShow"  value="0" 
			    <#if interface?exists &&interface.isUsing== 0>   checked="checked" name="isUsing"</#if>><span class="lbl"> 否</span></label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">描述：</label>
			<div class="col-sm-9">
				<div class="textarea-container">
				    <#if interface?exists>
	    			<textarea nullable="false" name="description" maxLength="30" id="description"  cols="30" rows="5" class="form-control js-limit-word" value = "${interface.description!}">${interface.description!} </textarea>
	    			<#else>
	    			<textarea nullable="false" name="description" maxLength="30" id="description"  cols="30" rows="5" class="form-control js-limit-word" ></textarea>
	    			</#if>
	    			<span style="right:15px;">30</span>
	    		</div>
			</div>
		</div>
	</div>
	<script>
	$(document).ready(function() {
		$("#isUsing .isShow").change(function() {
		    var checked = $(this).attr("checked")=="checked"?true:false;
            if(checked){
                $(this).attr("checked",false);
                $(this).removeAttr("name");
            }else{
                $(this).attr("checked",true);
                $(this).attr("name","isUsing");
                $(this).parent().siblings().find('input').removeAttr("name");
                $(this).parent().siblings().find('input').attr("checked",false);
            }
        });
		showInterfaceEdit();
    });
	//根据接口类型来维护页面
	function showInterfaceEdit(){
		var dataType = $('.inteEdit').val();
		if(dataType == 1 ) {
			$('#fpkColumnName').attr("nullable","false");
			$('#unitColumnName').attr("nullable","false");
			$('#resultType').attr("nullable","false");
			$('#tableName').attr("nullable","false");
		}else {
			$('#fpkColumnName').attr("nullable","true");
			$('#unitColumnName').attr("nullable","true");
			$('#resultType').attr("nullable","true");
			$('#tableName').attr("nullable","true");
		}
	}
	</script>
</div>