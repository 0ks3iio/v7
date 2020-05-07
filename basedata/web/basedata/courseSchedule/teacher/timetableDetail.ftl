<table class="table table-bordered table-view myshowTable">
    <thead>
        <tr>
            <th class="text-center" colspan="${edudays?default(7)+2}"><h3>${title!}</h3></th>
        </tr>
        <tr>
            <th class="text-center" colspan="2" width="9%">&nbsp;</th>
            <#if (edudays?default(7)>0)>
            <th class="text-center" width="13%">周一</th>
            </#if>
            <#if (edudays?default(7)>1)>
            <th class="text-center" width="13%">周二</th>
            </#if>
            <#if (edudays?default(7)>2)>
            <th class="text-center" width="13%">周三</th>
            </#if>
            <#if (edudays?default(7)>3)>
            <th class="text-center" width="13%">周四</th>
            </#if>
            <#if (edudays?default(7)>4)>
            <th class="text-center" width="13%">周五</th>
            </#if>
            <#if (edudays?default(7)>5)>
            <th class="text-center" width="13%">周六</th>
            </#if>
            <#if (edudays?default(7)>6)>
            <th class="text-center" width="13%">周日</th>
            </#if>
        </tr>
    </thead>
    <tbody>
        <#list timeIntervalMap?keys as key>
        <#if (timeIntervalMap[key]>0)>
        <#list 1..timeIntervalMap[key] as t>
        <tr>
            <#if t==1>
            <td class="text-center" rowspan="${timeIntervalMap[key]}"><#if key=='1'>早自习<#elseif key=='2'>上午<#elseif key=='3'>下午<#else>晚上</#if>
            </td>
            </#if>
            <td class="text-center order">${t_index+1}</td>
            <#list 0..(edudays?default(7)-1) as tt>
                    <#-- 每个时间点对应一个key 规则： 1_3_2 代表：周二，第三节课,上午 -->
                    <#assign courseScheduleKey='${tt}_${t}_${key!}'/>
                    <td class="<#if (courseScheduleListMap[courseScheduleKey])?? && courseScheduleListMap[courseScheduleKey]?size gt 1>double</#if>" data-value="${courseScheduleKey!}">


                    <#if (courseScheduleListMap[courseScheduleKey!])??>
                        <#assign size = courseScheduleListMap[courseScheduleKey]?size />
                        <#assign bgc = ''/>
                        <#assign bdc = ''/>
                        <#list courseScheduleListMap[courseScheduleKey!] as item1>
                            <#if item1_index==0>
                                <#if item1.bgColor?default('') != ''>
                                    <#assign bgc = item1.bgColor?default('') />
                                </#if>
                                <#if item1.borderColor?default('') != ''>
                                    <#assign bdc = item1.borderColor?default('') />
                                </#if>
                            </#if>
                        </#list>
                        <div class="infor"
                                <#if bgc?default('') !='' || bdc?default('') != ''> style="<#if bgc?default('') !=''>background:${bgc};</#if><#if bdc?default('') !=''>border-color:${bdc};</#if>"</#if>>
                        <#list courseScheduleListMap[courseScheduleKey!] as item>
                            <#if size gt 1><div></#if>
                            	<#if type?default('1')=='1'>
                                <b>${item.teacherName!}</b><br/><#if showClass>${item.className!}</#if><br/><#if showPlace>${item.placeName!}</#if>
                            	<#else>
                                <#if showClass><b>${item.className!}</b><br/>${item.subjectName!}<#else><b>${item.subjectName!}</b></#if><br/><#if showPlace>${item.placeName!}</#if>
                                </#if>
                            <#if size gt 1></div></#if>
                        </#list>
                        </div>
                    </#if>
                    <#--分割线  老版本-->
                    <#--<td class="item" data-value="${tt}_${t}_${key!}">-->
                    <#--<#if timetableCourseScheduleList?exists && (timetableCourseScheduleList?size>0)>-->
                        <#--<#assign bgc = ''/>-->
                        <#--<#assign bdc = ''/>-->
                        <#--<#list timetableCourseScheduleList as item>-->
                            <#--<#if '${item.dayOfWeek!}_${item.period!}_${item.periodInterval!}'=='${tt}_${t}_${key!}'>-->
                                <#--<#if item.bgColor?default('') != ''>-->
                                    <#--<#assign bgc = item.bgColor?default('') />-->
                                <#--</#if>-->
                                <#--<#if item.borderColor?default('') != ''>-->
                                    <#--<#assign bdc = item.borderColor?default('') />-->
                                <#--</#if>-->
                                <#--<div class="infor"-->
                                    <#--<#if bgc?default('') !='' || bdc?default('') != ''> style="<#if bgc?default('') !=''>background:${bgc};</#if><#if bdc?default('') !=''>border-color:${bdc};</#if>"</#if>>-->
                                    <#--<b>${item.className!}</b><br/>${item.subjectName!}<br/>${item.placeName!}</div>-->
                            <#--</#if>-->
                        <#--</#list>-->
                    <#--</#if>-->
                </td>
            </#list>
        </tr>
        </#list>
         <#if key_has_next>
            <th class="text-center" colspan="${edudays?default(7)+2}">&nbsp;</th>
         </#if>
        </#if>
           
        </#list>    
    </tbody>                            
