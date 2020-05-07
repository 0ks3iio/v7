<div class="box-header" id="quality-tab-sign">
    <ul class="tabs">
        <li class="active"><a href="javascript:" data-action="tab">综合评价</a></li>
    </ul>
</div>
<!-- content -->
<div class="box-body scroll-content quality_space">
    
</div>
<!-- footer -->
<div class="Com_footer">
    <ul class="tabs tabsFlex">
    	<#list qualityTypeList as typeDto>
        <li <#if typeDto_index==0>class="active"</#if> data-value="${typeDto.type!}"> <a href="javascript:;" data-action="tab" >${typeDto.typeName!}</a></li>
        </#list>
    </ul>
</div>

<!-- 弹窗 -->
<div class="zaijiezaili">
	<input type="hidden" name="qualityCodeId" id="qualityCodeId">
    <h1 class="title">再接再励</h1>
    <ul class="rating" count="3.5">
        <li><span>科目</span></li>
        <li class="rating-item start"><i class="count"></i></li>
    </ul>
    <div class="zaijiezaili-btn">
        <button class="btn qualityCancle">确定</button>
    </div>
</div>

<div class="wenxintishi" style="display:none;">
    <div class="wenxintishi-title">
        <p>×</p>
    </div>
    <div class="wenxintishi-content">
        <img src="${request.contextPath}/static/eclasscard/standard/show/images/smile.jpg">
        <h2>二维码已被录入，不可再次录入哦~</h2>
        <button class="btn wenxintishi_btn">我知道了</button>
    </div>
</div>

<script>
	$(document).ready(function(){
		 $('.scroll-content').each(function() {
            $(this).css({
                overflow: 'auto',
                height: $(window).height() - $(this).offset().top - 160-60
            });
        });
        $(".Com_footer").on("click","li",function(e){
        	e.preventDefault();
        	$(this).addClass("active").siblings().removeClass('active');
        	var type=$(this).attr("data-value");
        	changeQualityType(type,"");
        })
        $(".Com_footer").find(".active").click();
        
        
        $(".qualityCancle").on("click",function(){
        	cancelQuality();
        })
        $(".wenxintishi_btn").on("click",function(){
        	cancelQuality();
        })
        $('.wenxintishi-title p').on('click', function(e) {
	        e.preventDefault();
	        layer.closeAll();
	    })
	})
	function changeQualityType(type,itemKey){
		var url = "${request.contextPath}/eccShow/eclasscard/standard/stuQuality/list/page?userId=${userId!}"+"&view="+_view+"&qualityType="+type+"&showTypeKey="+itemKey;
		$(".quality_space").load(url);
	}
	var isSubmitCode=false;
	function checkCode(qualityCodeId){
		if(isSubmitCode){
			return;
		}
		//重新计算60秒内不操作
		timeoutJump();
		layer.closeAll();
		layer.msg("二维码数据已采集，正在上传处理中，请稍后", {
			offset: 't',
			time: 2000
		});
		isSubmitCode=true;
	    $.ajax({
	        url:'${request.contextPath}/eccShow/eclasscard/standard/stuQuality/saveStudentScore',
	        data:{"qualityCodeId":qualityCodeId,"userId":"${userId!}"},
	        type:'post',
	        success:function(data){
	        	isSubmitCode=false;
	        	var jsonO = JSON.parse(data);
	        	if(jsonO.status=="200"){
	        		//跳转显示成绩
	        		showSuccessQuality(jsonO.score,jsonO.codeName);
	        		//跳转
	        		if($(".Com_footer").find("li[data-value='"+jsonO.itemType+"']").length>0){
		        		$(".Com_footer").find("li[data-value='"+jsonO.itemType+"']").addClass("active").siblings().removeClass('active');
		        		changeQualityType(jsonO.itemType,jsonO.itemKey);
		        	}
	        	}else{
	        		showFailQuality(jsonO.msg);
	        	}
	        },
	        error : function(XMLHttpRequest, textStatus, errorThrown) {
				showPrompt("signFail");
	        }
	    });
		
	}
	
	function cancelQuality(){
		isSubmitCode=false;
		layer.closeAll();
	}
	
	function showSuccessQuality(score,subjname){
		//先关闭其他弹出框
		layer.closeAll();
		var title="再接再励";
		if(score==5){
			title="真棒，继续保持";
		}
		$(".zaijiezaili").find(".title").text(title);
		$(".zaijiezaili").find(".rating").attr("count",score);
		$(".zaijiezaili").find("span").text(subjname);
		// 弹框
        layer.open({
        	time:3000,
			type: 1,
            title: false,
            shade: 0,
            shadeClose: true,
            closeBtn: 0,
            area: '100px',
            content: $('.zaijiezaili')
		})

	}
	
	function showFailQuality(msg){
		// 弹框
		layer.closeAll();
		if(msg==""){
			msg="二维码已被录入，不可再次录入哦~";
		}
		$('.wenxintishi').find("h2").text(msg);
        layer.open({
        	time:3000,
            type: 1,
            title: false,
            shade: 0,
            shadeClose: true,
            closeBtn: 0,
            area: '100px',
            content: $('.wenxintishi')
        });
	}
	
	
	
</script>
