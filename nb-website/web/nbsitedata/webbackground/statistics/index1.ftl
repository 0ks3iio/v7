<title>网站后台</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${webUrl}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${webUrl}/static/sweetalert/sweetalert.css" />
<script src="${webUrl}/static/sweetalert/sweetalert.min.js"></script>
<div class="row">
    <div class="col-xs-12">
        <div class="tab-container">
            <div class="tab-header clearfix">
                <ul class="nav nav-tabs pull-left" id="tabId">
                    <#if manager?default(false) && type?default('')!='11'>
                    <li <#if container?default('a') == 'a'>class="active" </#if> >
                        <a data-toggle="tab" href="#a">下属单位总量</a>
                    </li>
                    
                    <li <#if container?default('') == 'b'>class="active" </#if> >
                        <a data-toggle="tab" href="#b">下属单位明细</a>
                    </li>
                    <li <#if container?default('') == 'c'>class="active" </#if> >
                        <a data-toggle="tab" href="#c">本单位发布明细</a>
                    </li>
                    <#else>
                    <li <#if container?default('d') == 'd'>class="active" </#if> >
                        <a data-toggle="tab" href="#d">个人发布总量</a>
                    </li>                    
                    </#if>                    
                </ul>
            </div>
            <div class="tab-content">
                <#if manager?default(false) && type?default('')!='11'>
                <div id="a" class="tab-pane fade in <#if container?default('a') == 'a'>active</#if>">
                    <@search  m=true divId="a" useSeach=true />
                </div>
                
                <div id="b" class="tab-pane fade in <#if container?default('') == 'b'>active</#if>">
                    <@search  m=true divId="b" useSeach=true />
                </div>
                <div id="c" class="tab-pane fade in <#if container?default('') == 'c'>active</#if>">
                    <@search  m=true divId="c" useSeach=true />
                </div>
                <#else>
                 <div id="d" class="tab-pane fade in <#if container?default('d') == 'd'>active</#if>">
                    <@search  m=false divId="d" useSeach=true />
                </div>
               </#if>
            </div>
        </div>
    </div>

</div><!-- 条件筛选结束 -->


