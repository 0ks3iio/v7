<#--panlf ecc 班级空间-->
<#import "../macro/eccStuListMacro.ftl" as stuList />
<div class="scroll-container">
	<@stuList.tableSeat rowNumbers colNumbers spaceNoArr/>
</div>



<script>
	var rowOtherIndex=0;
	var allIndexCol=${colNumbers};
	var colIndex=allIndexCol+1;
	var rowNumber=${rowNumbers};
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflowY: 'auto',
				overflowX: 'hidden',
				height: $(window).height() - 480 - 160
			});
		});

		 $('.container-parent').css({
			overflow: 'auto',
			height: $('.scroll-container').height() -35
		})
		
		 $('.container-parent .gradient').css({
		 	top:'5px'
		 })

		<#list studentWithInfoList as info>
		<#if info.rowNo gt 0 && info.colNo gt 0 >
		if($('.container-list td[data-row-col="${info.rowNo}_${info.colNo}"]').length>0){
			$('.container-list td[data-row-col="${info.rowNo}_${info.colNo}"]').find("a").append('<div class="checkin-stu-avatar"><img src="${request.contextPath}${info.showPictrueUrl!}"></div><span class="checkin-stu-name">${info.student.studentName!}</span>');
		}else{
			if(colIndex>allIndexCol){
				rowOtherIndex++;
				addMyRow();
				colIndex=1;
			}
			$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').find("a").append('<div class="checkin-stu-avatar"><img src="${request.contextPath}${info.showPictrueUrl!}"></div><span class="checkin-stu-name">${info.student.studentName!}</span>');
			colIndex++;
		}
		<#else>
		if(colIndex>allIndexCol){
			rowOtherIndex++;
			addMyRow();
			colIndex=1;
		}
		$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').find("a").append('<div class="checkin-stu-avatar"><img src="${request.contextPath}${info.showPictrueUrl!}"></div><span class="checkin-stu-name">${info.student.studentName!}</span>');
		colIndex++;
		</#if>
		</#list>
		if(rowOtherIndex>0){
			//有添加
			initCols(rowNumber);
		}
		// 监听滚轮事件
		$('.container-parent').scrollTop($('.container-parent')[0].scrollHeight);
	});

	function addMyRow(){
		var trHtml=addFirstRow(rowOtherIndex);
		rowNumber++;
		$('.container-list table tbody .space-td').attr('rowspan',rowNumber);
		$('.container-list table tbody .col_umber').attr('rowspan',rowNumber);
		$('.container-list table tbody').prepend(trHtml);
	}

</script>