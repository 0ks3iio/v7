<#import "/fw/macro/webmacro.ftl" as w>
<div class="main-content-inner">
<div class="page-content">
    <div class="row">
        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->
            <div class="box box-default">
                <div class="box-body">
                    <div class="filter filter-f16">
                        <a href="javascript:void(0);" id="addApp" class="btn btn-blue pull-right">新增应用</a>
                        <div class="filter-item">
                            <span class="filter-name">应用名称：</span>
                            <div class="filter-content">
                                <input type="text" id="appName" value="${appName!}" class="form-control">
                            </div>
                        </div>
                       <#if isShowSource == 1>
                         <div class="filter-item">
                            <span class="filter-name">应用来源：</span>
                            <div class="filter-content">
                                <select id="source" class="form-control">
                                    <option value="-1" ${(!source??||source==-1)?string('selected','') }>全部</option>
                                   <option value="1" ${(source??&&source==1)?string('selected','') }>内部产品</option>
                                   <option value="2" ${(source??&&source==2)?string('selected','') }>第三方应用</option>                          
                                </select>
                            </div>
                        </div>
                        </#if>
                        <div class="filter-item">
                            <span class="filter-name">状态：</span>
                            <div class="filter-content">
                                <select id="status" class="form-control">
                                    <option value="-1" ${(!status??||status==-1)?string('selected','') }>全部</option>
                                    <option value="0" ${(status??&&status==0)?string('selected','') }>已停用</option>
                                    <option value="1" ${(status??&&status==1)?string('selected','') }>已上线</option>
                                    <option value="2" ${(status??&&status==2)?string('selected','') }>未上线</option>
                                    <option value="3" ${(status??&&status==3)?string('selected','') }>未提交</option>
                                    <option value="4" ${(status??&&status==4)?string('selected','') }>未通过</option>
                                    <option value="5" ${(status??&&status==5)?string('selected','') }>审核中</option>
                                </select>
                            </div>
                        </div>
                       
                        <div class="filter-item">
                            <a href="javascript:void(0);" id="findApps" class="btn btn-blue">查找</a>
                        </div>
                    </div>
                    <table class="table table-striped table-hover no-margin">
                        <thead>
                            <tr>
                                <th>应用</th>
                               <!--   <th>更新时间</th>-->
                                <th>开发者</th>
                                  <#if isShowSource == 1>
                                    <th>应用来源</th>
                                </#if>
                                <th>订阅类型</th>
                                <th>适用机构</th>
                                <th>适用对象</th>
                                <th>适用学段</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#if apps??&&(apps?size>0)>
                                <#list apps as app>
                                    <tr data-id="${app.systemId!}" data-devid="${app.devId! }" data-appid="${app.id!}" data-status="${app.status}">
                                        <td>${app.name!}</td>
                                        <!--<td>${app.timeStr! }</td> -->
                                        <td>${app.devName!}</td>
                                         <#if isShowSource == 1>
                                            <td>${app.appSource!}</td>
                                        </#if>
                                        <td>${app.orderTypeName!}</td>
                                        <td>${app.unitTypeName!}</td>
                                        <td>${app.userTypeName!}</td>
                                        <td>${app.sectionsName!}</td>
                                        <td><span class="badge ${app.statusColor!}">${app.statusName!}</span></td>
                                        <td>
                                            <#if app.serverClass == 2>
                                              <#if app.status == 3>
                                                <a href="javascript:;" class="table-btn color-blue look">查看</a>
                                                <#elseif app.status == 5>
                                                <a href="javascript:;" class="table-btn color-blue audit">审核</a>
                                                <a href="javascript:;" class="table-btn color-red delete">删除</a>
                                                <#elseif app.status == 4>
                                                <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                                                <a href="javascript:;" class="table-btn color-red delete">删除</a>
                                                <#elseif app.status == 2>
                                                <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                                                <a href="javascript:;" class="table-btn color-red delete">删除</a>
                                                <a href="javascript:;" class="table-btn color-green online">上线</a>
                                                <a href="javascript:;" class="table-btn color-red stop">停用</a>
                                                <#elseif app.status == 1>
                                                 <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                                                 <a href="javascript:;" class="table-btn color-grey offline">下线</a>
                                                 <a href="javascript:;" class="table-btn color-red stop">停用</a>
                                                 <#elseif app.status == 0>
                                                 <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                                                <a href="javascript:;" class="table-btn color-red delete">删除</a>
                                                <a href="javascript:;" class="table-btn color-green start">启用</a>
                                              </#if>
                                              <#else>
                                                <a href="javascript:;" class="table-btn color-lightblue modify">修改</a>
                                                <#if app.subId??>
                                                    <a href="javascript:;" data-subid="${app.subId! }" class="table-btn color-blue lookModel">模块管理</a>
                                                </#if>
                                                <!--<#if app.serverClass == 1>
                                                    <a href="javascript:;" class="table-btn modelList">模块管理</a>
                                                </#if>-->
                                              </#if>
                                        </td>
                                    </tr>
                                </#list>
                            </#if>
                        </tbody>
                    </table>
                    <@w.pagination2  container="#moduleContentDiv" pagination=pagination page_index=2/>
                    
                </div>
            </div>
            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div><!-- /.page-content -->
