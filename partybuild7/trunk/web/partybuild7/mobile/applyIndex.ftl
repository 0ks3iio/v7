<#import "/fw/macro/mobilecommon.ftl" as common>
<#assign tn = '积极分子考察' />
<#if applyState?default(0) == 1>
<#assign tn = '入党审批' />	
<#elseif applyState?default(0) == 2>
<#assign tn = '党员转正' />
</#if>
<@common.moduleDiv titleName=tn>
<link href="${request.contextPath}/partybuild7/mobile/css/style.css" rel="stylesheet"/>
<header class="mui-bar mui-bar-nav" style="display:none;">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"><span>返回</span></a>
    <a class="mui-btn-close">关闭</a>
    <h1 class="mui-title">${tn!}</h1>
</header>
<nav class="mui-bar mui-bar-tab mui-bottom-fixed" id="btnDiv">
	<div class="mui-input-row mui-checkbox mui-left">
	   <span>
		   <label class="mui-checkbox-all">全选</label>
		   <input name="checkbox" id="checkbox-all" value="Item 1" type="checkbox">
	   </span>
       <#--
       <input type="text" id="remark" style="width:60px;" class="mui-input-clear" data-input-clear="1" placeholder="审核意见" value="同意">
       -->
       <button type="button" id="pass-btn" class="mui-btn mui-btn-primary mui-btn-outlined" onclick="agree(1);">同意</button>
	   <button type="button" id="deny-btn" class="mui-btn mui-btn-primary mui-btn-outlined" onclick="agree(0);">继续审核</button>
	   <#if applyState?default(-1) lt 1><button type="button" class="mui-btn mui-btn-outlined" onclick="agree(-1);">退回</button></#if>
	</div>
</nav>
<div class="mui-content" style="padding-top:0px;">
    <div class="mui-segmented-control mui-segmented-control-inverted mui-segmented-control-fixed auditstate-div">
	    <a class="mui-control-item mui-active" id="auditState-0" href="#" auditState="0" applyState="${applyState?default(0)}">
	    	<span>未审核</span>
	    </a>
	    <a class="mui-control-item" href="#" id="auditState-1" auditState="1" applyState="${applyState?default(0)+1}">
	    	<span>已通过审核</span>
	    </a>
	    <a class="mui-control-item" href="#" id="auditState-2" auditState="2" applyState="${applyState?default(0)}">
	    	<span>继续审核</span>
	    </a>
	</div>
	<div class="mui-content-scroll" style="padding-bottom: 30px;">
		<div class="mui-control-content mui-active" id="memberListDiv">
		</div>
	</div>
</div>
<script type="text/javascript" charset="utf-8">
mui.init({
    <#--
    pullRefresh : {
        container:".mui-content",//待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
        up : {
            height:50,// 可选.默认50.触发上拉加载拖动距离
            auto:false,// 可选,默认false.自动上拉加载一次
            contentrefresh : "正在加载...",// 可选，正在加载状态时，上拉加载控件上显示的标题内容
            contentnomore:'没有更多数据了',// 可选，请求完毕若没有更多数据时显示的提醒内容；
            callback : function() {
                var self = this; // 这里的this == mui('#refreshContainer').pullRefresh()
                // 加载更多的内容
                loadMore(this);
            } //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
        }
    }-->
});

$('#checkbox-all').click(function(){
	var isChk = $(this).is(':checked');
	if(isChk){
		$('#memberListDiv input[type=checkbox]').prop('checked','checked');
	} else {
		$('#memberListDiv input[type=checkbox]').removeAttr('checked');
	}
});

$('.mui-checkbox-all').click(function(){
	$('#checkbox-all').click();
});

//unbind('click').bind('click', 
$('.auditstate-div a').on('touchend',function(){
	clickTab(this);
});

function clickTab(obj){
	$('#btnDiv').show();
	isSubmit=false;
	
	$(obj).addClass('mui-active').siblings('a').removeClass('mui-active');
	var as = $(obj).attr('auditState');
	if(as=='1'){
		$('#pass-btn').hide();
		$('#deny-btn').show();
	} else if(as=='2'){
		$('#pass-btn').show();
		$('#deny-btn').hide();
	} else {
		$('#pass-btn').show();
		$('#deny-btn').show();
	}
	var aps =  $(obj).attr('applyState');
	var url = "${request.contextPath}/mobile/open/partybuild7/memberList/list?orgId=${orgId!}&applyState="+aps+"&auditState="+as;
	$('#memberListDiv').load(url);
}

$("#cancelId").click(function(){
	if($('#remarkDiv').is(":visible")){
		$("#remark").val('');
		$('#selectDiv').show();
		$('#btnDiv').show();
		$('#remarkDiv').hide();
	} else {
		load("${request.contextPath}/mobile/open/partybuild7/homepage?teacherId=${teacherId!}");
	}
});

storage.set('org.id','${orgId?default("")}');
clickTab($('#auditState-${auditState?default(0)}'));

var passVal;
var idStr ='';
var isSubmit=false;
function agree(pass){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	
	var idar = '';
	$('#memberListDiv input[type=checkbox]').each(function(){
		if($(this).is(':checked')){
			if(idar != ''){
				idar=idar+',';
			}
			idar=idar+$(this).val();
		}
	});
	if(idar==''){
		toastMsg('没有选择要操作的记录！');
		isSubmit=false;
		return;
	}
	
	passVal = pass;
	idStr = idar;
	if(pass == 1){
		saveData();
		return;
	}
	$('#selectDiv').hide();
	$('#btnDiv').hide();
	$('#remarkDiv').show();
}

function saveData(){
	var remark = $("#remark").val();
	var applyState= new Number('${applyState!}') + passVal;
	
	if(passVal == 1){
	    isDeny = "0";
	}else{
	    isDeny = "1"
	}
	var options={
	    url:"${request.contextPath}/mobile/open/partybuild7/memberList/audit",
		data:{"applyState":applyState,"isDeny":isDeny,"remark":remark,"ids":idStr},
		type:"post",
		dataType:"json",
            success:function(data){
                var jsonO = data;
                if(!jsonO.success){
					//layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    //$("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    toastMsg(jsonO.msg);
                    return;
                }else{
					clickTab($('.auditstate-div .mui-active'));
					//layer.closeAll();
					//layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                }
				//layer.close(ii);
            },
            clearForm : false,
            resetForm : false,
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
	}
	$.ajax(options);
}

function save(){
	var remark = $("#remark").val();
	if(passVal !=1 && !isNotBlank(remark)){
		toastMsg('审核意见 不能为空！');
		isSubmit=false;
		return;
	}
	if(getLength(remark) >100){
		toastMsg('审核意见 超出最大长度限制100个字符！');
		isSubmit=false;
		return;
	}
	
	saveData();
}

</script>  
</@common.moduleDiv>