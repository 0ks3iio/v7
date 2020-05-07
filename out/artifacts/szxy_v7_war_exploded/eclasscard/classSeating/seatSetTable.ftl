<#if showSeat>

	<#assign colWidth=98/(seatSet.colNumber+2)/>
	<div class="row bgc-f4f4f4">
	    <div class="col-xs-1 text-center" style="width: 1%"></div>
	    <#list 1..seatSet.colNumber as i>
	        <div class="col-xs-1 text-center" style="width: ${colWidth}%">${i}</div>
	        <#if seatSet.space1==i || seatSet.space2==i>
	            <div class="col-xs-1 text-center" style="width: ${colWidth}%"></div>
	        </#if>
	    </#list>
	</div>
	<ul class="numlist bgc-f4f4f4" style="width:20px;height: ${seatSet.rowNumber*50+20}px;">
	    <#list seatSet.rowNumber..1 as i>
	        <li class="text-center">${i}</li>
	    </#list>
	</ul>
	
	<#assign colWidth1=98/(seatSet.colNumber+2)+0.05/>
	<div class="row" style="padding:20px 0px 20px 30px;">
	    <table class="table table-bordered table-height col-xs-2 text-center" style="width: ${colWidth1*(seatSet.space1)}%">
	        <@tableSeat  1 seatSet.space1  false/>
	    </table>
	    <table class="ttable table-bordered table-height col-xs-3 text-center" style="width: ${colWidth1*(seatSet.space2-seatSet.space1)}%;margin-left: ${colWidth1}%">
	        <@tableSeat seatSet.space1+1 seatSet.space2  true/>
	    </table>
	    <table class="table table-bordered table-height col-xs-2 text-center" style="width: ${colWidth1*(seatSet.colNumber-seatSet.space2)}%;margin-left: ${colWidth1}%">
	        <@tableSeat seatSet.space2+1 seatSet.colNumber false />
	    </table>
	</div>
	<#macro tableSeat colMin colMax isMid>
	        <tbody>
	        <#list seatSet.rowNumber..1 as row>
	            <tr>
	                <#list colMin..colMax as col>
	                    <td class="text-center" style="width: ${colWidth}%">
	                        <#if stuSetdataMap?? && stuSetdataMap?size gt 0>
	                            ${stuSetdataMap[row+'_'+col]!}
	                        </#if></td>
	                </#list>
	            </tr>
	            <#if row_index==seatSet.rowNumber-1 && isMid>
	                <tr>
	                    <td colspan="${colMax-colMin+1}" class="text-center h3">讲台</td>
	                </tr>
	            </#if>
	        </#list>
	        </tbody>
	    </table>
	</#macro>
<#else>
	<div class="no-data" style="padding: 30px 100px 200px 100px">
	    <span class="no-data-img">
	    	<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
	    </span>
	    <div class="no-data-body">
	   	 	<p class="no-data-txt">暂无设置</p>
	    </div>
    </div>
</#if>