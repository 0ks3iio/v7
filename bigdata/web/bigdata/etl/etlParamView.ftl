<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="参数配置实例参考" showFramework=false>
<title>参数配置实例参考</title>
<div>
	<div class="error-container">
		<div class="well">
			<h3 class="lighter smaller">
				0 0/5 * * * ?&nbsp;&nbsp;&nbsp;&nbsp;每隔5分钟执行<br>
				0 0 12 * * ?&nbsp;&nbsp;&nbsp;&nbsp;每天12点执行<br>		
				0 15 10 ? * *&nbsp;&nbsp;&nbsp;&nbsp;每天的10:15执行<br>
				0 15 10 * * ? 2005&nbsp;&nbsp;&nbsp;&nbsp;2005年每天的10:15执行<br>
				0 * 14 * * ?&nbsp;&nbsp;&nbsp;&nbsp;每天的14:00到14:59期间每分钟执行<br>
				0 0/5 14 * * ?&nbsp;&nbsp;&nbsp;&nbsp;每天的14:00到14:55每隔5分钟执行<br>
				0 0/5 14,18 * * ?&nbsp;&nbsp;&nbsp;&nbsp;每天的14:00到14:55每隔5分钟执行和18:00到18:55每隔5分钟执行<br>
				0 0-5 14 * * ?&nbsp;&nbsp;&nbsp;&nbsp;每天的14:00到14:05执行<br>
				0 10,44 14 ? 3 WED&nbsp;&nbsp;&nbsp;&nbsp;三月的每一个周三的14:10和14:44执行<br>
				0 15 10 ? * MON-FRI&nbsp;&nbsp;&nbsp;&nbsp;工作日每天的10:15:00执行<br>
				0 15 10 15 * ?&nbsp;&nbsp;&nbsp;&nbsp;每个月的第15天的10:15:00执行<br>
				0 15 10 L * ?&nbsp;&nbsp;&nbsp;&nbsp;每个月最后一天的10:15:00执行<br>
				0 15 10 ? * 6L&nbsp;&nbsp;&nbsp;&nbsp;每个月最后一个周五的10:15:00执行<br>
				0 15 10 ? * 6L 2002-2005&nbsp;&nbsp;&nbsp;&nbsp;2002, 2003, 2004, 和2005年每个月最后一个周五的10:15:00执行<br>
				0 15 10 ? * 6#3&nbsp;&nbsp;&nbsp;&nbsp;每个月的第三个周五的10:15:00执行<br>
				0 0 12 1/5 * ?&nbsp;&nbsp;&nbsp;&nbsp;每个月的第一天的12:00:00开始执行，每隔5天间隔执行<br>
				0 11 11 11 11 ?&nbsp;&nbsp;&nbsp;&nbsp;每年的11月11日11:11:00执行<br>
			</h3>
		</div>
	</div>
</div>
</@webmacro.commonWeb>