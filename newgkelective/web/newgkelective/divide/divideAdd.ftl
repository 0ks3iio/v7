<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<#--<a href="javascript:" class="page-back-btn gotoDivideClass"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">新增分班方案</h3>
	</div>
	<div class="box-body">
		<div class="explain text-center">
			<span class="fa fa-exclamation-circle color-yellow"></span>
			开班方案对排班结果有极高的参考价值，请认真设置
		</div>
	<form id="divideForm">
		<input type="hidden" name="ent.id" value="${newGkDivide.id!}">
		<input type="hidden" name="ent.gradeId" value="${newGkDivide.gradeId!}">
		<ul class="setting-list">
			<li>
				<h4>1、请选择选课情况 <a href="javascript:" onclick="toSetItem('1')">去设置</a></h4>
				<ul>
					<#if newGkChoiceDtoList?exists && newGkChoiceDtoList?size gt 0>
					<#list newGkChoiceDtoList as item>
						<li>
							<label><input type="radio" class="wp" name="choiceId" value="${item.choiceId!}" <#if chooseChiceId?default('')==item.choiceId>checked</#if> ><span class="lbl"> ${item.choiceName!}</span></label>
							<div class="box box-primary chart choiceChart_${item.choiceId!}">
								<div class="box-header">
									<h3 class="box-title">${item.choiceName!}</h3>
								</div>
								<div class="box-body">
									<div class="row">
										<div class="col-sm-4">
											<div class="chart-result-pie" style="height:160px;"></div>
										</div>
										<div class="col-sm-8">
											<div class="chart-result-bar" id='chart${item_index}' style="height:160px;"></div>
										</div>
									</div>
								</div>
							</div>
						</li>
					</#list>
					<#else>
						暂无可用数据
					</#if>
				</ul>
			</li>
			<li>
				<h4>2、请选择参考成绩<a href="javascript:" onclick="toSetItem('2')">去设置</a></h4>
				<ul>
				<#if referScoreList?exists && referScoreList?size gt 0>
				<#list referScoreList as score>
					<li>
						<label><input type="radio" class="wp" name="referScoreId" value="${score.id!}"><span class="lbl"> ${score.name!}</span></label>
						<div class="box box-primary hidden">
							<div class="box-header">
								<h3 class="box-title">${score.name!}</h3>
							</div>
							<div class="box-body">
								<div class="number-container">
									<a href="javascript:void(0);">
										<ul class="number-list">
											<#if score.dataList?exists && score.dataList?size gt 0>
											<#list score.dataList as data>
											<li><em>${data[1]?default('')}</em><span>${data[0]?default('')}</span></li>
											</#list>
											</#if>
										</ul>
									</a>
									<p class="tip tip-grey text-right">创建时间：${score.creationTime?string('yyyy-MM-dd HH:mm:ss')}</p>
								</div>
							</div>
						</div>
					</li>
					</#list>
					<#else>
					暂无可用数据
					</#if>
				</ul>
			</li>
			<li>
				<h4>3、请选择开班科目 &nbsp;(需选择选课情况)</h4>
				<ul>
					<li>
						<div class="filter">
							<div class="filter-item">
								<span class="filter-name">选考：</span>
								<div class="filter-content">
									<input type="hidden" value="" name="courseA" id="courseA">
									<div class="publish-course" id="course-A">
										
									</div>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="filter">
							<div class="filter-item">
								<span class="filter-name">学考：</span>
								<div class="filter-content">
									<input type="hidden" value="" name="courseB" id="courseB">
									<div class="publish-course" id="course-B">
										
									</div>
								</div>
							</div>
						</div>
					</li>
				</ul>
			</li>
			<li>
				<h4>4、请选择跟着行政班一起上课的科目</h4>
				<ul>
					<li>
						<div class="filter">
							<div class="filter-item">
								<div class="filter-content">
									<input type="hidden" value="" name="courseO" id="courseO">
									<div class="publish-course" id="course-O">
									<#if xzbCourseList?exists && xzbCourseList?size gt 0>
									<#list xzbCourseList as itemCourse>
										<span class='active' id="${itemCourse.id!}_course-O" data-value='${itemCourse.id!}' title='${itemCourse.subjectName!}'>
											<#if ((itemCourse.subjectName?default(''))?length gt 4)>
												${itemCourse.subjectName?substring(0,4)}…
											<#else>
												${itemCourse.subjectName!}
											</#if></span>
									</#list>
									</#if>
									</div>
								</div>
							</div>
						</div>
					</li>
				</ul>
			</li>
			
			
			<li>
				<h4>5、每班可容纳学生数</h4>
				<ul>
					<li>
						每班可容纳
						<input type="text" class="form-control inline-block number" nullable="false" vtype="int" name="ent.galleryful" id="galleryful" maxLength="4">
						人，
						最多可再容纳
						<input type="text" class="form-control inline-block number" nullable="false" vtype="int" name="ent.maxGalleryful" id="maxGalleryful" maxLength="2">
						人
					</li>
				</ul>
			</li>
			<li>
				<h4>6、原行政班是否重组</h4>
				<ul>
					<li>
						<label><input type="radio" class="wp" name="openType" value="01"><span class="lbl"> 不重组</span></label>
					</li>
					<li>
						<label><input type="radio" class="wp" name="openType" value="02"><span class="lbl"> 重组</span></label>
					</li>
				</ul>
			</li>
		</ul>
	</form>
		<p><a href="javascript:" class="btn btn-long btn-blue" id="divide-commit" onclick="saveDivide()">确定</a></p>
	</div>
