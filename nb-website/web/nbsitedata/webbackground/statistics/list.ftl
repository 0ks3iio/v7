<#import "/fw/macro/webmacro.ftl" as w>
<#import "/nbsitedata/webbackground/detailmacro.ftl" as d>
<div class="col-lg-12 common-list">
    <table id="simple-table" class="table  table-bordered table-hover">
        <thead>
        <tr>  
            <#if userPosting>  <#-- 详细发布信息开始 -->
             <th style="width:14%;">标题</th>
             <th style="width:14%;">所属栏目</th>
             <th style="width:14%;">提交时间</th>         
             <#if type?default('')!='11'>
             <th style="width:10%">状态</th>
             <th style="width:7%;">点击量</th>
             </#if>
            <#elseif unitDetails?default(false)>
              <th style="width:10%;">发布单位</th>
              <th style="width:20%;">发布数</th>
              <th style="width:20%;">审核通过数</th>
              <th style="width:20%;">总的点击量</th>
            </#if> <#-- 详细发布信息 结束-->       
            <#if personalDetails>
             <th style="width:6%;">发布人</th>
            <#elseif schoolDetails>
             <th style="width:6%;">发布部门</th>
            </#if>
             <#if (personalDetails||schoolDetails) &&models?exists && models?size gt 0>
              <#list models as model>
              <th style="width:8%;">${model.mcodeContent!}</th>
              </#list>
             <th style="width:6%;">发布总量</th>
             </#if>
        </tr>
        </thead>
        <tbody>

        <#if unitDetails&&listWebArticle?exists && listWebArticle?size gt 0>
           <#list listWebArticle as webartcle>
           <tr>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.unitName}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.totalRelease}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.adoptRelease}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.clicksRelease}
           </td>              
           </tr>
           </#list>
         <#elseif mapList?exists && mapList?size gt 0>            
            <#list mapList as marketMap>            
            <#if marketMap??&&marketMap?size gt 0>
             <tr>                  
             <#list marketMap?keys as key>                 
                  <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">               
                  ${marketMap[key]!}
                  </td>                                   
             </#list>
             </tr>
            </#if>
          </#list>
        <#elseif schoolDetails&&listWebArticle?exists && listWebArticle?size gt 0>
            <tr>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${listWebArticle[0].unitName}
           </td>
           <#list listWebArticle as webartcle>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.totalRelease}
           </td>             
           </#list>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${listWebArticle[0].allRelease}
           </td>
           </tr>
        <#elseif userPosting&&listWebArticle?exists && listWebArticle?size gt 0&& listWebArticle ??>
           <#list listWebArticle as webartcle>
            <tr>           
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.title!}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${mcodeSetting.getMcode('DM-WZWZLX','${webartcle.type!}')}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.commitTime!}
           </td>
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
            <#if webartcle.commitState?default('-1')=="0">
                                   未提交
            <#elseif webartcle.commitState?default('-1')=="1">
                                提交未审核
            <#elseif webartcle.commitState?default('-1')=="2">
                                 已通过
            <#elseif webartcle.commitState?default('-1')=="4">
                                未通过
            <#elseif webartcle.commitState?default('-1')=="6">
                        撤回
            </#if>
           </td>          
           <td style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                ${webartcle.clickNumber?default(0)}
           </td>                        
           </tr>
           </#list>
        <#else>
        <tr>
            <td colspan="<#if manager && commitState?default('-1')=='2'>8<#elseif (commitState?default('-1')=='1' || commitState?default('-1')=='0')&& container?default('')!='b'  >9<#elseif type?default('')=='11'>6<#else>8</#if>">
                <p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
            </td>
        <tr>
        </#if>
        </tbody>
    </table>
</div>

<#assign pageIndex2 = '${container!}' />
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
        location.href="#${request.contextPath}/sitedata/workbench/edit/page?articleId="+id+"&container="+container;
    }

    function ${container!}btnDelete(container, id,soft){
        var msg ;
        var url ;
        if(soft){
            msg = "撤回";
            url = '${request.contextPath}/sitedata/${type!}/cancel?id='+id+'&state='+$("#cancle_"+id).val();
            ${container!}doDelete(url);
        }else{
            msg = "删除";
            var obj = new Array();
            obj.push(id);
            url = '${request.contextPath}/sitedata/${type!}/delete?ids='+JSON.stringify(obj);
            layer.confirm('彻底删除无法恢复，确认删除？', {
                btn: ['确认','取消'] //按钮
            }, function(index){
                layer.close(index);
                ${container!}doDelete(url);
            }, function(index){
                layer.close(index);
            });
        }
    }

    function ${container!}doDelete(url){
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
                        var url = "${request.contextPath}/sitedata/workbench/list/page"+"?state=";
                        $("#b .listDiv").load(url+"recent"+"&container=${container}");
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
                        $("#b .listDiv").load(url+"2");
                        layer.close(index);
                    });
                }
            }
        });
    }
      <#-- 修改文章类型  2017-1-19 -->
    function doUpdateType(id,thisId){
        var type=$("#select_id").val();
   //     alert(type);
      
        var url='${request.contextPath}/sitedata/updateArticleType.html?id='+id+'&type='+type;
   //     var panduan=false;
     
        layer.confirm('确认修改数据类型?',{icon:3,title:'提示'} ,function(index){
				  layer.close(index);
				 
				  $.ajax({
					 url:url,
				     type:'post',  
				     cache:false, 
				     async: false, 
				//     contentType: "application/json",
				     success:function(data) {
				// 	 	var jsonO = JSON.parse(data);
				// 	 	alert(jsonO);
				//	  	if(!jsonO.success){
				//     		showMsgSuccess(jsonO.msg,"操作成功!",function(index){
				//	 			layer.close(index);
				//	 		});
				//          panduan=true;
				 //         alert(data);
				          
		 $(".container").load(encodeURI('${request.contextPath}/sitedata/model.html?thisId=' + thisId));
					  	}
					  	
				    
				});
				});
			
		
	<#--	if(panduan){
		alert('刷新');
		 $(".container").load(encodeURI('${request.contextPath}/sitedata/model.html?thisId=' + thisId));
	  }-->
    }


</script>


