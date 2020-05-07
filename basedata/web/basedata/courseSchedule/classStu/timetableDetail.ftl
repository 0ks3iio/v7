<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<style>
    .table-view .double .infor{float:left;width:50%;}
</style>
<table class="table table-bordered layout-fixed table-view no-margin mystudenttable">
    <thead>
        <tr>
            <th class="text-center" colspan="${edudays?default(7)+2}"><h3>${title!}</h3></th>
        </tr>
        <tr>
            <th class="text-center" colspan="2" width="9%">&nbsp;</th>
            <#if (edudays?default(7)>0)>
            <#assign wid=91/(edudays?default(7))>
            <#else>
            <#assign wid=10>
            </#if>
            <#if (edudays?default(7)>0)>
            <th class="text-center" width="${wid}%">周一</th>
            </#if>
            <#if (edudays?default(7)>1)>
            <th class="text-center" width="${wid}%">周二</th>
            </#if>
            <#if (edudays?default(7)>2)>
            <th class="text-center" width="${wid}%">周三</th>
            </#if>
            <#if (edudays?default(7)>3)>
            <th class="text-center" width="${wid}%">周四</th>
            </#if>
            <#if (edudays?default(7)>4)>
            <th class="text-center" width="${wid}%">周五</th>
            </#if>
            <#if (edudays?default(7)>5)>
            <th class="text-center" width="${wid}%">周六</th>
            </#if>
            <#if (edudays?default(7)>6)>
            <th class="text-center" width="${wid}%">周日</th>
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
            <td class="item" data-value="${tt}_${t}_${key!}">
                <#if timetableCourseScheduleList?exists && (timetableCourseScheduleList?size>0)>
                    <#assign bgc = ''/>
                    <#assign bdc = ''/>
                    <#list timetableCourseScheduleList as item>
                        <#if '${item.dayOfWeek!}_${item.period!}_${item.periodInterval!}'=='${tt}_${t}_${key!}'>
                            <#if item.bgColor?default('') != ''>
                                <#assign bgc = item.bgColor?default('') />
                            </#if>
                            <#if item.borderColor?default('') != ''>
                                <#assign bdc = item.borderColor?default('') />
                            </#if>
                            <div class="infor" <#if bgc?default('') !='' || bdc?default('') != ''>style="<#if bgc?default('') !=''>background:${bgc};</#if><#if bdc?default('') !=''>border-color:${bdc};</#if>"</#if>>
	                    		<#if type=='1'>
	                    		<b>${item.subjectName!}</b><br/><#if showTeacher>${item.teacherName!}</#if><br/><#if showPlace>${item.placeName!}</#if>
	                            <#else>
	                            <b><#if showTeacher>${item.subAndTeacherName!}<#else>${item.subjectName!}</#if></b><br/>${item.className!}<br/><#if showPlace>${item.placeName!}</#if>
	                            </#if>
	                        </div>
                        </#if>
                    </#list>
                    <#if bgc?default('') !='' || bdc?default('') != ''>
                        <div class="node noprint"
                             style="<#if bgc?default('') !=''>background:${bgc};</#if><#if bdc?default('') !=''>border-color:${bdc};</#if>"></div>
                    </#if>
                </#if>
            </td>
            </#list>
        </tr>
        </#list>
        </#if>
            <#if key_has_next>
                <th class="text-center" colspan="${edudays?default(7)+2}">
                <#if key=='1'>&nbsp;
                <#elseif key=='2'>
                	午休
                 <#elseif key=='3'>
                 	晚自习
                 <#else>
                 &nbsp;
                </#if>
                </th>
            </#if>
        </#list>    
    </tbody>                            
</table>
<#if type=='1' && false>
<div class="box-boder box-bluebg">
    <div class="clearfix">
        <h4 class="pull-left">说明</h4>
        <a class="pull-right fold-btn" href="javascript:void(0);">展开 <i class="fa fa-angle-down"></i></a>
    </div>
    <div class="row schedule-collapse-infor collapsed">
        <div class="col-md-8">
            <#if (bathNum>0)>
                <#list 1..bathNum as item>
                    <p>${PCKC!}${item}: ${clsNameMap[item+""]!}</p>
                </#list>
            <#else>
                <p>没有设置${PCKC!}数</p>
            </#if>
        </div>
        <div class="col-md-4">
        </div>
    </div>
