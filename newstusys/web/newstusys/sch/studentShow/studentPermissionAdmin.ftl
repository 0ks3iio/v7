<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div id="showDiv" class="box box-default">
    <div class="box-body">
    <div class="nav-tabs-wrap clearfix" >
        <ul class="nav nav-tabs nav-tabs-1" id="roleStuShow"  role="tablist">
            <li role="presentation" class="active" val="1"><a href="#aa" onclick="stuShowList('1')" role="tab" data-toggle="tab">行政班</a></li>
            <li role="presentation"  val="2" ><a href="#bb" role="tab" onclick="stuShowList('2')" data-toggle="tab">教学班</a></li>
        </ul>
    </div>
	    <div id="showPermissionList" class="tab-content">
	    </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        stuShowList('1');
    });
    function stuShowList(tabIndex){
        var url =  '${request.contextPath}/stuwork/permission/list?permissionType=2&classType=' + tabIndex;
        $("#showPermissionList").load(url);
    }
</script>