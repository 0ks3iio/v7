<div class=" overview-set bg-fff metadata metadata clearfix ">
    <div class="filter-item active" onclick="showTab('1')" >
        <span>类型管理</span>
    </div>
    <div class="filter-item" onclick="showTab('2')">
        <span>接口管理</span>
    </div>
    <div class="filter-item" onclick="showTab('3')">
        <span>属性管理</span>
    </div>
</div>
<div class="box box-default clearfix">
    <div class="height-1of1 no-padding" id="mainDiv">
    
    </div>
</div>
<div class="layer layer-api">

</div>
<script>
$(function(){
	//线上样式高度改变
    $(".box").css("height","calc(100% - 46px)");
	
	
    $('.metadata .filter-item').on('click',function(){
    	$(this).addClass('active').siblings().removeClass('active');
    });
    showTab('1');
    function height() {
        $('.js-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top - 20,
            });
        });
        $('.js-chart').each(function () {
            $(this).height('100%').width('100%')
        });
    }
    height();
})
function showTab(type){
	if(type=='1'){
		router.go({
	        path: '/bigdata/api/interType/showIndex',
	        type: 'item',
	        name: '类型管理',
	        level: 2,
	    }, function () {
	        $('#mainDiv').load('${request.contextPath}/bigdata/api/interType/showIndex');
	    })
	}else if (type == '2'){
		router.go({
	        path: '/bigdata/api/inteManager/showIndex',
	        type: 'item',
	        name: '接口管理',
	        level: 2,
	    }, function () {
	        $('#mainDiv').load('${request.contextPath}/bigdata/api/inteManager/showIndex');
	    })
	}else {
		router.go({
	        path: '/bigdata/api/interEntity/showResultTypeList',
	        type: 'item',
	        name: '属性管理',
	        level: 2,
	    }, function () {
	        $('#mainDiv').load('${request.contextPath}/bigdata/api/interEntity/showResultTypeList');
	    })
	}
}

function dealDValue(container){
    var tags = ["input","select","textarea"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var type = $this.attr("type");
            if ((type == 'number' || type=='int') && value!='' && value!=null) {
                value = parseInt(value);
            }
            var name = $this.attr("name");
            name = name || $this.attr("id");
            var exclude = $this.attr("exclude");
            if (!exclude) {
                if (obj[name] && !(obj[name] instanceof Array)) {
                    var array = new Array();
                    array.push(obj[name]);
                    array.push(value);
                    obj[name] = array;
                } else if (obj[name] && obj[name] instanceof Array){
                    obj[name].push(value);
                }else{
                    obj[name] = value;
                }
            }
        });
    }
    return JSON.stringify(obj);
}
</script>
