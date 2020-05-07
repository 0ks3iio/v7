<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" nullable="false" maxLength="50" id="typeId" class="form-control" 
			  <#if interfaceType?exists> value="${interfaceType.id!}" </#if> />
		   <input type="text" nullable="false" maxLength="50" id="oldType" class="form-control" 
			  <#if interfaceType?exists> value="${interfaceType.type!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类别：</label>
			<div class="col-sm-9" >
			  <select name="classify" id="classify" class="form-control inteEdit"  <#if interfaceType?exists> disabled="true" </#if> >
					<option value="1" <#if interfaceType?exists &&interfaceType.classify== 1>selected</#if>>获取接口类型</option>
					<option value="2" <#if interfaceType?exists &&interfaceType.classify== 2>selected</#if>>获取结果类型</option>
					<option value="3" <#if interfaceType?exists &&interfaceType.classify== 3>selected</#if>>获取公用类型</option>
			  </select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型名称：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="typeName" class="form-control" 
			  <#if interfaceType?exists> value="${interfaceType.typeName!}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-9" >
			  <input type="text" nullable="false" maxLength="50" id="type" class="form-control" 
			  <#if interfaceType?exists> value="${interfaceType.type!}" </#if> />
			</div>
		</div>
	</div>
</div>