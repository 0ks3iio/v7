<div class="row">
      <div class="col-sm-4 deve-table-box">
          <div class="bold deve-table-title">选择接口</div>
          <table class="tables tables-nest">
              <thead>
                  <tr>
                      <th>&nbsp;</th>
                      <th></th>
                      <th>接口名称</th>
                  </tr>
              </thead>
              <tbody>
                 <#if applyInterfaces?exists && applyInterfaces?size gt 0>
                        <#list applyInterfaces?keys as key>
                           <#if applyInterfaces[key]?exists && applyInterfaces[key]?size gt 0>
                             <tr class="has-next">
			                      <td><i class="toggle-next"></i></td>
			                      <td>
			                          <label for="l1-${key!}" class="choice choice-allcheck">
			                              <input type="checkbox" name="l1-${key!}" id="l1-${key!}" value="${key!}"/>
			                              <span class="choice-name"></span>
			                          </label>
			                      </td>
			                      <td><label for="l1-${key!}" id = "label-${key!}_">${applyInterfaces[key][0].resultTypeName!}</label></td>
			                  </tr>
			                  <tr class="has-inner">
			                      <td colspan="3">
			                          <table class="tables">
			                              <tbody>
							                  <#list applyInterfaces[key] as api>
							                      <tr class = "${key!}">
				                                      <td>
				                                          <label for="l1-${api.id!}" class="choice">
				                                              <input type="checkbox" value= "${api.id!}" name="l1-${api.id!}" id="l1-${api.id!}" onclick="chooseInter('${key!}','${api.id!}')"/>
				                                              <span class="choice-name"></span>
				                                          </label>
				                                      </td>
				                                      <td>${api.uri!}</td>
				                                  </tr>
							                  </#list>
						                  </tbody>
			                          </table>
			                      </td>
			                  </tr>
			                </#if>
                       </#list>
                 </#if>
               </tbody>
          </table>
      </div><!-- /.col -->
      <div class="col-sm-4 deve-table-box">
           <div class="bold deve-table-title">选择属性</div>
           <table class="tables">
              <thead>
                  <tr>
					  <th>
                          <label for="l2-chooseEntity" class="choice choice-all">
                              <input type="checkbox" name="l2-chooseEntity"  id = "l2-chooseEntity" onclick="checkEntityAll(this)">
                              <span class="choice-name"></span>
                          </label>
                      </th>
                      <th>属性名称</th>
                      <th>参数名</th>
                      <th>是否非空</th>
                  </tr>
              </thead>
              <tbody id = "showEntity">
              
              </tbody>
           </table>
           
           <div class="bold deve-table-title">选择应用</div>
           <table class="tables">
              <thead>
                  <tr>
                      <th>&nbsp;</th>
                      <th>应用名称</th>
                  </tr>
              </thead>
              <tbody id = "showApiApps">
                  <#if apiApps?? && apiApps?size gt 0>
                        <#list apiApps as app>
	                      <tr class = "${app.id!}">
                               <td>
                                   <label for="l1-${app.id!}" class="choice">
                                       <input type="checkbox" value= "${app.id!}" name="l1-${app.id!}" id="l1-${app.id!}" checked  onclick= getChooseResult('appType')  
                                       <#if isMandatory?default(false)>disabled</#if> />
                                       <span  class="choice-name"></span>
                                   </label>
                               </td>
                               <td>${app.name!}</td>
                          </tr>
                        </#list>
                   </#if> 
              </tbody>
           </table>
           
      </div><!-- /.col -->
      <div class="col-sm-4 deve-table-last">
         <div class="deve-table-box" style="overflow-y:hidden;">
             <div class="bold deve-table-title">
                 <span>已选接口</span>
                 <button type="button" class="btn btn-primary deve-right-btn pull-right">提交申请</button>
             </div>
             <div class="deve-table-body">
                 <table class="tables">
                     <thead>
                         <tr>
                             <th>接口类型</th>
                             <th>已选接口</th>
                             <th>已选属性</th>
                         </tr>
                     </thead>
                     <tbody id = "showResult">
                     
                     </tbody>
                 </table>
             </div>
         </div>
     </div><!-- /.col -->
 </div><!--/.row-->
    
    
 <script>
 var selType;
 var selInterfaceId;
 var map_=getMap();
 var mapIcount_ = getMap();
 var mapEcount_ = getMap();
