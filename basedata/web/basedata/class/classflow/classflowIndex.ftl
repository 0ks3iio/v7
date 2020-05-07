<title>学生转班</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as t>
<#import "/basedata/class/detailmacro.ftl" as d>
<#import "/fw/macro/htmlcomponent.ftl" as htmlmacro>

<#-- ztree引入文件 -->

<div class="row">
	<div class="col-xs-12">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs pull-left" id="tabId">
				 	<li class="active">
				 		<a data-toggle="tab" href="#a">学生转班</a>
				 	</li>
				 	<li>
				 		<a data-toggle="tab" href="#b">操作记录</a>
				 	</li>
				 </ul>
			</div>
			<div class="tab-content">
			
				<div id="a" class="tab-pane fade in active searchFlowinDetail">
					<div class="filter">
						<@d.searchItem label="学生姓名" id="studentName" />
						<button class="btn btn-darkblue pull-left btn-search">查找</button>
						<#if isEdu?default(true)>
							<button class="btn btn-darkblue pull-right btn-batchImport">批量操作</button>
						</#if>
					</div>
					<div class="widget-box ">
						<div class="widget-body">
							<div class="widget-main searchResult">
								<p class="alert alert-info">请输入准确的学生信息，点击查询后查看结果</p>
							</div>
						</div>
					</div>
				</div>
				<!-- 历史记录查询 start-->
				<div id="b" class="tab-pane fade in searchFlowinList" style="">
						<!-- 条件筛选开始 -->
						<div class="filter">
							<@d.searchItem label="学生姓名" id="studentName" />
							<@d.searchItem label="操作时间" type="nested">
								<div class="input-daterange input-group">
								<@w.dateDivWithNoColumn nullable="false" id="startDate" displayName="" value="" inputClass="input-sm form-control"/>
									<span class="input-group-addon">
									<i class="fa fa-exchange"></i>
									</span>
								<@w.dateDivWithNoColumn nullable="false" id="endDate" displayName="" value="" inputClass="input-sm form-control"/>
								</div>
							</@d.searchItem>
							<button class="btn btn-darkblue pull-left btn-search">查找</button>
							<div class="filter-item pull-right">
								<a class="btn btn-darkblue btn-export" href="javascript:void(0);">导出操作记录</a>
							</div>
						</div>
						<div class="widget-box">
							<div class="widget-body">
								<div class="widget-main searchListResult">
									<p class="alert alert-info">请输入学生信息，点击查询后查看结果</p>
								</div>
							</div>
						</div>
			  </div>
					
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	var indexDiv = 0;
	$(window).bind("resize",function(){
		resizeLayer(indexDiv); 
	});

	// 需要用到的js脚本，延迟加载
	var scripts = [null,
	"${request.contextPath}/static/ace/components/moment/moment.js",
	"${request.contextPath}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
	"${request.contextPath}/static/ace/components/chosen/chosen.jquery.js",null
	];

	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
		initCalendar("#b");
		
		
		$("#a .btn-search").unbind("click").bind("click",function(){
			var url = "${request.contextPath}/basedata/${unitId!}/classflow/1/list/page";
			var studentName = $("#a #studentName").val();
			
			if($.trim(studentName)==''){
				layer.tips('学生姓名不能为空', '#a #studentName',{tipsMore: true,tips:3});
				return ;
			}
			
			$("#a .searchResult").load(encodeURI(url+"?studentName="+studentName));	
		});
		$("#b .btn-search").unbind("click").bind("click",function(){
			var url = "${request.contextPath}/basedata/${unitId!}/classflow/2/list/page";
			var studentName = $("#b #studentName").val();
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
				layer.tips('开始日期不能大于结束日期','#startDate',{tips:3,tipsMore:true});
				return ;
			}
			
			$("#b .searchListResult").load(url+getSearchUrl());	
		});
		
		$("#b .btn-export").unbind("click").bind("click",function(){
			
			location.href="${request.contextPath}/basedata/classflow/history/export"+getSearchUrl();
		});

		$("#a .btn-batchImport").unbind("click").bind("click",function(){
            var url = "${request.contextPath}/basedata/classflow/import/page";
            var indexDiv = layerDivUrl(url,{title:'批量操作',max:true});
		});

		function getSearchUrl(){
			var studentName = $.trim($("#b #studentName").val());
			var startDate = $("#b #startDate").val();
			var endDate = $("#b #endDate").val();
			
			var url = "?studentName="+studentName+"&startDateStr="+startDate+"&endDateStr="+endDate;
			return encodeURI(url);
		}
	});
		
	
</script>
