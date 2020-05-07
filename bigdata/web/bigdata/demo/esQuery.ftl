<div class="table-container no-margin">
<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="clearfix margin-b-20">
                <div class=" no-padding-right radius-5">
                    <div class="box-default no-padding-right radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">数据图表-1</p>
                        </div>
                        <div class="box-step radius-bottom-5" style="height:450px;">
                            <div class="chart" id="chart1"></div>
                        </div>
                    </div>
                     <div class="box-default no-padding-right radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">数据图表-2</p>
                        </div>
                        <div class="box-step radius-bottom-5" style="height:450px;">
                            <div class="chart" id="chart2"></div>
                        </div>
                    </div>
                     <div class="box-default no-padding-right radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">数据图表-3</p>
                        </div>
                        <div class="box-step radius-bottom-5" style="height:800px;">
                            <div class="chart" id="chart3"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="table-container-footer">
	<p class="text-center font-18 color-blue no-margin pointer"><b>分隔符</b></p>
</div>
<div class="table-container-body ">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th >单位</th>
				<th >姓名</th>
				<th >类型</th>
				<th >性别</th>
				<th >年龄</th>
				<th >登录省份</th>
				<th >省份</th>
				<th >城市</th>
				<th >系统</th>
				<th >版本</th>
				<th >浏览器</th>
				<th >版本</th>
				<th >时间</th>
				<th >小时</th>
				<th >时段</th>
			</tr>
		</thead>
		<tbody>
			<#if esList?exists&&esList?size gt 0>
	          	<#list esList as es>
					<tr>
						<td>${es.unit_name!}</td>
						<td>${es.user_name!}</td>
						<td>${es.user_type!}</td>
						<td>${es.sex!}</td>
						<td>${es.age!}</td>
						<td>${es.login_province!}</td>
						<td>${es.province!}</td>
						<td>${es.city!}</td>
						<td>${es.os_name!}</td>
						<td>${es.os_version!}</td>
						<td>${es.browser_name!}</td>
						<td>${es.browser_version!}</td>
						<td>${es.operation_date!}</td>
						<td>${es.operation_hour!}</td>
						<td>${es.operation_section!}</td>
					</tr>
	      	    </#list>
	  	    <#else>
				<tr >
					<td  colspan="15" align="center">
					暂无数据
					</td>
				<tr>
	        </#if>
		</tbody>
	</table>
</div>
</div>
<script type="text/javascript">
    $(function(){
        initChart1();
        initChart2();
        initChart3();
    });

    function initChart1() {
    	$.ajax({
            url: '${request.contextPath}/bigdata/frame/common/demo/chart1',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                }
                else {
                        var echart_div = echarts.init(document.getElementById('chart1'));
				        var data = JSON.parse(response.data);
				        echart_div.setOption(data);
                }
            }
        });
    }
    
    function initChart2() {
    	$.ajax({
            url: '${request.contextPath}/bigdata/frame/common/demo/chart2',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                }
                else {
                        var echart_div = echarts.init(document.getElementById('chart2'));
				        var data = JSON.parse(response.data);
				        echart_div.setOption(data);
                }
            }
        });
    }
    
    function initChart3() {
            $.get('${request.contextPath}/static/bigdata/js/echarts/map/json/province/00.json', function (geoJson) {
                echarts.registerMap("china", geoJson);
                $.ajax({
                    url: '${request.contextPath}/bigdata/frame/common/demo/chart3',
                    type: 'POST',
                    dataType: 'json',
                    success: function (response) {
                        if (!response.success) {
                            layer.msg(response.message, {icon: 2});
                        }
                        else {
                            var echart_div = echarts.init(document.getElementById('chart3'));
                            var data = JSON.parse(response.data);
                            echart_div.setOption(data);
                        }
                    }
                });
                if (typeof call === 'function') {
                    call();
                }
            });
    }
</script>