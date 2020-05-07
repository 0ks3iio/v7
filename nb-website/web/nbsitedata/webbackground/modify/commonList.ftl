<#import "/fw/macro/webmacro.ftl" as w>
<#import "/nbsitedata/webbackground/detailmacro.ftl" as d>
<div class="col-lg-12 common-list">
    <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>
            <th style="width:14%">标题</th>
            <th style="width:7%;">提交人</th>
            <th style="width:14%;">提交时间</th>    
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
				
                <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
				${dto.webArticle.title!}
                </td>
                <td>${dto.commitUserName!}</td>
                <td class="hidden-480">${(dto.webArticle.commitTime?string("yyyy-MM-dd HH:mm:ss"))?if_exists}</td>       
                <th>
                    <a href="javascript:;" onclick="btnEdit2('${container!}','${dto.webArticle.id!}');" othertext="11" value="${dto.webArticle.id!}" id="btn_edit_${dto.webArticle.id!}" class="green bigger-140 btn-edit btn-one-edit article-edit" title="编辑" type="edit">
                        <i class="ace-icon fa fa-pencil"></i>
                    </a>
				

                </th>
            </tr>
			</#list>
		<#else>
        <tr>
            <td colspan="<#if manager && commitState?default('-1')=='2'>8<#elseif commitState?default('-1')=='1' || commitState?default('-1')=='0' || !manager >8<#elseif type?default('')=='11'>6<#else>7</#if>">
                <p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
            </td>
        <tr>
		</#if>
        </tbody>
    </table>
</div>

<#assign pageIndex2 = '${page_index!}' />
<#assign container2 = '.listDiv' />
<#if container?default('') == ''>

<#else>
	<#assign container2 = '#${container!} .listDiv' />
</#if>

<@w.pagination  container="${container2!}" pagination=pagination page_index='${pageIndex2!}' />

<script>
    $('.page-content-area').ace_ajax('loadScripts', [], function() {

        $("<#if container?default('')!=''>#${container}</#if> #check-all").unbind("click").bind("click",function(){
            var checked = $("<#if container?default('')!=''>#${container}</#if> #check-all").attr("chk")=="false"?false:true;
            $(this).attr("chk",!checked);
            $("<#if container?default('')!=''>#${container}</#if> .cbx-td").find("span").each(function(){
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

        /*$(".article-edit").each(function(){
            $(this).unbind("click").bind("click",function(){

            });
        });*/

        $("<#if container?default('')!=''>#${container}</#if> .cbx-td").each(function(){
            $(this).find("span").unbind("click").bind("click",function(){
                var checked = $(this).attr("chk")=="true"?true:false;
                if(checked){
                    $(this).attr("chk","false");
                }else{
                    $(this).attr("chk","true");
                }
            });
        });

    });

    function btnEdit2(container,id) {
        location.href="#${request.contextPath}/sitedate/modify/12/addoredit/page?articleId="+id+"&container="+container;
    }

    function ${container!}btnDelete(container, id,soft){
        var msg ;
        var url ;
        if(soft){
            msg = "撤回";
            url = '${request.contextPath}/sitedata/${type!}/cancel?id='+id+'&state='+$("#cancle_"+id).val();
            ${container!}doDeleteList(url);
        }else{
            msg = "删除";
            var obj = new Array();
            obj.push(id);
            url = '${request.contextPath}/sitedata/${type!}/delete?ids='+JSON.stringify(obj);
            layer.confirm('彻底删除无法恢复，确认删除？', {
                btn: ['确认','取消'] //按钮
            }, function(index){
                layer.close(index);
                ${container!}doDeleteList(url);
            }, function(index){
                layer.close(index);
            });
        }
    }

    function ${container!}doDeleteList(url){
        $.ajax({
            url:url,
            type:'post',
            cahce:false,
            contentType:"application/json",
            success:function(data){
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                    showMsgError(jsonO.msg,"操作失败!",function(index){
                        layer.close(index);
                    });
                }else{
                    showMsgSuccess(jsonO.msg,"操作成功!",function(index){
                        var url = "${request.contextPath}/sitedate/${type!}/list/page"+"?commitState=";
                        $("#b .listDiv").load(url+"2"+"&container=${container!}");
                        layer.close(index);
                    });
                }
            }
        });
    }

    function changeTop(id){
        var isTop = $("#top"+id).val();
        $.ajax({
            url:'${request.contextPath}/sitedata/${type!}/top?isTop='+isTop+"&id="+id,
            type:'post',
            cache:false,
            contentType: "application/json",
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                    showMsgError(jsonO.msg,"操作失败!",function(index){
                        layer.close(index);
                    });
                }
                else{
                    // 需要区分移动端和非移动端返回处理不一样
                    showMsgSuccess(jsonO.msg,"操作成功!",function(index){
                        var url = "${request.contextPath}/sitedata/${type!}/list/page"+"?commitState=";
                        $("#b .listDiv").load(url+"2&container=b");
                        layer.close(index);
                    });
                }
            }
        });
    }


</script>