</div>
<div class="layer layer-view layer-scrollContainer showModel">
 
</div>

<script>
$(function(){
  $('#reservation').daterangepicker(null, function(start, end, label){});
  $('#findApps').on('click',findApps);//查找
  $('#addApp').on('click',addApp);//新增应用
  $('.delete').on('click',deleteApp);//删除应用
  $('.online').on('click',onlineApp);//应用上线
  $('.offline').on('click',offlineApp);//应用下线
  $('.stop').on('click',stopApp);//停用应用
  $('.start').on('click',startApp);//启用应用
  $('.modify').on('click',modifyApp);//修改应用
  $('.audit').on('click',modifyApp);//审核应用
  $('.look').on('click',modifyApp);//查看应用
  if($('.lookModel').length>0){
    $('.lookModel').on('click',lookModel);//查看模块
  }
  
  $('#appName').keyup(function(event) {
    if (event.keyCode == 13) {
      $('#findApps').trigger("click");
    }
  });
});

  // 模块管理
function lookModel(){
    var subId = $(this).data('subid');
	$('.showModel').load("${request.contextPath}/system/server/lookModel?subId="+subId,function(){			
	layer.open({
				type: 1,
				shade: .5,
				title: ['模块','font-size:20px;'],
				area: ['940px','600px'],
				btn:['确定','取消'],
				yes:function(index,layero){
                  addModel("${request.contextPath}");
                },
				content: $('.showModel')
				})
	  });
}

	function addModel(contextPath){
    var $active ;
    var functions = new Array();
    var n = 0;
    $("#modelManage").find("tr").each(function () {
        $this = $(this);
        var class1 = null;
        class1 = $(this).attr("class");
        var obj = dealEValue("."+class1);
        functions[n] = obj;
        n++;
    });  
    var options;
   options = {
            url:contextPath+"/system/server/updateModel",
            data:JSON.stringify(functions),
            clearForm : false,
            resetForm : false,
            dataType:"json",
            type:'post',
            contentType: "application/json",
            success:function (data) {
                if(data.success){
                   showSuccessMsg(data.msg);
                   moduleContentLoad('${request.contextPath}/system/server/appList');
                   layer.closeAll('page');
               //    showSuccessMsg(data.msg);
                }else{
                    showErrorMsg(data.msg);
                }
            }
        }
    $.ajax(options);
}
   function dealEValue(container){
    var tags = ["td"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var name = $this.attr("name");
            var isInput=$this.find("#"+name); 
            var selectPid=$this.find("#parentId");
            
            var value;
            if(isInput.length>0){
              value = $(this).find('#'+name).val();
            }else if(selectPid.length>0){
              value = selectPid.options[selectPid.selectedIndex].value;
            }else{
              value = $(this).text();
            } 
            
            
            //去掉空格和换行
            value=value.replace(/\ +/g,""); //去掉空格
            value=value.replace(/[\r\n\t]/g,"");//去掉回车换行
            obj[name] = value;
        });
    }
    return obj;
}








