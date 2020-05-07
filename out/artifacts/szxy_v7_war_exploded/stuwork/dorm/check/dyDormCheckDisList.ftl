<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="tab-content">
<div id="aa" class="tab-pane active" role="tabpanel">
	<#-- <div class="filter" id="searchType">
		<div class="filter-item">
			<span class="filter-name">日期：</span>
			<div class="filter-content">
				<div class="input-group" style="width:260px;">
					<input type="text" value="${(searchDto.startTime?string('yyyy-MM-dd'))!}" name="startTime" id="startTime"  class="form-control datepicker">
					<span class="input-group-addon">
						<i class="fa fa-minus"></i>
					</span>
					<input type="text" value="${(searchDto.endTime?string('yyyy-MM-dd'))!}" name="endTime" id="endTime"  class="form-control datepicker">
				</div>
			</div>
		</div>
	</div>-->
	<form name="checkForm" id="checkForm" action="" method="post">
	<input type="hidden" name="acadyear" id="acadyear" value="${dormDto.acadyear!}">
	<input type="hidden" name="semesterStr" id="semesterStr" value="${dormDto.semesterStr!}">	
	<div class="table-container">
		<div class="text-right">	
			<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="editDis('${buildingIds!}')">新增</a>
		</div>
		<div class="table-container-body">
			<table class="table table-striped layout-fixed">
				<thead>
					<tr>
						<th>姓名</th>
						<th>寝室号</th>
						<th>扣分</th>
						<th>扣分原因</th>
						<th>违纪日期</th>
						<th>操作</th>
					</tr>
				</thead>
			      <tbody>
			      	  <#if checkDisList?exists && checkDisList?size gt 0>
			      	  	  <#list checkDisList as item>
			      	  	  	 <tr>
			      	  	  	 	<td>${item.studentName!}</td>
			      	  	  	 	<td>${item.roomName!}</td>
			      	  	  	 	<td>${item.score!}</td>
			      	  	  	 	<td>${item.reason!}</td>
			      	  	  	 	<td>${(item.checkDate?string('yyyy-MM-dd'))!}</td>
			      	  	  	 	<td>
			      	  	  	 		<a href="javascript:" class="color-blue"  onclick="deleteDis('${item.id!}')">删除</a>
			      	  	  	 	</td>
			      	  	  	 </tr>
			      	  	  </#list>
			      	  <#else>
				          <tr >
				          	<td colspan="6" align="center">
				          		暂无数据
				          	</td>
				          </tr>
			          </#if>
			      </tbody>				
			</table>
			<@htmlcom.pageToolBar container="#itemShowDivId" class="noprint">
			</@htmlcom.pageToolBar>
		</div>
	</div>
	</form>
</div>
</div>
<script>
	<#-- $(function(){
		$('.datepicker').datepicker({
			language: 'zh-CN',
	    	format: 'yyyy-mm-dd',
	    	autoclose: true
	    }).next().on('click', function(){
			$(this).prev().focus();
		});
		
		$('.js-sort-table').DataTable({
			paging:false,
			scrollY:372,
			info:false,
			searching:false,
			autoWidth:false,
			columnDefs: [
			    { "orderable": false, "targets": 0 }
			]
		}).columns.adjust();
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			$('.js-sort-table').DataTable().columns.adjust();
		});
	});
	function doSearch(){
		var url="${request.contextPath}/stuwork/dorm/check/listDis/page";
//		var url="${request.contextPath}/stuwork/dorm/check/listDis/page?"+searchUrlValue("#searchType");
		$("#itemShowDivId").load(url);
	} -->
	function editDis(buildingIds){
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		var url = "${request.contextPath}/stuwork/dorm/check/editDis?buildingIds="+buildingIds+"&acadyear="+acadyear+"&semesterStr="+semesterStr;
		indexDiv = layerDivUrl(url,{title: "新增个人违纪",width:550,height:470});
	}
	function deleteDis(id){
		$.ajax({
			url:'${request.contextPath}/stuwork/dorm/check/deleteDis',
			data: {'id':id},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
				  	itemShowList(2);
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>