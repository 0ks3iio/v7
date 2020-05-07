<div class="box box-default">
    <div class="box-body clearfix">
        <div class="tab-container">
            <div class="tab-header clearfix">
                <ul class="nav nav-tabs nav-tabs-1">
                    <li class="active">
                        <a data-toggle="tab" href="#" onClick="searchBeginList('1');">每月活动填报</a>
                    </li>
                    <#--<li >-->
                        <#--<a data-toggle="tab" href="#" onClick="searchBeginList('2');">部门每月活动</a>-->
                    <#--</li>-->
                </ul>
            </div>
            <div class="tab-content" id="myTabDiv1"></div>
        </div>
    </div>
</div>

<script>
    $(function(){
        searchBeginList('1');
    });

    function searchBeginList(index){
        if(index == '1'){
            var url = "${request.contextPath}/familydear/famdearMonth/famdearMonthIndex";
            $('#myTabDiv1').load(url);
        }
    }
</script>