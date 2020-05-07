<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>我的报告</title>
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
							</div>
						</div>
						
						<form id="templateform" name="templateform" method="post">
							<input type="hidden" id="examId" name='examId' value="${examInfo.id!}">
							<input type="hidden" id="unitId" name='unitId' value="${unitId!}">
							<input type="hidden" id="studentId" name='studentId' value="${studentId!}">
							<input type="hidden" id="type"  name='type' value="${type!}">
							<input type="hidden" id="flag"  name="flag">
						</form>
						<div class="box box-default no-margin">
                            <div class="box-body no-padding-top">
                            	<div class="report-tit"><i class="icon-left"></i><span>本次考试概况</span><i class="icon-right"></i></div>
                            	 <p class="color-666 text-center mx130 mb20">进步度是本次考试相对于上次考试得来的。计算方法是先得出本次考试的平均标准分T，然后减去上次考试的平均标准分T，即可得出进步度。</p>
                                 <table class="table table-bordered table-striped table-report text-right">
                                    <thead>
                                        <tr>
                                            <th>学科</th>
                                            <th class="text-right">满分值</th>
                                            <th class="text-right">班级平均分</th>
                                            <th class="text-right">班级最高分</th>
                                            <th class="text-right">年级平均分</th>
                                            <th class="text-right">年级最高分</th>
                                            <th class="text-right">我的得分</th>
                                            <th class="text-right">进步度</th>
                                            <th class="text-right">班级排名</th>
                                            <th class="text-right">年级排名</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<#if statList?exists && statList?size gt 0>
                                    	<#list statList as item>
	                                        <tr>
	                                            <td class="text-left">${item.subjectName!}</td>
	                                            <td>${item.fullScore?default(0.0)?string("0.#")}</td>
	                                            <td>${item.classRange.avgScore?default(0.0)?string("0.#")}</td>
	                                            <td>${item.classRange.maxScore?default(0.0)?string("0.#")}</td>
	                                            <td>${item.schoolRange.avgScore?default(0.0)?string("0.#")}</td>
	                                            <td>${item.schoolRange.maxScore?default(0.0)?string("0.#")}</td>
	                                            <td>${item.score?default(0.0)?string("0.#")}<span class="color-999">(考试分)</span></td>
	                                            <td>${item.progressDegree?default(0.0)?string("0.#")}</td>
	                                            <td>${item.classRank?default(0)}
													<#if item.compareExamRank?default(0)==0>
														<span class="color-999">--</span>
                                            		<#elseif item.classRank?default(0) gt item.compareExamRank?default(0)> <#--排名当次大于上次 说明退步-->
	                                            		<span class="color-green"><i class="fa fa-long-arrow-down"></i>${item.classRank?default(0)-item.compareExamRank?default(0)}</span>
	                                            	<#elseif item.classRank?default(0) lt item.compareExamRank?default(0)>
                                            	 		<span class="color-red"><i class="fa fa-long-arrow-up"></i>${item.compareExamRank?default(0)-item.classRank?default(0)}</span>
	                                            	<#else>
	                                            		<span class="color-999">--</span>
	                                            	</#if>
	                                            </td>
	                                            <td>${item.gradeRank?default(0)}
													<#if item.compareGradeRank?default(0)==0>
														<span class="color-999">--</span>
	                                            	<#elseif item.gradeRank?default(0) gt item.compareGradeRank?default(0)> <#--排名当次大于上次 说明退步-->
	                                            		<span class="color-green"><i class="fa fa-long-arrow-down"></i>${item.gradeRank?default(0)-item.compareGradeRank?default(0)}</span>
	                                            	<#elseif item.gradeRank?default(0) lt item.compareGradeRank?default(0)>
                                            	 		<span class="color-red"><i class="fa fa-long-arrow-up"></i>${item.compareGradeRank?default(0)-item.gradeRank?default(0)}</span>
	                                            	<#else>
	                                            		<span class="color-999">--</span>
	                                            	</#if>
	                                            </td>
	                                        </tr>
                                        </#list>
                                        </#if>
                                    </tbody>
                                </table>
                                </div>
                                <div class="report-tit"><i class="icon-left"></i><span>学科均衡度分析</span><i class="icon-right"></i></div>
                                <p class="color-666 text-center mx130 mb20">学科均衡度分析是以标准分T为指标进行分析，对比我的标准分T与年级平均标准分T（年级平均标准分T为50）。标准分T高的是相对优势的学科；低的则是相对弱势的，需要着重提升的学科。</p>
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
				getChart4();
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
			//科目均衡度分析
			function getChart4(){
				var examId=$("#examId").val();
				var studentId=$("#studentId").val();
				var unitId=$("#unitId").val();
				var subjectCompare1=$("#subjectCompare1"); 
				var subjectCompare2=$("#subjectCompare2"); 
				$.ajax({
					url:"${request.contextPath}/examanalysis/emReport/stuSubjectCom",
					data:{examId:examId,studentId:studentId,unitId:unitId},
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
								subjectComHtml1+='<td>'+oneList[i].scoreT+'</td></tr>';
							}
						}
						subjectComHtml1+='</tbody></table>';
						if(otherList && otherList.length>0){
							for(var i=0;i<otherList.length;i++){
								subjectComHtml2+='<tr><td class="text-left">'+otherList[i].subjectName+'</td>';
								subjectComHtml2+='<td>'+otherList[i].scoreT+'</td></tr>';
							}
						}
						subjectComHtml2+='</tbody></table>';
						subjectCompare1.append(subjectComHtml1);
						subjectCompare2.append(subjectComHtml2);
						debugger;
						if(json.array &&json.array.length>0){
							setChart4("chart4",json.array,json.loadingData);
						}
					}
				});
			}
			//均衡度
			function setChart4(chartId,array,loadingData){
				var chart4 = echarts.init(document.getElementById(chartId));
				var indicator4=[];
				for(var i=0;i<array.length;i++){
					indicator4[i]={
						name: array[i].subjectName, max: 100
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
				                name: '学科均衡度分析',
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
		</script>
	</body>
</html>
