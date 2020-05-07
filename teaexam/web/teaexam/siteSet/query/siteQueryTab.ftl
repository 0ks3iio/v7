<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
			<ul class="nav nav-tabs nav-tabs-1" role="tablist">
				<li role="presentation" <#if index=='1'>class="active"</#if>><a href="#aa" role="tab" data-toggle="tab" onClick="queryList('1')">考场门贴</a></li>
				<li role="presentation" <#if index=='2'>class="active"</#if>><a href="#bb" role="tab" data-toggle="tab" onClick="queryList('2')">考生桌贴</a></li>
				<li role="presentation" <#if index=='3'>class="active"</#if>><a href="#cc" role="tab" data-toggle="tab" onClick="queryList('3')">考生准考证</a></li>
			</ul>
		</div>
		<div class="tab-content" id="siteTabDiv"></div>
	</div>
</div>
<script>

$(function(){	
	<#if index=='1'>
       queryList('1');
   <#elseif index=='2'>
       queryList('2');
   <#else>
       queryList('3');
   </#if>
   showBreadBack(back,true,"考场查询");
});

function back(){
    var acadyear = '${year!}';
    var url = "${request.contextPath}/teaexam/siteSet/query/page?year="+acadyear;
    $('.model-div').load(url);
}

function queryList(type){
    var url = '';
    var examId = '${examId!}';
    if(type=='1'){
        url = "${request.contextPath}/teaexam/siteSet/query/mPasteList?examId="+examId;
    }else if(type=='2'){
        url = "${request.contextPath}/teaexam/siteSet/query/zPasteList?examId="+examId;
    }else{
        url = "${request.contextPath}/teaexam/siteSet/query/examCardList?examId="+examId;
    }
    $('#siteTabDiv').load(url);
}
</script>