</table>
<div style="display:none;">
<div class="print">
<style>
	.table-print,.table-print > tbody > tr > td,.table-print > tbody > tr > th,.table-print > thead > tr > th{ border: 2px solid #333;background-color:white;}
</style>
<table class="table table-bordered table-view table-print no-margin">
	<thead>
        <tr>
            <th class="text-center" colspan="${edudays?default(7)+2}"><h3>${title!}</h3></th>
        </tr>
        <tr>
            <th class="text-center" colspan="2" width="9%">&nbsp;</th>
            <#if (edudays?default(7)>0)>
            <th class="text-center" width="13%">周一</th>
            </#if>
            <#if (edudays?default(7)>1)>
            <th class="text-center" width="13%">周二</th>
            </#if>
            <#if (edudays?default(7)>2)>
            <th class="text-center" width="13%">周三</th>
            </#if>
            <#if (edudays?default(7)>3)>
            <th class="text-center" width="13%">周四</th>
            </#if>
            <#if (edudays?default(7)>4)>
            <th class="text-center" width="13%">周五</th>
            </#if>
            <#if (edudays?default(7)>5)>
            <th class="text-center" width="13%">周六</th>
            </#if>
            <#if (edudays?default(7)>6)>
            <th class="text-center" width="13%">周日</th>
            </#if>
        </tr>
    </thead>
    <tbody>
        <#list timeIntervalMap?keys as key>
        <#if (timeIntervalMap[key]>0)>
        <#list 1..timeIntervalMap[key] as t>
        <tr>
            <#if t==1>
            <td class="text-center" rowspan="${timeIntervalMap[key]}"><#if key=='1'>早<br/>自<br/>习<#elseif key=='2'>上午<#elseif key=='3'>下午<#else>晚上</#if>
            </td>
            </#if>
            <td class="text-center order">${t_index+1}</td>
            <#list 0..(edudays?default(7)-1) as tt>
            	<#-- 每个时间点对应一个key 规则： 1_3_2 代表：周二，第三节课,上午 -->
                    <#assign courseScheduleKey='${tt}_${t}_${key!}'/>
                    <td data-value="${courseScheduleKey!}">
                    
                    <#if (courseScheduleListMap[courseScheduleKey!])??>
                        <#assign size = courseScheduleListMap[courseScheduleKey]?size />
                        <div class="">
                        <#list courseScheduleListMap[courseScheduleKey!] as item>
                        	<#if size gt 1><div></#if>
                   			<#if type?default('1')=='1'>
                            <b>${item.teacherName!}</b><br/><#if showClass>${item.className!}</#if><br/><#if showPlace>${item.placeName!}</#if>
                        	<#else>
                                <#if showClass><b>${item.className!}</b><br/>${item.subjectName!}<#else><b>${item.subjectName!}</b></#if>
                           	<br/><#if showPlace>${item.placeName!}</#if>
                            </#if>
                            <#if size gt 1><div></#if>
                        </#list>
                        </div>
                    </#if>
                   </td>
    
           <#-- <td class="" data-value="${tt}_${t}_${key!}">-->
           <#--     <#if timetableCourseScheduleList?exists && (timetableCourseScheduleList?size>0)>-->
           <#--          <#assign bgc = ''/>-->
           <#--     	 <#assign bdc = ''/>-->
           <#--     	 <#assign ff=false>-->
           <#--          <#list timetableCourseScheduleList as item>-->
           <#--          <#if '${item.dayOfWeek!}_${item.period!}_${item.periodInterval!}'=='${tt}_${t}_${key!}'>-->
	       <#--              <#if ff>-->
	       <#--              	<br/>-->
	       <#--              <#else>-->
	       <#--              	<#assign ff=true>-->
	       <#--              </#if>-->
	       <#--              <div class="">-->
	       <#--          		<#if item.className?default('')!=''>-->
	       <#--             		<b>${item.className!}</b>-->
	       <#--             		<#if item.subjectName?default('')!=''>-->
	       <#--             		<br/>-->
	       <#--             		</#if>-->
	       <#--             	</#if>-->
	       <#--                 <#if item.subjectName?default('')!=''>-->
	       <#--             		<b>${item.subjectName!}</b>-->
	       <#--             		<#if item.placeName?default('')!=''>-->
	       <#--             		<br/>-->
	       <#--             		</#if>-->
	       <#--             	</#if>-->
	       <#--            	<#if item.placeName?default('')!=''>-->
	       <#--             		<b>${item.placeName!}</b>-->
	       <#--            	</#if>-->
	       <#--             </div>-->
           <#--          </#if>-->
           <#--          </#list>-->
           <#--     </#if>-->
           <#-- </td>-->
            </#list>
        </tr>
        </#list>
         <#if key_has_next>
            <th class="text-center" colspan="${edudays?default(7)+2}">
            	<#if key=='1'>&nbsp;
                <#elseif key=='2'>
                	<b>午休</b>
                 <#elseif key=='3'>
                 	<b>晚自习</b>
                 <#else>
                 &nbsp;
                </#if>
            </th>
         </#if>
        </#if>
           
        </#list>    
    </tbody>     
</table>
</div>
</div>
<script>
$(function(){
	$("td.item").each(function(){
		if($(this).find(".infor").length>1){
			$(this).addClass("double");
		}
	})
})
</script>