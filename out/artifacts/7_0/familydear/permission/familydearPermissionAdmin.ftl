<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div id="showDiv" class="box box-default">
    <div class="nav-tabs-wrap clearfix" >
        <ul class="nav nav-tabs nav-tabs-1" id="roleStuShow"  role="tablist">
            <li role="presentation" class="active" val="1"><a href="#aa" onclick="stuShowList('1')" role="tab" data-toggle="tab">报名审核</a></li>
            <li role="presentation"  val="2" ><a href="#cc" role="tab" onclick="stuShowList('2')" data-toggle="tab">年度文件管理</a></li>
            <li role="presentation"  val="3" ><a href="#dd" role="tab" onclick="stuShowList('3')" data-toggle="tab">轮次管理</a></li>
            <li role="presentation"  val="4" ><a href="#dd" role="tab" onclick="stuShowList('4')" data-toggle="tab">信息填报管理员</a></li>
            <li role="presentation"  val="6" ><a href="#dd" role="tab" onclick="stuShowList('6')" data-toggle="tab">部门活动管理员</a></li>
            <li role="presentation"  val="5" ><a href="#ee" role="tab" onclick="stuShowList('5')" data-toggle="tab">结亲对象管理权限</a></li>
            <li role="presentation"  val="8" ><a href="#ee" role="tab" onclick="stuShowList('8')" data-toggle="tab">访亲人员名单管理</a></li>
            <li role="presentation"  val="7" ><a href="#ff" role="tab" onclick="stuShowList('7')" data-toggle="tab">三进两联管理权限</a></li>
        </ul>
    </div>
    <div id="showPermissionList">

    </div>
</div>
<script type="text/javascript">
    $(function(){
        stuShowList('1');
    });
    function stuShowList(tabIndex){
        var url =  '${request.contextPath}/familydear/permissionSet/list?permissionType='+tabIndex;
        $("#showPermissionList").load(url);
    }
</script>