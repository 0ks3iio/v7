<div id="a1" class="tab-pane active">
	<div class="picker-table">
        <table class="table">
            <tbody>
            <#if isAdmin>
                <tr>
                    <th width="150" style="vertical-align: top;">科目：</th>
                    <td>
                        <div class="outter">
                        	<#if courseList?exists && courseList?size gt 0>
                        		<#list courseList as course>
	                        		<#if (course_index==0)>
                                        <input style="display: none" value="${course.id}" id="initSubId">
                                    </#if>
								<a style="cursor:pointer" class="subList" id="sub_${course.id}" onclick="onSubClick('${course.id!}')">${course.subjectName!}</a>
								</#list>
                        	<#else>
                        		<a href="">无课程设置</a>
                        	</#if>
                        	<input value="${subjectId!}" id="subjectId" style="display: none">
                        </div>
                    </td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="javascript:void(0)"><span>展开</span><i class="fa fa-angle-down"></i></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>班级：</th>
                    <td>
                        <div class="outter">
                        	<#if clslist?exists && clslist?size gt 0>
                        		<#list clslist as cls>
								<a onclick="onClsClick('${cls.id!}_1')" class="clsList" style="cursor:pointer" id="clsId_${cls.id}_1">${cls.classNameDynamic!}</a>
								</#list>
                        	</#if>
                        	<#if teaClslist?exists && teaClslist?size gt 0>
                        		<#list teaClslist as cls>
								<a onclick="onClsClick('${cls.id!}_2')" class="clsList" style="cursor:pointer" id="clsId_${cls.id}_2">${cls.name!}</a>
								</#list>
                        	</#if>
                        	<input value="${clsTypeId!}" id="clsTypeId" style="display: none">
                        </div>
                    </td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
                        </div>
                    </td>
                </tr>
                <#else>
                <tr>
                    <th>班级：</th>
                    <td>
                        <div class="outter outter-auto">
                        	<#if clslist?exists && clslist?size gt 0>
                        		<#list clslist as cls>
								<a onclick="onClsClick('${cls.id!}_1')" class="clsList" style="cursor:pointer" id="clsId_${cls.id}_1">${cls.classNameDynamic!}</a>
								</#list>
                        	</#if>
                        	<#if teaClslist?exists && teaClslist?size gt 0>
                        		<#list teaClslist as cls>
								<a onclick="onClsClick('${cls.id!}_2')" class="clsList" style="cursor:pointer" id="clsId_${cls.id}_2">${cls.name!}</a>
								</#list>
                        	</#if>
                        	<input value="${clsTypeId!}" id="clsTypeId" style="display: none">
                        </div>
                    </td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="#"><span>折叠</span><i class="fa fa-angle-up"></i></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th width="150" style="vertical-align: top;">科目：</th>
                    <td>
                        <div class="outter">
                        	<#if courseList?exists && courseList?size gt 0>
                        		<#list courseList as course>
	                        		<#if (course_index==0)>
                                        <input style="display: none" value="${course.id}" id="initSubId">
                                    </#if>
								<a style="cursor:pointer" class="subList" id="sub_${course.id}" onclick="onSubClick('${course.id!}')">${course.subjectName!}</a>
								</#list>
                        	<#else>
                        		<a href="">无课程设置</a>
                        	</#if>
                        	<input value="${subjectId!}" id="subjectId" style="display: none">
                        </div>
                    </td>
                    <td width="75" style="vertical-align: top;">
                        <div class="outter">
                            <a class="picker-more" href="javascript:void(0)"><span>展开</span><i class="fa fa-angle-down"></i></a>
                        </div>
                    </td>
                </tr>
                </#if>
            </tbody>
        </table>
    </div>
    <div id="listDiv">
    </div>
</div>

<script>
$(function(){
	showTableList();
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
})
<#if isAdmin>
function onSubClick(subId) {
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var gradeId=$("#gradeId").val();
    var url =  '${request.contextPath}/exammanage/credit/register/daily?acadyear='+acadyear+'&semester='+ semester+'&gradeId='+gradeId+'&subjectId='+subId;
    $("#editDiv").load(url,function () {
        $(".subList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("sub_"+subId)).addClass("selected"); 
    });
    $("#initSubId").attr('value',subId);
};


function onClsClick(clsTypeId) {
    if(clsTypeId==""){
        $("#tableDiv").remove();
    }else{
    	var subjectId=$("#subjectId").val();
        var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
        var url = '${request.contextPath}/exammanage/credit/register/daily/page?subjectId=' + subjectId + '&clsTypeId=' + clsTypeId+ '&acadyear=' + acadyear+ '&semester=' + semester;
        $("#listDiv").load(url,function () {
            $(".clsList").each(function () {
                $(this).removeClass("selected")
            });
            $(eval("clsId_" + clsTypeId)).addClass("selected");
        });
    }
};
<#else>
function onClsClick(clsTypeId) {
    var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
    var url = '${request.contextPath}/exammanage/credit/register/daily?clsTypeId=' + clsTypeId+ '&acadyear=' + acadyear+ '&semester=' + semester;
    $("#editDiv").load(url,function () {
        $(".clsList").each(function () {
            $(this).removeClass("selected")
        });
        $(eval("clsId_" + clsTypeId)).addClass("selected");
    });
};

function onSubClick(subId) {
    if(subId==""){
        $("#tableDiv").remove();
    }else{
    	var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var clsTypeId=$("#clsTypeId").val();
	    var url =  '${request.contextPath}/exammanage/credit/register/daily/page?acadyear='+acadyear+'&semester='+ semester+'&clsTypeId='+clsTypeId+'&subjectId='+subId;
        $("#listDiv").load(url,function () {
            $(".subList").each(function () {
	            $(this).removeClass("selected")
	        });
	        $(eval("sub_"+subId)).addClass("selected"); 
        });
    }
};
</#if>
function showTableList(){
    var clsTypeId=$("#clsTypeId").val();
    if(clsTypeId==""){
        $("#listDiv").remove();
    }else{
        var subjectId=$("#subjectId").val();
        var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
        if(clsTypeId!=""&& typeof(clsTypeId)!="undefined"&&subjectId!=""&&typeof (subjectId)!="undefined") {
            var url = '${request.contextPath}/exammanage/credit/register/daily/page?subjectId=' + subjectId + '&clsTypeId=' + clsTypeId+ '&acadyear=' + acadyear+ '&semester=' + semester;
            $("#listDiv").load(url, function () {
                $(".clsList").each(function () {
                    $(this).removeClass("selected")
                });
                //$(eval("clsId_" + clsTypeId)).addClass("selected");
                //$(eval("sub_" + subjectId)).addClass("selected");
                $('#clsId_'+clsTypeId).addClass("selected")
                $("#sub_" + subjectId).addClass("selected");
            });
        }
    }
}

</script>
