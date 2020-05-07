<#import "/fw/macro/webmacro.ftl" as w>
<#import "/nbsitedata/webbackground/detailmacro.ftl" as d>
<div class="col-lg-12 common-list">
    <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>
		<#if commitState?default('-1')=='1' || commitState?default('')=='0' || !manager || type?default('')=='11'>
            <th class="center" style="width:6%;">
                <label class="pos-rel">
                    <input type="checkbox"  class="ace" name="nnnn"/>
                    <span class="lbl " id="check-all" chk="false"></span>
                </label>
            </th>
		</#if>
            <th class="hidden-280">标题</th>
            <th style="width:7%;">提交人</th>
            <th style="width:14%;">提交时间</th>
            <th style="width:7%;">点击量</th>
		<#if manager?default(false) && commitState?default('-1')=='2'>
            <th style="width:7%">置顶</th>
		</#if>
		<#if type?default('')!='11'>
            <th style="width:10%"><#if container?default('')=='b'>发布状态<#else>提交状态</#if></th>
            <th style="width:10%">审核状态</th>
		</#if>
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
				<#if commitState?default('-1')=='1' || commitState?default('')=='0' || !manager || type?default('')=='11'>
                    <td class="center cbx-td">
						<@w.cbx id="${dto.webArticle.id!}"/>
                    </td>
				</#if>
                <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
				${dto.webArticle.title!}
                </td>
                <td>${dto.commitUserName!}</td>
                <td class="hidden-480">${(dto.webArticle.commitTime?string("yyyy-MM-dd HH:mm:ss"))?if_exists}</td>
                <td class="hidden-480">${dto.webArticle.clickNumber?default(0)}</td>
				<#if manager && commitState?default('-1')=='2'>
                    <td>
                        <select id="top${dto.webArticle.id!}" nullable="false" data-placeholder="请选择" class="form-control col-md-12 col-sm-12 col-xs-12 topSelect" onChange="changeTop('${dto.webArticle.id!}');">
                            <option value="0" <#if dto.webArticle.isTop?default('-1')?trim=='0'>selected="true"</#if>>否</option>
                            <option value="1" <#if dto.webArticle.isTop?default('-1')?trim=='1'>selected="true"</#if>>是</option>
                        </select>
                    </td>
				</#if>
				<#if type?default('')!='11'>
                    <td class="hidden-480">
						<#if container?default("")!="b">
							<#if dto.webArticle.commitState?default('-1')=="0" || dto.webArticle.commitState?default('-1')=="5">
                                未提交
							<#else>
                                已提交
							</#if>
						<#else>
                            <#if !manager?default(fasle)>
                                <#if dto.webArticle.commitState?default('-1')?trim=='2'>已发布
                                <#elseif dto.webArticle.commitState?default('-1')?trim=='6'>
                                    撤回
                                </#if>
                            <#else >
                                <select id="cancle_${dto.webArticle.id!}" nullable="false" data-placeholder="请选择" class="form-control col-md-12 col-sm-12 col-xs-12 topSelect" onChange="${container!}btnDelete('${container?default("b")}','${dto.webArticle.id!}','soflt');">
                                    <option value="0" <#if dto.webArticle.commitState?default('-1')?trim=='2'>selected="true"</#if>>已发布</option>
                                    <option value="1" <#if dto.webArticle.commitState?default('-1')?trim=='6'>selected="true"</#if>>撤回</option>
                                </select>
                            </#if>
						</#if>
                    </td>
                    <td class="hidden-480">
						<#if dto.webArticle.commitState?default('-1')=="0">

						<#elseif dto.webArticle.commitState?default('-1')=="1">
                            待审核
						<#else>
							<#if dto.webArticle.commitState?default('-1')=="2">
                                已通过
							<#elseif dto.webArticle.commitState?default('-1')=="5">
                                退回
							<#elseif dto.webArticle.commitState?default('-1')=='6'>
                                已通过
							<#else>
                                未通过
							</#if>

						</#if>
                    </td>
				</#if>
                <th>
				<#--<@d.btnList value="" type="" class="" title="编辑"/>-->
                    <a href="javascript:;" onclick="btnEdit2('${container!}','${dto.webArticle.id!}');" othertext="11" value="${dto.webArticle.id!}" id="btn_edit_${dto.webArticle.id!}" class="green bigger-140 btn-edit btn-one-edit article-edit" title="编辑" type="edit">
                        <i class="ace-icon fa fa-pencil"></i>
                    </a>
					<#if container?default('')=='b'>
                        <a href="javascript:;" onclick="${container!}btnDelete('${container!}','${dto.webArticle.id!}');" othertext="11" value="${dto.webArticle.id!}" id="btn_edit_${dto.webArticle.id!}" class="red bigger-140 btn-delete btn-one-edit article-delete" title="删除" type="delete">
                            <i class="ace-icon fa fa-trash-o"></i>
                        </a>
					</#if>
				<#--
                -->

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
        location.href="#${request.contextPath}/sitedata/${type!}/addoredit/page?articleId="+id+"&container="+container;
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
                        var url = "${request.contextPath}/sitedata/${type!}/list/page"+"?commitState=";
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


