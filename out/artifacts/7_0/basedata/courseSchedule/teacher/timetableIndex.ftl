<#import "/fw/macro/treemacro.ftl" as treemacro>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/ztree/js/jquery.ztree.exhide-3.5.min.js"></script>

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body">
                <div class="clearfix">
                    <!-- 树菜单开始 -->
                    <div class="tree-wrap">
                        <h4>${unitName!}</h4>
                        <div class="input-group">
						 	<input type="text" id="searchTeacherName" class="form-control" placeholder="输入教师姓名查询">
							<div class="input-group-btn btn-search">
								<button type="button" class="btn btn-default">
									<i class="fa fa-search"></i>
								</button>
							</div>
						</div>
                        <@treemacro.teacherForGroupTree height="645" click="onTreeClick" parameter="&isRole=1"/>
                        
                    </div><!-- 树菜单结束 -->
                    <div id="showConditionDiv">
                      <div class="box-header">
                          <div class="filter filter-f16">
                              <div class="filter-item">
                                  <span class="filter-name">入学学年：</span>
                                  <div class="filter-content">
                                      <select class="form-control" id="searchAcadyear" onchange="changeWeek()">
                                          <#if (acadyearList?size>0)>
                                              <#list acadyearList as item>
                                              <option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}</option>
                                              </#list>
                                          <#else>
                                              <option value="">暂无数据</option>
                                          </#if>
                                      </select>
                                  </div>
                              </div>
                              <div class="filter-item">
                                  <span class="filter-name">学期：</span>
                                  <div class="filter-content">
                                      <select class="form-control" id="searchSemester" onchange="changeWeek()">
                                          ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
                                      </select>
                                  </div>
                              </div>
                              <div class="filter-item">
                                  <span class="filter-name">周次：</span>
                                  <div class="filter-content">
                                      <select class="form-control" id="searchWeek" onchange="changeTable()">
                                          <#if (max??)>
                                            <#list 1..max as t>
                                            <option value="${t!}" <#if t==weekSearch>selected="selected"</#if>>第${t!}周</option>
                                            </#list>
                                          <#else>
                                              <option value="">暂无数据</option>
                                          </#if>
                                      </select>
                                  </div>
                              </div>
							  <div class="filter-item">
								  <div class="filter filter-f16">
									  <input type="hidden" class="additionSwitch" id="showPlace" value="true">
									  <a href="javascript:" class="btn btn-blue"
										 onclick="showClassOrPlace('.addition_place', this)">隐藏场地</a>
									  <input type="hidden" class="additionSwitch" id="showClass" value="true">
									  <a href="javascript:" class="btn btn-blue"
										 onclick="showClassOrPlace('.addition_class', this)">隐藏班级</a>
								  </div>
							  </div>
                          </div>
                          <div class="filter filter-f16" id="printDivId" style="display:none">
	                         <@htmlcomponent.printToolBar container=".print" printDirection="true" printUp=0 printLeft=0 printBottom=0 printRight=0/>
                          </div>
                          <div class="filter filter-f16" id="doExportId" style="display:none">
                          		<#if isAdmin?default(false)>
                          		<a href="javascript:" class="btn btn-blue" data-toggle="tooltip" data-original-title="导出全部课表">导出全部教师课表</a>
                          		</#if>
                          </div>
                      </div>
                      <br>
                      <div id="showTableDiv">
                         
                      </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->


<!-- page specific plugin scripts -->
<script type="text/javascript">
$(function(){
	$('#searchTeacherName').bind('keyup', function(event) {
		if (event.keyCode == "13") {
			//回车执行查询
			filter();
		}
	});
	$('.btn-search').on('click',function(){
		filter();
	});
})

	var hiddenNodes=[]; //用于存储被隐藏的结点
	
	//过滤ztree显示数据
	function filter(){
		var valueObj=$("#searchTeacherName").val();
		if(valueObj!=""){
			valueObj=valueObj.trim();
		}
		//显示上次搜索后背隐藏的结点
	    var zTreeObj = $.fn.zTree.getZTreeObj("teacherForGroupTree");
	    if(hiddenNodes.length>0){
	    	zTreeObj.showNodes(hiddenNodes);
	    }
		if(valueObj==""){
			hiddenNodes=[];
			zTreeObj.expandAll(false);
			return;
		}
	    
	    //查找不符合条件的叶子节点
	    function filterFunc(node){
	        if(node.isParent || node.type != "teacher" || node.name.indexOf(valueObj)!=-1) {
	        	return false;
	        }
	        return true;
	    };
	
	    //获取不符合条件的叶子结点
	    hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
	    //隐藏不符合条件的叶子结点
	    zTreeObj.hideNodes(hiddenNodes);
	    zTreeObj.expandAll(true);
	};


