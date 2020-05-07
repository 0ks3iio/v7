<#import "/fw/macro/webmacro.ftl" as webmacro>
<@webmacro.commonWeb title="提示信息" showFramework=false>
<!-- ajax layout which only needs content area -->
<title>提示信息</title>
<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						温馨提示
					</span>
				</h1>

				<hr />
				<h3 class="lighter smaller">
					${_promptMsg!}
				</h3>
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