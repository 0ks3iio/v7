<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body" id="metaHeadtableList">
<input type="hidden" id="pageIndex" value="1">
<input type="hidden" id="metaId" value="${metaId!}">
    <table class="tables">
        <thead>
        <#if cols?exists&&cols?size gt 0>
        <tr>
      		<#list cols as col>
            <th>${col.name!}</th>
            </#list>
            <th>操作</th>
        </tr>
        <#else>
        <tr>
            <th></th>
        </tr>
      	</#if>
        </thead>
        <tbody id="metaListTableBody">
        	
        </tbody>
    </table>
</div>
<div class="table-container-footer" id="queryMore">
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999" id="no-data-text" >无数据</p>
		</div>
	</div>
    <p class="text-center font-18 color-blue no-margin pointer"><b onclick="queryMore()" style="font-size: 16px;">加载更多</b></p>
</div>
<script>
    $(function () {
     	<#if cols?exists&&cols?size gt 0>
        queryMore();
        <#else>
        $("#no-data-text").text('未设置展示列');
        $('#queryMore').find('.pointer').remove();
      	</#if>
    });
    
    function showMetaOne(id,path) {
    	if(!id){
    		return;
    	}
	    router.go({
	        path: '/bigdata/property/general/meta/${metaId!}/'+id,
	        name: '详情',
	        level: 4
	    }, function () {
			var url = '${request.contextPath}/bigdata/property/general/meta/${metaId!}/'+id;
		    $('.page-content').load(url);
	    });
    }
    var isOk = false;
    function queryMore() {
    	if(isOk)return;
        var pageIndex = $('#pageIndex').val();
        var metaId = $('#metaId').val();
        if (pageIndex == null) {
            pageIndex = 1;
        }

        var url = '${request.contextPath}/bigdata/property/general/meta/list/detail';
		isOk = true;
        $.ajax({
            url: url,
            type: 'POST',
            data : {metaId : "${metaId!}",  pageIndex: pageIndex},
            dataType: 'json',
            success: function (data) {
	            if(data.success){
	            	var result = data.data;
	            	var html='';
	            	result.forEach(function(e) {
	            		html+='<tr>';
		                <#list cols as col>
			            html+='<td>'+returnEmptyStr(e["${col.columnName!}"])+'</td>'
			            </#list>
			            <#if showDetail>
			            html+='<td><a href="javascript:void(0);" onclick="showMetaOne(\''+e["${mainkey!}"]+'\')"><span class="color-blue">详情</span></a></td>'
			            <#else>html+='<td></td>';
			            </#if>
		            	html+='</tr>';
					});
					if(result.length<50){
						$('#queryMore').find('.pointer').remove();
					}
					if(pageIndex=='1'&&result.length>0){
		                $('#queryMore').find('.no-data-common').remove();
					}
	            	$("#metaListTableBody").append(html);
					$('#pageIndex').val(parseInt(pageIndex)+1)
					
	            }else{
	                $('#queryMore').find('.pointer').remove();
	            }
	            isOk = false;
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){$('#queryMore').find('.pointer').remove();isOk = false;}
        });
    }
    function returnEmptyStr(str) {
		if(str){
			return str;
		}else{
			return '';
		}
	}
</script>