<div class="table-show">
    <div class="filter-made mb-10">
        <div class="filter-item filter-item-right clearfix">
            <button class="btn btn-lightblue" onclick="addGroupJob();">新增</button>
        </div>
    </div>
    <table class="tables tables-border no-margin">
        <thead>
        <tr>
            <th>组名称</th>
            <th>定时执行</th>
            <th>上次执行时间</th>
            <th>上次执行状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
		<#if groupList?exists&&groupList?size gt 0>
            <#list groupList as group>
			<tr>
                <td>${group.name!}</td>
                <td><#if group.isSchedule?default(0) ==1>是<#else>否</#if></td>
                <td>${group.lastCommitTime!}</td>
                <td>
						<#if group.lastCommitState?default(0) ==1>
                            <span class="success-left">成功</span>
                        <#elseif group.lastCommitState?default(0) ==2>
							<span class="fail-left">失败</span>
                        <#else>
							<span>&nbsp;<img src="${request.contextPath}/static/images/big-data/not-begin-icon.png" />&nbsp;&nbsp;未开始</span>
                        </#if>
                </td>
                <td>
					<#if group.lastCommitState?default(3) ==2>
						<a class="look-over" href="javascript:void(0);"  onclick="viewErrorLog('${group.lastCommitLogId!}')">
                            错误日志</a><span class="tables-line">|</span>
                    </#if>
                    <a href="javascript:;" class="look-over" onclick="execGroupJob('${group.id!}','${group.hasParam?default(0)}')">执行</a><span class="tables-line">|</span>
                    <a class="look-over" href="javascript:void(0);" onclick="editGroupJob('${group.id!}')">编辑</a><span class="tables-line">|</span>
                    <a class="remove" href="javascript:void(0);" onclick="deleteGroupJob('${group.id!}','${group.name!}')">删除</a><span class="tables-line">|</span>
                    <a id="log-${group.id!}-button"  href="javascript:;" class="look-over js-log-show" onclick="loadGroupLogList('${group.id!}');" >日志</a>
                </td>
            </tr>
            </#list>
        <#else>
			<tr >
                <td  colspan="5" align="center">
                    暂无批处理组数据
                </td>
			<tr>
      </#if>
        </tbody>
    </table>
</div>
<div id="logListDiv" class="log-show height-1of1 col-md-3 no-padding-right hide"></div>
<div id="logDiv" style="padding:20px 20px 0;border:1px;"></div>
<script>
    function addGroupJob(){
        router.go({
            path: '/bigdata/etl/group/edit',
            name:'新增',
            level: 3
        }, function () {
            var url =  '${request.contextPath}/bigdata/etl/group/edit';
            $("#contentDiv").load(url);
        });
    }

    function editGroupJob(id){
        router.go({
            path: '/bigdata/etl/group/edit?id='+id,
            name: '编辑',
            level: 3
        }, function () {
            var url =  '${request.contextPath}/bigdata/etl/group/edit?id='+id;
            $("#contentDiv").load(url);
        });
    }

    function loadGroupLogList(id){
        var logButtonObj=$("#log-"+id+"-button");
        if(logButtonObj.text() == '日志'){
            logButtonObj.parents('.table-show').addClass('col-md-9 no-padding-left');
            $('.log-show').removeClass('hide');
            logButtonObj.text('关闭');
            if(logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text().indexOf('关闭') != -1){
                logButtonObj.parents('tr').siblings('tr').find('.js-log-show').text('日志')
            }
            var logUrl = "${request.contextPath}/bigdata/etl/log/jobId?jobId="+id;
            $("#logListDiv").load(logUrl);
        }else{
            logButtonObj.parents('.table-show').removeClass('col-md-9 no-padding-left');
            $('.log-show').addClass('hide');
            logButtonObj.text('日志');
            $("#logListDiv").empty();
        }
    }

    function viewErrorLog(logId){
        var url =  '${request.contextPath}/bigdata/etl/viewLog?logId='+logId;
        $("#logDiv").load(url,function(){
            layer.open({
                type: 1,
                shade: .5,
                title: ['错误日志','font-size:16px'],
                area: ['800px','600px'],
                maxmin: false,
                btn:['确定'],
                content: $('#logDiv'),
                resize:true,
                yes:function (index) {
                    layer.closeAll();
                    $("#logDiv").empty();
                },
                cancel:function (index) {
                    layer.closeAll();
                    $("#logDiv").empty();
                }
            });
            $("#logDiv").parent().css('overflow','auto');
        })
    }

    function execGroupJob(id,hasParam){
        if(hasParam =="0"){
            $.ajax({
                url:'${request.contextPath}/bigdata/etl/job/exec',
                data:{
                    'id':id
                },
                type:"post",
                dataType: "json",
                success:function(data){
                    if(!data.success){
                        showLayerTips('success',data.msg,'t');
                    }else{
                        showLayerTips('success','后台正在执行，请稍后刷新页面查看结果','t');
                    }
                    var url =  "${request.contextPath}/bigdata/etl/list?etlType=9";
                    $("#contentDiv").load(url);
                }
            });
        }else{
            layer.open({
                type: 1,
                shade: 0.5,
                closeBtn:1,
                title: '请输入执行参数',
                area: '500px',
                btn: ['确定'],
                yes:function(index,layero){
                    $.ajax({
                        url:"${request.contextPath}/bigdata/etl/job/exec",
                        data: {
                            'id':id,
                            'params':$("#params").val()
                        },
                        dataType:'json',
                        type:'post',
                        success:function (data) {
                            if(!data.success){
                                showLayerTips('success',data.msg,'t');
                            }else{
                                showLayerTips('success',data.msg,'t');
                            }
                            parent.layer.close(index);
                            var url =  "${request.contextPath}/bigdata/etl/list?etlType=9";
                            $("#contentDiv").load(url);
                        }
                    })
                },
                content: $('.layer-param')
            })
        }
    }


    function deleteGroupJob(id,name){
        showConfirmTips('prompt',"提示1","您确定要删除"+name+"吗？",function(){
            $.ajax({
                url:"${request.contextPath}/bigdata/etl/group/delete",
                data:{
                    'id':id
                },
                type:"post",
                clearForm : false,
                resetForm : false,
                dataType: "json",
                success:function(data){
                    layer.closeAll();
                    if(!data.success){
                        showLayerTips4Confirm('error',data.msg);
                    }else{
                        showLayerTips('success',data.msg,'t');
                        var url =  "${request.contextPath}/bigdata/etl/list?etlType=9";
                        $("#contentDiv").load(url);
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){}
            });
        },function(){

        });
    }
</script>