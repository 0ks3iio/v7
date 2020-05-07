<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select class="form-control" id="searchClassId" name="searchClassId" onChange="showFilterList()">
				<#if clazzList?exists && (clazzList?size>0)>
                    <#list clazzList as item>
	                     <option value="${item.id!}">${item.classNameDynamic!}</option>
                    </#list>
                <#else>
                    <option value="">未设置</option>
                 </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">类别：</span>
		<div class="filter-content">
			<select class="form-control" id="searchFilterType" name="searchFilterType" onChange="showFilterList()">
				<option value="">全部</option>
				<option value="1">排考</option>
				<option value="0">不排考</option>
			</select>
		</div>
	</div>
	 <div class="filter-item" >
        <div class="filter-content">
            <div class="input-group input-group-search">
                <select name="searchSelectType" id="searchSelectType" class="form-control">
                    <option value="1">姓名</option>
                    <option value="2">学号</option>
                    <option value="3">身份证号</option>
                </select>
                <div class="pos-rel pull-left">
                	<input type="text" style="width:100px" class="form-control" name="searchCondition" id="searchCondition" value="" onkeydown="dispRes()">
                </div>
                <div class="input-group-btn">
                    <a href="javascript:" class="btn btn-default"  onclick="findByCondition()">
                        <i class="fa fa-search"></i>
                    </a>
                </div>
            </div><!-- /input-group -->
        </div>
    </div>
</div>

<div class="table-container">
	<#if isEdit && canEdit?exists && canEdit>
		<input type="hidden" id="canEdit" value="true">
        <div class="filter">
            <div class="filter-item">
                <a href="javascript:" class="btn btn-blue js-saveFilter" id="js-saveFilter" onclick="saveFilter();">保存</a>
                <a href="javascript:" class="btn btn-white js-setFilter0" id="js-setFilter0" onclick="setFilter('0');">设置不排考</a>
                <a href="javascript:" class="btn btn-white js-setFilter1" id="js-setFilter1" onclick="setFilter('1');">设置排考</a>
                <a href="javascript:" class="btn btn-white js-saveFilter" id="js-exportFilter" onclick="exportFilter();">导入</a>
            </div>
        </div>
    <#else>
    	<input type="hidden" id="canEdit" value="false">
	</#if>
	<div class="table-container-body" id="filterList">
</div>

<script>
$(function(){
<#if isEdit>
	showButton();
</#if>
	showFilterList();
});
function dispRes(){
	var x;
    if(window.event) // IE8 以及更早版本
    {	x=event.keyCode;
    }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    {
        x=event.which;
    }
    if(13==x){
        findByCondition();
    }
}

function showButton(){
	var searchFilterType=$("#searchFilterType").val();
	if("0"==searchFilterType){
		$(".js-setFilter1").show();
		$(".js-setFilter0").hide();
	}else{
		$(".js-setFilter0").show();
		$(".js-setFilter1").hide();
	}
}


function showFilterList(){
	var searchClassId=$("#searchClassId").val();
	var searchFilterType=$("#searchFilterType").val();
	showButton();
	var str='&searchClassId='+searchClassId+'&searchFilterType='+searchFilterType;
	var url =  '${request.contextPath}/exammanage/examArrange/filterList/page?examId=${examId!}'+str;
	$("#filterList").load(url);
}

function findByCondition(){
	var searchSelectType=$("#searchSelectType").val();
	var searchCondition=$("#searchCondition").val();
	var ss="请先输入要查询的";
	if(searchFilterType=="1"){
		ss="姓名";
	}else if(searchFilterType=="2"){
		ss="学号";
	}else if(searchFilterType=="3"){
		ss="身份证号";
	}
	if(searchCondition=="" || searchCondition.trim()=="" || searchSelectType==""){
		layerTipMsg(false,"提示",ss+"！");
		return;
	}
	$("#searchFilterType").val("");
	showButton();
	var str='&searchSelectType='+searchSelectType+'&searchCondition='+searchCondition.trim();
	var url =  '${request.contextPath}/exammanage/examArrange/filterOneList/page?examId=${examId!}'+str;
	url=encodeURI(encodeURI(url));
	$("#filterList").load(url);
}

var isSubmit=false;
function saveFilter(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$("#js-saveFilter").addClass("disabled");
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/filterSave',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			$("#js-saveFilter").removeClass("disabled");
	 			isSubmit=false;
	 			layer.closeAll();
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				showFilterList();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#js-saveFilter").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#filterForm").ajaxSubmit(options);
}
function exportFilter(){
     var url="${request.contextPath}/exammanage/filter/main?examId=${examId!}"
	 $("#showTabDiv").load(url);
}
</script>