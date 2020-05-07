<div class="table-show">
	<div class="filter-made mb-10">
		<div class="filter-item">
			<div class="form-group">
	            <input type="hidden" id="logType" value="${logType!}"/>
        		<select class="form-control"  onChange="searchLog(this.value);">
					<option value="all" <#if logType! == 'all'>selected="selected"</#if> >请选择操作类型</option>
					<option value="query" <#if logType! == 'query'>selected="selected"</#if> >查询</option>
					<option value="insert" <#if logType! == 'insert'>selected="selected"</#if> >新建</option>
					<option value="update" <#if logType! == 'update'>selected="selected"</#if> >修改</option>
					<option value="delete" <#if logType! == 'delete'>selected="selected"</#if> >删除</option>
					<option value="stat" <#if logType! == 'stat'>selected="selected"</#if> >统计</option>
					<option value="auth" <#if logType! == 'auth'>selected="selected"</#if> >授权</option>
					<option value="init" <#if logType! == 'init'>selected="selected"</#if> >初始化</option>
					<option value="other" <#if logType! == 'other'>selected="selected"</#if> >其他</option>
				</select>
	        </div>
		</div>
		<div class="filter-item">
            <input type="text"  id="bizName" value="${bizName!}" class="form-control" placeholder="请输入名称">
		</div>
		<div class="filter-item">
			<div class="form-group">
                <div class="input-group">
                    <input type="text"  id="subsystem" value="${subsystem!}" class="form-control" placeholder="请输入子系统名称">
                    <a href="javascript:void(0);" class="input-group-addon" onclick="searchLog()"  hidefocus="true"><i class="wpfont icon-search"></i></a>
                </div>
            </div>
		</div>
	</div>
	<table class="tables tables-border no-margin">
		<thead>
			<tr>
				<th >操作类型</th>
				<th >名称</th>
				<th >详细描述</th>
				<th >所属子系统</th>
				<th >操作人</th>
				<th >操作时间</th>
				<th >操作</th>
			</tr>
		</thead>
		<tbody>
			<#if logList?exists&&logList?size gt 0>
	          	<#list logList as log>
					<tr>
						<td >
							<#if log.logType! ="insert">新建
							<#elseif log.logType! ="update">修改
							<#elseif log.logType! ="delete">删除
							<#elseif log.logType! ="stat">统计
							<#elseif log.logType! ="auth">授权
							<#elseif log.logType! ="init">初始化
							<#elseif log.logType! ="other">其他
							<#elseif log.logType! ="query">查询
							</#if>
						</td>
						<td>${log.bizName!}</td>					
						<td  title="${log.description!}"><#if log.description! !="" && log.description?length gt 50>${log.description?substring(0, 50)}......<#else>${log.description!}</#if></td>
						<td>${log.subSystem!}</td>				
						<td>${log.operator!}</td>				
						<td>${log.operationTime!}</td>	
						<td>
							<#if log.logType!  !="query" && log.logType! !="stat">	
							<a href="javascript:;" class="look-over" onclick="viewDetailLog('${log_index!}')">数据追溯</a>
							</#if>
						</td>	
					</tr>
	      	    </#list>
	  	    <#else>
				<tr >
					<td  colspan="7" align="center">
						暂无数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
<#if logList?exists&&logList?size gt 0>
  	<#list logList as log>
		<div class="layer layer-param-${log_index!}">
			<div class="layer-content">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">老数据：</label>
						<div class="col-sm-6">
							  <textarea name="remark" id="remark" type="text/plain" readonly style="width:480px;height:166px;resize: none;">${log.oldData!}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">新数据：</label>
						<div class="col-sm-6">
							<textarea name="remark" id="remark" type="text/plain" readonly style="width:480px;height:166px;resize: none;">${log.newData!}</textarea>
						</div>
					</div>
				</div>
			</div>
		</div>
	</#list>
</#if>
<script>
	function searchLog(logType){
		if(!logType)
			logType=$("#logType").val();
		$("#logType").val(logType);
		var bizName = $("#bizName").val();
		var subsystem =$("#subsystem").val();
		var url =  "${request.contextPath}/bigdata/monitor/log/query/list?type=business&logType="+logType+"&bizName="+bizName+"&subsystem="+subsystem;
		$("#contentDiv").load(url);	
	}
	
	function viewDetailLog(logIndexNum){
		layer.open({
		    	type: 1,
		    	shade: 0.5,
		    	closeBtn:1,
		    	title: '详细信息',
		    	area: '666px',
		    	btn: ['关闭'],
		    	yes:function(index,layero){
	                parent.layer.close(index);
	            },
		    	content: $('.layer-param-'+logIndexNum)
		    })
	}
</script>