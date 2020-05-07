<#--友情链接列表页-->
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/nbsitedata/webbackground/detailmacro.ftl" as d>
<div class="col-lg-12 common-list">
    <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>
            <th class="center" style="width:6%;">
                <label class="pos-rel">
                    <input type="checkbox"  class="ace" name="nnnn"/>
                    <span class="lbl " id="check-all" chk="false"></span>
                </label>
            </th>
            <th style="width:10%;word-wrap: break-word;">地区名称</th>
            <th style="width:30%;word-wrap: break-word;">操作人</th>
            <th style="width:30%;word-wrap: break-word;">图片名称</th>
            <th style="width:10%">
                <i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
                操作
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
        <#if webArticles?exists && webArticles?size gt 0>
            <#list webArticles as dto>
            <tr>
                <td class="center cbx-td">
                    <@w.cbx id="${dto.webArticle.id!}"/>
                </td>
                <td>
                ${dto.webArticle.title!}
                </td>
                <td>${dto.commitUserName!}</td>
                <td>${dto.webArticle.titleImageName!}</td>
                <th>
                    <@d.btnList value="${dto.webArticle.id!}" type="edit" class="btn-one-edit article-edit" title="编辑"/>
                </th>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="5">
                <p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
            </td>
        <tr>
        </#if>
        </tbody>
    </table>
</div>

<@w.pagination  container=".listDiv" pagination=pagination page_index='1' />

<script>
    $('.page-content-area').ace_ajax('loadScripts', [], function() {
        $('.article-edit').on("click",function(){
            var url = "${request.contextPath}/sitedata/${type!}/addoredit/page?articleId="+$(this).attr("value");
            var indexDiv = layerDivUrl(url,{height:270,width:600,title:'编辑'});
            resizeLayer(indexDiv, 270, 600);
        });
        $("#check-all").unbind().bind("click",function () {
            var checked = $("#check-all").attr("chk")=="false"?false:true;
            $(this).attr("chk",!checked);
            $(".cbx-td").find("span").each(function(){
                if(!checked==true && $(this).attr("chk")=="true"){
                    return ;
                }
                if(!checked==false && $(this).attr("chk")=="false"){
                    return ;
                }
                $(this).click();
                $(this).attr("chk",!checked);
            });
        });
    });
</script>