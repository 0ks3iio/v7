<script src="${resourceUrl}/components/bootstrap-daterangepicker/moment.min.js"></script>
<script src="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker.js"></script>
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker-bs3.css">
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<div class="btn-group" role="group" aria-label="...">
					<button type="button" class="btn btn-default btn-blue" value = "1">按日</button>
					<button type="button" class="btn btn-default" value = "2">按周</button>
					<button type="button" class="btn btn-default" value = "3">按月</button>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group">
						<input class="form-control date-range" id="reservation" type="text">
						<label for="reservation" class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</label>
					</div>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">接口：</span>
				<div class="filter-content">
				    <select name="openApiApply" id="openApiApply" class="form-control" onChange="changeInterface();" style="width:200px"> 
				        <option value="">全部</option>
				        <#if openApiApplys?exists && openApiApplys?size gt 0>
					      <#list openApiApplys as type>
					          <option value="${type.type!}">${type.typeNameValue!}</option>
					      </#list>
	                    </#if>
	                </select>
				</div>
			</div>
			<#if isBackstage?default(false)>
				<div class="filter-item">
					<span class="filter-name">开发者：</span>
					<div class="filter-content" >
					    <select name="developer" id="developer" class="form-control" onChange="changeDeveloper();" style="width:200px"> 
					    
		                </select>
					</div>
				</div>
			</#if>
		</div>
		<div id="chart-result-bar" style="height:400px;"></div>
	</div>
</div>

<script>
var start,end,showType,interfaceType='',developerId='';
$(function(){
    showTime();
    getSortDeveloper();
    changeOpenapiCount();
})
  

function changeInterface(){
	interfaceType = $('#openApiApply').val();
	if(developerId === undefined || developerId == ''){ getSortDeveloper(interfaceType) }
    changeOpenapiCount();
}
  
function changeDeveloper(){
	developerId = $('#developer').val();
	changeOpenapiCount();
}
  
  //初始化
function showTime(){
    $('#reservation').val('${startTime!} - ${endTime!}');
    start = '${startTime!}';
    end = '${endTime!}';
}

$('.btn-group .btn-default').click(function(e){
	e.preventDefault();
	$(this).addClass('btn-blue').siblings('.btn-default').removeClass('btn-blue');
	changeOpenapiCount();
});

$('#reservation').daterangepicker({
 timePicker: true,
 startDate:'${startTime!}',
 endDate:'${endTime!}',
 format: 'YYYY-MM-DD',
 },
 function(start, end, label){
        start = start.format('YYYY-MM-DD');
        end = end.format('YYYY-MM-DD');
        changeOpenapiCount(start,end);
 });
 
function changeOpenapiCount(start1,end1){
      showType = $('.btn-group .btn-blue').val();
      if(start1 || end1){
        start = start1;
        end = end1;
      }
      var url = "${request.contextPath}/developer/interface/count/changeOpenapiCount/page";
      $("#chart-result-bar").load(url,{"start":start,"end":end, "showType":showType,"interfaceType":interfaceType,
    	  "developerId":developerId,"isBackstage":'${isBackstage?string('true', 'false')}'});
}

function getSortDeveloper(type){
	if(type===undefined){type = ""}
	$.ajax({
        url:"${request.contextPath}/developer/interface/count/showDeveloperList?type="+type,
        data:{},
        async: false,
        dataType:'json',
        contentType:'application/json',
        type:'GET',
        success: function(data) {
			var array = data;
			var htmlStr = '<option value="">全部</option>';
			if(array.length > 0){
				$.each(array, function(index, json){
					htmlStr += '<option value="'+json.id+'">' + json.realName + '</option>' 
				});
			}
			$("#developer").html(htmlStr);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    });
}
</script>
