<div class="btn-suite">
	<input type="hidden" id="dsType" value=""/>
    <div class="btn-group">
        <span class="btn btn-blue js-db" onclick="showList('1');">数据库</span>
        <span class="btn btn-default js-api" onclick="showList('2');">API</span>
    </div>
    <button id="dbButton" class="btn btn-lightblue add-data" onclick="addDb();">新增数据库</button>
    <button id="apiButton" class="btn btn-lightblue add-api none" onclick="addApi();">新增API</button>
</div>
<div id="tableList" class=""></div>
<div class="layer layer-data">
    <div class="grid clearfix grid-made">
        <#list typeList as item>
            <div class="grid-cell grid-1of3" onclick="addDatabase('${item.type!}')">
                <div class="grid-content">
                    <div class="drive no-padding mydrive" style="cursor:pointer;">
                        <img src="${request.contextPath}/bigdata/v3/static/images/big-data/${item.thumbnail!}" alt="" class="four-three">
                    </div>
                    <div class="padding-side-20">
                        <p align="center">${item.thumbnail?replace(".png", "")}</p>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</div>
<script type="text/javascript">
function showList(dsType){
	 if(!dsType)
			dsType=$("#dsType").val();
	 var titleName="数据库";	
	 if(dsType =="2"){
	 	 titleName="API";	
	 }
	router.go({
        path: '/bigdata/datasource/list?dsType='+dsType,
        name: titleName,
        level: 2
    }, function () {
	    if(dsType =="1"){
	    	$('#dbButton').show();
			$('#apiButton').hide();
			$('.js-db').removeClass('btn-default').addClass('btn-blue').siblings('.btn').removeClass('btn-blue').addClass('btn-default');
			$('#tableList').removeClass().addClass('box data-page no-margin');
	    }else if (dsType == "2"){
	    	$('#dbButton').hide();
			$('#apiButton').show();
			$('.js-api').removeClass('btn-default').addClass('btn-blue').siblings('.btn').removeClass('btn-blue').addClass('btn-default');
			$('#tableList').removeClass().addClass('box-default');
	    }
		$("#dsType").val(dsType);
    	var url =  "${request.contextPath}/bigdata/datasource/list?dsType="+dsType;
        $("#tableList").load(url);
    });
}

function addDb(){
    layer.open({
        type: 1,
        shade: .6,
        title:'新增数据库',
		area: ['750px','700px'],
        content: $('.layer-data'),
        yes: function(){
        }
    });
    $('.layer-data').parent('.layui-layer-content').css('overflow','auto')
};

function addDatabase(type) {
    $('.layui-layer-close').trigger('click');
    if (type == "06" || type == "07" || type == "08" || type == "09" || type == "12") {
        addNoSqlDB(type);
    } else if (type == "10" || type=="11") {
        addFileDB(type);
    } else if (type == "13") {
        addHbase();
    } else {
        addDB(type);
    }
}

$(document).ready(function(){
	showList('1');
});
</script>