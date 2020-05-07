<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title><#if type?default("")=="2">教研组长报告<#else>校级报告</#if></title>
		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${request.contextPath}/static/components/font-awesome/css/font-awesome.css" />
		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
		<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
		<link rel="stylesheet" href="${request.contextPath}/static/css/page-desk.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${request.contextPath}/static/css/pages-ie.css" />
		<![endif]-->
		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
		<!--[if lte IE 8]>
			<script src="${request.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
			<script src="${request.contextPath}/static/components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body class="no-skin report-container">
		<!-- #section:basics/navbar.layout -->
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			<div class="main-content">
				<div class="main-content-inner">
					<div class="page-content">
						<div class="color-fff mt40 mb40">
							<h2>${examInfo.examName!}</h2>
							<div class="font-16">
								<span class="mr20">年级：${gradeName!}</span>
								<#if type?default("")=="2">
									<span class="mr20">科目：${subjectName!}<#if subType?default("")=="1">（选考）<#elseif subType?default("")=="2">（学考）</#if></span>
								</#if>
							</div>
						</div>
						<form id="templateform" name="templateform" method="post">
							<input type="hidden" id="examId" name='examId' value="${examInfo.id!}">
							<input type="hidden" id="unitId" name='unitId' value="${unitId!}">
							<input type="hidden" id="type"  name='type' value="${type!}">
							<input type="hidden" id="subjectId" name='subjectId' value="${subjectId!}">
							<input type="hidden" id="flag"  name="flag">
						</form>
						<div class="box box-default no-margin">
						    <div class="box-body no-padding-top">
						    	<div class="report-tit"><i class="icon-left"></i><span>学科情况总体概况</span><i class="icon-right"></i></div>
						        <table class="table table-bordered table-striped table-report text-right">
						            <thead>
						                <tr>
						                    <th class="text-center" width="44">序号</th>
						                    <th>学科</th>
						                    <th class="text-right">参考人数</th>
						                    <th class="text-right">满分值</th>
						                    <th class="text-right">难度系数</th>
						                    <th class="text-right">平均分</th>
						                    <th class="text-right">最高分</th>
						                    <th class="text-right">最低分</th>
						                    <th class="text-right">标准差</th>
						                    <th class="text-right">优秀人数</th>
						                    <th class="text-right">不及格人数</th>
						                </tr>
						            </thead>
						            <tbody>
						            	<#if totalList?exists && totalList?size gt 0>
							            	<#list totalList as item>
								                <tr>
								                    <td class="text-center">${item_index+1}</td>
												    <td class="text-left">${item.subjectName!}</td>
												    <td>${item.statNum!}</td>
												    <td>${item.fullScore?default(0.0)?string("0.#")}</td>
												    <td>${(item.avgScore/item.fullScore)?default(0.0)?string("0.0#")}</td>
												    <td>${item.avgScore?default(0.0)?string("0.#")}</td>
												    <td>${item.maxScore?default(0.0)?string("0.#")}</td>
												    <td>${item.minScore?default(0.0)?string("0.#")}</td>
												    <td>${item.normDeviation?default(0.0)?string("0.#")}</td>
												    <td>${item.goodNumber!}<span class="color-999">(${(item.goodNumber/item.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
												    <td>${item.failedNumber!}<span class="color-999">(${(item.failedNumber/item.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
								                </tr>
							                </#list>
						                </#if>
						            </tbody>
						        </table>
                        		<#if type?default("")=="2"><#assign name1="年级排名统计"><#assign name2="考试分排名"><#assign name3="赋分排名"><#assign name4="本学科班级对比分析"><#assign name5="科目">
                        		<#else><#assign name1="年级排名统计"><#assign name2="考试分排名"><#assign name3="赋分总分排名"><#assign name4="班级对比分析"><#assign name5="总分">
                        		</#if>
                                <div class="report-tit"><i class="icon-left"></i><span>年级排名统计</span><i class="icon-right"></i></div>
                                <div class="row">
                                	<div class="col-xs-6">
                                		<div class="report-subtit">考试分前10名</div>
                                		<div class="row">
                                			<div class="col-xs-6">
		                                		<table class="table table-rank">
				                                    <tbody>
				                                    	<#if oneExamScoreList?exists && oneExamScoreList?size gt 0>
				                                    	<#list oneExamScoreList as item>
					                                        <tr>
					                                            <td width="64"><span class="<#if item.gradeRank?default(0)==1>first<#elseif item.gradeRank?default(0)==2>second<#elseif item.gradeRank?default(0)==3>third<#else>num</#if>">${item.gradeRank!}</span></td>
					                                            <td>
					                                            	<div class="font-16"><b>${item.studentName!}</b></div>
					                                            	<div>${item.className!}</div>
					                                            </td>
					                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
					                                        </tr>
				                                        </#list>
				                                        </#if>
				                                    </tbody>
				                                </table>
		                                	</div>
		                                	<div class="col-xs-6">
		                                		<table class="table table-rank">
				                                    <tbody>
				                                        <#if fiveExamScoreList?exists && fiveExamScoreList?size gt 0>
				                                    	<#list fiveExamScoreList as item>
					                                        <tr>
					                                            <td width="64"><span class="<#if item.gradeRank?default(0)==1>first<#elseif item.gradeRank?default(0)==2>second<#elseif item.gradeRank?default(0)==3>third<#else>num</#if>">${item.gradeRank!}</span></td>
					                                            <td>
					                                            	<div class="font-16"><b>${item.studentName!}</b></div>
					                                            	<div>${item.className!}</div>
					                                            </td>
					                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
					                                        </tr>
				                                        </#list>
				                                        </#if>
				                                    </tbody>
				                                </table>
		                                	</div>
                                		</div>
                                	</div>
                                	<div class="col-xs-6">
                                		<div class="report-subtit">考试分倒数10名</div>
                                		<div class="row">
                                			<div class="col-xs-6">
		                                		<table class="table table-rank">
				                                    <tbody>
				                                        <#if lastOneScoreList?exists && lastOneScoreList?size gt 0>
				                                    	<#list lastOneScoreList as item>
					                                        <tr>
					                                            <td width="64"><span class="num">${item.gradeRank!}</span></td>
					                                            <td>
					                                            	<div class="font-16"><b>${item.studentName!}</b></div>
					                                            	<div>${item.className!}</div>
					                                            </td>
					                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
					                                        </tr>
				                                        </#list>
				                                        </#if>
				                                    </tbody>
				                                </table>
		                                	</div>
		                                	<div class="col-xs-6">
		                                		<table class="table table-rank">
				                                    <tbody>
				                                        <#if lastFiveScoreList?exists && lastFiveScoreList?size gt 0>
				                                    	<#list lastFiveScoreList as item>
					                                        <tr>
					                                            <td width="64"><span class="num">${item.gradeRank!}</span></td>
					                                            <td>
					                                            	<div class="font-16"><b>${item.studentName!}</b></div>
					                                            	<div>${item.className!}</div>
					                                            </td>
					                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
					                                        </tr>
				                                        </#list>
				                                        </#if>
				                                    </tbody>
				                                </table>
		                                	</div>
                                		</div>
                                	</div>
                                </div>
                                <#if gkType?default(false)>
	                                <div class="row">
	                                	<div class="col-xs-6">
	                                		<div class="report-subtit">赋分前10名</div>
	                                		<div class="row">
	                                			<div class="col-xs-6">
			                                		<table class="table table-rank">
					                                    <tbody>
					                                    	<#if oneSubScoreList?exists && oneSubScoreList?size gt 0>
					                                    	<#list oneSubScoreList as item>
						                                        <tr>
						                                            <td width="64"><span class="<#if item.gradeRank?default(0)==1>first<#elseif item.gradeRank?default(0)==2>second<#elseif item.gradeRank?default(0)==3>third<#else>num</#if>">${item.gradeRank!}</span></td>
						                                            <td>
						                                            	<div class="font-16"><b>${item.studentName!}</b></div>
						                                            	<div>${item.className!}</div>
						                                            </td>
						                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
						                                        </tr>
					                                        </#list>
					                                        </#if>
					                                    </tbody>
					                                </table>
			                                	</div>
			                                	<div class="col-xs-6">
			                                		<table class="table table-rank">
					                                    <tbody>
					                                        <#if fiveSubScoreList?exists && fiveSubScoreList?size gt 0>
					                                    	<#list fiveSubScoreList as item>
						                                        <tr>
						                                            <td width="64"><span class="<#if item.gradeRank?default(0)==1>first<#elseif item.gradeRank?default(0)==2>second<#elseif item.gradeRank?default(0)==3>third<#else>num</#if>">${item.gradeRank!}</span></td>
						                                            <td>
						                                            	<div class="font-16"><b>${item.studentName!}</b></div>
						                                            	<div>${item.className!}</div>
						                                            </td>
						                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
						                                        </tr>
					                                        </#list>
					                                        </#if>
					                                    </tbody>
					                                </table>
			                                	</div>
	                                		</div>
	                                	</div>
	                                	<div class="col-xs-6">
	                                		<div class="report-subtit">赋分倒数10名</div>
	                                		<div class="row">
	                                			<div class="col-xs-6">
			                                		<table class="table table-rank">
					                                    <tbody>
					                                        <#if lastOneSubScoreList?exists && lastOneSubScoreList?size gt 0>
					                                    	<#list lastOneSubScoreList as item>
						                                        <tr>
						                                            <td width="64"><span class="num">${item.gradeRank!}</span></td>
						                                            <td>
						                                            	<div class="font-16"><b>${item.studentName!}</b></div>
						                                            	<div>${item.className!}</div>
						                                            </td>
						                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
						                                        </tr>
					                                        </#list>
					                                        </#if>
					                                    </tbody>
					                                </table>
			                                	</div>
			                                	<div class="col-xs-6">
			                                		<table class="table table-rank">
					                                    <tbody>
					                                        <#if lastFiveSubScoreList?exists && lastFiveSubScoreList?size gt 0>
					                                    	<#list lastFiveSubScoreList as item>
						                                        <tr>
						                                            <td width="64"><span class="num">${item.gradeRank!}</span></td>
						                                            <td>
						                                            	<div class="font-16"><b>${item.studentName!}</b></div>
						                                            	<div>${item.className!}</div>
						                                            </td>
						                                            <td width="66"><span class="font-18 color-666">${item.score?default(0.0)?string("0.#")}分</span></td>
						                                        </tr>
					                                        </#list>
					                                        </#if>
					                                    </tbody>
					                                </table>
			                                	</div>
	                                		</div>
	                                	</div>
	                                </div>
                            	</#if>
                                <div class="report-tit"><i class="icon-left"></i><span>${name4!}</span><i class="icon-right"></i></div>
                                <div class="mb20">
                                	<div class="text-center" id="mySubjectList">
                                	</div>
                                	<div class="report-subtit mt30">平均分分析</div>
                                	<p class="mt20 font-16 xuankaoShow"><b>考试平均分</b></p>
                                	<div id="chart1" style="height: 300px;"></div>
                                	<p class="mt20 font-16 xuankaoShow"><b>赋分平均分</b></p>
                                	<div class="xuankaoShow" id="chart11" style="height: 300px;"></div>
                                	<div class="report-subtit">分数段分析</div>
                                	<p class="mt20 font-16"><b>班级对比</b></p>
                                	<div id="chart2" style="height: 300px;"></div>
                                	<p class="mt20 font-16"><b>年级统计</b></p>
                                	<div id="chart22" style="height: 300px;"></div>
                                	<div class="report-subtit">名次段分析</div>
                                	<p class="mt20 font-16"><b>班级对比</b></p>
                                	<div id="chart3" style="height: 300px;"></div>
                                	<p class="mt20 font-16"><b>年级统计</b></p>
                                	<div id="chart33" style="height: 300px;"></div>
                                </div>
                                <#if type?default("")!="2">
	                                <div class="report-tit"><i class="icon-left"></i><span>科目均衡度分析</span><i class="icon-right"></i></div>
	                                <p class="color-666 text-center mx130">科目均衡度分析是以标准分T为指标进行分析，对比班级平均标准分T与年级平均标准分T（年级平均标准分T为50）。标准分T高的是相对优势的学科；低的则是相对弱势的学科，需要相应班级着重提升教学质量的学科。</p>
	                                <ul class="mt20 report-layout clearfix" id="subjectCompare">
                                </#if>
                                </ul>
                                <div class="report-tit"><i class="icon-left"></i><span>${name5!}进步度分析</span><i class="icon-right"></i></div>
                                <p class="color-666 text-center mx130">${name5!}进步度是本次考试相对于上次考试得来的。计算方法是先得出该班本次考试的${name5!}平均标准分T，然后减去上次考试该班的平均标准分T，即可得出进步度。</p>
                                <div>
                                	<div class="report-subtit mt20">平均标准分T</div>
	                                <div id="chart5" style="height: 300px;"></div>
	                                <div class="report-subtit mt20">进步度排名</div>
	                                <ul class="list-rank2 clearfix" id="proRank">
                                		
                                	</ul>
	                            </div>
                            </div>
                        </div>
                        <#if isExport?default("")!="true">
							<div class="navbar-fixed-bottom opt-bottom">
			                    <a class="btn btn-blue" onclick="exprotPdf();" href="javascript:void(0);">导出为pdf</a>
			                </div>
		                </#if>
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

		</div><!-- /.main-container -->
		<!-- basic scripts -->
		<!--[if !IE]> -->
		<script src="${request.contextPath}/static/components/jquery/dist/jquery.js"></script>
		<!-- <![endif]-->

		<!--[if IE]>
		<script src="${request.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>
		<!-- page specific plugin scripts -->
		<script src="${request.contextPath}/static/components/layer/layer.js"></script>
		<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
		<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
		<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
		<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
		<script src="${request.contextPath}/static/components/typeahead.js/dist/typeahead.jquery.min.js"></script>
		<script src="${request.contextPath}/static/components/dragsort/jquery-list-dragsort.js"></script>
		<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
		<!-- inline scripts related to this page -->
		<script src="${request.contextPath}/static/js/desktop.js"></script>
		<script>
			$(function(){
				$(".xuankaoShow").hide();
				getSubjectList();
			})
			var isSubmit=false;
			function exprotPdf(){
				if(isSubmit){
					return;
				}
				isSubmit=true;
				var flag=false;
				if(confirm('是否覆盖已生成的pdf')){
					var flag=true;
				}
				$("#flag").val(flag);
				var templateform=document.getElementById('templateform');
				if(templateform){
					templateform.action="${request.contextPath}/examanalysis/emReport/exprotPdf/page";
					templateform.target="hiddenIframe";
					templateform.submit();
					isSubmit=false;
				}
			}
			//获取班级对比分析的科目
			function getSubjectList(){
				<#if type?default("")=="2">
					getChart1();
				<#else>
					var examId=$("#examId").val();
					var unitId=$("#unitId").val();
					var mySubjectList=$("#mySubjectList");
					$.ajax({
						url:"${request.contextPath}/exammanage/common/queryExamSubList",
						data:{examId:examId,unitId:unitId,fromNewGrade:'true',fromReport:'true'},
						dataType: "json",
						success: function(data){
							var infolist=data.infolist;
							mySubjectList.html("");
							if(infolist==null || infolist.length==0){
								
							}else{
								var subjectHtml='';
								for(var i = 0; i < infolist.length; i ++){
									subjectHtml='<button onclick="getClassCom(\''+infolist[i].id+'\',this)" class="btn btn-ringblue btn-radius18 mr10 test1';
									if(i==0){
										$("#subjectId").val(infolist[i].id);
										subjectHtml+=' active';
									}
									subjectHtml+=' ">'+infolist[i].name+'</button>';
									mySubjectList.append(subjectHtml);
								}
							}
							getChart1();
						}
					});
				</#if>
			}
			function getClassCom(subjectId,rr){
				$("#subjectId").val(subjectId);
				$(rr).addClass("active").siblings().removeClass("active");
				getChart1();
			}
			//平均分分析
			function getChart1(){
				var subjectId=$("#subjectId").val();
				var examId=$("#examId").val();
				var unitId=$("#unitId").val();
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/schoolClassCom",
					data:{subjectId:subjectId,examId:examId,unitId:unitId},
					dataType: "json",
					success: function(json){
						if(json.loadingData11){//赋分平均分
							$(".xuankaoShow").show();
							setChart1("chart11",json.xAxisData,json.loadingData11[0]);
						}else{
							$(".xuankaoShow").hide();
						}
						if(json.loadingData){//考试分平均分
							setChart1("chart1",json.xAxisData,json.loadingData[0]);
						}
						if(json.legendData2){//分数段-班级对比
							setChart2("chart2",json.legendData2,json.xAxisData2,json.loadingData2,json.stackData2);
						}
						if(json.legendData22){//分数段-年级统计
							setChart22("chart22",json.xAxisData22,json.loadingData22);
						}
						getChart3();
					}
				});
			}
			//获取名次段
			function getChart3(){
				var subjectId=$("#subjectId").val();
				var examId=$("#examId").val();
				var unitId=$("#unitId").val();
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/schoolClassRankCom",
					data:{subjectId:subjectId,examId:examId,unitId:unitId},
					dataType: "json",
					success: function(json){
						if(json.legendData2){//名次段-班级对比
							setChart2("chart3",json.legendData2,json.xAxisData2,json.loadingData2,json.stackData2);
						}
						if(json.loadingData2 && json.loadingData2.length>0){//名次段-年级统计
							setChart22("chart33",json.xAxisData22,json.loadingData22);
						}
						getChart4();
					}
				});
			}
			//科目均衡度分析
			function getChart4(){
				<#if type?default("")=="2">
					getChartSub5();
				<#else>
					var examId=$("#examId").val();
					var unitId=$("#unitId").val();
					var subjectCompare=$("#subjectCompare"); 
					$.ajax({
						url:"${request.contextPath}/examanalysis/emReport/schoolSubjectCom",
						data:{examId:examId,unitId:unitId},
						dataType: "json",
						success: function(json){
							var array=json.array;
							subjectCompare.html("");
							var subjectComHtml="";
							if(json.courseList &&json.courseList.length>0 && array && array.length>0){
								for(var i=0;i<array.length;i++){
									subjectComHtml='<li><div class="btn btn-ringblue btn-radius18 <#if isExport?default("")!="true">active</#if>">';
									subjectComHtml+=array[i].className;
									subjectComHtml+='</div><div class="chart" id="chart4'+i+'"></div></li>';
									subjectCompare.append(subjectComHtml);
									setChart4("chart4"+i,json.courseList,array[i].loadingData,array[i].className);
								}
							}
							getChart5();
						}
					});
				</#if>
			}
			//总分进步度分析
			function getChart5(){
				var examId=$("#examId").val();
				var unitId=$("#unitId").val();
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/schoolZeroScoreCom",
					data:{examId:examId,unitId:unitId},
					dataType: "json",
					success: function(json){
						if(json.legendData && json.xAxisData && json.loadingData && json.loadingData.length>0){
							setChart5("chart5",json.legendData,json.xAxisData,json.loadingData);
						}
						setProRank(json.statList);
					}
				});
			}
			//科目进步度分析
			function getChartSub5(){
				var examId=$("#examId").val();
				var subjectId=$("#subjectId").val();
				var unitId=$("#unitId").val();
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/schoolSubScoreCom",
					data:{examId:examId,subjectId:subjectId,unitId:unitId},
					dataType: "json",
					success: function(json){
						if(json.legendData && json.xAxisData && json.loadingData && json.loadingData.length>0){
							setChart5("chart5",json.legendData,json.xAxisData,json.loadingData);
						}
						setProRank(json.statList);
					}
				});
			}
			function setProRank(statList){
				var proRankClass=$("#proRank");
				if(statList &&statList.length>0){
					var proRankHtml='';
					for(var i=0;i<statList.length;i++){
						var rank=statList[i].progressDegreeRankGrade;
						var rankType='num';
						if(rank){
							if(rank==1){
								rankType='first';	
							}else if(rank==2){
								rankType='second';	
							}else if(rank==3){
								rankType='third';	
							}
						}
						proRankHtml='<li><div class="'+rankType+'">'+rank;
						proRankHtml+='</div><div class="float-left"><div class="font-16"><b>'+statList[i].studentName;
						proRankHtml+='</b></div><div>'+statList[i].className;
						proRankHtml+='</div></div><div class="float-right font-18 color-666 mt10">'+statList[i].progressDegree;
						proRankHtml+='</div></li>';
						proRankClass.append(proRankHtml);
					}
				}
			}
			function setChart1(chartId,xAxisData,loadingData,color){
				var chart1 = echarts.init(document.getElementById(chartId));
				option1 = {
					tooltip : {
	                    show: true
	                },
				    grid: {
						x: 35,
						x2: 0,
						y: 30
					},
				    xAxis: {
	                    data: xAxisData,
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    }
	                },
	                yAxis: {
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    },
	                    splitLine:{
	                        show:false
	                    }
	                },
					series: [
	                    {
	                        type: 'bar',
	                        barWidth : '40%',
	                        itemStyle: {
	                            normal: {
	                                color: new echarts.graphic.LinearGradient(
	                                    0, 0, 0, 1,
	                                    [
	                                        {offset: 0, color: '#56E4FC'},
	                                        {offset: 1, color: '#29A8FA'}
	                                    ]
	                                )
	                            }
	                        },
	                        label: {
				                normal: {
				                    show: true,
				                    position: 'top'
				                }
				            },
	                        data: loadingData
	                    }
	                ]
				};
				chart1.setOption(option1);
			}
			//班级对比
			function setChart2(chartId,legendData,xAxisData,loadingData,stackData){
				var chart2 = echarts.init(document.getElementById(chartId));
				var series2=[];
				for(var i=0;i<loadingData.length;i++){
					series2[i]={
						name: legendData[i],
						stack:stackData[i],
						type:'bar',
						barWidth : 30,
						data: loadingData[i]
					}
				}
				option2 = {
		            legend: {
		                data: legendData,
		                itemWidth: 8,
		                itemHeight: 8,
		                textStyle:{
		                    color: '#999',
		                    fontSize: 14
		                },
		                bottom: 0
		            },
		            tooltip : {
	                    trigger: 'axis',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
	                },
				    grid: {
						x: 35,
						x2: 0,
						y: 30
					},
					xAxis: {
	                    data: xAxisData,
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    }
	                },
		            yAxis: {
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    },
	                    splitLine:{
	                        show:false
	                    }
	                },
	            series : series2
	        };               
			chart2.setOption(option2);
		}
			function setChart22(chartId,xAxisData,loadingData){
				var chart22 = echarts.init(document.getElementById(chartId));
				option22 = {
					tooltip : {
	                    show: true
	                },
				    grid: {
						x: 35,
						x2: 0,
						y: 30
					},
				    xAxis: {
	                    data: xAxisData,
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    }
	                },
	                yAxis: {
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    },
	                    splitLine:{
	                        show:false
	                    }
	                },
					series: [
	                    {
	                    	name:'人数',
	                        type: 'bar',
	                        barWidth : '50%',
	                        itemStyle: {
	                            normal: {
	                                color: new echarts.graphic.LinearGradient(
	                                    0, 0, 0, 1,
	                                    [
	                                        {offset: 0, color: '#56E4FC'},
	                                        {offset: 1, color: '#29A8FA'}
	                                    ]
	                                )
	                            }
	                        },
	                        label: {
				                normal: {
				                    show: true,
				                    position: 'top'
				                }
				            },
	                        data: loadingData[0]
	                    }
	                ]
				};
				chart22.setOption(option22);
			}
			//均衡度
			function setChart4(chartId,courseList,loadingData,className){
				var chart4 = echarts.init(document.getElementById(chartId));
				var indicator4=[];
				for(var i=0;i<courseList.length;i++){
					indicator4[i]={
						name: courseList[i].subjectName, max: 100
					}
				}
				option4 = {
					radar: {
				        name: {
				            textStyle: {
				                color: '#333',
				                fontSize: 14
				           }
				        },
				        indicator: indicator4
				    },
					color:['#54C8FB'],
					tooltip: {},
					series: [{
				        type: 'radar',
				        itemStyle: {
			                normal: {
			                    areaStyle: {
			                        type: 'default'
			                    }
			                }
			            },
				        data: [
				            {
				                value: loadingData[0],
				                name: className,
				                areaStyle: {
			                        normal: {
			                            color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [
			                                {
			                                    color: '#54C8FB',
			                                    offset: 0
			                                },
			                                {
			                                    color: '#4A96F8',
			                                    offset: 1
			                                }
			                            ])
			                        }
			                    }
				            }
				        ]
				    }]
				};               
				chart4.setOption(option4);
			}
			function setChart5(chartId,legendData,xAxisData,loadingData){
				var chart5 = echarts.init(document.getElementById(chartId));
				var series5=[];
				for(var i=0;i<loadingData.length;i++){
					series5[i]={
						name: legendData[i],
	                    type:'line',
	                    itemStyle: {
                            normal: {
                            }
                        },
	                    data: loadingData[i]
					}
				}
				option5 = {
					legend: {
						itemWidth: 8,
		                itemHeight: 8,
		                textStyle:{
		                    color: '#999',
		                    fontSize: 14
		                },
		                bottom: 0,
		                data: legendData
		            },
					tooltip : {
	                    show: true,
	                    trigger: 'axis'
	                },
				    grid: {
						x: 35,
						x2: 0,
						y: 30
					},
				    xAxis: {
	                    data: xAxisData,
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    }
	                },
	                yAxis: {
	                    axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#E8E8E8'
	                        } 
	                    },
	                    splitLine:{
	                        show:false
	                    }
	                },
	                series : series5
				};
				chart5.setOption(option5);
			}
		</script>
	</body>
</html>
