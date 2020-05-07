<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="paramId" class="form-control" 
			  <#if param?exists> value="${param.id!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">参数字段：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="paramName" class="form-control" 
			  <#if param?exists> value="${param.paramName!}" </#if> />
			</div>
		</div>
		<div class="form-group layerServerSee">   
			<label class="col-sm-3 control-label no-padding-right">uri：</label>
			<div class="col-sm-9">
				<input type="text"  disabled="true"  nullable="false" maxLength="50" id="uri" class="form-control"  
			    <#if uri?exists> value="${uri!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">数据库参数：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="paramColumnName" class="form-control" 
			  <#if param?exists> value="${param.paramColumnName!}" </#if> />
			</div>
		</div>
		<div class="form-group"> 
			<label class="col-sm-3 control-label no-padding-right">是否必填：</label>
			<div class="col-sm-9" id= "mandatory">
			  <label><input type="radio"  class="wp isShow" value="1" 
				<#if param?exists &&param.mandatory== 1>   checked="checked" name="mandatory"</#if>><span class="lbl"> 是</span></label>
			  <label><input type="radio" class="wp isShow"  value="0" 
			    <#if param?exists &&param.mandatory== 0>   checked="checked" name="mandatory"</#if>><span class="lbl"> 否</span></label>
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
	    			<textarea nullable="false" name="description" maxLength="255" id="description"  cols="30" rows="10" class="form-control js-limit-word" value = "${param.description!}">${param.description!} </textarea>
	    			<#else>
	    			<textarea nullable="false" name="description" maxLength="255" id="description"  cols="30" rows="10" class="form-control js-limit-word" ></textarea>
	    			</#if>
	    			<span style="right:15px;">255</span>
	    		</div>
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