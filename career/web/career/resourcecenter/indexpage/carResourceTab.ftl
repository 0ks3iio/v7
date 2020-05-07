<div class="page-content" id="showResourceTabDiv">
	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
					<div class="resourceType clearfix">
						<a class="type1" href="https://gaokao.chsi.com.cn/sch/search--ss-on,option-qg,searchType-1,start-0.dhtml" target="_blank"><img src="${request.contextPath}/static/images/plan/plan1.png"><br>院校信息</a>
						<a class="type2" href="https://gaokao.chsi.com.cn/zyk/zybk/" target="_blank"><img src="${request.contextPath}/static/images/plan/plan2.png"><br>专业信息</a>
						<a class="type3" href="https://gaokao.chsi.com.cn/gkzt/zyjdhz" target="_blank"><img src="${request.contextPath}/static/images/plan/plan3.png"><br>专业解读</a>
						<a class="type4" href="https://gaokao.chsi.com.cn/zsgs/zhangcheng/listVerifedZszc--method-index,lb-1.dhtml" target="_blank"><img src="${request.contextPath}/static/images/plan/plan4.png"><br>招生简章信息</a>
						<a class="type5" href="https://gaokao.chsi.com.cn/z/gaozhi/" target="_blank"><img src="${request.contextPath}/static/images/plan/plan5.png"><br>高职信息</a>
						<a class="type6" href="https://gaokao.chsi.com.cn/gkzt/jxzs" target="_blank"><img src="${request.contextPath}/static/images/plan/plan6.png"><br>军校信息</a>
						<a class="type7" href="http://career.eol.cn/html/sy/zhiye/" target="_blank"><img src="${request.contextPath}/static/images/plan/plan7.png"><br>职业信息</a>
					</div>
                    <div class="tab-container">
						<div class="tab-header clearfix">
							<ul class="nav nav-tabs nav-tabs-1">
							 	<li class="active" onClick="showResourceList(1)">
							 		<a data-toggle="tab" href="#">志愿填报指导</a>
							 	</li>
							 	<li class="" onClick="showResourceList(2)">
							 		<a data-toggle="tab" href="#">选科指导</a>
							 	</li>
							 	<li class="" onClick="showResourceList(3)">
							 		<a data-toggle="tab" href="#">自主招生指导</a>
							 	</li>
							</ul>
						</div>
						<div class="tab-content" id="showResourceListDiv">
							
						</div>
				    </div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="showResourceDiv" style="display:none">
	
</div>
<script>
	$(function(){
		showResourceList(1);
	});
	
	function showResourceList(resourceType) {
		var url = "${request.contextPath}/career/resourcecenter/indexpage/resourcelist?resourceType="+resourceType;
		$("#showResourceListDiv").load(url);
	}
</script>