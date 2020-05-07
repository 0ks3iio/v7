<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="错误信息" showFramework=false>
<!-- ajax layout which only needs content area -->

<title>错误信息</title>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						<i class="ace-icon fa fa-random">出错啦</i>
					</span>
				</h1>

				<hr />
				<h3 class="lighter smaller">
					<#if _errorMsg?default("") == "">我们将尽快修复<i class="ace-icon fa fa-wrench icon-animated-wrench bigger-125"></i>
					<#else>${_errorMsg!}</#if>
					
				</h3>

				<div class="space"></div>
<#--
				<div>
					<h4 class="lighter smaller">同时，您可以尝试以下操作:</h4>

					<ul class="list-unstyled spaced inline bigger-110 margin-15">
						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							阅读FAQ
						</li>

						<li>
							<i class="ace-icon fa fa-hand-o-right blue"></i>
							反馈更多出现此问题的信息！
						</li>
					</ul>
				</div>
-->
				<hr />
				<div class="space"></div>

				<div class="center">
					<#if _errorOperations?exists>
					<#list _errorOperations as ops>
						<#assign json = ops?eval />
						<#if !json.url?default("")?starts_with("http")>
						<#assign url = request.contextPath + json.url />
						<#else>
						<#assign url = json.url />
						</#if>
						<#if json.divJquery?exists>
							<a href="javascript:" class="btn btn-primary" onclick="$('${json.divJquery}').load('${request.contextPath}${url!}')">
								<i class="ace-icon fa"></i>
								${json.name!}
							</a>
						<#else>
							<a href="${request.contextPath}${url!}" class="btn btn-primary">
								<i class="ace-icon fa"></i>
								${json.name!}
							</a>
						</#if>
					</#list>
					</#if>
				</div>
			</div>
		</div>

		<!-- /section:pages/error -->

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<!-- page specific plugin scripts -->
<script type="text/javascript">

</script>
</@webmacro.commonWeb>