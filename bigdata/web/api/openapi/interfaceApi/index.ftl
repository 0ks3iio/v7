<div class=" overview-set bg-fff metadata interfaceApi clearfix">
    <div class="filter-item"  dataType="1" dataName="基础数据">
        <span>基础数据</span>
    </div>
    <div class="filter-item"  dataType="2" dataName="业务数据">
        <span>业务数据</span>
    </div>
    <div class="filter-item"  dataType="3" dataName="保存数据">
        <span>保存数据</span>
    </div>
    <div class="filter-item"  dataType="4" dataName="更新数据">
        <span>更新数据</span>
    </div>
</div>
<div class="box clearfix">
      <div class="deve-content clearfix" id="interDiv">
      </div>
</div>
<div class="layer layer-metadata">

</div>
<script type="text/javascript">
    var dataType;
    $(function () {
        // 顶部切换按钮
        $('.interfaceApi .filter-item').on('click', function () {
            $(this).addClass('active').siblings().removeClass('active');
            dataType = $(this).attr('dataType');
            var name = $(this).attr('dataName');
            showTab(dataType,name);
        });
        $('.interfaceApi .filter-item').first().trigger('click');
    })
    function applyInterface(){
    	var url = "${request.contextPath}/bigdata/api/apply/list?dataType=" + dataType;
        $("#interDiv").load(url);
    }
    
    function showTab(dataType,name){
    	router.go({
	        path: '${request.contextPath}/bigdata/api/interface/'+dataType+'/list',
	        type: 'item',
	        name: name,
	        level: 2,
	    }, function () {
	        $('#interDiv').load('${request.contextPath}/bigdata/api/interface/'+dataType+'/list');
	    })
	    $('#interDiv').load('${request.contextPath}/bigdata/api/interface/'+dataType+'/list');
    }
</script>