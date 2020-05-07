<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="tab-content">
	<form name="checkForm" id="checkForm" action="" method="post">
	<div id="aa" class="tab-pane active" role="tabpanel">

		<div class="filter" id="searchType">
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group">
						<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 120px" type="text" nullable="false" name="searchDate" id="searchDate" value="${(searchDto.searchDate?string('yyyy-MM-dd'))!}" onchange="doSearch();">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">寝室楼：</span>
				<div class="filter-content">
					<select name="searchBuildId" id="searchBuildId" class="form-control" onchange="doSearch()" style="width:120px">
                    <#if buildingList?? && (buildingList?size>0)>
                        <#list buildingList as building>
                        <option value="${building.id!}" <#if building.id==searchDto.searchBuildId?default("")>selected="selected"</#if>>${building.name!}</option>
                        </#list>
                    </#if> 
                    </select>
				</div>
			</div>
			<div class="filter-item chosenClassHeaderClass">
				<span class="filter-name">寝室号：</span>
				<div class="filter-content">
					<select vtype="selectOne" name="searchRoomId" id="searchRoomId" class="form-control input-sm" data-placeholder="" onchange="doSearch()">
						<option value="">全部</option>
						<#if roomList?exists && (roomList?size>0)>
							<#list roomList as item>
								<option value="${item.id!}" <#if item.id==searchDto.searchRoomId?default("")>selected="selected"</#if> >${item.roomName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
		</div>
		
			<input type="hidden" value="${searchDto.acadyear!}" name="acadyear">
			<input type="hidden" value="${searchDto.semesterStr!}" name="semesterStr">
			<input type="hidden" value="${searchDto.week!}" name="week">
			<input type="hidden" value="${searchDto.day!}" name="day">
		<div class="table-container">
			<div class="table-container-header text-right">
				<a href="javascript:" class="btn btn-white " style="margin-bottom:5px;margin-right: 10px" onclick="doImport()">导入Excel</a>
				<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="saveAll()">保存</a>
				<#--<button class="btn btn-blue" onclick="saveAll()">保存</button>-->
			</div>
			<div class="table-container-body" id="checkDiv">
				<table class="table table-striped layout-fixed">
					<thead>
						<tr>
							<th width="8%">寝室楼</th>
							<th width="8%">楼层</th>
							<th width="8%">寝室号</th>
							<th width="8%">卫生</th>
							<th width="8%">内务</th>
							<th width="8%">纪律</th>
							<th width="17%">扣分</th>
							<th width="18%">提醒事项</th>
							<th width="17%">其他情况</th>
						</tr>
					</thead>
					<tbody>
						<#if roomResultList?exists && roomResultList?size gt 0>
			      	  	  <#list roomResultList as room>	
			      	  	  	 <tr>
			      	  	  	 	<input type="hidden" value="${room.id!}" name="resultList[${room_index}].roomId">
			      	  	  	 	<input type="hidden" value="${room.id!}" name="remindList[${room_index}].roomId">
			  	  	  	 		<input type="hidden" value="${room.result.id!}" name="resultList[${room_index}].id">
			  	  	  	 		<input type="hidden" value="${room.remind.id!}" name="remindList[${room_index}].id">
			      	  	  	 	<td>${buildingName!}</td>
			      	  	  	 	<td>${room.floor!}F</td>
			      	  	  	 	<td>${room.roomName!}</td>
			      	  	  	 	<#if room.result.id?exists && room.result.id!="">
				      	  	  	 	<td>
				      	  	  	 		<input type="text"   id="wsScore${room_index}" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="10" value="${room.result.wsScore!}" name="resultList[${room_index}].wsScore" style="width:40px" nullable="false">
				      	  	  	 	</td>
				      	  	  	 	<td>
				      	  	  	 		<input type="text" id="nwScore${room_index}" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="10"  value="${room.result.nwScore!}" name="resultList[${room_index}].nwScore" style="width:40px" nullable="false">
				      	  	  	 	</td>
				      	  	  	 	<td>
				      	  	  	 		<input type="text"  id="jlScore${room_index}" regextip="只能一位小数点"  regex="/^(\d+\.\d{1,1}|\d+)$/" vtype="number" min="0" max="10" value="${room.result.jlScore!}" name="resultList[${room_index}].jlScore" style="width:40px" nullable="false">
				      	  	  	 	</td>
				      	  	  	 <#else>
				      	  	  	 <td>
				      	  	  	 		<input type="text" id="wsScore${room_index}" value="10" vtype="number" min="0" max="10" nullable="false" maxlength="3" name="resultList[${room_index}].wsScore" style="width:40px" nullable="false"> 
				      	  	  	 	</td>
				      	  	  	 	<td>
				      	  	  	 		<input type="text" id="nwScore${room_index}" value="10" vtype="number" min="0" max="10" nullable="false" maxlength="3" name="resultList[${room_index}].nwScore" style="width:40px" nullable="false">
				      	  	  	 	</td>
				      	  	  	 	<td>
				      	  	  	 		<input type="text" id="jlScore${room_index}" value="10" vtype="number" min="0" max="10" nullable="false" maxlength="3" name="resultList[${room_index}].jlScore" style="width:40px" nullable="false">
				      	  	  	 	</td>
			      	  	  	 	</#if>
			      	  	  	 	<td>
			      	  	  	 		<input type="text" value="${room.result.remark!}" id="resultRemark${room_index}"  maxlength="100" name="resultList[${room_index}].remark">
			      	  	  	 	</td>
			      	  	  	 	<td>
			      	  	  	 		<input type="text" value="${room.remind.remark!}" id="remindRemark${room_index}"  maxlength="400" name="remindList[${room_index}].remark">
			      	  	  	 	</td>
			      	  	  	 	<td>
			      	  	  	 		<input type="text" value="${room.remind.otherInfo!}" id="otherInfo${room_index}"  maxlength="400" name="remindList[${room_index}].otherInfo">
			      	  	  	 	</td>
			      	  	  	 </tr>
			      	  	  </#list>
			      	  <#else>
				          <tr >
				          	<td colspan="9" align="center">
				          		暂无数据
				          	</td>
				          </tr>
			          </#if>
					</tbody>
				</table>
				<#if searchDto.searchRoomId?default("")=="">
					<@htmlcom.pageToolBar container="#itemShowDivId" class="noprint">
	  				</@htmlcom.pageToolBar>
				</#if>
			</div>
		</div>
	</div>
	</form>
