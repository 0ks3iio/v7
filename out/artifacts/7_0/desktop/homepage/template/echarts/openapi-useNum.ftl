<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<#assign CHART_TYPE_RADAR=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_RADAR") />
<#assign CHART_TYPE_HISTOGRAM=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_HISTOGRAM") />
<#assign CHART_TYPE_LINE=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_LINE") />
<#assign CHART_TYPE_PIE=stack.findValue("@net.zdsoft.desktop.constant.DeskTopConstant@CHART_TYPE_PIE") />
<!-- 复合图表 -->
<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<div class="col-sm-${data.col?default('12')}">
    <div class="box box-default">
        <div class="box-header">
            <h4 class="box-title">${data.title!}</h4>
        </div>
        <div class="box-body" style="height: 450px;" >
           <ul >
              <li style="width: ${((data.col/12)*100)!0}%;float: left;height: 180px;">
                <label class="control-label">调用者：</label>
                 <select name="developerName" id="developerName" class="form-control" onChange="changeOpenapiCount();"> 
                   <#if data.developers?exists && data.developers?size gt 0>
				      <#list data.developers as developer>
				          <option value="${developer.ticketKey!}">${developer.realName!}</option>
				      </#list>
                   </#if>
                 </select>
				<label class="control-label">时间：</label>
				<div class="input-group">
					<input type="text" id="start" class="form-control date-picker calendar-time-start" value="${data.startTime!}" onblur="changeOpenapiCount();" />
					<span class="input-group-addon"><i class="fa fa-minus"></i></span>
					<input type="text" id="end" class="form-control date-picker calendar-time-end" value="${data.endTime!}" onblur="changeOpenapiCount();"/>
				</div>
				<label class="control-label">调用的接口：</label>
                <select name="interfaceName" id="interfaceName" class="form-control" onChange="changeOpenapiCount();"> 
                   <#if data.interfaces?exists && data.interfaces?size gt 0>
				      <#list data.interfaces as interface>
				          <option value="${interface.id!}">${interface.description!}</option>
				      </#list>
                   </#if>
                 </select>
                 <div id="showOpenapi">
                 
                 </div>
              </li>
            </ul>
        </div>
    </div>
</div>
<script>
  
   
  var isOk=true;
  var ticketKey,start,end,interfaceId;
  $(function(){
    changeOpenapiCount();
  })
  
  function showOpenapiCount(){
    ticketKey = $('#developerName').val();
    start = $('input[id=start]').val();
    end   = $('input[id=end]').val();
    var startTime = new Date(start.replaceAll("-","/")).getTime();
    var endTime = new Date(end.replaceAll("-","/")).getTime();
    interfaceId = $('#interfaceName').val();
    if(start =='' || end == ''){
      layerError("input[id=start]","请选择时间");
      return isOk=false;
    }
    if ( startTime >= endTime ) {
      layerError("input[id=end]", "结束时间应大于开始时间");
      return isOk=false;
    }
    return isOk= true;
  }
  
  function changeOpenapiCount(){
    showOpenapiCount();
    if(isOk){
      $("#showOpenapi").load("${request.contextPath}/desktop/app/changeOpenapiCount?ticketKey="+ticketKey+"&interfaceId="+interfaceId+"&start="+start+"&end="+end);
    }
  }
  $('.date-picker').datepicker({
        format: 'YYYY-MM-DD',
		sideBySide: true,
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});
	
</script>