<!-- page specific plugin scripts -->
<script type="text/javascript">
    var indexDiv = 0;
    var scripts = [null,
        "${webUrl}/static/ace/components/moment/moment.js",
        "${webUrl}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
        "${webUrl}/static/ace/components/chosen/chosen.jquery.js",null
    ];
    $('.page-content-area').ace_ajax('loadScripts', scripts, function() {
        initCalendar(".row");
        var url =  "${request.contextPath}/sitedata/statistics/list/page";
        var unitId="";
        $("#a .listDiv").load(url+"?state=unit&container=a");
        
        $("#a #btn-search").on("click",function(){
            if(!checkDate("a")){
                return ;
            }
         //   $('#unitList option:selected').attr('value');
            unitId=$('#unitList option:selected').attr('value');
            $("#a .listDiv").load(url+getParams("a")+"&state=unit");
       //     $("#b .listDiv").load(url+getParams("a")+"&state=unitDetails");
       //     $("#c .listDiv").load(url+getParams("a")+"&state=personalDetails");
            
        });
 
    
        $("#b .listDiv").load(url+"?state=unitDetails&container=b");
      
        //搜索

        $("#b #btn-search").on("click",function(){
            if(!checkDate("b")){
                return ;
            }
            $("#b .listDiv").load(url+getParams("b")+"&state=unitDetails");
        });
        $("#c .listDiv").load(url+"?state=personalDetails&container=c");
      
        //搜索

        $("#c #btn-search").on("click",function(){
            if(!checkDate("c")){
                return ;
            }
            $("#c .listDiv").load(url+getParams("c")+"&state=personalDetails");
        });
        

		
		 $("#d .listDiv").load(url+"?state=personalDetails&container=d");
      
        //搜索

        $("#d #btn-search").on("click",function(){
            if(!checkDate("d")){
                return ;
            }
            $("#d .listDiv").load(url+getParams("d")+"&state=personalDetails");
        });
        
        
        
        $('#b #unitList').chosen({
			width:'160px;',
			results_height:'120px',
			multi_container_height:'100px',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			search_contains:true,//模糊匹配，false是默认从第一个匹配
			max_selected_options:1 //当select为多选时，最多选择个数
		});
       
        
    });

    function getIds(container){
        var ids = new Array();
        var obj ;
        if(container){
            obj = $("#"+container+" .cbx-td");
        }
        else{
            obj = $(".cbx-td");
        }
        $(obj).each(function(){
            if($(this).find("span").attr("chk")=="true"){
                ids.push($(this).find("input").attr("id"));
            }
        });
        return ids;
    }

    function getParams(divId){
        var params = "";
        if(divId == ''){
            if("commitTime"==$("#option").val()){
                params = "?startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();

            }else{
                params = "?title="+$("#titleInput").val();
            }
            params = params+"&container=a&page_index=1";
        }else{
       
            params = "?startDate="+$("#"+divId+" #startDate").val()+"&endDate="+$("#"+divId+" #endDate").val();
 
            var unitId=$('#unitList option:selected').attr('value');
          
            var searchTxt2 = $("#searchTxt").val();
       
           
            if("a"==divId){
                params = params+"&container=a&page_index=1";
            }
            else if("b"==divId){
                params = params+"&container=b&page_index=2&commitUnitId="+unitId+"&unitName="+searchTxt2;
            }
            else if("c"==divId){
                params = params+"&container=c&page_index=3";
            }else if("d"==divId){
                params = params+"&container=d&page_index=4";
            }
            
        }
        params = params + "&commitState=";
        return encodeURI(params);
    }

    function checkDate(divId){
        var isOk = true;
        if("" != divId){
            var startDate = $("#"+divId+" #startDate").val();
            var endDate = $("#"+divId+" #endDate").val();
            if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
                layer.tips('开始日期不能大于结束日期','#'+divId+' #startDate',{tips:3,tipsMore:true});
                isOk = false;
            }
        }else{
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
                layer.tips('开始日期不能大于结束日期','#startDate',{tips:3,tipsMore:true});
                isOk = false;
            }
        }
        return isOk;
    }

    function optionChange(obj,container){
        if("commitTime"==$(obj).val()){
            $("#"+container+" #timeDiv").css("display","none");
            $("#"+container+" #startDate").val("");
            $("#"+container+" #endDate").val("");
            $("#"+container+" #inputDiv").show();
        }else{
            $("#"+container+" #inputDiv").css("display","none");
            $("#"+container+" #timeDiv").show();
            
        }
    }

    function updateState(state,container){
        var ids = getIds(container);
        if(!ids.length){
            alert("请选择数据！");
            return ;
        }
        $.ajax({
            url:'${request.contextPath}/sitedata/${type!}/commitoruncommit?commitState='+state+'&ids='+JSON.stringify(ids),
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
                        var url = "${request.contextPath}/sitedata/workbench/list/page";
                        $("#a .listDiv").load(url+"?state=pending&container=a");
                        $("#b .listDiv").load(url+"?state=recent&container=b");
                        $("#c .listDiv").load(url+"?state=uncommit&container=c");
                        layer.close(index);
                    });
                }
            }
        });
    }
    
    
    $("#searchTxt").blur(function(){
      var searchTxt = $("#searchTxt").val();
		var searchTxt2 = $("#searchTxt").val();
	    $("#a .pub-search-list").load("${request.contextPath}/sitedata/statistics/unitLists/page?unitName="+searchTxt2,function(data,status){
        if (status=="success"){ 
         $("#a .pub-search-list").html("<select id='unitList'></select>");
        if(data=='没有该单位'){
        $('<option></option>').html(data).appendTo('select');       
        }
         document.getElementById("searchTxt").style.display="none";
         document.getElementById("userNamesDiv").style.display="";
         var  d = JSON.parse(data);
         $.each(d,function(index,item){
         if(index==0){ 
            return true; 
            }
         var unitName = item.unitName;
         var unitId=item.id;
         $("<option id='option1'></option>").val(unitId).html(unitName).appendTo('select');        
         })
       }
       });
    });
    
        function OnChanged () { 
            event = document.getElementById('searchTxt').value;           
            $.ajax({
            url:'${request.contextPath}/sitedata/statistics/unitLists/page?unitName='+event,
            type:'post',
            cache:false,
            contentType: "application/json",
            success:function(data) {                             
             	$("#b #unitListId").html("<select id='unitList'></select>");
             	try{
	             	var d = JSON.parse(data);	             	
	             	if(d.code =='-1'){	             	 
             			$('<option></option>').html(d.msg).appendTo('select');
             			return;            			
             		}
             	}catch(error){
             		alert('3');
             	}
           		$("<option id='option1'></option>").val('').html('请选择').appendTo('select');        
           		$.each(d,function(index,item){
           			var unitName = item.unitName;
           			var unitId=item.id;
          			$("<option id='option1'></option>").val(unitId).html(unitName).appendTo('select');        
         		})
            }
        }); 
        }
