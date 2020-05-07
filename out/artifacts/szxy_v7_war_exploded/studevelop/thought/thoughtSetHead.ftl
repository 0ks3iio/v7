<!DOCTYPE html>
<html lang="en">
<body></body>
    <div class="box-body">
        <div class="filter">
            <input type="hidden" name="schoolId" id="schoolId" value="${schoolId!}" >
            <input type="hidden" name="code" id="code" value="${code!}" >
            <div class="filter-item">
                <label for="" class="filter-name">学年：</label>
                <div class="filter-content">
                    <select class="form-control" id="queryAcadyear" onChange="changeOptions();" style="width:168px;">
                    <#if (acadyearList?size>0)>
                        <#list acadyearList as item>
                            <option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学期：</label>
                <div class="filter-content">
                    <select class="form-control" id="querySemester" onChange="changeOptions();" style="width:168px;">
                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学段：</label>
                <div class="filter-content">
                    <select vtype="selectOne" class="form-control" id="section" onChange="changeOptions();" style="width:168px;">
                    <#list sectionMap?keys as key>
                        <option value="${key!}">${sectionMap[key]}</option>
                    </#list>
                    </select>
                </div>
            </div>
        </div>
        <div id="aa" class="tab-pane active" role="tabpanel">
            <div class="table-container">
                <div class="table-container-header text-right">
                    <button type="button" class="btn btn-blue js-addScoreItem" onclick="editLink('')">添加思想素质项目</button>
                    <#--<button type="button" class="btn btn-blue" onclick="copyLink();">复制</button>-->
                </div>
                <div id="itemDiv">

                </div>
            </div>
        </div>

    </div>
<form id="performForm">
    <div class="layer layer-addScoreItem" id="editDiv" >
    </div>
</form>

<!-- basic scripts -->

<!-- inline scripts related to this page -->
<script>
    function copyLink() {

        var id_array=[];
        var ind=0;
        var itemIds="";
        $("input[name='stu-checkbox']:checked").each(function(){
            id_array[ind++]=$(this).val();
            itemIds+=','+$(this).val();
        });
        if(id_array.length == 0){
            layer.alert('至少选择一个项目！',{icon:7});
            return;
        }
        itemIds = itemIds.substring(1,itemIds.length);
        $("#editDiv").load("${request.contextPath}/stuDevelop/healthyHeart/projectItemCopyLink?itemIds="+itemIds );
        layer.open({
            type: 1,
            offset:"t",
            shade: .5,
            title:'复制到其他学年学期学段',
            btn :['确定','取消'],
            btn1:function(index ,layero){
                doProjectItemCopy();
                return false;
            },
            btn2:function(index ,layro){
                layer.closeAll();
            },
            area: '360px',
            content: $(' .layer-addScoreItem')
        });

    }
    function doProjectItemCopy(){
        var options={
            url:"${request.contextPath}/stuDevelop/healthyHeart/projectItemDoCopy",
            dataType:"json",
            type:"post",
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    if(jsonO.code == "-11"){
                        showWarnMsg(jsonO.msg);
                    }else{
                        layerTipMsg(jsonO.success,"复制失败",jsonO.msg);
                    }

                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"复制成功",jsonO.msg);
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}

        };
        $("#performForm").ajaxSubmit(options);
    }
    function getParamter(){
        var acadyear = $("#queryAcadyear").val();
        var semester = $("#querySemester").val();
        if(acadyear == '${acadyear!}' && semester=='${semester!}'){
            $('#copyButton').show();
        }else{
            $('#copyButton').hide();
        }
        var schSection = $("#section").val();
        var schoolId = $("#schoolId").val();
        var code = $("#code").val();
        var objectType = '31';
        var url = "&acadyear="+acadyear+"&semester="+semester + "&section="+schSection+"&objectType="+objectType+"&schoolId="+schoolId+"&code="+code;

        return url;
    }
    function editLink(itemId ){
        var url ="${request.contextPath}/stuDevelop/commonProject/projectItem/edit?id=" + itemId;

        url = url+ getParamter();
        $("#editDiv").load(url);

        layer.open({
            type: 1,
            offset:"t",
            shade: .5,
            title:'添加身心项目',
            btn :['确定','取消'],
            closeBtn: 0,
            btn1:function(index ,layero){
                saveProject();
                return false;
            },
            btn2:function(index ,layro){

                layer.closeAll();
            },
            area: '500px',
            content: $('.layer-addScoreItem')
        });
    }
    function saveItem() {
        var check = checkValue("#performForm")
        if(!check){
            return;
        }
        var options={
            url:"${request.contextPath}/stuDevelop/healthyHeart/healthyItem/save",
            dataType:"json",
            type:"post",
//            data:{"itemGrade":gradeCode},
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    return;
                }else{
                    layer.closeAll();
                    layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
                    changeOptions();
                }
            },
            clearForm:false,
            resetForm:false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
        };
        $("#performForm").ajaxSubmit(options);
    }
    jQuery(document).ready(function(){
        var projectType = $("#healthType").find("li[class = 'active']").find("a").attr("val");
        // itemList(projectType);
    })
    <#--function gradeList() {-->
        <#--var section = $("#section").val();-->
        <#--var options = {-->
            <#--url:"${request.contextPath}/studevelop/performanceItem/gradeList/page?section="+section,-->
            <#--type:"post",-->
            <#--dataType:"json",-->
            <#--async:false,-->
            <#--success:function (data) {-->
                <#--if(data.length == 0 ){-->
                    <#--$("#gradeUl").html("");-->
                    <#--$("#itemDiv").html("");-->
                    <#--return;-->
                <#--}-->
                <#--var len = data.length;-->

                <#--var html="";-->
                <#--for(var i=0;i<data.length;i++){-->
                    <#--var gradeCode = data[i].gradeCode;-->
                    <#--var name = data[i].gradeName;-->
                    <#--html += "<li role=\"presentation\"  ";-->
                    <#--if(i ==0){-->
                        <#--html += " class=\"active\"  ";-->
                    <#--}-->

                    <#--html += " ><a href=\"javascript:void(0)\" role=\"tab\" val=\"";-->
                    <#--html += gradeCode +"\" onclick=\"gradePerformItem('" + gradeCode + "');\" data-toggle=\"tab\">" +  name+ "</a>"-->
                <#--}-->
                <#--$("#gradeUl").html(html);-->
            <#--}-->
        <#--}-->
        <#--$.ajax(options);-->

        <#--var gradeCode = $("#gradeUl").find("li[class = 'active']").find("a").attr("val");-->
        <#--debugger-->
        <#--if(gradeCode != "" && gradeCode != undefined ){-->
            <#--var url = "${request.contextPath}/studevelop/performanceItem/list/page";-->
            <#--$("#itemDiv").load(url,{"gradeCode":gradeCode});-->
        <#--}-->


    <#--}-->
    function itemList(code){
        var url = "${request.contextPath}/stuDevelop/healthyHeart/list/page?projectType="+code ;
        var acadyear = $("#queryAcadyear").val();
        var semester = $("#querySemester").val();
        var schSection = $("#section").val();
        var schoolId = $("#schoolId").val();
         url = url+ "&acadyear="+acadyear+"&semester="+semester + "&schSection="+schSection+"&schoolId="+schoolId;

        $("#itemDiv").load(url);

    }
    function changeOptions(){
        var url = "${request.contextPath}/stuDevelop/healthyHeart/list/page?a=1";
        url = url + getParamter();
        $("#itemDiv").load(url);

    }
</script>
</body>
</html>
