<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title><#if type?default("")=="3">班主任报告<#else>任课老师报告</#if></title>
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
								<span class="mr20">班级：${className!}</span>
								<#if type?default("")=="4">
									<span class="mr20">科目：${subjectName!}<#if subType?default("")=="1">（选考）<#elseif subType?default("")=="2">（学考）</#if></span>
								</#if>
							</div>
						</div>
						<form id="templateform" name="templateform" method="post">
							<input type="hidden" id="examId" name='examId' value="${examInfo.id!}">
							<input type="hidden" id="classId" name='classId' value="${classId!}">
							<input type="hidden" id="unitId"  name='unitId' value="${unitId!}">
							<input type="hidden" id="subjectId" name='subjectId' value="${subjectId!}">
							<input type="hidden" id="subType"  name='subType' value="${subType!}">
							<input type="hidden" id="teachClassType" name='teachClassType' value="${teachClassType!}">
							<input type="hidden" id="type"  name='type' value="${type!}">
							<input type="hidden" id="flag"  name="flag">
						</form>
						<#if type?default("")=="3"><#assign name1="总分概况"><#assign name2="考试总分"><#assign name3="赋分总分"><#assign name4="本班总分排名情况"><#assign name5="本班学生总分进步度分布">
                		<#else><#assign name1="成绩概况"><#assign name2="考试分"><#assign name3="赋分"><#assign name4=className+"排名统计"><#assign name5="本班学生该科目进步度分布">
                		</#if>
						<div class="box box-default no-margin">
                            <div class="box-body no-padding-top">
                            	<div class="report-tit"><i class="icon-left"></i><span>${className!}${name1!}</span><i class="icon-right"></i></div>
                            	<div class="mb20">
                            		<div class="report-subtit mb20">${name2!}</div>
	                                <table class="table table-bordered table-striped table-report text-center no-margin">
	                                    <thead>
	                                        <tr>
	                                            <th rowspan="2">本班有效<br>统计人数</th>
	                                            <th colspan="3" class="text-center">年级</th>
	                                            <th colspan="3" class="text-center">本班</th>
	                                            <th rowspan="2">平均分排名</th>
	                                            <th rowspan="2">本次考试班级<br>平均标准分T</th>
	                                            <th rowspan="2">上次考试班级<br>平均标准分T</th>
	                                            <th rowspan="2">进步度</th>
	                                            <th rowspan="2">进步度<br>排名</th>
	                                            <th rowspan="2">本班优秀<br>人数(占比)</th>
	                                            <th rowspan="2">本班不及格<br>人数(占比)</th>
	                                        </tr>
	                                        <tr>
	                                        	<th>最高分</th>
	                                        	<th>最低分</th>
	                                        	<th>平均分</th>
	                                        	<th>最高分</th>
	                                        	<th>最低分</th>
	                                        	<th>平均分</th>
	                                        </tr>
	                                    </thead>
	                                    <tbody>
	                                        <tr>
	                                            <td>${zeroClassRange.statNum!}</td>
	                                            <td>${zeroGradeRange.maxScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroGradeRange.minScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroGradeRange.avgScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.maxScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.minScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.avgScore?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.rank!}</td>
	                                            <td>${zeroClassRange.avgScoreT?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.compareExamAvgScoreT?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroClassRange.progressDegree?default(0.0)?string("0.#")}</td>
	                                            <td>${zeroRank!}</td>
	                                            <td>${zeroClassRange.goodNumber!}<span class="color-999">(${(zeroClassRange.goodNumber/zeroClassRange.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
	                                            <td>${zeroClassRange.failedNumber!}<span class="color-999">(${(zeroClassRange.failedNumber/zeroClassRange.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
	                                        </tr>
	                                    </tbody>
	                                </table>
	                                <div class="color-666 font-12 mt10">注：上次考试为${comExamInfo.examName!}</div>
	                                <#if gkType?default(false)>
		                                <div class="report-subtit mt20 mb20">${name3!}</div>
		                                <table class="table table-bordered table-striped table-report text-center no-margin">
		                                    <thead>
		                                        <tr>
		                                            <th rowspan="2">本班有效<br>统计人数</th>
		                                            <th colspan="3" class="text-center">年级</th>
		                                            <th colspan="3" class="text-center">本班</th>
		                                            <th rowspan="2">平均分排名</th>
		                                            <th rowspan="2">本次考试班级<br>平均标准分T</th>
		                                            <th rowspan="2">上次考试班级<br>平均标准分T</th>
		                                            <th rowspan="2">进步度</th>
		                                            <th rowspan="2">进步度<br>排名</th>
		                                            <th rowspan="2">本班优秀<br>人数(占比)</th>
		                                            <th rowspan="2">本班不及格<br>人数(占比)</th>
		                                        </tr>
		                                        <tr>
		                                        	<th>最高分</th>
		                                        	<th>最低分</th>
		                                        	<th>平均分</th>
		                                        	<th>最高分</th>
		                                        	<th>最低分</th>
		                                        	<th>平均分</th>
		                                        </tr>
		                                    </thead>
		                                    <tbody>
		                                         <tr>
		                                            <td>${conClassRange.statNum!}</td>
		                                            <td>${conGradeRange.maxScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conGradeRange.minScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conGradeRange.avgScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.maxScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.minScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.avgScore?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.rank!}</td>
		                                            <td>${conClassRange.avgScoreT?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.compareExamAvgScoreT?default(0.0)?string("0.#")}</td>
		                                            <td>${conClassRange.progressDegree?default(0.0)?string("0.#")}</td>
		                                            <td>${conRank!}</td>
		                                            <td>${conClassRange.goodNumber!}<span class="color-999">(${(conClassRange.goodNumber/conClassRange.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
		                                            <td>${conClassRange.failedNumber!}<span class="color-999">(${(conClassRange.failedNumber/conClassRange.statNum*100)?default(0.0)?string("0.#")}%)</span></td>
		                                        </tr>
		                                    </tbody>
		                                </table>
		                                <div class="color-666 font-12 mt10">注：上次考试为${comExamInfo.examName!}</div>
	                                </#if>
                                </div>
                                <div class="report-tit"><i class="icon-left"></i><span>${name4!}</span><i class="icon-right"></i></div>
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
					                                            <td width="64"><span class="<#if item.classRank?default(0)==1>first<#elseif item.classRank?default(0)==2>second<#elseif item.classRank?default(0)==3>third<#else>num</#if>">${item.classRank!}</span></td>
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
					                                            <td width="64"><span class="<#if item.classRank?default(0)==1>first<#elseif item.classRank?default(0)==2>second<#elseif item.classRank?default(0)==3>third<#else>num</#if>">${item.classRank!}</span></td>
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
					                                            <td width="64"><span class="num">${item.classRank!}</span></td>
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
					                                            <td width="64"><span class="num">${item.classRank!}</span></td>
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
						                                            <td width="64"><span class="<#if item.classRank?default(0)==1>first<#elseif item.classRank?default(0)==2>second<#elseif item.classRank?default(0)==3>third<#else>num</#if>">${item.classRank!}</span></td>
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
						                                            <td width="64"><span class="<#if item.classRank?default(0)==1>first<#elseif item.classRank?default(0)==2>second<#elseif item.classRank?default(0)==3>third<#else>num</#if>">${item.classRank!}</span></td>
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
						                                            <td width="64"><span class="num">${item.classRank!}</span></td>
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
						                                            <td width="64"><span class="num">${item.classRank!}</span></td>
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
                            	<#if type?default("")=="3">
	                                <div class="report-tit"><i class="icon-left"></i><span>学科均衡度分析</span><i class="icon-right"></i></div>
	                                <p class="color-666 text-center mx130 mb20">科目均衡度分析是以标准分T为指标进行分析，对比我的标准分T与年级平均标准分T（年级平均标准分T为50）。标准分T高的是相对优势的学科；低的则是相对弱势的，需要着重提升的学科。</p>
	                                <ul class="report-layout clearfix">
	                                	<li style="width: 1160px;">
	                                		<div class="row">
	                                			<div class="col-xs-4">
	                                				<div class="chart" id="chart4"></div>
	                                			</div>
	                                			<div class="col-xs-4" id="subjectCompare1">
	                                			</div>
	                                			<div class="col-xs-4" id="subjectCompare2">
	                                			</div>
	                                		</div>
	                                	</li>
	                                </ul>
                                </#if>
                                <div class="report-tit"><i class="icon-left"></i><span>${name5!}</span><i class="icon-right"></i></div>
                                <p class="color-666 text-center mx130 mb20">进步度分析是以标准分T为指标进行分析，对比班级平均标准分T与年级平均标准分T（年级平均标准分T为50）。标准分T高的是相对优势的学科；低的则是相对弱势的学科，需要相应班级着重提升教学质量的学科。</p>
                                <div id="showOtherClass">
                                	
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
				<#if type?default("")=="3">
					getChart4();
				<#else>
					showOtherClass();
				</#if>
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
			function showOtherClass(){
				var examId=$("#examId").val();
				var classId=$("#classId").val();
				var unitId=$("#unitId").val();
				var url="${request.contextPath}/examanalysis/emReport/classZreoScoreCom?examId="+examId+"&classId="+classId+"&unitId="+unitId;
				var subjectId=$("#subjectId").val();
				var subType=$("#subType").val();
				if(subjectId){
					url+="&subjectId="+subjectId+"&subType="+subType;
				}
				$("#showOtherClass").load(url);
			}
			//科目均衡度分析
			function getChart4(){
				var examId=$("#examId").val();
				var classId=$("#classId").val();
				var unitId=$("#unitId").val();
				var subjectCompare1=$("#subjectCompare1"); 
				var subjectCompare2=$("#subjectCompare2"); 
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/classSubjectCom",
					data:{examId:examId,classId:classId,unitId:unitId},
					dataType: "json",
					success: function(json){
						var array=json.array;
						subjectCompare1.html("");
						subjectCompare2.html("");
						var subjectComHtml='<table class="table table-report text-right mt10"><thead><tr><th width="100">科目</th><th class="text-right">我的标准分T</th></tr></thead><tbody>';
						var subjectComHtml1=subjectComHtml;
						var subjectComHtml2=subjectComHtml;
						var oneList=json.oneList;
						var otherList=json.otherList;
						if(oneList && oneList.length>0){
							for(var i=0;i<oneList.length;i++){
							
								subjectComHtml1+='<tr><td class="text-left">'+oneList[i].subjectName+'</td>';
								subjectComHtml1+='<td>'+oneList[i].avgScoreT+'</td></tr>';
							}
						}
						subjectComHtml1+='</tbody></table>';
						if(otherList && otherList.length>0){
							for(var i=0;i<otherList.length;i++){
								subjectComHtml2+='<tr><td class="text-left">'+otherList[i].subjectName+'</td>';
								subjectComHtml2+='<td>'+otherList[i].avgScoreT+'</td></tr>';
							}
						}
						subjectComHtml2+='</tbody></table>';
						subjectCompare1.append(subjectComHtml1);
						subjectCompare2.append(subjectComHtml2);
						if(json.courseList &&json.courseList.length>0 && array && array.length>0){
							setChart4("chart4",json.courseList,array[0].loadingData);
						}
						showOtherClass();
					}
				});
			}
			//均衡度
			function setChart4(chartId,courseList,loadingData){
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
				                name: '${className!}',
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
			//总分进步度分析
			function getChart5(){
				var examId=$("#examId").val();
				var classId=$("#classId").val();
				var unitId=$("#unitId").val();
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/classZreoScoreCom",
					data:{examId:examId,classId:classId,unitId:unitId},
					dataType: "json",
					success: function(json){
						if(json.loadingData1 && json.loadingData1.length>0){
							setChart5("chart5",json.loadingData1);
							setChart5("chart6",json.loadingData2);
						}
					}
				});
			}
			function setChart5(chartId,loadingData){
				var chart5 = echarts.init(document.getElementById(chartId));
				var data5=[];
				for(var i=0;i<loadingData.length;i++){
					data5[i]=loadingData[i];
				}
				//进步度/本次考试标准分T（年级）象限图
				var img = new Image();
				img.src='${request.contextPath}/static/images/scoreAnalysis/echarts-bg.png'
				option5 = {
					color: ['#F65353','#05D2B3', '#0073F9', '#F4AE38'],
					backgroundColor: {
					    image: img,
					    repeat: 'no-repeat'
					},
					tooltip : {
	                    show: true
	                },
					grid: {
						x: 35,
						y: 30
					},
				    xAxis: {
				    	name: '标准分T-50',
				    	splitLine:{
	                        show:false
	                   },
				    	axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#666'
	                        } 
	                    }
				    },
				    yAxis: {
				    	name: '进步度',
				    	axisLabel: {
	                        textStyle: {
	                            color: '#666'
	                        }
	                    },
	                    axisLine:{
	                        show:true,
	                        lineStyle: {
	                            color: '#666'
	                        } 
	                    },
	                    splitLine:{
	                        show:false
	                    }
				    },
				    series: [{
				        symbolSize: 6,
						name:'标准分T,进步度',
				        data: [
				        	data5
				        ],
				        type: 'scatter'
				    }]
				};             
				chart5.setOption(option5);
			}
		</script>
	</body>
</html>
