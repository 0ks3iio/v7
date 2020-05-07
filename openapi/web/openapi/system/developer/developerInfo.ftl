<a href="javascript:moduleContentLoad('${request.contextPath}/system/developer/index');" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="main-content-inner">
    <div class="">
        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                <!-- S 面包屑导航 -->
                <ol class="breadcrumb">
                    <li><a href="javascript:loadPage2('${request.contextPath}/system/developer/manage');">开发者管理 </a></li>
                    <li class="active">开发者信息-查看</li>
                </ol><!-- E 面包屑导航 -->
                <input id="developerId" type="hidden" value="${developerDto.id!}">
                <input id="ticketKey" type="hidden" value="${developerDto.ticketKey!}">
                <div class="box box-default">
                    <div class="box-body">
                        <h4><b>基本信息</b></h4>
                        <table class="table table-show">
                            <tbody>
                                <tr>
                                    <th>用户名：</th>
                                    <td>${developerDto.username!}</td>
                                </tr>
                                <tr>
                                    <th>开发者：</th>
                                    <td>
                                        <div class="td-lock">
                                            <span class="input-icon">
                                                <input type="text" value="" class="form-control modifyName" id="unitName" style="display: none;">
                                                <span class="txt-val">${developerDto.unitName!}</span>
                                                <i class="fa fa-pencil"></i>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>白名单：</th>
                                    <td>
                                        <div class="td-lock">
                                            <span class="input-icon">
                                                <input type="text" value="" placeholder="多个ip之间用英文逗号分隔" class="form-control modifyIps" id="ips" style="display: none;">
                                                <span class="txt-val">${developerDto.ips!}</span>
                                                <i class="fa fa-pencil"></i>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <h4><b>联系方式</b></h4>
                        <table class="table table-show">
                            <tbody>
                                <tr>
                                    <th>联系人姓名：</th>
                                    <td>${developerDto.realName!}</td>
                                </tr>
                                <tr>
                                    <th>联系人地址：</th>
                                    <td>${developerDto.address!}</td>
                                </tr>
                                <tr>
                                    <th>手机号码：</th>
                                    <td>${developerDto.mobilePhone!}</td>
                                </tr>
                                <tr>
                                    <th>邮箱：</th>
                                    <td>${developerDto.email!}</td>
                                </tr>
                                <tr>
                                    <th>官网地址：</th>
                                    <td>${developerDto.homepage!}</td>
                                </tr>
                            </tbody>
                        </table>
                        <h4><b>接口信息</b></h4>
	                        <ul class="nav nav-tabs" role="tablist">
								<li role="presentation" class="active">
								    <a href="#" onclick="javascript:lookInterface('aa','3');" role="tab" data-toggle="tab">
										审核中接口
									</a>
								</li>
								<li role="presentation">
								    <a href="#" onclick="javascript:lookInterface('bb','1');" role="tab" data-toggle="tab">
										已通过接口
									</a>
								</li>
								<li role="presentation">
								    <a href="#" onclick="javascript:lookInterface('cc','2');" role="tab" data-toggle="tab">
										未通过接口
									</a>
								</li>
							</ul>
	                        <div class="tab-content">
	                        
	                        </div>
				    </div>
				</div>
                <div class="box box-default" >
                    <div class="box-body">
                      <h4><b>单位列表</b></h4>
                      <div class="block" id = "developerEmpower">
                      
                      
                      </div>
                    </div>
                </div>
                <div class="box box-default">
                    <div class="box-body">
                        <h4><b>已有应用</b></h4>
                        <hr>
                        <#if apps?exists && apps?size gt 0>
                        <ul class="base-app-list clearfix">
                            <#list apps as app>
                                <li class="base-app-item">
                                    
                                    <img src="${app.fullIcon}" alt="" class="base-app-img">
                                    <span class="base-app-title">${app.name}</span>
                                    
                                </li>
                            </#list>
                        </ul>
                        <#else>
                            <p>暂无应用！</p>
                        </#if>
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div>
<div class="layer layer-scrollContainer layer-sensitive">