$(function(){
	//线上样式高度改变
    $(".deve-content").parents('.box').css("height","calc(100% - 46px)");

	//删除
	$(".fa-close").click(function(){
	     $(this).parents("tr").remove();
	})
	 //表格全选
	 $('.tables .choice-allcheck input').click(function(){
		 var type = $(this).val();
	     var chk = $(this).prop('checked'),
	         $choice = $(this).parents('.has-next').next().find('.choice > input');
	     if (chk === true) {
	         $choice.prop('checked', true);
	     } else{
	         $choice.prop('checked', false);
	     }
	     chooseInter(type);
	     //selType = type;
	    // getEntityList(type);
	 	// getChooseResult(type);
	 });
	
	//表格折叠
	$('.tables-nest .toggle-next').click(function(){
		var $next = $(this).parents('.has-next').next('.has-inner');
		$(this).toggleClass('open').parents('.has-next').next('.has-inner').toggleClass('open-inner');
	});
})

function checkEntityAll(obj){
	var checked = $(obj).prop("checked");
	var all = $(obj).parents("div").find("tbody  input:checkbox[name='entity-checkbox']");
	if(checked){
		all.prop('checked',true);
	}else{
		all.prop('checked',false);
	}
	getChooseResult(selType, selInterfaceId);
}


function chooseInter(type,interfaceId){
	selType = type;
	selInterfaceId = interfaceId;
	doUpdateEntity();
	getEntityList(type,interfaceId);
	getChooseResult(type,interfaceId);
}

function doUpdateEntity(){
	var checked = $('#l2-chooseEntity').prop("checked");
	if(checked){
		$('#l2-chooseEntity').prop('checked',false);
	}
}


function getChooseResult(type,interfaceId){
	 if("appType" == type){
		 getEndResult(selType,selInterfaceId);
	 }else{
		 getEndResult(type,interfaceId);
		 var selInter = $('.'+type+' :checkbox:checked');
		 mapIcount_.put(type+"_",selInter.length);
		 showEndResult();
	 }
}

