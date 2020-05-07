<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="a1" class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
        <span class="filter-name">年度：</span>
        <div class="filter-content">
            <select name="year" id="year" class="form-control" onchange="searchAudit()" style="width:135px">
                <#list minYear..maxYear as item>
                    <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
				</#list>
            </select>
        </div>
    </div>
        <div class="filter-item">
            <div class="filter-content">
                <span class="filter-name">访亲开始时间：</span>
                <div class="pos-rel pull-left">
                    <input class='form-control date-picker' vtype='data' id="startTime" autocomplete="off" data-provide="typeahead" placeholder="请输入访亲开始时间" value="${startTime!}" onchange="searchAudit()" <#--onkeydown="dispResInvigilate()"-->>
                </div>
            </div>
        </div>
        <div class="filter-item">
            <div class="filter-content">
                <span class="filter-name">访亲结束时间：</span>
                <div class="pos-rel pull-left">
                    <input class='form-control date-picker' vtype='data' id="endTime" autocomplete="off" data-provide="typeahead" placeholder="请输入访亲结束时间" value="${endTime!}" onchange="searchAudit()">
                </div>
            </div>
        </div>
        <div class="filter-item ">
            <div class="text-right">
                <a class="btn btn-white" onclick="clearTime()">清空时间</a>
            </div>
        </div>
	</div>
        <div class="filter">
        <div class="filter-item">
            <span class="filter-name">结亲活动标题：</span>
            <div class="filter-content">
                <input class="form-control" id="activityTitle" value="${activityTitle!}">
            </div>
        </div>
		<div class="filter-item">
			<span class="filter-name">部门：</span>
			<div class="filter-content">
				<select name="" id="deptId" class="form-control" onchange="searchAudit();">
				<option value="">--请选择--</option>
				<#if depts?exists && depts?size gt 0>
				    <#list depts as item>
					    <option value="${item.id!}" <#if '${deptId!}' == '${item.id!}'>selected="selected"</#if>>
					    ${item.deptName!}
					    </option>
					</#list>
			    <#else>
			        <option value="">--请选择--</option>
				</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
            <button class="btn btn-blue js-addTerm" onclick="searchAudit();">查询</button>
	    </div>
		<#if hasPermission>
			<div class="filter-item ">
				<div class="text-right">
					<a class="btn btn-blue" onclick="exportList()">导出</a>
				</div>
			</div>
		</#if>
	</div>
<#--部门、姓名、职务、亲戚身份、结亲对象村、报名批次，访亲到达时间、返回时间-->
	<div class="table-container">
		<div class="table-container-body">
		  <div style="overflow-x:auto;">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>部门</th>
						<th>姓名</th>
						<th>职务</th>
						<#--<th>性别</th>-->
						<#--<th>民族</th>-->
						<#--<th>手机号码</th>-->
						<#--<th>亲戚姓名</th>-->
						<#--<th>亲戚民族</th>-->
						<#--<th>身份证</th>-->
						<th>亲戚身份</th>
						<th>结亲对象村</th>
						<#--<th>家庭住址</th>-->
						<#--<th>联系电话</th>-->
						<th>报名批次</th>
						<th>访亲到达时间</th>
                        <th>返回时间</th>
						<#--<th>是否上传火车票</th>-->
						<#--<th>备注</th>-->
					</tr>
				</thead>
				<tbody>
				<#if familyDearRegisters?exists && familyDearRegisters?size gt 0>
				    <#list familyDearRegisters as item>
					<tr>
					    <td>${item_index+1}</td>
					    <td><span class="ellipsis">${item.deptName!}</span></td>
					    <td><span class="ellipsis">${item.teacherName!}</span></td>
					    <td><span class="ellipsis">${item.cadreType!}</span></td>
					    <#--<td>${item.sex!}</td>-->
					    <#--<td>${item.nationName!}</td>-->
					    <#--<td><span class="ellipsis">${item.phone!}</span></td>-->
					    <#--<td><span class="ellipsis">${item.objectName!}</span></td>-->
					    <#--<td>${item.objectNation!}</td>-->
					    <#--<td><span class="ellipsis">${item.objectCard!}</span></td>-->
					    <td><span class="ellipsis">${item.objectType!}</span></td>
					    <td title=${item.contry!}>${item.contrySub!}</td>
					    <#--<td>${item.objectDizhi!}</td>-->
					    <#--<td><span class="ellipsis">${item.objectPhone!}</span></td>-->
					    <td title=${item.batchType!}>${item.batchTypeSub!}</td>
					    <td><span class="ellipsis">${item.arriveTimeStr!}</span></td>
                        <td><span class="ellipsis">${item.returnTimeStr!}</span></td>
					    <#--<td>${item.upload!}</td>-->
					    <#--<td><#if item.remarkNew!!=''><a class="color-blue" data-toggle="tooltip" data-placement="right" title="${item.remarkNew!}" href="javascript:;"><i class="fa fa-commenting-o"></i></a></#if></td>-->
					</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="19" align="center">暂无数据</td>
					</tr>
				</#if>
				</tbody>
			</table>
			</div>
			<@htmlcom.pageToolBar container="#bmTblDiv" class="noprint">
	    </@htmlcom.pageToolBar>
		</div>
	</div>
</div>
<script>
	$(function () {
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#a1",".date-picker",viewContent);
    })
	function searchAudit(){
		var year = $('#year').val();
	   var deptId = $('#deptId').val();
	   var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var activityTitle = $("#activityTitle").val();
	   var url = "${request.contextPath}/familydear/nameList/index/list?year="+year+"&deptId="+deptId+"&startTime="+startTime+"&endTime="+endTime+"&activityTitle="+activityTitle;
	   $('#bmTblDiv').load(url);
	}


// function exportList() {
//     alert("导出");
// }

    function clearTime(){
        $("#startTime").val("");
        $("#endTime").val("");
        searchAudit();
    }
function exportList() {
    var year = $('#year').val();
    var deptId = $('#deptId').val();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var activityTitle = $("#activityTitle").val();
    document.location.href="${request.contextPath}/familydear/nameList/export?year="+year+"&deptId="+deptId+"&startTime="+startTime+"&endTime="+endTime+"&activityTitle="+activityTitle;
    // alert("导出");
}

</script>