</div>
<script>
            $(function(){
                 lookInterface('aa','3');
              //$('.main-content').unbind();
                //$('.main-content').on('click','.td-lock i.fa',function(e){
                $('.td-lock i.fa').on('click',function(e){
                    e.stopPropagation();
                    var txt=$(this).siblings('.txt-val').text();
                    $(this).hide().siblings('.txt-val').hide().siblings('.form-control').show().val(txt);
                });
                $('.main-content-inner').click(function(event){
                    var eo=$(event.target);
                    if(!eo.hasClass('form-control') && !eo.parents('.td-lock').length){
                        if($('.td-lock .form-control:visible').length>0){
                          var val=$('.td-lock .form-control:visible').val();
                          var type = $('.td-lock .form-control:visible').attr('id');
                          $('.td-lock .form-control:visible').hide().siblings('.txt-val').show().text(val).siblings('i.fa').show();
                          
                          if(type == "unitName"){
                            if(typeof(val)=='string' && val!=''){
                              $.post('${request.contextPath}/system/developer/modifyUnitName',{'developerId':$('#developerId').val(),'unitName':val},function(reslut){
                                var jsonO = JSON.parse(reslut);
                                if(!jsonO.success){
                                  showMsgError(jsonO.msg,'');
                                }
                              });
                            }
                          }
                          
                          if(type == "ips"){
                            if(typeof(val)=='string' && val!=''){
                              $.post('${request.contextPath}/system/developer/modifyIps',{'developerId':$('#developerId').val(),'ips':val},function(reslut){
                                var jsonO = JSON.parse(reslut);
                                if(!jsonO.success){
                                  showMsgError(jsonO.msg,'');
                                }
                              });
                            }
                          }
                        }
                    }
                });
                //$('.main-content').on('click','.updateSensitive',function(){
                $('.updateSensitive').on('click',function(){
                  var arr=new Array();
                  $('.updateSensitive').parent().parent().find('.xgwp').each(function(){
                    var $this=$(this)
                    if($this.prop('checked')){
                      arr.push($this.val());
                    }
                  });
                  if(arr.length==0){
                    arr.push('');
                  }
                  $.post('${request.contextPath}/system/developer/modifySensitive',{'type':$('#intName_sen').attr('data-type'),'ticketKey':$('#ticketKey').val(),'columnNames':arr},function(){
                	  showMsgSuccess('修改成功！','');
                      lookInterface('bb','1');
                  });
                });
                  //加载列表
                  showEmpowerUnit();
            });
               
            function delInterface(type, tab){
              $.ajax({
                url:"${request.contextPath}/system/developer/delInterface",
                data:{'type':type,'ticketKey':$('#ticketKey').val(),'developerId':$('#developerId').val()},
                type:'get',
                dataType:'json',
                success:function(result){
                  if(result.success){
                    showMsgSuccess('删除成功！','');
                    if(tab == 'bb'){
                    	lookInterface('bb','1');
                    }else{
                    	lookInterface('cc','2');
                    }
                    return;
                  }
                }
              });
            };
            function loadPage2(url){
              moduleContentLoad(url);
            }
            //查看要通过订阅的接口
            function showPassDiv(type){
            	var url =  '${request.contextPath}/system/developer/checkInVerify?type='+type + '&developerId=' +$('#developerId').val();
            	$('.layer-sensitive').load(url,function(){			
           			layer.open({
                           type: 1,
                           shade: .5,
                           title: ['审核通过','font-size:20px;'],
                           area: '720px',
                           btn: ['确定','取消'],
	       				   yes:function(index,layero){
	       						savePassInter();
	       		           },
	       		           content: $('.layer-sensitive')
	                       });
            	});
            }
            //接口审核不通过
            function toUnPass(types){
              $.ajax({
                url:'${request.contextPath}/system/developer/unpassApply',
                data:{'types':types,'developerId':$('#developerId').val()},
                type:'post',
                dataType:'json',
                success:function(){
                  returnDeal('操作成功！');
                  return
                }
              })
            };
            function returnDeal(msg){
              var url='${request.contextPath}/system/developer/info?developerId='+$('#developerId').val();
              var f = function (index){layer.close(index);moduleContentLoad(url);}
              if(typeof(msg)!='string'){
                msg='';
              }
             showMsgSuccess(msg,'',f);
            };
            function showMsgSuccess(content,title,yesFunction){
              if(!content)content='';
              if(!title)title='成功';
              if(!(typeof yesFunction === "function")){
                  yesFunction = function (index){layer.close(index);}
              }
              options = {btn: ['确定'],title:title, icon: 1,closeBtn:0};
              showConfirm(content, options,yesFunction, function(){});
            };
            function showConfirm(content,options,yesFunction,cancelFunction){
              layer.confirm(content, options,yesFunction,cancelFunction);
            }
            function showMsgError(content,title,yesFunction){
              if(!content)content='';
              if(!title)title='错误';
              if(!(typeof yesFunction === "function")){
                  yesFunction = function (index){layer.close(index);}
              }
              options = {btn: ['确定'],title:title, icon: 2,closeBtn:0};
              showConfirm(content, options,yesFunction, function(){});
          }
          

//加载单位列表
function showEmpowerUnit(){
  var url =  '${request.contextPath}/system/developer/empower/findUnit?developerId='+$('#developerId').val();
  $('#developerEmpower').load(url);
};

function lookInterface(activeId,type){
    var url =  '${request.contextPath}/system/developer/interface?developerId='+$('#developerId').val();
	$(".tab-content").load(url,{"activeId":activeId,"type":type});
};


//参数的保存
var isSubmit=false;
function saveParam(contextPath){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var check = checkValue('.layer-sensitive');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-sensitive"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            isSubmit = false;
            if(data.success){
               showSuccessMsgWithCall(data.msg,lookInterface('bb','1'));
            }else{
               showErrorMsg(data.msg);
            }
        }
  })
}
</script>
