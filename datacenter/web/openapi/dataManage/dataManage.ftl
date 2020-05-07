              <div class="main-content-inner">
                  <div class="page-content">
                      <div class="row">
                          <div class="col-xs-12">
                              <!-- PAGE CONTENT BEGINS -->
                              <p>
                                  <button class="btn btn-blue js-apply">&nbsp;申请接口&nbsp;</button>
                                  <#if dataType?default(1) == 1>
                                     <span class="color-blue">&emsp;点击 申请接口 勾选需要拉取的基础数据，审核通过后可进行调用。</span>
                                  <#elseif  dataType == 2>
                                     <span class="color-blue">&emsp;点击 申请接口 勾选需要拉取的业务数据，审核通过后可进行调用。</span>
                                  <#else>
                                     <span class="color-blue">&emsp;点击 申请接口 勾选需要推送的基础数据，审核通过后可进行调用。</span>
                                  </#if>
                              </p>
                              <!--  <div class="base-lead">点击 申请接口 勾选需要拉取的基础数据，审核通过后方可进行调用。<br />如果接口返回的数据很多，超过了设定的条数限制（默认是100， 最大为1000），则会进行分批返回数据。在返回的数据中，会多出一个节点nextDataUri，根据此节点的地址，获取下一批数据如果此节点不存在，则说明数据已经全部获取完毕。</div>-->
                              <input type="hidden" id="ticketKey" value="${ticketKey!}">
                              <input type="hidden" id="developer" value="${developerId!}">
                              <div class="box box-default">
                                  <div class="box-body">
                                      <h4>
                                            <b>数据管理</b>
                                           <span class="color-blue" style="font-size: 14px;">&emsp;点击下面的各行信息，可以查看各数据接口的详细说明，申请数据审核通过后，可进行调试</span>
                                       </h4>
                                      <hr />
                                      <div class="clearfix">
                                          <label class="pull-left">
                                              <input id="check" name="switch-field-1" class="wp wp-switch" type="checkbox">
                                              <span class="lbl"></span>
                                          </label>
                                          &nbsp;已通过审核接口
                                          <!--  <span id="lookSubscribe">&nbsp;只看订阅数据</span>-->
                                      </div>
                                      <p class="color-blue" id="noDataTip" style="display:none;">&nbsp;您还未订阅任何数据，请点击<a href="javascript:void(0);" class="js-apply">申请接口</a>进行申请！</p>
                                      
                                      <div class="base-lead" style="display: none;">输入ticketKey点击更新按钮后，才能看到具体的字段以及接口。<br />如果接口返回的数据很多，超过了设定的条数限制（默认是100， 最大为1000），则会进行分批返回数据。在返回的数据中，会多出一个节点nextDataUri，根据此节点的地址，获取下一批数据如果此节点不存在，则说明数据已经全部获取完毕。</div>
                                      
                                      <!-- LIST BEGINS -->
                                      <#list openApiApplys as type>
                                      <div class="baseData-wrap"  data-type="${type.type}" data-status="${type.status}" data-typename="${type.typeNameValue}">
                                          <div class="baseData-header baseData-header-re">
                                              <i class="fa fa-angle-down"></i>
                                              <span>${type.typeNameValue}</span>
                                          </div>
                                      </div>
                                      </#list>
                                      
                                      <!-- LIST ENDS -->
                                      
                                  </div>
                              </div>
                              
                              <!-- PAGE CONTENT ENDS -->
                          </div><!-- /.col -->
                      </div><!-- /.row -->
                  </div><!-- /.page-content -->
              </div>
            <!-- /.main-content -->
            
            <div class="layer layer-view layer-scrollContainer">
              <table class="table table-striped table-outline table-hover no-margin" id="rmTr">
                  <thead>
                      <tr>
                          <th>代码</th>
                          <th>内容</th>
                          <th>代码</th>
                          <th>内容</th>
                      </tr>
                  </thead>
                  <tbody id="mcodeIdTable">
                  </tbody>
              </table>
            </div>
            <div class="layer layer-apply">
                <div class="filter">
                    <div class="filter-item block" >
                        <span class="filter-name">接口名称：</span>
                        <div class="filter-content" id="applyDiv">
                        </div>
                        <div class="layui-layer-btn">
                            <a class="layui-layer-btn0" id="applyId">确定</a>
                            <a class="layui-layer-btn1">取消</a>
                        </div>
                    </div>
                </div>
            </div>
            <script>
            $(function(){
              //打开收起一级
                $('.baseData-wrap .baseData-header').on('click',function(){
                    if('yes'!=$(this).data('mark')){
                      var data={'type':$(this).parent('.baseData-wrap').data('type'), 'dataType':${dataType!}};
                      ajax('${request.contextPath}/data/manage/interface',data,$(this),1);
                    }
                    $(this).parent('.baseData-wrap').toggleClass('active');
                });
                //打开收起二级
                $('.main-content-inner').on('click','.baseData-inner-wrap .baseData-inner-header',function(){
                    var $parent=$(this).parent().parent().parent();
                    if('yes'!=$(this).data('mark')){
                      var data={'applyStatus':$parent.data('status') ,'interfaceId':$(this).data('id')};
                      ajax('${request.contextPath}/data/manage/param',data,$(this),2);
                    }
                    if($(this).parent('.baseData-inner-wrap').hasClass("active")){
                      $(this).parent('.baseData-inner-wrap').removeClass('active');
                      $(this).next('.baseData-inner-body').hide();
                    }else{
                      $(this).parent('.baseData-inner-wrap').toggleClass('active');
                      $(this).next('.baseData-inner-body').show();
                    }
                });
                //收起结果
                $('.main-content-inner').on('click','.baseData-inner-wrap .fa-angle-double-up',function(){
                  $(this).hide().parents('.baseData-inner-wrap').find('.baseData-response').hide();  
                });
                //获取数据
                $('.main-content-inner').on('click','.btn-request',function(){
                  queryJson($(this));
                });
                //参数拼接
                $('.main-content-inner').on('change','.cspj',function(){
                  if($(this).attr('type')!='checkbox'){
                    changeUri($(this));
                  }
                });
                
                //提交申请的接口
                $('#applyId').click(function(){
                  var types=new Array();
                  $('.tjwp').each(function(){
                    var $this=$(this);
                    if($this.prop('checked')){
                      types.push($this.val());
                    }
                  });
                  var data={'types':types,'developId':$('#developer').val()}
                  if(types.length>0){
                    $.ajax({
                      url:'${request.contextPath}/data/manage/apply',
                      data:data,
                      type:'post',  
                      cache:false,  
                      success:function(result){
                        ajaxSessionVai(result);
                        showMsgSuccess('申请成功','成功');
                      }
                    });
                  }
                });
                
                //查看未订阅接口
                $('#check').on('click',function(e){
                  var islogin = ${isLogin!};
                  if(null == islogin||islogin == 0){
                    e.preventDefault();
                    $('.js-login').click();
                    return;
                  }
                  
                  
                  var isCheck=$(this).prop('checked');
                  var modules=$('.baseData-wrap');
                  modules.each(function(){
                    var $this=$(this);
                    if($(this).data('status')==0){
                      if(isCheck){
                        $this.hide();
                      }else{
                        $this.show();
                      }
                    }
                  });
                  
                  var isOrderData = 1;
                  if($('.baseData-wrap[data-status="1"]').length == 0){
                    isOrderData = 0;
                  }
                  
                  if ($(this).prop('checked') == true) {
                    if(isOrderData == 0){
                      $('#noDataTip').show();
                    }else{
                      $('.base-lead').show();
                    }
                  } else {
                    $('.base-lead').hide();
                    $('#noDataTip').hide();
                  };
                  
                  
                });
                
                // 申请数据
                $('.js-apply').on('click',function(){
                  var islogin = ${isLogin!};
                  if(null == islogin||islogin == 0){
                    $('.js-login').click();
                    return;
                  }
                  var modules=$('.baseData-wrap');
                  var enable=false;
                  modules.each(function(){
                    var $this=$(this);
                    if($this.data('status')==0){
                      enable=true;                    
                    }
                  });
                   if(!enable){
                     showMsgSuccess('所有接口都是已订阅','提示');
                     return false;
                   }
                   
                  var url =  '${request.contextPath}/data/manage/applyInterface?dataType='+${dataType!};
              	  $(".main-content-inner").load(url);
                });
            });
             
            function changeUri($this){
              var val=$this.val();
              var name=$this.parent().prev().text()+'=';
              var oldVal=$this.attr('data-old');
              if(typeof(oldVal)!='string'){
                oldVal='';
              }
              if(val!=''){
                val='&'+name+val;
              }
              var spans=$this.parents('.baseData-inner-wrap').find('.uri-span');
              spans.each(function(){
                var $span=$(this);
                var uri=$span.text();
                if(oldVal=='' && val!=''){
                  uri=uri+val;
                }else if(oldVal!='' && val==''){
                  uri=uri.replace(oldVal,val)
                }else if(oldVal!='' && val!=''){
                  uri=uri.replace(oldVal,val);
                }
                $span.text(uri);
              });
              $this.attr('data-old',val);
            };
            
            function ajax(url,data,$this,level){
              $.ajax({
                url:url,
                data:data,
                dataType:'json',
                success:function(msg){
                  //ajaxSessionVai(msg);
                  if(level==1){
                    if(msg.success){
                      var objs=JSON.parse(msg.msg)
                      var html='<div class="baseData-body">';
                      var chil='';
                      for(var i in objs){
                        var o=objs[i];
                        chil=chil+'<div class="baseData-inner-wrap" data-resulttype="'+o.resultType+'"><div class="baseData-inner-header tab_menu clearfix" data-uri="'+
                        o.uri+'" data-id = "'+o.id+'"><span class="description-wrap"><i class="fa fa-caret-down"></i><span class="description">'+
                        o.description+'</span></span><span class="method">'+o.methodType+'</span><span class="path">'+
                        o.uri+'</span></div></div>'
                      }
                      $this.after(html+chil+'</div>');
                      $this.attr('data-mark','yes');
                    }
                  }else{
                    if(typeof(msg)=='object'){
                        var resultType=$this.parent().data('resulttype').toUpperCase();
                        var isShowButton = msg.isShowButton;
                        var pushtype = resultType.indexOf("PUSH_");
                        if(pushtype == 0){
                          var body='<div class="baseData-inner-body" style="display: block;"><h5><b>数据属性（'+resultType+'）</b></h5><table class="table table-striped">'+
                          '<tbody><tr><th>属性名</th><th>参数名</th><th>数据类型</th><th>是否必填</th><th>说明</th><th>数据字典</th></tr>';
                       	}else if(pushtype == -1){
                       	  var body='<div class="baseData-inner-body" style="display: block;"><h5><b>数据属性（'+resultType+'）</b></h5><table class="table table-striped">'+
                          '<tbody><tr><th>属性名</th><th>参数名</th><th>数据类型</th><th>是否非空</th><th>说明</th><th>数据字典</th></tr>';
                       	}
                        var entityHtml='';
                        var paramHtml='';
                      
                        var entitys=msg.entitys;
                        if(typeof(entitys)=='object'){
                          for(i in entitys){
                            var entity=entitys[i];
                            var mcode='';
                            if(typeof(entity.mcodeId)=='string' && typeof(entity.entityComment)!='string'){
                              var mcodeId="'"+entity.mcodeId+"'";
                              mcode='<a href="javascript:void(0);" class="data-show" onclick="javascript:openModeId('+mcodeId+');">查看字典</a>';
                            }
                            var entityComment='';
                            if(typeof(entity.entityComment)=='string'){
                              entityComment=entity.entityComment;
                            }
                            var modatory=(entity.mandatory==0)?'否':'是';
                            entityHtml=entityHtml+'<tr><td>'+entity.entityName+'</td><td>'+entity.displayName+'</td><td>'+
                            entity.entityType+'</td><td>'+modatory+'</td><td>'+entityComment+'</td><td>'+mcode +'</tr>';
                          }
                        }
                      body=body+entityHtml+'</tbody></table>';
                      
                      if(pushtype == 0){
                    	  var dycs = '';  
                      }else{
	                      var dycs='<h5><b>调用参数</b></h5><table class="table table-striped"><tbody>'+
	                      '<tr><th width="200">参数名</th><th width="250">参数值</th><th>说明</th><th width="100">是否必填</th><th></th></tr>';
                      }
                      
                      var params=msg.params;
                      if(typeof(params)=='object'){
                          for(i in params){
                              var param=params[i];
                              var modatory=(param.mandatory==1)?'是':'否';
                              var mhtml='<input type="text" class="form-control cspj" type-modatory="'+param.mandatory+'" typename="'+param.paramName+'">'
                              if(typeof(param.mcodeId)=='string' &&  param.mcodeId != ''){
                                var modeId=param.mcodeId;
                                mhtml='<select class="form-control cspj" type-modatory="'+param.mandatory+'"  typename="'+param.paramName+'"><option value="">--- 请选择 ---</option>';
                                mhtml=mhtml+'</select>'
                              }
                              paramHtml=paramHtml+'<tr><td>'+param.paramName+'</td><td>'+mhtml+'</td><td>'+
                              param.description+'</td><td>'+modatory+'</td><td></tr>';
                          }
                      }
                      var $parent=$this.parent().parent().parent();
                      var ticket=$('#ticketKey').val();
                      var id =Date.parse(new Date());
                      var uri='${request.contextPath!"http://api.wanpeng.com"}'+$this.data('uri')+'?ticketKey='+ticket;
                     dycs=dycs+paramHtml+'</tbody></table><h5><b>调用样例</b></h5><ul class="nav nav-tabs" role="tablist">'+
                    '<li role="presentation" class="active"><a href="#'+id+'-1" role="tab" data-toggle="tab">Java</a></li><li role="presentation"><a href="#'+id+'-2" role="tab" data-toggle="tab">PHP</a></li><li role="presentation"><a href="#'+id+'-3" role="tab" data-toggle="tab">Curl</a></li></ul>'+
                    '<div class="tab-content">'+
                       '<div role="tabpanel" class="tab-pane active" id="'+id+'-1">'+
                           '<code class="base-hljs">Request.Get("<span class="uri-span">'+uri+'</span>").execute().returnContent().asString();</code></div>'+
                       '<div role="tabpanel" class="tab-pane" id="'+id+'-2">'+
                           '<code class="base-hljs">$ch = curl_init("<span class="uri-span">'+uri+'</span>");<br>curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);<br>$response = json_decode(curl_exec($ch));</code></div>'+
                       '<div role="tabpanel" class="tab-pane" id="'+id+'-3">'+
                           '<code class="base-hljs">curl -X GET <span class="uri-span">'+uri+'</span></code></div></div>';
                    
                    var getDataDiv='<h2 class="text-center"><button class="btn btn-blue btn-request">&nbsp;获取&nbsp;</button></h2><div class="baseData-response"><h5><b>返回结果数据</b></h5><pre><code class="hljs json">'+
                    '</code></pre></div><h2 class="text-center"><i class="fa fa-angle-double-up"></i></h2>'
                    if($this.parent().parent().parent().data('status')==1){
                      if(pushtype == 0 || !isShowButton){
                    	body=body+dycs; 
                      }else{
                        body=body+dycs+getDataDiv;
                      }
                    }
                    $this.after(body+'</div>');
                    $this.attr('data-mark','yes');
                  }
                }
              }});
              };
              //打开字典DIV
              function openModeId(mcodeId){
                $("#rmTr tr:gt(0)").remove();
                var aj = $.ajax({  
                  url:'${request.contextPath}/remote/openapi/mcode/' + mcodeId,
                  type:'get',  
                  cache:false,  
                  dataType:'json',
                  success:function(data) {  
                    ajaxSessionVai(data);
                      var count = 0;
                      var newRow = "";
                      $(data).each(function(){
                          if(count % 2 == 0){
                              newRow += "<tr>";
                          }
                          count ++;
                          newRow += "<td>" + $(this).attr('thisId') + "</td><td>" + $(this).attr('mcodeContent') + "</td>";
                          if(count % 2 == 0){
                              newRow += "</tr>";
                          }
                      });
                      if(count % 2 != 0){
                          newRow += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
                      }
                      $("#mcodeIdTable").append(newRow);
                      openMcode();
                  }  
                });
              };
              
              //显示数据字典
              function openMcode(){
                layer.open({
                  type: 1,
                  shade: .5,
                  title: ['数据字典','font-size:20px;'],
                  area: '500px',
                  btnAlign: 'c',
                  content: $('.layer-view')
                });
              };
              //请求数据
              function queryJson($this){
                var isTrue=true;
                $parent=$this.parents('.baseData-inner-body');
                var objsLimit = $parent.find('input[typename="limit"]');
                if(objsLimit.length > 0){
                  if(objsLimit.get(0).value !='' && !/^\d+$/.test(objsLimit.get(0).value)){
                    showMsgError('limit只能填写大于0的正整数','错误');
                    isTrue=false;
                    return;
                  }
                  if(objsLimit.get(0).value > 1000){
                      showMsgError('limit不能超过1000','错误');
                      isTrue=false;
                      return; 
                  }
                  if(objsLimit.get(0).value !='' && objsLimit.get(0).value<=0){
                    showMsgError('limit只能填写大于0的正整数','错误');
                    isTrue=false;
                    return;
                  }
                }
                var uri=$parent.prev().data('uri');
                var methodType = $parent.prev().find('.method').html();
                var tgs=['input','select'];
                var len = tgs.length;
                var objs = new Object();
                for(var j = 0; j < len; j ++){
                    var params = $parent.find(tgs[j]);
                    if(params.length>0){
                      params.each(function(){
                        var $param=$(this);
                        var v = $param.val();
                        var n = $param.attr('typename');
                        var t = $param.attr('type-modatory');
                        if(t == '1' && v == ''){
                          showMsgError(n + '为必填参数，不能为空！','错误');
                          isTrue=false;
                          return;
                        }
                        if(v != ''){
                          var othIndex = uri.indexOf("{"+n+"}");
                          if(othIndex >=0){
                             if(n=='id' && v.length != 32){
                                showMsgError('参数id必须为32位GUID','错误');
                                isTrue=false;
                                return;
                             }
                             uri = uri.replace("{"+n+"}", v);
                          }else{
                             if(typeof(v)=='string' && v!="")
                             objs[n] = v;
                          }
                        } 
                      });
                    }
                }
                if(!isTrue){
                  return false;
                }
               // alert('${request.contextPath}/remote' + uri);
                var aj = $.ajax({  
                    url:'${request.contextPath}/remote' + uri,
                    data:$.param(objs),  
                    type: methodType,  
                    cache:false,  
                    dataType:'json',
                    beforeSend:function(XMLHttpRequest){
                        $this.text("获取中……");
                        //alert($('#ticketKey').val());
                        XMLHttpRequest.setRequestHeader("ticketKey",$('#ticketKey').val());
                    },  
                    success:function(data) {
                        ajaxSessionVai(data);
                        $parent.find('.baseData-response').find('code').text(formatJson(data));
                        $this.parents('.baseData-inner-wrap').find('.baseData-response').show().next('.text-center').children('.fa-angle-double-up').show();
                        $this.text("获取");
                     },  
                     error:function(XMLHttpRequest, textStatus, errorThrown) {
                       showMsgError(XMLHttpRequest.responseText);
                        //$parent.find('.baseData-response').find('code').text(formatJson(data));
                        $this.text("获取");
                    }  
                }); 
              }
              //格式化返回回来的json
              function formatJson(json, options){
                var reg = null,
                formatted = '',
                pad = 0,
                PADDING = '    '; // one can also use '\t' or a different number of spaces
             
                options = options || {};
                options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
                options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
             
                if (typeof json !== 'string') {
                    json = JSON.stringify(json);
                } else {
                    json = JSON.parse(json);
                    json = JSON.stringify(json);
                }
             
                reg = /([\{\}])/g;
                json = json.replace(reg, '\r\n$1\r\n');
             
                reg = /([\[\]])/g;
                json = json.replace(reg, '\r\n$1\r\n');
             
                reg = /(\,)/g;
                json = json.replace(reg, '$1\r\n');
             
                reg = /(\r\n\r\n)/g;
                json = json.replace(reg, '\r\n');
             
                reg = /\r\n\,/g;
                json = json.replace(reg, ',');
             
                if (!options.newlineAfterColonIfBeforeBraceOrBracket) {         
                    reg = /\:\r\n\{/g;
                    json = json.replace(reg, ':{');
                    reg = /\:\r\n\[/g;
                    json = json.replace(reg, ':[');
                }
                if (options.spaceAfterColon) {          
                    reg = /\:/g;
                    json = json.replace(reg, ':');
                }
             
                $.each(json.split('\r\n'), function(index, node) {
                    var i = 0,
                        indent = 0,
                        padding = '';
             
                    if (node.match(/\{$/) || node.match(/\[$/)) {
                        indent = 1;
                    } else if (node.match(/\}/) || node.match(/\]/)) {
                        if (pad !== 0) {
                            pad -= 1;
                        }
                    } else {
                        indent = 0;
                    }
             
                    for (i = 0; i < pad; i++) {
                        padding += PADDING;
                    }
             
                    formatted += padding + node + '\r\n';
                    pad += indent;
                });
                return formatted;
            }
        </script>
