<div class="layer-content">
	<div class="form-horizontal">
	    <div class="form-group layerServerSee" style="display:none">   
	       <input type="text" id="openApiId" class="form-control" 
			  <#if openApiApplie?exists> value="${openApiApplie.id!}" </#if> />
		</div>
		<div class="form-group">
			<label class="col-sm-4 control-label no-padding-right">每天最大调用次数：</label>
			<div class="col-sm-5" >
			  <input type="text" class="form-control" nullable="false"  id="maxNumDay" 
			  vtype="digits" min="1" max="100000"  <#if openApiApplie?exists> value="${openApiApplie.maxNumDay?default(500)}" </#if> />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 control-label no-padding-right">每次最大获取数量：</label>
			<div class="col-sm-5" >
			  <input type="text" nullable="false" vtype="digits" min="1" max="1000" id="limitEveryTime" class="form-control" 
			  <#if openApiApplie?exists> value="${openApiApplie.limitEveryTime?default(1000)}" </#if> />
			</div>
		</div>
	</div>
</div>
