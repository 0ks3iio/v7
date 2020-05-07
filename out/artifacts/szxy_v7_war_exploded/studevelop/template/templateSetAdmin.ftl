<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div id="showDiv" class="box box-default">
    <div class="nav-tabs-wrap clearfix">
        <ul class="nav nav-tabs nav-tabs-1" role="tablist">
            <li role="presentation" class="active"><a href="#aa" onclick="itemShowList('1')" role="tab" data-toggle="tab">成绩项目设置</a></li>
            <#if deployRegion?default('') != 'ucan' >
            <li role="presentation" ><a href="#bb" onclick="itemShowList('2')"  data-toggle="tab">身心项目设置</a></li>
            <li role="presentation" ><a href="#cc" onclick="itemShowList('3')"  data-toggle="tab">思想素质设置</a></li>
            </#if>

            <#--<li role="presentation" ><a href="#dd" onclick="itemShowList('4')"  data-toggle="tab">模板设置</a></li>-->

            <li role="presentation"><a href="#ee" role="tab" onclick="itemShowList('5')" data-toggle="tab">开学通知设置</a></li>
            <#--<a href="javascript:copyData();" class="btn btn-blue pull-right" id="copyButton" style="margin-top:10px;margin-right:10px;">复制上一学年学期数据</a>-->
        </ul>
		
    </div>
    
    <div id="itemShowDivId">

    </div>
</div>
<script type="text/javascript">
    $(function(){
        itemShowList('1');
    });
    function itemShowList(tabIndex){
        if(!tabIndex || tabIndex == '1'){
            $('#copyButton').show();
            var url =  '${request.contextPath}/studevelop/subject/tab';
        }else if(tabIndex == '4'){
            $('#copyButton').hide();
            var url =  '${request.contextPath}/studevelop/stuQualityReportSet/index/page?';
        }else if(tabIndex =='2' || tabIndex =='3'){
            $('#copyButton').show();
            var url=  '${request.contextPath}/stuDevelop/commonProject/head/index/page?code='+tabIndex;
        }else if(tabIndex == '5'){
            $('#copyButton').hide();
            var url =  '${request.contextPath}/studevelop/templateSet/schoolNotice/head?';
        }
        $("#itemShowDivId").load(url);
    }
    function copyData(){
         showConfirmMsg('复制上一学年学期数据，复制后将会覆盖本学年学期数据，确认复制？','提示',function(){
             var ii = layer.load();
             $.ajax({
			     url:'${request.contextPath}/studevelop/subject/doCopy',
			     //data: {'subjectIds':subjectIds},
			     type:'post',
			     success:function(data) {
				    var jsonO = JSON.parse(data);
		 		    if(jsonO.success){
                       layer.closeAll();
					   layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
				  	    $("#showDiv").load("${request.contextPath}/studevelop/templateSet/index/page");
		 		    }else{
		 			   layerTipMsg(jsonO.success,"复制失败",jsonO.msg);
		 			   isSubmit=false;
				    }
				    layer.close(ii);
			    },
	 		    error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
	    });   
    }
</script>
