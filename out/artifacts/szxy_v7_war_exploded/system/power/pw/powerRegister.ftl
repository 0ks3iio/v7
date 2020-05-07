<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">权限名称：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" <#if isSee?default(false)> disabled="true" </#if> id="powerName" class="form-control" 
			  <#if sysPower?exists> value="${sysPower.powerName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">权限特征值：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" <#if isSee?default(false)> disabled="true" </#if> id="value" class="form-control"
			  <#if sysPower?exists> value="${sysPower.value!}" </#if>  />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">权限类型：</label>
			<div class="col-sm-9" >
			        <select name="source"  nullable="false" id="source" class="form-control" onChange="doChange();" <#if isSee?default(false)> disabled="true" </#if> >
						<option value="1" <#if sysPower?exists &&sysPower.source== 1>selected</#if>>默认</option>
						<option value="2" <#if sysPower?exists &&sysPower.source== 2>selected</#if>>其它</option>
					</select>
			</div>
		</div>
		<div class="form-group layerServerSee" style="display:none">   
			<label class="col-sm-3 control-label no-padding-right">第三方ap：</label>
			<div class="col-sm-9">
				<select name="serverId" id="serverId" nullable="false" class="form-control"  <#if isSee?default(false)> disabled="true" </#if>>
						<#if serverList?exists && (serverList?size>0)>
		                    <#list serverList as server>
			                     <option value="${server.id!}" <#if serverId?exists && serverId == server.id>selected</#if>>${server.name!}</option>
		                    </#list>
		                <#else>
					        <option value="">--请选择--</option>
	                    </#if>
				</select>
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否启用：</label>
			<div class="col-sm-9" id= "isActive">
			  <label><input type="radio"  class="wp isShow" <#if isSee?default(false)> disabled="true" </#if> value="1" 
				<#if sysPower?exists &&sysPower.isActive== '1'>   checked="checked" name="isActive"</#if>><span class="lbl"> 是</span></label>
			  <label><input type="radio" class="wp isShow" <#if isSee?default(false)> disabled="true" </#if> value="0" 
			    <#if sysPower?exists &&sysPower.isActive== '0'>   checked="checked" name="isActive"</#if>><span class="lbl"> 否</span></label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">内容：</label>
			<div class="col-sm-9">
				<div class="textarea-container">
				    <#if sysPower?exists>
	    			<textarea name="description" maxLength="255" id="description" <#if isSee?default(false)> disabled="true" </#if> cols="30" rows="10" class="form-control js-limit-word" value = "${sysPower.description!}">${sysPower.description!} </textarea>
	    			<#else>
	    			<textarea name="description" maxLength="255" id="description" <#if isSee?default(false)> disabled="true" </#if> cols="30" rows="10" class="form-control js-limit-word" ></textarea>
	    			</#if>
	    			<#if !isSee?default(false)>
	    			<span style="right:15px;">255</span>
	    			</#if>
	    		</div>
			</div>
		</div>
	</div>
	<script>
	$(document).ready(function() {
		   <#if sysPower?exists &&sysPower.source== 2>
		    doChange();
		   </#if>
			$("#isActive .isShow").change(function() {
			    var checked = $(this).attr("checked")=="checked"?true:false;
	            if(checked){
	                $(this).attr("checked",false);
	                $(this).removeAttr("name");
	            }else{
	                $(this).attr("checked",true);
	                $(this).attr("name","isActive");
	                $(this).parent().siblings().find('input').removeAttr("name");
	                $(this).parent().siblings().find('input').attr("checked",false);
	            }
	        });
	 });
	
	function doChange(){
	       var recordType = $("#source").val();
	       if(recordType == '1'){
	         $('.layerServerSee').hide();
	       }else{
	         $('.layerServerSee').show();
	       }
	    }
	</script>
	
</div>