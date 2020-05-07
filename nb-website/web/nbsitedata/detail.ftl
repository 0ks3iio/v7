<!--每个栏目的详情页-->
    <div class="main fn-fr">
        <!-- S 面包屑导航 -->
        <div class="breadcrumb module">
            <div class="model now_place">当前位置：<#if navigator?default('')!=''>${navigator!} > </#if>${modelName!}</div>
        </div><!-- E 面包屑导航 -->
        <div class="module module01 moreList">
            <div class="module-header">
                <h3 class="module-title">${modelName!}</h3>
            </div>
            <div class="module-body fn-clearfix artileList">

            </div>
        </div>
    </div>
    <div class="side fn-fl" id="test">
        <div class="sideNav-wrap module">
            <div class="module-header">
                <h3 class="module-title">教育实验区</h3>
            </div>
            <div class="module-body" style="max-height:710px;overflow:hidden;overflow-y:scroll;">
                <ul class="sideNav js-nav">
                <#if regions?exists && regions?size gt 0>
                    <#list regions as region>
                    <li  class="sideNav-item<#if regionCode?default('')==region.fullCode> active</#if>">
                        <a href="javascript:;" onclick="doOpen(this,'${region.fullCode!}');" regionCode="${region.fullCode!}" onclick="" >${region.regionName!}</a>
                        <ul class="subNav" <#if regionCode?default('')!=region.fullCode>style="display: none;"</#if>>
                            <#assign units=unitMap[region.fullCode]?default([]) />
                            <#if units?exists && units?size gt 0 >
                                <#list units as sch>
                                    <li class="subNav-item"><a href="javascript:;" onclick="doSelectSch('${region.fullCode!}','${sch.id!}','${sch.schoolName!}','${region.regionName!}')"><span>></span>${sch.schoolName!}</a></li>
                                </#list>
                            </#if>
                        </ul>
                    </li>
                    </#list>
                </#if>

                </ul>
            </div>
        </div>
    </div>

<script>
    jQuery(document).ready(function(){
        doSelectSch('${regionCode!}','','','${regionName!}');
    });

    <#--var modelName = '${modelName!}';
    var navigator = '${navigator!}';-->
    function doOpen(obj,regionCode){

        if($(obj).attr("open") == "open"){
            $(obj).removeAttr("open");
            $(obj).parent("li").removeClass("active");
            $(obj).next().css("display","none");
            //doSelectSch(regionCode,'','','');
        }else{
            $(obj).attr("open","open");
            $(obj).parent("li").addClass("active");
            $(obj).next().css("display","block");
            //doSelectSch('','','','');
        }
    }
    
    function doSelectSch(regionCode, schId, schName,regionName) {
        doLoadList(regionCode, schId, schName,regionName,'${modelName!}');
    }

    function doLoadList(regionCode, schId, schName,regionName,modelName){
        $('.artileList').load(encodeURI('${request.contextPath}/sitedata/webarticle/list.html?regionCode='+regionCode+'&schId='+schId+'&type=${type!}'+'&navigator=${navigator!}'));

        if('${navigator!}' && '${navigator!}'!=''){
            modelName = '${navigator!}' + ' > '+ modelName;
        }
        if(regionName && regionName != ''){
       
            $('.now_place').html('当前位置：'+modelName +' > '+regionName)
        }
        if(schName && schName != ''){
            $('.now_place').html('当前位置：'+modelName +' > '+regionName +' > '+schName)
        }
    }

    function doArticleDetail(id) {
        $(".container").load(encodeURI('${request.contextPath}/sitedata/webarticle/detail.html?id='+id+'&modelName=${modelName!}'));
    }


</script>