</div>
<script>
	$(function(){
		//初始化单选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '150px',//输入框的宽度
			'results_height' : '150px'//下拉选择的高度
		}
		initChosenOne(".chosenClassHeaderClass","",viewContent);
		
		//初始化日期控件
		var viewContent1={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#checkForm",".date-picker",viewContent1);
		
	});
	function doImport(){
		$("#itemShowDivId").load("${request.contextPath}/stuwork/check/checkImport/main?"+searchUrlValue("#searchType"));
	}
	function doSearch(){
		var url="${request.contextPath}/stuwork/dorm/check/list/page?"+searchUrlValue("#searchType");
		$("#itemShowDivId").load(url);
	}
	var isSubmit=false;
	function saveAll(){
		if(isSubmit){
			return false;
		}
		isSubmit = true;
		var check = checkValue('#checkDiv');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var options = {
		       url:'${request.contextPath}/stuwork/dorm/check/saveResult', 
		       dataType : 'json',
		       clearForm : false,
		       resetForm : false,
		       type : 'post',
		       success : function(data){
		 			var jsonO = data;
			 		if(!jsonO.success){
			 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 			isSubmit = false;
			 		}else{
			 			//layer.closeAll();
						layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					  	doSearch();
	    			}
    			},
    			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	   	 };
		$('#checkForm').ajaxSubmit(options);
	}
</script>
