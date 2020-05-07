<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div class="box box-default">
<div class="box-header chosenSubjectHeaderClass" >
	<div class="filter filter-f16">
	    <div class="filter-item">
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
	    
	    <div class="filter-item noprint" >
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
	            </div><!-- /input-group -->
	        </div>
	    </div>
	    
	    <div class="filter-item">
	        <span class="filter-name">性别：</span>
	        <div class="filter-content">
	            <select name="searchSex" id="searchSex" class="form-control" onchange="findByCondition()" style="width:110px">
	            <option value="">所有</option>
	            ${mcodeSetting.getMcodeSelect("DM-XB","","0")}
	            </select>
	        </div>
	    </div>
	</div>
	<div>
	<@htmlcomponent.printToolBar container=".print"/>
	</div>
</div>
<div class="itemShowDivId print">
</div>
</div>
<script>
	$(function(){
		findByCondition();
	});
	function findByCondition(){
	    var url = '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/notArrangeStuList/page?'+searchUrlValue('.chosenSubjectHeaderClass');
	    $(".itemShowDivId").load(url);
	  }
</script>