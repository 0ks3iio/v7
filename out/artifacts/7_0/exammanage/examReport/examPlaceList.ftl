<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#if isgk=="1">
    <#if (groupList?exists && groupList?size>0) && (emPlaceList?exists && emPlaceList?size>0)>
        <div class="filter filter-f16">
            <div class="filter">
                <div class="filter-item">
                    <@htmlcomponent.printToolBar container=".print"/>
                    <a href="javascript:" class="btn btn-white  detaileExport" data-toggle="tooltip" data-original-title="导出考场考生详细安排">批量导出</a>
                </div>
            </div>
            <div class="picker-table">
                <table class="table">
                    <tbody>
                    <tr>
                        <th width="150">科目组：</th>
                        <td>
                            <div class="outter">
                                <#list groupList as group>
                                    <#if (group_index==0)>
                                        <input style="display: none" value="${group.id}" id="initId">
                                    </#if>

                                    <#if (group.subType?exists&&group.subType=="1")>
                                        <a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-orange position-relative top-1">选</span></a>
                                    <#elseif (group.subType?exists&&group.subType=="2")>
                                        <a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}<span class="badge badge-green position-relative top-1">学</span></a>
                                    <#else >
                                        <a class="groupList" style="cursor:pointer" onclick="onGroupClick('${group.id}')" id="group_${group.id}">${group.groupName!}</a>
                                    </#if>
                                </#list>
                            </div>
                        </td>
                        <td width="75" style="vertical-align: top;">
                            <div class="outter">
                                <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    <#else>
    <div class="filter filter-f16">
        <div class="no-data-container">
            <div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
			</span>
                <#if !(groupList?exists && groupList?size>0)>
                    <h3 class="no-data-body">
                        还未设置过考试科目
                    </h3>
                <#else >
                    <h3 class="no-data-body">
                        还未设置过考场考生编排
                    </h3>
                </#if>
            </div>
        </div>
    </div>
    </#if>
</#if>
<#if isgk=="1">
    <#if (groupList?exists && groupList?size>0)>
        <#if (emPlaceList?exists && emPlaceList?size>0)>
        <div class="print">
            <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                    <th class="text-center">考场编号</th>
                    <th class="text-center">考试场地</th>
                    <th class="text-center">所属教学楼</th>
                    <th class="text-center">考生总数</th>
                    <th class="text-center noprint">查看考生</th>
                </tr>
                </thead>
                <tbody>
                    <#if emPlaceList?exists>
                        <#list emPlaceList as item>
                        <tr>
                            <td class="text-center">${item.examPlaceCode!}</td>
                            <td class="text-center">${item.placeName!}</td>
                            <td class="text-center">${item.buildName!}</td>
                            <td class="text-center">${item.stuNum}</td>
                            <td class="text-center noprint"><a href="javascript:" onclick="showStudents('${item.id}')" class="table-btn">查看</a></td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
        </#if>
    </#if>
<#else >
    <#if (emPlaceList?exists && emPlaceList?size>0)>
    <div class="filter">
        <div class="filter-item">
        <@htmlcomponent.printToolBar container=".print"/>
        <a href="javascript:" class="btn btn-white  detaileExport" data-toggle="tooltip" data-original-title="导出考场考生详细安排">批量导出</a>
        </div>
    </div>
    <div class="table-container-body print">
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th class="text-center">考场编号</th>
                <th class="text-center">考试场地</th>
                <th class="text-center">所属教学楼</th>
                <th class="text-center">考生总数</th>
                <th class="text-center noprint">查看考生</th>
            </tr>
            </thead>
            <tbody>
                <#list emPlaceList as item>
                <tr>
                    <td class="text-center">${item.examPlaceCode!}</td>
                    <td class="text-center">${item.placeName!}</td>
                    <td class="text-center">${item.buildName!}</td>
                    <td class="text-center">${item.stuNum}</td>
                    <td class="text-center noprint"><a href="javascript:" onclick="showStudents('${item.id}')" class="table-btn">查看</a></td>
                </tr>
                </#list>
            </tbody>
        </table>
    <#else >
        <div class="filter filter-f16">
            <div class="no-data-container">
                <div class="no-data">
            <span class="no-data-img">
                <img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
            </span>
                    <h3 class="no-data-body">
                        还未设置过考场考生编排
                    </h3>
                </div>
            </div>
        </div>
    </#if>
</#if>
<iframe style="display:none;" id="hiddenFrame" name="hiddenFrame" />

<script>
function showStudents(emPlaceId){
	<#if isgk=="1">
		var url =  '${request.contextPath}/exammanage/examReport/studentPlaceList/page?emPlaceId='+emPlaceId+'&groupId=${groupId!}'
	<#else>
	var url =  '${request.contextPath}/exammanage/examReport/studentPlaceList/page?emPlaceId='+emPlaceId;
	</#if>
	$("#showTabDiv").load(url);
}

<#if isgk=="1">
function onGroupClick(groupId){
	var url =  '${request.contextPath}/exammanage/examReport/examPlaceList/page?examId=${examId!}&groupId='+groupId;
	$("#showTabDiv").load(url,function () {
        $(".groupList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("group_"+groupId)).addClass("selected");
    });
}
</#if>
$(function(){
    <#--<#if (groupList?exists && groupList?size>0)>-->
        <#--var initId = $("#initId").val();-->
        <#--onGroupClick(initId);-->
    <#--</#if>-->
	$('.detaileExport').on("click",function(){
		var url = "${request.contextPath}/exammanage/examReport/exportAllPlace?examId=${examId!}";
		hiddenFrame.location.href=url;
	});
    $('.picker-more').click(function(){
        if($(this).children('span').text()=='展开'){
            $(this).children('span').text('折叠');
            $(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
        }else{
            $(this).children('span').text('展开');
            $(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
        };
        $(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
    });
});

</script>