</script>
<#macro search m=false divId="a" useSeach=true>
    <#if m?default(false)>
    <div class="filter " >
        <!--按钮-->
        <#if divId=="b">
          <div class="filter-item" style="width: 202px; height:33px">
			<label for="" class="filter-name">单位：</label>
			<div class="filter-content" style="width: 160px; height:33px" >
				<select  class="form-control multi-ateschids tag-input-style" id='unitList' style="width: 160px;">
			      <#if uList?exists && uList?size gt 0>
					<#list uList as unit>
					 <option value="${unit.id!}">${unit.unitName!}</option>					 
					</#list>
			      </#if>
				</select>
			</div>
		</div>
            
            
  <#--      <div class="filter-item" style="width: 400px; height:33px">
			<label for="" class="filter-name">单位：</label>
			<div class="filter-content" style="width: 100px; height:33px" >
			    <input type="text"   id="searchTxt"  oninput="OnChanged ()"   onpropertychange="OnChanged ()" 
                   style="width: 80px; height:33px;color:#999999" value="请搜索" onFocus="if(value==defaultValue){value='';this.style.color='#000'}" onBlur="if(!value){value=defaultValue;this.style.color='#999'}" />			    			
			</div>
			<div class="filter-content" id="unitListId" style="width: 160px; height:33px" >	    
				<select  class="form-control " id='unitList' style="width: 160px;">
			      
				</select>
			</div>
		</div> -->
		
		
		
		
		</#if>	
		
		
			
  <#--      <#if divId=="a">
        <i>单位名字：</i>   	
         <div class="pub-search fn-left ">
                    <input type="text" value="" class="txt" id="searchTxt" >                    
                    <div id="userNamesDiv" class="pub-search-list" style="overflow:auto;display:none"  >                            
                    </div>
                    <a id="testOne" ></a>
         </div>
        </#if> -->
        <#if useSeach>
            <@timeItem container=divId/>
        </#if>
    </div>
    <div class="widget-box ">
        <div class="widget-body">
            <div class="widget-main listDiv">
            </div>
        </div>
    </div>
    <#else>
		<div class="filter " >		
			<#if useSeach>
				<@timeItem container=divId/>
			</#if>
		</div>
		<div class="row listDiv">
		</div>    
    </#if>
</#macro>

<#macro btnItem btnValue divClass="pull-left" btnClass="" btnId="">
<div class="filter-item  ${divClass!}">
    <@w.btn btnId="${btnId!}" btnValue="${btnValue!}" btnClass="${btnClass!}" />
</div>
</#macro>

<#macro timeItem container>
<div class="filter-item pull-right">

    <div class="filter-content" >
       
      <option value="commitTime">提交时间</option>      
       
    </div>
    <div class="filter-content"  id="timeDiv">
        <div class="input-daterange input-group" >
            <@w.dateDivWithNoColumn nullable="false" id="startDate" displayName="" value="" inputClass="input-sm form-control"/>
            <span class="input-group-addon">
				<i class="fa fa-exchange"></i>
				</span>
            <@w.dateDivWithNoColumn nullable="false" id="endDate" displayName="" value="" inputClass="input-sm form-control"/>
        </div>
    </div>
    <@w.btn btnId="btn-search" btnValue="查找" btnClass="fa-align-center common-search" />
</div>
</#macro>

