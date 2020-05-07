<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print" id="a1">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
					<span class="filter-name">起止日期：</span>
					<div class="filter-content">
						<div class="input-group float-left"">
							<input id="startTime" autocomplete="off" name="startTime" vtype='data' data-provide="typeahead" class="form-control date-picker" style="width:120px" type="text" nullable="false"  placeholder="开始时间" value="${((startTime)?string('yyyy-MM-dd'))?if_exists}" />
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</div>
						<span class="float-left mr10 ml10"> 至 </span>
						<div class="input-group float-left">
							<input id="endTime" autocomplete="off" name="endTime" vtype='data' data-provide="typeahead" class="form-control date-picker" style="width:120px" type="text" nullable="false"  placeholder="结束时间" value="${((endTime)?string('yyyy-MM-dd'))?if_exists}" />
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</div>
					</div>
				</div>
				<div class="filter-item ">
		            <div class="text-right">
		                <a class="btn btn-white" onclick="clearTime()">清空时间</a>
		            </div>
		        </div>
				<div class="filter-item">
					<span class="filter-name">部门：</span>
					<div class="filter-content">
						<select name="" id="deptId" class="form-control" style="width:400px;" onchange="doSearch();">
						<option value="">--请选择--</option>
						<#if depts?exists && depts?size gt 0>
						    <#list depts as item>
							    <option value="${item.id!}">
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
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">活动主题：</span>
                            <div class="filter-content">
                                <select name="title" id="title" class="form-control" notnull="false" onChange="doSearch();" style="width:168px;">
                                	${mcodeSetting.getMcodeSelect("DM-XJHDZT", '', "1")}
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item">
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">干部名：</span>
                            <div class="pos-rel pull-left">
                                <input type="text" name="teaName" id="teaName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入干部名">
                            </div>
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-default" onclick="doSearch()">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="table-container" id="showList1">
            </div>
        </div>
    </div>
</div>
<!-- page specific plugin scripts -->

<script type="text/javascript">

$(function(){

	var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
    initCalendarData("#a1",".date-picker",viewContent);
	
	$("#startTime").on("change",function(){
		doSearch();
	})
	
	$("#endTime").on("change",function(){
		doSearch();
	})
});

function compareDate(d1,d2){
	if(d1&&d2){
		return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
}

function clearTime(){
        $("#startTime").val("");
        $("#endTime").val("");
        doSearch();
    }

function doSearch(){
   var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	if(compareDate(startTime, endTime)){
		layer.tips('开始日期不能大于结束日期，请重新选择！', $("#endTime"), {
				tipsMore: true,
				tips:3				
			});
		return;
	}
   var deptId = $('#deptId').val();
   var teaName = $('#teaName').val();
   var title = $('#title').val();
   var url = "${request.contextPath}/familydear/threeInTwo/edu/statistic/list?startTime="+startTime+"&endTime="+endTime+"&deptId="+deptId+"&teaName="+encodeURIComponent(encodeURIComponent(teaName))+"&title="+title;
   $('#showList1').load(url);
}

    $(function(){
        doSearch();
    });

</script>