function onTreeClick(event, treeId, treeNode, clickFlag){
  var id = treeNode.id;
  if(treeNode.type == "teacher"){
      var idArr= id.split("_");
      doChangeTable('2',idArr[1]);
      $("#printDivId").show();
      $("#doExportId").hide();
  }else if(treeNode.type == "group"){
  	<#if isAdmin?default(false)>
 	 doChangeTable('1',id);
 	 </#if>
	 $("#doExportId").hide();
  }else if(treeNode.type == "school"){
	  $("#printDivId").hide();
      $("#doExportId").show();
      $("#doExportId").on("click",function(){
      	doExportResult(id);
      });
  }else{
	  $("#printDivId").hide();
	  $("#doExportId").hide();
  }
}
function doChangeTable(type,id){
  var searchAcadyear = $("#searchAcadyear").val();
  var searchSemester = $("#searchSemester").val();
  var searchWeek = $("#searchWeek").val();
  if(searchWeek ==""){
	  layer.msg('周次为空，请先维护基础信息节假日设置', {
	      offset: 't',
	      time: 5000
	    });
  	return ;
  }
  
  $("#printDivId").show();
  $("#doExportId").hide();
  var url =  '${request.contextPath}/timetable/teacher/timeTable/page?searchAcadyear='+searchAcadyear
      +"&searchSemester="+searchSemester+"&id="+id+"&week="+searchWeek+"&type="+type + "&showPlace=" + $("#showPlace").val() + "&showClass=" + $("#showClass").val();
  $("#showTableDiv").load(url);
};

function showClassOrPlace(name, obj) {
	if ($(obj).prev().val() == "true") {
		$(obj).prev().val("false");
		$(obj).text("显示" + $(obj).text().substring(2, 4));
		$(name).hide();
		$(".addition").each(function () {
			var tmp = true;
			$(this).find("span").each(function () {
				if($(this).is(":visible")) {
					tmp = false;
				}
			});
			if (tmp) {
				$(this).hide();
			}
		});
	} else {
		$(obj).prev().val("true");
		$(obj).text("隐藏" + $(obj).text().substring(2, 4));
		$(".addition").show();
		$(name).show();
	}
	changeTable();
}

function changeWeek(){
	var searchAcadyear = $("#searchAcadyear").val();
	var searchSemester = $("#searchSemester").val();
	$.ajax({
		url:"${request.contextPath}/timetable/getWeek/json",
		data:{"searchAcadyear":searchAcadyear,"searchSemester":searchSemester},
		type:'post', 
		dataType:'json',
		success:function(data){
			$("#showTableDiv").empty();
			$('#searchWeek').empty();
			$('#printDivId').hide();
			$("#doExportId").hide();
			var dataJson = data;
			var mw = dataJson.max;
			if(!mw){
				mw = 1;
			}
			var cw = dataJson.weekSearch;
			if(!cw){
				cw = 0;
			}
			var sh = '';
			for(var i=1;i<=mw;i++){
				sh=sh+('<option value="'+i+'"');
				if(cw > 0 && i==cw)
					sh=sh+(' selected="selected"');
				sh=sh+('>第'+i+'周</option>');
			}
			$('#searchWeek').html(sh);
		}
	});
}


function changeTable(){
  var zTree = $.fn.zTree.getZTreeObj("teacherForGroupTree");
  var node = zTree.getSelectedNodes();
  if(node.length!=0){
    var id = node[0].id;
    var type = node[0].type;
    if(type == "teacher"){
      var idArr= id.split("_");
      doChangeTable('2',idArr[1]);
    }else if(type=="group"){
      doChangeTable('1',id);
    }else{
    	layer.msg('请先选择要查询老师或教研组', {
	      offset: 't',
	      time: 2000
	    });
	}
  }else{
    layer.msg('请先选择要查询老师或教研组', {
      offset: 't',
      time: 2000
    });
  }
}
function doExportResult(id){
	var searchAcadyear = $("#searchAcadyear").val();
	var searchSemester = $("#searchSemester").val();
	var searchWeek = $("#searchWeek").val();
  
	if(searchWeek ==""){
		layer.msg('周次为空，请先维护基础信息节假日设置', {
			offset: 't',
			time: 5000
	    });
  		return ;
	}

	var url = '${request.contextPath}/timetable/teacher/exportTimetableZip?searchAcadyear=' + searchAcadyear
			+ "&searchSemester=" + searchSemester + "&id=" + id + "&week=" + searchWeek + "&showPlace=" + $("#showPlace").val() + "&showClass=" + $("#showClass").val();
	document.location.href = url;
}
</script>

