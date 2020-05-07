<table class="table table-striped table-bordered table-hover text-center no-margin js-table-scroll">

    <#if goodStatList?exists&&(goodStatList?size>0)>
    <thead>
    <tr>
        <th>序号</th>
        <th>学生姓名</th>
        <th>学号</th>
        <th>${allGoodItem.itemName!}</th>
        <th>所有科目综合得分</th>
    </tr>
    </thead>
    <tbody>
    <#list goodStatList as item>
        <tr>
            <td>${item_index+1}</td>
            <td>${item.studentName!}
            <#if item.haveGood==1><img src="${request.contextPath}/static/images/evaluate/good.png" class="good_student"></#if></td>
            <td>${item.studentCode!}</td>
            <td>${item.standardSocre!}</td>
            <td>${item.allScore!}</td>
        </tr>
    </#list>
    </tbody>
    <#else >
        <div class="no-data-container">
            <div class="no-data">
                <span class="no-data-img">
                    <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                </span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无相关数据</p>
                </div>
            </div>
        </div>
    </#if>


    <#--<tr>-->
        <#--<th>序号</th>-->
        <#--<th>学生姓名</th>-->
        <#--<th>学号</th>-->
        <#--<th>体育综合得分</th>-->
        <#--<th>所有科目综合得分</th>-->
    <#--</tr>-->
    <#--</thead>-->
    <#--<tbody>-->
    <#--<tr>-->
        <#--<td>1</td>-->
        <#--<td>曹丽霞<img src="../images/evaluate/good.png" class="good_student" /></td>-->
        <#--<td>201106596</td>-->
        <#--<td>4</td>-->
        <#--<td>5</td>-->
    <#--</tr>-->

    <#--</tbody>-->
</table>
<script>
    $(function () {
        <#if percent?exists>
            $("#percent").val('${percent}');
        </#if>
    })
    <#--function statCon1(convertId){-->
        <#--$.ajax({-->
            <#--url : "${request.contextPath}/teacherasess/convert/stat",-->
            <#--data:{convertId:convertId},-->
            <#--dataType : 'json',-->
            <#--success : function(data){-->
                <#--var obj=data;-->
                <#--if(obj.type=="success"){-->
                    <#--var spanHtml = '<i class="fa fa-circle color-green font-12"></i> 已统计';-->
                    <#--$("#span"+convertId).html(spanHtml);-->
                    <#--$("#show"+convertId).removeClass("disabled");-->
                    <#--$("#stat"+convertId).removeClass("disabled");-->
                <#--}else if(obj.type=="error"){-->
                    <#--var spanHtml = '<i class="fa fa-circle color-red font-12"></i> 统计失败';-->
                    <#--$("#span"+convertId).html(spanHtml);-->
                    <#--$("#stat"+convertId).removeClass("disabled");-->
                <#--}else{-->
                    <#--//循环访问结果-->
                    <#--window.setTimeout("statCon1('"+convertId+"')",5000);-->
                <#--}-->
            <#--},-->
            <#--type : 'post',-->
            <#--error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错-->
        <#--});-->
    <#--}-->
</script>