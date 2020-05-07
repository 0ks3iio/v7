<iframe name="${iframeId}" id="${iframeId}" frameborder="no" border="0" marginwidth="0" marginheight="0" allowtransparency="yes" 
width="${width}" height="${height}" src="about:blank" style="">
  </iframe>
  <script type="text/javascript">
  		function ${iframeId}doChangeChart(){
  			<#if documentUrl?default('') == ''>
  				layer.msg('配置不正确，请联系管理员！');
  			<#else>
				var url="${documentUrl}";
				document.getElementById("${iframeId}").src=url;
  			</#if>
		}
		${iframeId}doChangeChart();
  </script>