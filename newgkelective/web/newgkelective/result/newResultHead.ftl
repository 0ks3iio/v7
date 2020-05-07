<#--<a href="javascript:void(0);" class="page-back-btn" onclick="goback('${gradeId!}')"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default" style="box-shadow: 0px 1px 3px rgba(255,255,255,0.6);">
	<div class="box-header">
		<h3 class="box-title">${choiceName!}</h3>
		<div class="box-header-tools">
			<div class="btn-group js-changeView" data-toggle="buttons">
				<button id="view-chart" class="btn btn-sm btn-white active">统计图</button>
				<button id="view-table" class="btn btn-sm btn-white">表格</button>
			</div>
			<button id="view-table-export" class="btn btn-sm btn-blue" type="button">导出</button>
		</div>
	</div>
	<input type="hidden" value="${choiceId!}" id="choiceId">
	<div class="box-body">
		<div class="picker-table">
			<table class="table">
				<tbody>
					<tr>
						<th width="150">统计维度：</th>
						<td>
							<a href="javascript:;" statisticType="1" class="statistic-type statistic-combine selected">按组合统计</a>
							<#if isGradeRole?default(false)><a href="javascript:;" statisticType="2" class="statistic-type statistic-score">按成绩统计</a></#if>
						</td>
					</tr>
					<tr>
						<th width="150">班级：</th>
						<td>
							<div class="outter clazz-mark">
								<a href="javascript:;" data-value="" id="allClass">全部</a>
								<#if clazzList?exists && clazzList?size gt 0>
								<#list clazzList as item>
								<a href="javascript:;" data-value="${item.id!}" class="classListClass">${item.classNameDynamic!}</a>
								</#list>
								</#if>
							</div>
						</td>
						<td width="75" style="vertical-align: top;">
							<div class="outter">
								<a class="picker-more" href="javascript:;"><span>展开</span><i class="fa fa-angle-down"></i></a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="detailList"></div>
	</div>
</div>
<script>
function gobackChoice(){
	goback('${gradeId!}');
}
$(function(){

	$('.js-changeView .btn').on('click', function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('[data-id=' + $(this).attr('id') + ']').show().siblings().hide();
	});

	$("#view-table-export").on("click", function () {
		var url = "${request.contextPath}/newgkelective/" + $("#choiceId").val() + "/chosen/list/export";
		var classIdArr = new Array();
		$(".clazz-mark a.selected").each(function() {
			if ($(this).attr("data-value")) {
				classIdArr.push($(this).attr("data-value"));
			}
		});
		url += "?classIds=" + classIdArr.join(",");
		document.location.href = url;
	});

	$('.picker-more').click(function(){
		if($(this).children('span').text()=='展开'){
			$(this).children('span').text('折叠');
			$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
		}else{
			$(this).children('span').text('展开');
			$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
		};
		$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
    });

	$(".statistic-type").on("click", function () {
		if ($(this).hasClass("selected")) {
			return;
		} else {
			if ($(this).hasClass("statistic-combine")) {
				$(".classListClass").show();
			} else {
				$(".classListClass").hide();
				$(".classListClass").removeClass("selected");
				$("#allClass").addClass("selected");
			}
			$(this).siblings().removeClass("selected");
			$(this).addClass("selected");
			changeView();
		}
	});
    
    $(".clazz-mark a").on("click",function(){
    	if($(this).attr("data-value")==""){
    		if(!$(this).hasClass("selected")){
    			$(this).addClass("selected");
				$(this).siblings().removeClass("selected");
			}
		}else{
    		if($(this).hasClass("selected")){
	    		$(this).removeClass("selected");
	    		if($(".clazz-mark a.selected").length==0){
	    			$("#allClass").addClass("selected");
	    		}
	    	}else{
	    		$(this).addClass("selected");
	    		$("#allClass").removeClass("selected");
	    	}
    	}

    	changeView();
    });
	
	$("#allClass").click();
});

function changeView() {
	var choiceId = $("#choiceId").val();
	var showType = "1";
	if ($("#view-table").hasClass("active")) {
		showType = "2";
	}
	var url;
	if ($(".statistic-combine").hasClass("selected")) {
		url = "${request.contextPath}/newgkelective/" + choiceId + "/choiceResult/list/page?showType=" + showType;
	} else {
		var edge = "";
		if ($(".edge-rank").size() > 0) {
			edge += $(".edge-1").val() + ",";
			edge += $(".edge-2").val() + ",";
			edge += $(".edge-3").val();
		}
		url = "${request.contextPath}/newgkelective/" + choiceId + "/choiceResult/list/page?statisticType=2&showType=" + showType + "&edge=" + edge;
	}
	if ($(this).attr("data-value") != "") {
		var classIdArr = new Array();
		$(".clazz-mark a.selected").each(function () {
			classIdArr.push($(this).attr("data-value"));
		})
		url += "&classIds=" + classIdArr.join(",");
	}
	$("#detailList").load(url);
}

function goback(gradeId){
	var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goChoice/index/page';
	$("#showList").load(url);
}
</script>		