//返回最后的结果
function showEndResult(){
	var endResult = '';
	var keys=mapIcount_.keyset();
	for(var i=0;i<keys.length;i++){   
		var name = $('#label-'+keys[i]+'').html();
		var count = mapIcount_.get(keys[i]);
		if(count > 0) {
			endResult += '<tr><td>'+name+'</td>';
			endResult += '<td>'+mapIcount_.get(keys[i])+' </td>';
			endResult += '<td>'+mapEcount_.get(keys[i])+' </td>';
			endResult += '</tr>'
		}
	}  
	$("#showResult").html(endResult);
}
 
 
 function getEntityList(type,interfaceId){
	$.ajax({
        url:"${request.contextPath}/bigdata/api/apply/showEntityList?type="+type,
        data:{},
        async: false,
        dataType:'json',
        contentType:'application/json',
        type:'GET',
        success: function(data) {
			var array = data;
			var htmlStr = '';
			if(array.length > 0){
				$.each(array, function(index, json){
					htmlStr += '<tr><td><label for="l2-1-'+json.id+'" class="choice"><input type="checkbox" name="entity-checkbox" id="l2-1-'+json.id+'" value="'+json.id+'"  onclick= getChooseResult("'+type+'","'+interfaceId+'") ><span class="choice-name"></span></label></td>';
					htmlStr += '<td value="'+json.id+'">' + json.entityName + '</td>'
					htmlStr += '<td name="entity-displayName">' + json.displayName + '</td>';
					htmlStr += '<td>' + json.mandatory + '</td></tr>';
					htmlStr += '<input type="hidden" id="entityId_type" value="'+json.id+'"></input>';
				});
			}
			$("#showEntity").html(htmlStr);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    });
}
 

 
function getEndResult(type,interfaceId){
	   var selEle = $('#showEntity :checkbox:checked');
	   var htmlResult = "";
	   var array  = new Array();
	   if(selEle.length > 0) {
		   for(var i=0;i< selEle.length;i++){
			    var event = selEle.eq(i);
			    var entityId = event.parents('tr').next("#entityId_type").val();
			    array.push(entityId);
		   } 
	   }
	   mapEcount_.put(type+"_",selEle.length);
	   
	   
	   //得到页面中选中的应用
	    var appIds = new Array;
	    var selApType = $('#showApiApps :checkbox:checked');
	    if(selApType.length > 0) {
			   for(var i=0;i< selApType.length;i++){
				    var event = selApType.eq(i);
				    var appId = event.val();
				    appIds.push(appId);
			   } 
		}
	   
	   //统计数据
	   if(interfaceId && 'undefined' != interfaceId){
		   if($('#l1-'+interfaceId+'').prop('checked') === true ){
			   map_.put(interfaceId+"_",array); 
			   map_.put(interfaceId+"_app",appIds);
			   var keys=map_.keyset();
		   }else{
			   map_.remove(interfaceId+"_");
			   map_.remove(interfaceId+"_app");
			   $("#showEntity").html('');
		   }
	   }else if(type){
		   if($('#l1-'+type+'').prop('checked') === true ){
			   var selInter = $('.'+type+' :checkbox:checked');
			   for(var i=0;i<selInter.length;i++){
				  var interId = selInter.eq(i).val();
		          map_.put(interId+"_",array);  
		          map_.put(interId+"_app",appIds);
			   } 
		   }else{
			   var selInter = $('.'+type+' :checkbox');
			   for(var i=0;i<selInter.length;i++){
				  if (!selInter.eq(i).checked) {
					  var interId = selInter.eq(i).val();
			          map_.remove(interId+"_");
			          map_.remove(interId+"_app");
				  }
			   } 
			   $("#showEntity").html('');
		   }
	   }
}



 
function getMap(){//初始化map_，给map_对象增加方法，使map_像个Map  
    var map_=new Object();  
    //属性加个特殊字符，以区别方法名，统一加下划线_  
    map_.put=function(key,value){    map_[key]=value;}   
    map_.get=function(key){    return map_[key];}  
    map_.remove=function(key){    delete map_[key];}      
    map_.keyset=function(){  
        var ret="";  
        for(var p in map_){      
            if(typeof p =='string' && (p.substring(p.length-1)=="_" || p.substring(p.length-4)=="_app")){   
                ret+=",";  
                ret+=p;  
            }  
        }             
        if(ret==""){  
            return ret.split(","); //empty array  
        }else{  
            return ret.substring(1).split(",");   
        }  
    }     
    return map_;  
}
 
//提交申请属性
var isSubmit=false;
$('.btn-primary').on('click',function(){
	if(isSubmit){
	   return;
    }
    isSubmit = true;
    var keys=map_.keyset();
    if($.trim(keys).length<1){
    	showLayerTips4Confirm('error','请先选择接口和属性');
        isSubmit = false;
    }else{
	    var keys = map_.keyset();
		for(var i=0;i<keys.length;i++){   
	   		var key = keys[i];
	   		if(keys[i].indexOf("_app") != -1){
	   			key = keys[i].replace('_app','_');
	   		    var appIdArray =  map_.get(keys[i]);
	   		    if($.trim(appIdArray).length<1){
	   		        isSubmit = false;
	   		    }
	   		}
		}
		if(!isSubmit){
			showLayerTips4Confirm('error','请勾选对应的应用！');
		}
    }
    if(isSubmit){
        isSubmit = false;
        var passInterfaceDtos=new Array();
        var keys=map_.keyset();
	   	for(var i=0;i<keys.length;i++){   
	   		var interfaceDto=new Object();
	   		var key = keys[i];
	   		if(keys[i].indexOf("_app") != -1){
	   			interfaceDto.interfaceId = keys[i].replace('_app','');
	   			key = keys[i].replace('_app','_');
	   		    var appIdArray =  map_.get(keys[i]);
	   		    var appIds;
		   	    for(var j = 0; j < appIdArray.length ; j ++){
		   	    	if(j == 0){
		   	    		appIds = appIdArray[j];
		   	    	}else{
		   	    		appIds = appIds + ",";
		   	    		appIds = appIds + appIdArray[j];
		   	    	}
		   	    }
	   			interfaceDto.appIds = appIds;
	   			
	   		}else{
		        //interfaceDto.interfaceId = keys[i].replace('_','');
	   			continue;
	   		}
	        var entitys=new Array();
	   	    var entityIds =  map_.get(key);
	   	    for(var j = 0; j < entityIds.length ; j ++){
	   	    	var entityDto=new Object();       
	   	        entityDto.entityId=entityIds[j];
	            entitys.push(entityDto);
	   	    }
	   	    interfaceDto.entityDtos = entitys;
	   	    passInterfaceDtos.push(interfaceDto);
	   	}
 	    var params = {'passInterfaceDtos':JSON.stringify(passInterfaceDtos)};
        $.ajax({
          url:'${request.contextPath}/bigdata/api/apply/save',
          data:params,
          type:'post',  
          cache:false,  
          success:function(data){ 
            if(data.success){
                showLayerTips('success','保存成功!','t');
                var url = "${request.contextPath}/bigdata/api/apply/list?dataType=" + ${dataType!};
                $("#interDiv").load(url);
            }else{
                showLayerTips4Confirm('error',data.message);
            }
          }
        });
    }
})
 
 </script>      
      
      
