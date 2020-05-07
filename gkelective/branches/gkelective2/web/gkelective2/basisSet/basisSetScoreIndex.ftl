<div class="box print">
	<div class="tab-pane chosenSubjectHeaderClass">
        <div class="filter filter-f16">
            <div class="filter-item" style="margin-right: 10px;">
                <span class="filter-name">行政班：</span>
                <div class="filter-content">
                    <select name="searchClassId" id="searchClassId" class="form-control" onchange="findByCondition()" style="width:120px">
                    <#if clazzList?? && (clazzList?size>0)>
                    	<option value="">全年级</option>
                        <#list clazzList as clazz>
                        <option value="${clazz.id!}">${clazz.classNameDynamic!clazz.className!}</option>
                        </#list>
                    <#else>
                        <option value="">暂无数据</option>
                    </#if> 
                    </select>
                </div>
            </div>
            <div class="filter-item noprint"  style="margin-right: 10px;">
                <div class="filter-content">
                    <div class="input-group input-group-search">
                        <select name="searchSelectType" id="searchSelectType" class="form-control">
                            <option value="2">姓名</option>
                            <option value="1">学号</option>
                        </select>
                        <div class="pos-rel pull-left">
                        	<input type="text" style="width:100px" class="form-control" name="searchCondition" id="searchCondition" value="" onkeydown="dispRes()">
                        </div>
                        <div class="input-group-btn">
                            <a href="javascript:" class="btn btn-default"  onclick="findByCondition()">
                                <i class="fa fa-search"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="filter">
			<#if !(gkArrange?exists && gkArrange.isLock==1)>
			<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="synScore()">获取考试成绩</a>
			<a href="javascript:" class="btn btn-blue pull-right" style="margin-right:5px;" onclick="scoreImport()">导入</a>
			
			<#else><em>温馨提示：开班开课流程正在进行中不能修改，需流程走完之后才能再次设置</em></#if>
			</div>
        </div>
    </div>
    <div id="showScoreListDivId">
    </div>
</div>
<script>
	$(function(){
		findByCondition();
	});
	function findByCondition(){
		var url =  '${request.contextPath}/gkelective/${arrangeId}/basisSet/score/list/page?'+searchUrlValue('.chosenSubjectHeaderClass');
		$("#showScoreListDivId").load(url);
	}
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
    function synScore(){
		var url = "${request.contextPath}/gkelective/${arrangeId}/basisSet/score/edit/page";
		indexDiv = layerDivUrl(url,{title: "同步成绩",width:400,height:430});
	}
	
	function scoreImport() {
		var url="${request.contextPath}/gkelective/scoreImport/main?arrangeId=${arrangeId!}"
		$("#itemShowDivId").load(url);
	}
</script>
