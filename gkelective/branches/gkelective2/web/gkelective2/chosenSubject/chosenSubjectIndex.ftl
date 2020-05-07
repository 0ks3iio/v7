<script src="${request.contextPath}/gkelective2/js/myscriptCommon.js"></script> 
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<style>
.filter-item{margin-right:0px}
.searchSubjectDivClassHide{display:none}
</style>
<a href="javascript:void('0');" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}</h4>
	</div>
	<div class="box-body">
		<div class="nav-tabs-wrap">
		    <div class="nav-tabs-btns">
		        <#--button type="button" class="btn btn-green">批量新增</button-->
  				<@htmlcomponent.printToolBar container=".print"/>
  				<a href="javascript:selectStuImport();" class="btn btn-blue">已选学生导入</a>
		        <a href="javascript:" class="btn btn-blue export-btn">导出已选结果</a>
		        <#--if gkSubArr.isLock?default(0) = 0>
		        	<a href="javascript:" class="btn btn-blue js-addNewStudent" onclick="chooseSubject()">单个新增</a>
		        </#if-->
		        <a href="javascript:void(0);" onclick="openClassArrange();" class="btn btn-blue">去开班排课</a>
		    </div>
		    <ul id = 'genre' class="nav nav-tabs nav-tabs-1" role="tablist">
		   		<li role="presentation" class="active"><a href="#cc" role="tab" id="cc" data-toggle="tab" onclick="itemShowList(3)">已选统计</a></li>
		        <li role="presentation"><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">已选学生<span class="alChosenNum"></span></a></li>
		        <li role="presentation"><a href="#bb"  role="tab" id="bb" data-toggle="tab" onclick="itemShowList(2)">未选学生<span class="unChosenNum"></span></a></li>
		        
		    </ul>
		</div>
        <div class="box box-transparent">
        		<div class="noprint">
	       		<div class="box-header chosenSubjectHeaderClass3 noprint">
		            <div class="filter filter-f16">
		            <#--
		            	<div class="filter-item">
		                    <span class="filter-name">视图类型：</span>
		                    <div class="filter-content">
		                    	<input type="hidden" name="searchViewType" id="searchViewType" value="1"/>
		                    	<label class="pos-rel">
									<input type="radio" class="wp form-control form-radio" name="searchViewTypeRedio" id="searchViewTypeRedio1" value="1" checked onclick="findBySearchViewType(true,'1')">
									<span class="lbl"> 按图表查看</span>
								</label>
								<label class="pos-rel">
									<input type="radio" class="wp form-control form-radio" name="searchViewTypeRedio" id="searchViewTypeRedio2" value="2" onclick="findBySearchViewType(true,'2')">
									<span class="lbl"> 按表格查看</span>
								</label>
								<span class="lbl"> &nbsp;&nbsp;&nbsp;&nbsp;</span>
		                    </div>
		                </div>
		                -->
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
		          	</div>
		         </div>
		         </div>
	         <div class="noprint">
			<div class="box-header chosenSubjectHeaderClass" style="display:none">
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
	                <div class="filter-item noprint searchSubjectDivClass" style="margin-right: 8px;">
	                    <span class="filter-name">科目：</span>
	                    <div class="filter-content">
	                    	<select multiple vtype="selectMore" name="searchSubject" id="searchSubject" data-placeholder=" " onchange="findByCondition()">
								<#if coursesList?? && (coursesList?size>0)>
									<#list coursesList as item>
										<option value="${item.id}" >${item.subjectName}</option>
									</#list>
								<#else>
									<option value="">暂无数据</option>
								</#if>
							</select>
	                    </div>
	                </div>
	            </div>
	        </div>
	        </div>
			<div class="itemShowDivId ">
			</div>
		</div>
	</div>
