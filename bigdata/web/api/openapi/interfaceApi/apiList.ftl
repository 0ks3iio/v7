<#if interfaceDtos?exists &&(interfaceDtos?size gt 0)>
 <#list interfaceDtos as dto>
  <div class="deve-basic-box">
      <div class="deve-basic-heard">
        <input type="hidden" id=ticketKey class="form-control" value="${dto.ticketKey!}" /> 
        <div class="deve-bh-first method">${dto.apiInterface.methodType!}</div>
        <div class="deve-bh-right">
           <div class = "baseData-inner-body uri" data-uri="${dto.apiInterface.uri!}" >${dto.apiInterface.uri!}</div>
           <div>（${dto.apiInterface.description!}）</div>
        </div>
     </div>
     <div class="deve-basic-body">
        <div class="bold deve-basic-title">数据属性（${dto.apiInterface.resultType!}）</div>
        <div class="row deve-table-ma">
            <table class="tables">
                <thead>
                    <tr>
                        <th>属性名</th>
                        <th>参数名</th>
                        <th>数据类型</th>
                        <th>是否非空</th>
                        <th>说明</th>
                        <th>数据字典</th>
                    </tr>
                </thead>
                <tbody>
                <#if dto.entitys?exists &&(dto.entitys?size gt 0)>
                  <#list dto.entitys as entity> 
                    <tr>
                        <td>${entity.entityName!}</td>
                        <td>${entity.displayName!}</td>
                        <td>${entity.entityType!}</td>
                        <td><#if entity.mandatory == 0> 否 <#else> 是 </#if> </td>
                        <td>${entity.entityComment!}</td>
                        <td>/</td>
                    </tr>
                   </#list>
                 </#if>
                </tbody>
            </table>
        </div>
        <#if dto.show?default(false) >
	         <div class="bold deve-basic-title">调用参数</div>
	         <div class="row paramList">
	             <table class="tables">
	                 <thead>
	                     <tr>
	                         <th>参数名</th>
	                         <th>参数值</th>
	                         <th>说明</th>
	                         <th>是否必填</th>
	                     </tr>
	                 </thead>
	                 <tbody>
	                    <#if dto.parameters?exists &&(dto.parameters?size gt 0)>
		                   <#list dto.parameters as param> 
		                     <tr>
		                         <td>${param.paramName!}</td>
		                         <td>
		                             <input type="text" class="form-control form-control cspj" type-modatory="${param.mandatory!}" <#if param.mandatory == 0> nullable="false" </#if>
		                             typename="${param.paramName!}" >
		                         </td>
		                         <td>${param.description!}</td>
		                         <td><#if param.mandatory == 0> 否 <#else> 是 </#if></td>
		                     </tr>
		                    </#list>
		                  </#if>
	                 </tbody>
	             </table>
	         </div>
	         <div class="bold deve-basic-title2">调用样例</div>
	         <div class="row">
	             <ul class="tabs tabs-basics" tabs-group="g1">
	                 <li class="tab active"><a href="#g1-tab1">java</a></li>
	                 <li class="tab"><a href="#g1-tab2">php</a></li>
	                 <li class="tab"><a href="#g1-tab3">curl</a></li>
	             </ul>
	             <div class="tabs-panel active" tabs-group="g1" id="g1-tab1">
	                 <div class="deve-sam-content">
	                      Request.Get("       
	                      <span class = "uri-span">
	                             ${dto.apiInterface.uri!}?ticketKey=${dto.ticketKey!}
	                      </span>
	                      ").execute().returnContent().asString();
	                 </div>
	             </div>
	             <div class="tabs-panel" tabs-group="g1" id="g1-tab2">
	                 <div class="deve-sam-content uri-span">
	                     $ch = curl_init("
	                     <span class = "uri-span">
	                             ${dto.apiInterface.uri!}?ticketKey=${dto.ticketKey!}
	                     </span>
	                     ");
	                     curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
						 $response = json_decode(curl_exec($ch));       
	                 </div>
	             </div>
	             <div class="tabs-panel" tabs-group="g1" id="g1-tab3">
	                 <div class="deve-sam-content uri-span">
	                     curl -X GET
	                     <span class = "uri-span">
	                             ${dto.apiInterface.uri!}?ticketKey=${dto.ticketKey!}
	                     </span>
	                 </div>
	             </div>
	         </div>
	         <button class="btn btn-primary btn-request" style="margin: 10px 0;">获取数据</button>
	         <div class="bold deve-basic-title2">返回结果数据</div>
	         <div class="deve-result-content baseData-response">
	         
	         </div>
	     </#if>
	   </div>
    </div>
  </#list>
<#else>
  暂无接口
</#if>
 <script>
 $(function(){ 
	 $('.tabs-basics').tabs();
	 $(".deve-basic-heard").click(function(){
	     if($(this).parent().hasClass("active")){
	         $(this).parent().removeClass("active");
	     }else{
	         $(this).parent().siblings().removeClass("active");
	         $(this).parent().addClass("active");
	     }
	 })
	 
     //获取数据
     $('.btn-request').on('click',function(){
       queryJson($(this));
     });
     //参数拼接
     $('.paramList').on('change','.cspj',function(){
       if($(this).attr('type')!='checkbox'){
         changeUri($(this));
       }
     });
 })
 
 
 function changeUri($this){
       var val=$this.val();
       var name = $this.attr('typename')+'=';
       var oldVal=$this.attr('data-old');
       if(typeof(oldVal)!='string'){
         oldVal='';
       }
       if(val!=''){
         val='&'+name+val;
       }
       var spans=$this.parents('.deve-basic-box').find('.uri-span');
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
 
 
 //请求数据
     function queryJson($this){
       var isTrue=true;
       $parent=$this.parents('.deve-basic-box');
       var uri=$parent.find('.uri').html();
       var methodType = $parent.find('.method').html();
       var tgs=['input'];
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
            	 showLayerTips4Confirm('error',n+ '为必填参数，不能为空！');   
                 isTrue=false;
                 return false;
               }
               if(v != ''){
                 var othIndex = uri.indexOf("{"+n+"}");
                 if(othIndex >=0){
                    if(n=='id' && v.length != 32){
                       showLayerTips4Confirm('error',"参数id必须为32位GUID");
                       isTrue=false;
                       return false;
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
       var aj = $.ajax({  
           url:'${request.contextPath}/api' + uri,
           data:$.param(objs),  
           type: methodType,  
           cache:false,  
           dataType:'json',
           beforeSend:function(XMLHttpRequest){
               $this.text("获取中……");
               XMLHttpRequest.setRequestHeader("ticketKey",$('#ticketKey').val());
           },  
           success:function(data) {
               ajaxSessionVai(data);
               $parent.find('.baseData-response').text(formatJson(data));
              // $this.parents('.baseData-inner-wrap').find('.baseData-response').show().next('.text-center').children('.fa-angle-double-up').show();
               $this.text("获取");
            },  
            error:function(XMLHttpRequest, textStatus, errorThrown) {
              // alert(XMLHttpRequest.responseText+"----" + textStatus);
               showLayerTips4Confirm('error',XMLHttpRequest.responseText);
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
 
function ajaxSessionVai(result){
	  var obj=result;
	  if(typeof(result)=="string"){
	    try{
	      obj=JSON.parse(result);
	    }catch(e){
	      return;
	    }
	  }
	  if(typeof(obj)=='object' && obj.code==-2){
	    location.href=obj.msg;
	  }
}
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 </script>