</div>
<script>
$(function(){
	showBreadBack(toMyBack,false,"新增分班");
	initCourseSelect();
	$('input[type=radio]').on('click', function(){

		var option = $(this).attr('name');
		var optionValue = $(this).attr('value');
		$('[name=' + option + ']').parent().next().addClass('hidden');
		if($(this).prop('checked') === true){
			var b = $(this).parent().next('.box');

			if(typeof b !== 'object') return;
			b.removeClass('hidden');
		}
		if("choiceId"==option){
			findSubjectByChoiceId(optionValue);
		}
	});
	<#--
		$(".gotoDivideClass").on("click",function(){
			toMyBack();
		});
	-->
	
})
;(function(){
<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
	var data = [
	<#list newGkChoiceDtoList as item>
		<#if item_index == 0>
			{
	       		chart: 'chart' + '${item_index}',
	       		content: [
					{value: ${item.selectNum}, name: '已选'},
					{value: ${item.noSelectNum}, name: '未选'}
				]
	       	}
	    <#else>
	    	,{
	       		chart: 'chart' + '${item_index}',
	       		content: [
					{value: ${item.selectNum}, name: '已选'},
					{value: ${item.noSelectNum}, name: '未选'}
				]
	       	}
	    </#if>
    </#list>
    ];
</#if>
var chart_pie = document.querySelectorAll('.chart-result-pie');	
for( var i=0; i< chart_pie.length; i++){
	(function(){
		var c = echarts.init( chart_pie[i] );
		c.setOption({
			color:['#317eeb', '#ccc'],
		    tooltip : {
		        trigger: 'item',
		        formatter: "{b} : {c}人"
		    },
		    series : [
		        {
	         		name:'选项',
			   		type:'pie',
			        data: data[i].content
			    }
			]
	   })      
	})()
}
<#if newGkChoiceDtoList?exists && (newGkChoiceDtoList?size>0)>
	<#list newGkChoiceDtoList as item>
		var chart = document.getElementById('chart'+'${item_index}');
	    var jsonStringData1=jQuery.parseJSON('${item.jsonObject}');
	    var legendData1=jsonStringData1.legendData;
	    var loadingData1=jsonStringData1.loadingData;
	    var c02 = echarts.init( chart );
		c02.setOption({
		color: ['#60b1cc'],
	    tooltip : {
	        trigger: 'item',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    grid: {
	        left: '2%',
	        right: '2%',
	        bottom: '2%',
	        top: '10%',
	        containLabel: true,
	        borderColor: '#e8e8e8'
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : legendData1,
	            axisTick: {
	                alignWithLabel: true
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'直接访问',
	            type:'bar',
	            barWidth: '60%',
	            tooltip:{
	            	formatter: '{b} {c}人'
	            },
	            data:loadingData1,
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'top'
	                }
	            }
	        }
	    ]
    });
	</#list>
</#if>
var items = $('.chart');
	items.each(function(i){
		$(this).addClass('hidden');
	});
//初始化
<#if chooseChiceId?default('')!=''>
$('.choiceChart_${chooseChiceId!}').removeClass('hidden');

findSubjectByChoiceId('${chooseChiceId!}');	
</#if>	
})();
	function toSetItem(type){
		if("1"==type){
			var url =  '${request.contextPath}/newgkelective/${newGkDivide.gradeId!}/goChoice/index/page';
			$("#showList").load(url);
		}else if("2"==type){
		    var choiceId = $("input[type='radio']:checked").val();
		    if(choiceId == undefined){
		        layerTipMsgWarn("提示","请至少选择一个选课！");
                return;
		    }
			var url =  '${request.contextPath}/newgkelective/newGkScoreResult/newGkReferScoreList?gradeId=${newGkDivide.gradeId!}&urlType=2&chioceId='+choiceId;
			$("#showList").load(url);
		}
	}
	

	function findSubjectByChoiceId(choiceId){
		$.ajax({
			url:"${request.contextPath}/newgkelective/goDivide/chosenSubjectByChoiceId",
			data:{"choiceId":choiceId},
			dataType: "json",
			success: function(data){
				$("#course-A").html("");
				$("#course-B").html("");
				var htmlA="";
				var htmlB="";
				if(data.length>0){
					for(var i = 0; i < data.length; i ++){
						htmlA=htmlA+"<span class='active' data-value='"+data[i].id+"'>"+data[i].subjectName+"</span>";
						htmlB=htmlB+"<span class='active' data-value='"+data[i].id+"'>"+data[i].subjectName+"</span>";
					}
				}
				$("#course-A").html(htmlA);
				$("#course-B").html(htmlB);
				clearChooseXZBSubject();
				initCourseSelect();
				clearChooseSubject();
			}
		});
	}
	
	function clearChooseXZBSubject(){
		$("#course-O span").each(function(){
			if($(this).hasClass("disabled")){
				$(this).removeClass("disabled");
			}
		});
	}
	
	function initCourseSelect(){
		$('.publish-course span').off('click').on('click', function(e){
			e.preventDefault();
			if($(this).hasClass('disabled')) return;
	
			if($(this).hasClass('active')){
				$(this).removeClass('active');
			}else{
				$(this).addClass('active');
			}
			clearChooseSubject();
		});
	}
	function checkMyForm(){
		var choiceId=$("input[name='choiceId']:checked").val();
		if(!choiceId){
			layerTipMsg(false,"保存失败","请选择选课情况");
			return false;
		}
//		var referScoreId=$("input[name='referScoreId']:checked").val();
//		if(!referScoreId){
//			layerTipMsg(false,"保存失败","请选择参考成绩");
//			return false;
//		}
		var subjectIdA="";
		$("#course-A span").each(function(){
			if($(this).hasClass("active")){
				var subId=$(this).attr("data-value");
				subjectIdA=subjectIdA+","+subId;
			}
		})
		if(subjectIdA!=""){
			subjectIdA=subjectIdA.substring(1);
		}
		$("#courseA").val(subjectIdA);
		
		var subjectIdB="";
		$("#course-B span").each(function(){
			if($(this).hasClass("active")){
				var subId=$(this).attr("data-value");
				subjectIdB=subjectIdB+","+subId;
			}
		})
		if(subjectIdB!=""){
			subjectIdB=subjectIdB.substring(1);
		}
		$("#courseB").val(subjectIdB);
		if(subjectIdB=="" &&　subjectIdA==""){
			layerTipMsg(false,"保存失败","请选择开班科目");
			return false;
		}
		var subjectIdO="";
		$("#course-O span").each(function(){
			if($(this).hasClass("active") && (!$(this).hasClass("disabled"))){
				var subId=$(this).attr("data-value");
				subjectIdO=subjectIdO+","+subId;
			}
		})
		if(subjectIdO!=""){
			subjectIdO=subjectIdO.substring(1);
		}
		$("#courseO").val(subjectIdO);
		var openType=$("input[name='openType']:checked").val();
		if(!openType){
			layerTipMsg(false,"保存失败","请选择原行政班是否重组");
			return false;
		}
		var check = checkValue('#divideForm');
	    if(!check){
	        return false;
	    }
	    var ff=$("#galleryful").val();
	    if(parseInt(ff)<=0){
	    	layer.tips('请输入正整数', $("#galleryful"), {
				tipsMore: true,
				tips:3				
			});
			return false;
	    }
	    var gg=$("#maxGalleryful").val();
	    if(parseInt(gg)<0){
	    	layer.tips('请输入非负整数', $("#maxGalleryful"), {
				tipsMore: true,
				tips:3				
			});
			return false;
	    }
	    
		return true;
	}
	var isSubmit=false;
	function saveDivide(){
		var check = checkMyForm();
	    if(!check){
	        $("#divide-commit").removeClass("disabled");
	        isSubmit=false;
	        return;
	    }
	    isSubmit=true;
		var options = {
			url : "${request.contextPath}/newgkelective/goDivide/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#divide-commit").removeClass("disabled");
		 			return;
		 		}else{
		 			layer.closeAll();
		 			if("success"==jsonO.msg){
		 				layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
		 			}else{
		 				toDivideItem(jsonO.msg);
		 			}
					
				  	
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#divideForm").ajaxSubmit(options);
	}
	
	function toDivideItem(divideId){
		var url =  '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList?type=1';
		$("#showList").load(url);
	}
	function toMyBack(){
		var url =  '${request.contextPath}/newgkelective/${newGkDivide.gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}
	
	function clearChooseSubject(){
		var nochooseA={};
		$("#course-A span").each(function(){
			var subId=$(this).attr("data-value");
			if($(this).hasClass("active")){
				if(!$("#"+subId+"_course-O").hasClass('disabled')){
					if($("#"+subId+"_course-O").hasClass('active')){
						$("#"+subId+"_course-O").removeClass("active");
					}
					
					$("#"+subId+"_course-O").addClass('disabled');
				}
			}else{
				nochooseA[subId]=subId;
			}
		})
		$("#course-B span").each(function(){
			var subId=$(this).attr("data-value");
			if($(this).hasClass("active")){
				if(!$("#"+subId+"_course-O").hasClass('disabled')){
					if($("#"+subId+"_course-O").hasClass('active')){
						$("#"+subId+"_course-O").removeClass("active");
					}
					
					$("#"+subId+"_course-O").addClass('disabled');
				}
			}else{
				if(nochooseA[subId]){
					//AB都没有选中
					if($("#"+subId+"_course-O").hasClass('disabled')){
						$("#"+subId+"_course-O").removeClass('disabled');
					}
				}
			}
		})
	}
</script>