function modifyApp(){
  var appId = $(this).parents('tr').data('appid');
  var devName = $(this).parents('tr').find('td:eq(1)').html();
  moduleContentLoad("${request.contextPath}/system/server/modifyApp",{"devName":devName,"appId":appId});
}

function startApp(){
  var appId = $(this).parents('tr').data('appid');
  var devId = $(this).parents('tr').data('devid');
  var appName = $(this).parents('tr').find('td:eq(0)').html();
  showConfirm("确认要启用该应用吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
    $.post("${request.contextPath}/system/server/startApp",{"appId":appId,"devId":devId,"appName":appName},function(result){
      moduleContentLoad('${request.contextPath}/system/server/appList?_pageIndex='+${pagination.pageIndex});
      layer.closeAll('dialog');
    });
  },function(){
    layer.closeAll('dialog');
  });
}

function stopApp(){
  var appId = $(this).parents('tr').data('appid');
  var devId = $(this).parents('tr').data('devid');
  var appName = $(this).parents('tr').find('td:eq(0)').html();
  showConfirm("确认要停用该应用吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
    $.post("${request.contextPath}/system/server/stopApp",{"appId":appId,"devId":devId,"appName":appName},function(result){
      moduleContentLoad('${request.contextPath}/system/server/appList?_pageIndex='+${pagination.pageIndex});
      layer.closeAll('dialog');
    });
  },function(){
    layer.closeAll('dialog');
  });
}

function offlineApp(){
  var appId = $(this).parents('tr').data('appid');
  var devId = $(this).parents('tr').data('devid');
  var appName = $(this).parents('tr').find('td:eq(0)').html();
  showConfirm("确认要下线该应用吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
    $.post("${request.contextPath}/system/server/offlineApp",{"appId":appId,"devId":devId,"appName":appName},function(result){
      moduleContentLoad('${request.contextPath}/system/server/appList?_pageIndex='+${pagination.pageIndex});
      layer.closeAll('dialog');
    });
  },function(){
    layer.closeAll('dialog');
  });
}

function onlineApp(){
  var appId = $(this).parents('tr').data('appid');
  var devId = $(this).parents('tr').data('devid');
  var appName = $(this).parents('tr').find('td:eq(0)').html();
  showConfirm("确认要上线该应用吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
    $.post("${request.contextPath}/system/server/onlineApp",{"appId":appId,"devId":devId,"appName":appName},function(result){
      moduleContentLoad('${request.contextPath}/system/server/appList?_pageIndex='+${pagination.pageIndex});
      layer.closeAll('dialog');
    });
  },function(){
    layer.closeAll('dialog');
  });
}

function deleteApp(){
  var appId = $(this).parents('tr').data('appid');
  var devId = $(this).parents('tr').data('devid');
  var appName = $(this).parents('tr').find('td:eq(0)').html();
  showConfirm("确认要删除吗？",{btn: ["确定","取消"],title:"提示", icon:3,closeBtn:0},function(){
    $.post("${request.contextPath}/system/server/delApp",{"appId":appId,"devId":devId,"appName":appName},function(result){
      moduleContentLoad('${request.contextPath}/system/server/appList?_pageIndex='+${pagination.pageIndex});
      layer.closeAll('dialog');
    });
  },function(){
    layer.closeAll('dialog');
  });
}

function addApp(){
  moduleContentLoad("${request.contextPath}/system/server/addApp");
}

function findApps(){
  var appName =$('#appName').val();
  var reservation = $('#reservation').val();
  var status = $('#status').val();
  var source = $('#source').val();
  
  moduleContentLoad("${request.contextPath}/system/server/appList",{"appName":appName,"reservation":reservation,"status":status,"source":source});
}

</script>