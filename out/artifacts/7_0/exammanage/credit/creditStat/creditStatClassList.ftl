


                    <div class="credit-item">
                        <div class="row">
                            <#if gradeList?exists&& (gradeList?size > 0)>
                            <#list gradeList as item>
                                <div class="col-sm-6">
                                    <div class="credit-item-body">
                                        <div class="credit-minitem-title">
                                            <div class="row">
                                                <div class="col-sm-3">${item.gradeName}</div>
                                                <div class="col-sm-9 text-right">
                                                    <#if isAdmin>
                                                        <a class="btn btn-link btn-info credit-text" id="sumScore${item_index}" onclick="sumScore('${item.id}','${item.gradeName}',${item_index})">
                                                            统计总分
                                                        </a>
                                                        <i class="credit-minitem-line"></i>
                                                    </#if>
                                                        <a class="btn btn-link btn-info credit-text" id="exportScore${item_index}" onclick="exportList('${item.id}')">
                                                            导出补考名单
                                                        </a>
                                                        <i class="credit-minitem-line" id="exportScoreLine${item_index}"></i>
                                                        <a class="btn btn-link btn-info credit-text" id="importScore${item_index}" onclick="importList('${item.id}','${item.gradeName}')">
                                                            导入补考成绩
                                                        </a>
                                                        <i class="credit-minitem-line" id="importScoreLine${item_index}"></i>
                                                    <a class="btn btn-link btn-info credit-text" onclick="viewDetail('${item.id}','${item.gradeName}')">
                                                        详情
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="progress credit-bar">
                                            <div id="exambar_${item.id!}"
                                                    class="progress-bar"
                                                    role="progressbar"
                                                    aria-valuenow="60"
                                                    aria-valuemin="0"
                                                    aria-valuemax="100"
                                                    style="width: 0%;"
                                            ></div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-6">登记率</div>
                                            <div class="col-sm-6 text-right" id="exam_${item.id!}">
                                                0%
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                    <#if (item_index+1)%2==0>
                                        <div class="cl"></div>
                                    </#if>
                                </#list>
                                <#else >
                                <div class="no-data-container">
                                    <div class="no-data">
                                        <span class="no-data-img">
                                            <img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
                                        </span>
                                        <div class="no-data-body">
                                            <p class="no-data-txt">暂无数据</p>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>
                    <!-- tab切换结束 -->

                    <#--<div class="credit-nocontent">暂无数据...</div>--
    <!-- /.row -->
<script>
    <#if gradeList?exists&& (gradeList?size > 0)>
        <#list gradeList as grade>
		        getNum('${grade.id!}',"");
        </#list>
    </#if>
    var isSubmit=false;
    function sumScore(gradeId,gradeName,index) {
        if(!isSubmit) {
            isSubmit = true;
            var year = $("#searchAcadyear").val();
            var semester = $("#searchSemester").val();
            $("#exportScore"+index).hide();
            $("#importScore"+index).hide();
            $("#exportScoreLine"+index).hide();
            $("#importScoreLine"+index).hide();
            $("#sumScore"+index).html("正在统计...");
            var url = '${request.contextPath}/exammanage/credit/creditStatStuSum?';
            $.ajax({
                data: {'gradeId': gradeId, 'year': year, 'semester': semester},
                url: url,
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    var jsonO = data;
                    if (!jsonO.success) {
                        layerTipMsg(jsonO.success, "统计失败", jsonO.msg);
                        $("#sumScore"+index).html("统计总分");
                        $("#exportScore"+index).show();
                        $("#sumScore"+index).show();
                        $("#importScore"+index).show();
                        $("#exportScoreLine"+index).show();
                        $("#importScoreLine"+index).show();
                        return;
                    } else {
                        var url = '${request.contextPath}/exammanage/credit/creditStatSumScore?gradeId=' + gradeId + "&year=" + year + "&gradeName=" + gradeName + "&semester=" + semester;
                        $("#mydiv").load(url);
                    }
                    isSubmit = false;
                    $("#sumScore"+index).html("统计总分");
                    $("#exportScore"+index).show();
                    $("#importScore"+index).show();
                    $("#sumScore"+index).show();
                    $("#exportScoreLine"+index).show();
                    $("#importScoreLine"+index).show();
                },
                error:function () {
                    isSubmit = false;
                }
            })
        }
        // $("#mydiv").load(url);
    }


    function getNum(gradeId,type){
        var acadyear = $('#searchAcadyear').val();
        var semester = $('#searchSemester').val();
        $.ajax({
            url:"${request.contextPath}/exammanage/credit/getNum?acadyear="+acadyear+"&semester="+semester,
            data:{'gradeId':gradeId,'type':type},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                $("#exam_"+gradeId).html(jsonO.num+"%");
                $("#exambar_"+gradeId).attr('style','width: '+jsonO.num+'%;');
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
    function exportList(gradeId) {
        if(!isSubmit) {
            var year = $("#searchAcadyear").val();
            var semester = $("#searchSemester").val();
            var url = '${request.contextPath}/exammanage/credit/exportPatchStudent?year=' + year + "&semester=" + semester + "&gradeId=" + gradeId
            document.location.href = url;
        }

    }
    function importList(gradeId,gradeName) {
        if(!isSubmit) {
            var year = $("#searchAcadyear").val();
            var semester = $("#searchSemester").val();
            var url = '${request.contextPath}/exammanage/credit/import/main?gradeId=' + gradeId + "&year=" + year + "&gradeName=" + gradeName + "&semester=" + semester;
            $("#mydiv").load(url);
        }
    }
    function viewDetail(gradeId,gradeName) {
        if(!isSubmit) {
            var year = $("#searchAcadyear").val();
            var semester = $("#searchSemester").val();
            var url = '${request.contextPath}/exammanage/credit/creditStatSumScore?gradeId=' + gradeId + "&year=" + year + "&gradeName=" + gradeName + "&semester=" + semester;
            $("#mydiv").load(url);
        }
    }
</script>