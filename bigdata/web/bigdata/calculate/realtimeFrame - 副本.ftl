<div class="row">
    <div class="col-xs-12">
        <div class="box box-default clearfix padding-20">
            <div class="col-md-4 no-padding-side clearfix">
            	<div class="tree labels no-radius-right float-left width-123">
                    <p class="tree-name border-bottom-cfd2d4 no-margin">
                        <b>类型</b>
                    </p>
                    <div class="type-choose js-scroll-height js-click">
						<div class="" id="2" >
							<img src="${request.contextPath}/static/images/big-data/Kylin-12143.png"/>
						</div>
						<div class="" id="6">
							<img src="${request.contextPath}/static/images/big-data/Flink-12143.png"/>
						</div>
						<div class="" id="4" >
							<img src="${request.contextPath}/static/images/big-data/spark-12143.png"/>
						</div>
						<div class="" id="5">
							<img src="${request.contextPath}/static/images/big-data/jstorm-12143.png"/>
						</div>
						<div class="" id="1">
							<img src="${request.contextPath}/static/images/big-data/KETTLE-12143.png"/>
						</div>
						<div class="" id="3">
							<img src="${request.contextPath}/static/images/big-data/SQOOP-12143.png"/>
						</div>
                    </div>
                </div>
                <div class="tree labels no-radius no-border-left float-left width-1of1-123" id="listDiv">
                </div>
            </div>
            <div class="col-md-5 no-padding-side" >
            	<div class="tree labels no-radius no-border-left" id="detailDiv"></div>
            </div>
            <div class="col-md-3 no-padding-side" >
            	<div class="tree labels no-radius-left no-border-left js-active-one" id="logListDiv"></div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="calculateType" value=""/>
<div class="layer layer-log">
	<div class="layer-content clearfix">
	</div>
</div>
<script type="text/javascript">
    $(function(){
        $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top -41,
                overflow: 'auto'
            })
        });
        
        $('.tree').each(function(){
            $(this).css({
                height: $(window).height() - $(this).offset().top - 40
            })
        });
        
    })
    
    $('.js-click>div').click(showList).hover(function(){
        $(this).addClass('select')
    },function(){
    	$(this).removeClass('select')
    });
    
    function showList(){
		var index=layer.msg('数据加载中......', {
	      icon: 16,
	      time:0,
	      shade: 0.01
	    });
	    
		var calculateType =	$(this).attr('id');
		$("#calculateType").val($(this).attr('id'));
		
		if($(this).hasClass('active') == false){
    		var $s = $(this).find('img').attr('src').slice(0,-4) + '-Click.png';
    		$(this).find('img').attr('src',$s);
    		if($(this).siblings().hasClass('active')){
    			var $s2 = $(this).siblings('.active').find('img').attr('src').slice(0,-10) + '.png';
    			$(this).siblings('.active').find('img').attr('src',$s2);
    		}
    		$(this).addClass('active').siblings().removeClass('active');
    	}
		
		var url =  "${request.contextPath}/bigdata/calculate/offline/list?calculateType="+calculateType;
		$("#listDiv").load(url,function() {
	  		layer.close(index);
		});
	}

	$(document).ready(function(){
		$("#${calculateType!}").click();
	});
</script>