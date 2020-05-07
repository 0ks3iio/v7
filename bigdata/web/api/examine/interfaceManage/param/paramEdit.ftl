<div class="form-horizontal form-made">
	    <input type="hidden" id="interfaceId" class="form-control" 
			  <#if interfaceId?exists> value="${interfaceId!}" </#if> /> 
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="paramId" class="form-control" 
			  <#if param?exists> value="${param.id!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>参数字段：</label>
			<div class="col-sm-9" >
			  <input type="text" onkeyup="this.value=this.value.replace(/[^a-zA-Z]/g,'')" nullable="false" maxLength="50" id="paramName" class="form-control" 
			  <#if param?exists> value="${param.paramName!}" </#if> />
			</div>
		</div>
		<div class="form-group layerServerSee">   
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>url：</label>
			<div class="col-sm-9">
				<input type="text"  disabled="true"  nullable="false" maxLength="50" id="uri" class="form-control"  
			    <#if uri?exists> value="${uri!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"><font style="color:red;">*</font>数据库参数：</label>
			<div class="col-sm-9" >
			  <input type="text" onkeyup="this.value=this.value.replace(/[^a-zA-Z_]/g,'')" nullable="false" maxLength="50" id="paramColumnName" class="form-control" 
			  <#if param?exists> value="${param.paramColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否必填：</label>
			<div class="col-sm-9" id= "mandatory">
			   <#if param?exists>
				    <label><input type="radio"  class="wp isShow" value="1" 
					<#if param?exists &&param.mandatory== 1>   checked="checked" name="mandatory"</#if>><span class="lbl"> 是</span></label>
				    <label><input type="radio" class="wp isShow"  value="0" 
				    <#if param?exists &&param.mandatory== 0>   checked="checked" name="mandatory"</#if>><span class="lbl"> 否</span></label>
			   <#else>
				    <label><input type="radio" class="wp isShow" value="1" ><span class="lbl"> 是</span></label>
				    <label><input type="radio" class="wp isShow"  value="0" checked="checked" name="mandatory"><span class="lbl"> 否</span></label>
			   </#if> 
			</div>
		</div>
		<div class="form-group">   
			<label class="col-sm-3 control-label no-padding-right">微代码：</label>
			<div class="col-sm-9">
			   <input type="text" maxLength="50" id="mcodeId" class="form-control" 
			   <#if param?exists> value="${param.mcodeId!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">参数描述：</label>
			<div class="col-sm-9">
				<div class="textarea-container">
				    <#if param?exists>
	    			<textarea  name="description" maxLength="100" id="description"  cols="30" rows="10" class="form-control js-limit-word" value = "${param.description!}">${param.description!} </textarea>
	    			<#else>
	    			<textarea  name="description" maxLength="100" id="description"  cols="30" rows="10" class="form-control js-limit-word" ></textarea>
	    			</#if>
	    		</div>
			</div>
		</div>
	<script>
	$(document).ready(function() {
		$("#mandatory .isShow").change(function() {
		    var checked = $(this).attr("checked")=="checked"?true:false;
            if(checked){
                $(this).attr("checked",false);
                $(this).removeAttr("name");
            }else{
                $(this).attr("checked",true);
                $(this).attr("name","mandatory");
                $(this).parent().siblings().find('input').removeAttr("name");
                $(this).parent().siblings().find('input').attr("checked",false);
            }
        });
    });
	</script>
</div>