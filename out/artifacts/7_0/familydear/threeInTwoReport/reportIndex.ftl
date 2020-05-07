<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box print" id="a1">
    <div class="tab-pane">
        <div class="box-body">
            <div class="filter filter-f16">
                <div class="filter-item">
					<span class="filter-name">起止日期：</span>
					<div class="filter-content">
						<div class="input-group float-left"">
							<input id="startTime" style="width:103px;" autocomplete="off" name="startTime" vtype='data' data-provide="typeahead" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="开始时间" value="${((startTime)?string('yyyy-MM-dd'))?if_exists}" />
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</div>
						<span class="float-left mr10 ml10"> 至 </span>
						<div class="input-group float-left">
							<input id="endTime" style="width:103px;" autocomplete="off" name="endTime" vtype='data' data-provide="typeahead" class="form-control datepicker" style="width:120px" type="text" nullable="false"  placeholder="结束时间" value="${((endTime)?string('yyyy-MM-dd'))?if_exists}" />
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
                    <div class="filter-content">
                        <div class="input-group input-group-search">
                            <span class="filter-name">主题：</span>
                            <div class="filter-content" style="width:111px;">
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
                            <span class="filter-name">学生姓名：</span>
                            <div class="pos-rel pull-left" style="width:168px;">
                                <input type="text" name="stuName" id="stuName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入学生姓名">
                            </div>
                            <div class="input-group-btn">
                                <button type="button" class="btn btn-default" onclick="doSearch()">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="filter-item ">
                    <div class="text-right">
                        <a class="btn btn-blue" onclick="addTwo()">新增</a>
                    </div>
                </div>
            </div>
            <div class="table-container" id="showList1">
            </div>
        </div>
    </div>
</div>
<!-- page specific plugin scripts -->
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script type="text/javascript">

$(function(){

	var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
    initCalendarData("#a1",".datepicker",viewContent);
	
	$("#startTime").on("change",function(){
		doSearch();
	})
	
	$("#endTime").on("change",function(){
		doSearch();
	})
});

 function clearTime(){
        $("#startTime").val("");
        $("#endTime").val("");
        doSearch();
    }

function compareDate(d1,d2){
	if(d1&&d2){
		return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
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
   var title = $('#title').val();
   var stuName = $('#stuName').val();
   var url = "${request.contextPath}/familydear/threeInTwo/edu/report/list?startTime="+startTime+"&endTime="+endTime+"&title="+title+"&stuName="+encodeURIComponent(encodeURIComponent(stuName));
   $('#showList1').load(url);
}

    function addTwo(){
        var url = "${request.contextPath}/familydear/threeInTwo/edu/report/edit";
        $(".model-div").load(url);
    }
    $(function(){
        doSearch();
    });

</script>

