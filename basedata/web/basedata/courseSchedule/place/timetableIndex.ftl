<#import "/fw/macro/treemacro.ftl" as treemacro>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="box box-default">
            <div class="box-body">
                <div class="clearfix">
                    <!-- 树菜单开始 -->
                    <div class="tree-wrap">
                        <h4>${unitName!}</h4>
                        <@treemacro.placeForBuildingTree height="645" click="onTreeClick"/>
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
                                      <input type="hidden" class="additionSwitch" id="showTeacher" value="true">
                                      <a href="javascript:" class="btn btn-blue"
                                         onclick="showClassOrTeacher('.addition_teacher', this)">隐藏老师</a>
                                      <input type="hidden" class="additionSwitch" id="showClass" value="true">
                                      <a href="javascript:" class="btn btn-blue"
                                         onclick="showClassOrTeacher('.addition_class', this)">隐藏班级</a>
                                  </div>
                              </div>

                          </div>
                          <div class="filter filter-f16" id="printDivId" style="display:none">
	                         <@htmlcomponent.printToolBar container=".print" printDirection="true" printUp=0 printLeft=0 printBottom=0 printRight=0/>
                          </div>
                          <div class="filter filter-f16" id="doExportId" style="display:none">
                              <a href="javascript:" class="btn btn-blue" data-toggle="tooltip" data-original-title="导出全部课表">导出全部场地课表</a>
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
    function onTreeClick(event, treeId, treeNode, clickFlag){
        var id = treeNode.id;
        if (treeNode.type=="build") {
            $("#printDivId").hide();
            $("#doExportId").show();
            $("#doExportId").on("click",function(){
                doExportResult(id);
            });
        }
        if(treeNode.type == "place"){
            doChangeTable(id);
            $("#doExportId").hide();
            $("#printDivId").show();
        }
    }
function doChangeTable(id){
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
    var url = '${request.contextPath}/timetable/place/timeTable/page?searchAcadyear=' + searchAcadyear
        + "&searchSemester=" + searchSemester + "&id=" + id + "&week=" + searchWeek + "&showTeacher=" + $("#showTeacher").val() + "&showClass=" + $("#showClass").val();
  $("#showTableDiv").load(url);
};

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

    function showClassOrTeacher(name, obj) {
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

function changeTable(){
  var zTree = $.fn.zTree.getZTreeObj("placeForBuildingTree");
  var node = zTree.getSelectedNodes();
  if(node.length!=0){
    var id = node[0].id;
    var type = node[0].type;
    if(type == "place"){
      doChangeTable(id);
    }else{
      	layer.msg('请先选择要查询场地', {
	      offset: 't',
	      time: 2000
	    });
      }
  }else{
    layer.msg('请先选择要查询场地', {
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

    var url = '${request.contextPath}/timetable/place/exportTimetableZip?searchAcadyear=' + searchAcadyear
        + "&searchSemester=" + searchSemester + "&id=" + id + "&week=" + searchWeek + "&showPlace=" + $("#showPlace").val() + "&showClass=" + $("#showClass").val();
    document.location.href=url;
}
</script>

