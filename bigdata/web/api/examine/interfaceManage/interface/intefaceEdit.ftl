<div class="form-horizontal form-made">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="interfaceId" class="form-control" 
			  <#if interface?exists> value="${interface.id!}" </#if> />
		   <input type="text" nullable="false" maxLength="50" id="metadataId" class="form-control" 
			  <#if interface?exists> value="${interface.metadataId!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>调用类型：</label>
			<div class="col-sm-8" >
			  <select name="dataType" id="dataType" class="form-control inteEdit" onChange="showInterfaceEdit()">
					<option value="1" <#if interface?exists &&interface.dataType== 1>selected</#if>>基础数据</option>
					<option value="2" <#if interface?exists &&interface.dataType== 2>selected</#if>>业务数据</option>
					<option value="3" <#if interface?exists &&interface.dataType== 3>selected</#if>>保存数据</option>
					<option value="4" <#if interface?exists &&interface.dataType== 4>selected</#if>>更新数据</option>
			  </select>
			</div>
		</div>
		<div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="typeName" class="form-control" 
			  <#if interface?exists> value="${interface.typeName!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>接口类型：</label>
			<div class="col-sm-8" >
			  <select name="type" id="type" class="form-control" style="height:34px">
			     <#if interTypeList?exists && (interTypeList?size>0)>
	                <#list interTypeList as item>
	                  <option value="${item.type!},${item.typeName!}" <#if interface?exists &&interface.type?default('a')==item.type?default('b')>selected</#if>>
	                     ${item.type!}</option>
	                </#list>
                </#if>
			  </select>
			</div>
		</div>
		<div class="form-group">   
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>url：</label>
			<div class="col-sm-8">
				<input type="text" nullable="false" maxLength="50" id="uri" class="form-control"  
			    <#if interface?exists> value="${interface.uri!}" </#if> />
			</div>
		</div>
		<#--
		<div class="form-group">
            <label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据源：</label>
            <div class="col-sm-8">
                <select id="metadataId" name="metadataId" class="form-control" nullable="false">
                	<option value="" dbType="">请选择数据源</omption>
                	<#if dbList?exists&&dbList?size gt 0>
						<#list dbList as db>
		                    <option <#if interface?exists && interface.metadataId! == db.id!>selected="selected"</#if> value="${db.id!}" dbType="${db.type!}">${db.name!}</omption>
		               </#list>
					</#if>     
                </select>
            </div>
        </div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据库表名：</label>
			<div class="col-sm-8" >
			  <input type="text"  maxLength="50" id="tableName" class="form-control" 
			  <#if interface?exists> value="${interface.tableName!}" </#if> />
			</div>
		</div>
		-->
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">栏目名：</label>
			<div class="col-sm-8" >
			  <input type="text" maxLength="50" id="fpkColumnName" class="form-control" 
			  <#if interface?exists> value="${interface.fpkColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>返回结果类型：</label>
			<div class="col-sm-8" >
			  <select name="resultType" id="resultType" class="form-control" nullable="false" onChange="setMetadataId()" style="height:34px">
			     <#if resultTypeList?exists && (resultTypeList?size>0)>
	                <#list resultTypeList as item>
	                  <option id = "${item.metadataId!}" value="${item.type!}" <#if interface?exists && interface.resultType?default('a')==item.type?default('b')>selected</#if>>
	                      ${item.type!}</option> 
	                </#list>
                </#if>
			  </select>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">方法类型：</label>
			<div class="col-sm-8">
			  <select name="methodType" id="methodType" class="form-control" style="height:34px">
					<option value="get"  <#if interface?exists &&interface.methodType== 'get'>selected</#if>>get</option>
					<option value="post" <#if interface?exists &&interface.methodType== 'post'>selected</#if>>post</option>
			  </select>
			</div>
		</div> 
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否启用：</label>
			<div class="col-sm-8" id= "isUsing">
			   <#if interface?exists>
				    <label><input type="radio"  class="wp isShow" value="1" 
				    <#if interface?exists &&interface.isUsing== 1>   checked="checked" name="isUsing"</#if>><span class="lbl"> 是</span></label>
			        <label><input type="radio" class="wp isShow"  value="0" 
			        <#if interface?exists &&interface.isUsing== 0>   checked="checked" name="isUsing"</#if>><span class="lbl"> 否</span></label>
			   <#else>
				    <label><input type="radio" class="wp isShow" value="1"  checked="checked" name="isUsing"><span class="lbl"> 是</span></label>
				    <label><input type="radio" class="wp isShow"  value="0"><span class="lbl"> 否</span></label>
			   </#if> 
			</div>
		</div> 
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>描述：</label>
			<div class="col-sm-8">
				<div class="textarea-container">
				    <#if interface?exists>
	    			<textarea  name="description"  nullable="false" maxLength="30" id="description"  cols="30" rows="5" class="form-control js-limit-word" value = "${interface.description!}">${interface.description!}</textarea>
	    			<#else>
	    			<textarea  name="description"  nullable="false" maxLength="30" id="description"  cols="30" rows="5" class="form-control js-limit-word" ></textarea>
	    			</#if>
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
	setMetadataId();
   });
//根据接口类型来维护页面
function showInterfaceEdit(){
	var dataType = $('.inteEdit').val();
	if(dataType == 1 ) {
		$('#resultType').attr("nullable","false");
		$('#tableName').attr("nullable","false");
	}else {
		$('#resultType').attr("nullable","true");
		$('#tableName').attr("nullable","true");
	}
}
function setMetadataId(){
   var options=$("#resultType option:selected");
   var metadataId = options.attr("id");
   $('#metadataId').val(metadataId);
}

</script>
</div>