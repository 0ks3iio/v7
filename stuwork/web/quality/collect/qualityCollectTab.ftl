<div class="box box-default">
	<div class="box-body">
        <ul class="nav nav-tabs nav-tabs-1">
            <li class="active">
                <a href="javascript:void(0)" onclick="showIndex('1');" data-toggle="tab" data-value="1">学习汇总</a>
            </li>
            <li>
                <a href="javascript:void(0)" onclick="showIndex('2');" data-toggle="tab" data-value="2">德育汇总</a>
            </li>
            <li>
                <a href="javascript:void(0)" onclick="showIndex('3');" data-toggle="tab" data-value="3">体育汇总</a>
            </li>
            <li>
                <a href="javascript:void(0)" onclick="showIndex('4');" data-toggle="tab" data-value="4">节日汇总</a>
            </li>
        </ul>
        <div class="tab-content" id="showIndexDiv">
        	
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
       showIndex('1');
	})
	function showIndex(type){
		$("#showIndexDiv").load("${request.contextPath}/quality/collect/index/page?type="+type);
	}
</script>
