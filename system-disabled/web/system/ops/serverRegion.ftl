<#import "/fw/macro/webmacro.ftl" as w>
<div class="box box-default">
<div class="box-body">
<#if initLicense?default(true)>
    <div class="filter filter-f16">
        <div class="filter-item">
            <span class="filter-name">使用地区：</span>
            <div class="filter-content">
                <@w.region regionCode="" regionName="" />
            </div>
        </div>
        <div class="filter-item">
            <button class="filter-name btn btn-blue" id="serarchUnit">选择单位</button>
            <span class="filter-name">&nbsp;&nbsp;&nbsp;</span>
	        <div  class="filter-content" id="unitRegionList">
	        
	        </div>
        </div>
        <div class="filter-item filter-item-right">
            <button class="btn btn-blue" id="searchBtn">查找</button>
        </div>
    </div>
    <table class="table table-striped table-middle">
        <thead>
        <tr>
            <th class="text-center">应用名称</th>
            <th class="text-center">域名/serverId</th>
            <th class="text-center">首页/协议/端口/serverKey</th>
            <th class="text-center">上下文</th>
            <th class="text-center">操作</th>
        </tr>
        </thead>
        <tbody id="serverRegionContent">

        </tbody>
    </table>
    <script>
        $(document).ready(function () {
            $('#serverRegionContent').load("${request.contextPath}/system/ops/serverRegion/list");
            $('#searchBtn').unbind('click').bind('click',function () {
            	var unitId = $("#regionUnitId").val();
            	if(!unitId){
            		unitId = "";
            	}
                $('#serverRegionContent').load("${request.contextPath}/system/ops/serverRegion/list?regionCode=" + $('#regionCode').val()
                		+ "&unitId=" + unitId);
            });
            $('#serarchUnit').on('click',function(){
                doChange();
            });
        });
        var isSubmit=false;
        function doChange(){
        	if(isSubmit){
    			return;
    		}
    		isSubmit = true;
    		$("#unitRegionList").empty();
        	$.ajax({
                url:"${request.contextPath}/system/ops/serverRegion/unitList?regionCode="+$('#regionCode').val(),
                dataType:'json',
                success:function(msg){
                	 if(typeof(msg)=='object'){
                		 var unitList=msg.unitList;
                		 if(typeof(unitList)=='object'){
                			 var mhtml='<select class="form-control" id="regionUnitId" style="width:180px"><option value="">--- 请选择 ---</option>';
                             for(i in unitList){
                                 var obj=unitList[i];
                                 mhtml=mhtml+'<option value="'+obj.id+'">'+obj.unitName+'</option>'
                             }
                             mhtml=mhtml+'</select>'
                             $("#unitRegionList").append(mhtml);
                         }
                	 }
                	 isSubmit=false;
                }
            })
        }
        
    </script>
<#else>
    <h1 style="text-align: center">请先激活序列号,创建顶级单位管理员</h1>
</#if>
</div>
</div>