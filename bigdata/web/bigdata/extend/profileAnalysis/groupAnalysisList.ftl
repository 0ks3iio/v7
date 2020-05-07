<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body" id="tableList">
    <table class="tables">
        <thead>
       <#if cols?exists&&cols?size gt 0>
       <#assign per =100/(cols?size) >
        <tr>
      		<#list cols as col>
            <th width="${per!}%">${col.name!}</th>
            </#list>
        </tr>
        <#else>
        <tr>
            <th></th>
        </tr>
      	</#if>
        </thead>
        <tbody id="userTableBody">
        </tbody>
    </table>
</div>
<div class="table-container-footer" id="queryMore">
<#if cols?exists&&cols?size gt 0>
    <p class="text-center font-18 color-blue no-margin pointer"><b onclick="queryMore()" style="font-size: 16px;">加载更多</b></p>
<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999" id="no-data-text" >无展示列</p>
		</div>
	</div>
</#if>
</div>
<script>
    $(function () {
        queryMore();
        <#if cols?exists&&cols?size gt 0>
        $('.downloadList').show();
        <#else>
        $('.downloadList').hide();
        </#if>
    });
    function queryMore() {
        var pageIndex = $('#pageIndex').val();
        if (pageIndex == null) {
            pageIndex = "1";
        }
        var profileCode = $('#profileCode').val();
        var tagArray = [];
        $('.tagSelect.selected').each(function (i, v) {
            tagArray.push($(v).attr('tagId'));
        });

        var url = '${request.contextPath}/bigdata/groupAnalysis/list';

        $.ajax({
            url: url,
            type: 'POST',
            data : {profileCode : profileCode, tagArray:JSON.stringify(tagArray), pageIndex: pageIndex},
            dataType: 'html',
            success: function (val) {
                //生成报表
                if (val.match("^\{(.+:.+,*){1,}\}$")) {
                    var rdata = jQuery.parseJSON(val);
                    val = rdata.msg;
                    layer.msg(val, {icon: 2});
                }

                $('#pageIndex').remove();
                $('#hasMore').remove();
                $('#count').remove();
                $('#userTableBody').append(val);
                var count = parseInt($('#count').val());
                if (count < 50) {
                    $('#queryMore').remove();
                }
                
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){$('#queryMore').html('<div class="no-data-common"><div class="text-center"><img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/><p class="color-999">无数据</p></div></div>');}
        });
    }
</script>