<a id="backA" onclick="goBack();" style="display: none" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
    <div class="box-body clearfix">
        <div class="tab-container">
            <div class="tab-header clearfix" id = "headIndex">
                <ul class="nav nav-tabs nav-tabs-1">
				 	<li class="active">
                        <a data-toggle="tab" href="#" onClick="searchReport('1');">信息填报</a>
                    </li>
                    <#--<li >
                        <a data-toggle="tab" href="#" onClick="searchBeginList('2');">每月活动填报</a>
                    </li>-->
                </ul>
            </div>
            <div class="tab-content" id="myTabDiv"></div>
        </div>
    </div>
</div>

<script>
    $(function(){
        searchReport('1');
    });

    function goBack(){
        var picIds = $("#picIds").val();
        var url= "";
        if(picIds){
            url="${request.contextPath}/familydear/famdearActualReport/famdearCheckAttach?picIds="+picIds
        }else {
            url="${request.contextPath}/familydear/famdearActualReport/famdearCheckAttach?"
        }
        //返回的时候删除为未添加标题的图片
        var options = {
            url : url,
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    return;
                }else{
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
        $("#backA").hide();
        $("#headIndex").show();
        searchReport('1');
        // searchList();
    }

    function searchReport(index){
        var activityId = "";
        if($("#activityId").val()){
            activityId = $("#activityId").val();
        }
        var year = "";
        if($("#year").val()){
            year = $("#year").val();
        }
        var village="";
        if($("#village").val()){
            village = $("#village").val()
        }
        if(index == '1'){
            var url = "${request.contextPath}/familydear/famdearActualReport/famdearActualReportIndex?activityId="+activityId+"&year="+year+"&village="+village;
            $('#myTabDiv').load(url);
        }
    }
</script>