</div>
<iframe style="display:none;" id="hiddenFrame" name="hiddenFrame" />
<script>
	var chosenSubjectTabType = '';
	var detailsPagevalue='';
	$(function(){
		//初始化多选控件
		var viewContent1={
			'width' : '250px',//输入框的宽度
			'multi_container_height' : '33px',//输入框的高度
			'max_selected_options' : '3'//限制3个
		}
		initChosenMore(".chosenSubjectHeaderClass","",viewContent1);
		itemShowList(3);
	});
	function openClassArrange(){
		var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangeRounds/index/page';
		$("#showList").load(url);
	}
	
	function selectStuImport() {
		var type = $("#genre li[class='active']").children().attr('onclick');
		if (type == 'itemShowList(1)') {
			type = 1;
		} else if (type == 'itemShowList(2)') {
			type = 2;
		} else if (type == 'itemShowList(3)') {
			type = 3;
		}
		url =  '${request.contextPath}/gkelective/selectStuImport/main?arrangeId=${arrangeId!}&type='+type;
		$(".chosenSubjectHeaderClass3").hide();
		$(".chosenSubjectHeaderClass").hide();
		$(".itemShowDivId").load(url);
	}
	
	function itemShowList(tabType){
		var url = '';
		initSearch();
		if(tabType == 1){
	        url =  '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/list/page';
	        $(".searchSubjectDivClass").removeClass('searchSubjectDivClassHide');
	        $(".chosenSubjectHeaderClass").show();
	        $(".chosenSubjectHeaderClass3").hide();
		}else if(tabType == 2){
			url =  '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/unChosenlist/page';
			$(".searchSubjectDivClass").addClass('searchSubjectDivClassHide');
			$(".chosenSubjectHeaderClass").show();
			$(".chosenSubjectHeaderClass3").hide();
		}else if(tabType == 3){
			url =  '${request.contextPath}/gkelective/${arrangeId}/countChosenSubject/list/page';
			$(".searchSubjectDivClass").addClass('searchSubjectDivClassHide3');
			$(".chosenSubjectHeaderClass").hide();
			$(".chosenSubjectHeaderClass3").show();
			detailsPagevalue='';
		}else{
			$(".chosenSubjectHeaderClass").hide();
			$(".chosenSubjectHeaderClass3").hide();
		}
		chosenSubjectTabType = tabType;
        $(".itemShowDivId").load(url);
        findSubCount();
	}
	function initSearch(){
		$("#searchViewTypeRedio1").prop("checked","checked");
		$("#searchViewType").val("");
		$("#searchViewTypeRedio2").prop("checked","");
		$("#searchClassId").val("");
		$("#searchSex").val("");
		$("#searchSubject").val("");
		$("#searchCondition").val("");
		$("#searchSubject").trigger("chosen:updated");
	}
	function findBySearchViewType(isClear,viewType){
		if(isClear){
			detailsPagevalue = "";
		}
		if(viewType){
			$("#searchViewType").val(viewType);
		}
		findByCondition();
	}
	function findByCondition(){
	    var url = '';
    	if(chosenSubjectTabType == 1){
		    url =  '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/list/page?'+searchUrlValue('.chosenSubjectHeaderClass');
    	}else if(chosenSubjectTabType == 2){
    		url =  '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/unChosenlist/page?'+searchUrlValue('.chosenSubjectHeaderClass');
    	}else if(chosenSubjectTabType == 3){
    		if(detailsPagevalue==""){
    			url =  '${request.contextPath}/gkelective/${arrangeId}/countChosenSubject/list/page?'+searchUrlValue('.chosenSubjectHeaderClass3');
    		}else{
    			url =  '${request.contextPath}/gkelective/${arrangeId}/doQueryStudent/list/page?detailsPagevalue='+detailsPagevalue+$("#searchClassId").val();
    		}
    	}
	    $(".itemShowDivId").load(url);
	    findSubCount();
	  }
	function findSubCount(){
		$.ajax({
		    url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/count',
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
				$(".alChosenNum").text('('+jsonO.countIsChosen+')');
		  		$(".unChosenNum").text('('+jsonO.countUnChosen+')');
		    }
		});
	}
	
	function chooseSubject(){
	  var url = "${request.contextPath}/gkelective/${arrangeId}/chosenSubject/edit/page";
	  indexDiv = layerDivUrl(url,{title: "单个新增",width:400,height:460});
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
    
	$(".export-btn").on("click",function(){
		var url = "${request.contextPath}/gkelective/${arrangeId!}/chosenSubject/export";
		hiddenFrame.location.href=url;
	});	
	
	function editResult(studentId){
	    var url = "${request.contextPath}/gkelective/${arrangeId}/chosenSubject/edit/page?studentId="+studentId;
	    indexDiv = layerDivUrl(url,{title: "编辑选课信息",width:400,height:430});
	  }
</script>