</div>
</#if>
<div style="display:none;">
<div class="print">
<style>
	.table-print,.table-print > tbody > tr > td,.table-print > tbody > tr > th,.table-print > thead > tr > th{ border: 2px solid #333;background-color:white;}
</style>
<table class="table table-bordered  table-view table-print no-margin">
	<thead>
		<tr>
            <th class="text-center" colspan="${edudays?default(7)+2}"><h3>${title!}</h3></th>
        </tr>
        <tr>
           <th class="text-center" colspan="2" width="9%">&nbsp;</th>
            <#if (edudays?default(7)>0)>
            <#assign wid=91/(edudays?default(7))>
            <#else>
            <#assign wid=10>
            </#if>
            <#if (edudays?default(7)>0)>
            <th class="text-center" width="${wid}%">周一</th>
            </#if>
            <#if (edudays?default(7)>1)>
            <th class="text-center" width="${wid}%">周二</th>
            </#if>
            <#if (edudays?default(7)>2)>
            <th class="text-center" width="${wid}%">周三</th>
            </#if>
            <#if (edudays?default(7)>3)>
            <th class="text-center" width="${wid}%">周四</th>
            </#if>
            <#if (edudays?default(7)>4)>
            <th class="text-center" width="${wid}%">周五</th>
            </#if>
            <#if (edudays?default(7)>5)>
            <th class="text-center" width="${wid}%">周六</th>
            </#if>
            <#if (edudays?default(7)>6)>
            <th class="text-center" width="${wid}%">周日</th>
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
            <td class="" data-value="${tt}_${t}_${key!}">
                <#if timetableCourseScheduleList?exists && (timetableCourseScheduleList?size>0)>
                     <#assign bgc = ''/>
                	 <#assign bdc = ''/>
                	  <#assign ff=false>
                     <#list timetableCourseScheduleList as item>
                    
                     <#if '${item.dayOfWeek!}_${item.period!}_${item.periodInterval!}'=='${tt}_${t}_${key!}'>
                       <#if ff>
	                     <br/>
	                    <#else>
	                      <#assign ff=true>
	                    </#if>
                       <div class="">
                        <#if type=='1'>
                        	<#if item.subjectName?default('')!=''>
                        		<b>${item.subjectName!}</b>
                        	</#if>
                			<br/>
	                        <#if item.teacherName?default('')!=''>
                        		<b>${item.teacherName!}</b>
                        	</#if>
                			<br/>
                        	<#if item.placeName?default('')!=''>
                        		<b>${item.placeName!}</b>
                        	</#if>
						<#else>
							<#if item.subAndTeacherName?default('')!=''>
                        		<b>${item.subAndTeacherName!}</b>
                        	</#if>
                			<br/>
	                        <#if item.className?default('')!=''>
                        		<b>${item.className!}</b>
                        	</#if>
                			<br/>
                        	<#if item.placeName?default('')!=''>
                        		<b>${item.placeName!}</b>
                        	</#if>
						</#if>
						</div>
                     </#if>
                     </#list>
                     
                </#if>
            </td>
            </#list>
        </tr>
        </#list>
        </#if>
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
        </#list>    
    </tbody>            
</table>
</div>
</div>
<script type="text/javascript">
// 说明展开收起
$(".fold-btn").on('click',function(){
    var $btnHtml_down='展开 <i class="fa fa-angle-down"></i>',
        $btnHtml_up='收起 <i class="fa fa-angle-up"></i>',
        $content=$('.schedule-collapse-infor');
        
        $content.toggleClass('collapsed');
        if($content.hasClass('collapsed')){
            $(this).html($btnHtml_down)
        }else{
            $(this).html($btnHtml_up)
        }
});
$(function(){
	$("td.item").each(function(){
		if($(this).find(".infor").length>1){
			$(this).addClass("double");
		}
	})
})
</script>
