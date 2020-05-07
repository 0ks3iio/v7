                    <!-- PAGE CONTENT BEGINS -->
                    <!-- S 面包屑导航 -->
                    <ol class="breadcrumb">
                        <li>授权管理</li>
                        <li><a href="javascript:void(0);" onclick="moduleContentLoad('${request.contextPath}/system/userPerm/index?tabCode=rolePerm')">用户组授权 </a></li>
                        <li class="active">${breadcrumbName!}</li>
                    </ol><!-- E 面包屑导航 -->
                    
                    <div class="box box-default">
                        <div class="box-body">
                            <div role="tabpanel" class="tab-pane active" id="aa">
                                <form class="form-horizontal margin-10" role="form">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding" for="form-field-1">用户组信息</label>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-1">用户组名称</label>
                                        <div class="col-sm-6">
                                            <input type="text" id="form-field-1" maxLength="20" value="${role.name! }" placeholder="" class="form-control">
                                            <span class="control-disabled">不超过10个汉字</span>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-2">用户组成员</label>

                                        <div class="col-sm-6">
                                            <select multiple value="" class="form-control chosen-select" data-placeholder="未选择" id="roleUserSelect">
                                                <option value=""></option>
                                                <#if userList?exists && userList?size gt 0>
                                                    <#list userList as user>
                                                        <option value="${user.id! }">${user.realName! }</option>
                                                    </#list>
                                                </#if>
                                            </select> 
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-3">备注</label>

                                        <div class="col-sm-6">
                                            <input type="text" id="form-field-3" value="${role.description! }" maxLength="40" placeholder="备注" class="form-control">
                                            <span class="control-disabled">不超过20个汉字</span>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding" for="form-field-1">权限设置</label>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-title no-padding" for="form-field-1"></label>
                                        <div class="col-sm-6">
                                            <div class="authorization-title clearfix" id="treeBtnDiv">
                                                <label class="inline">
                                                    <input class="wp" type="checkbox" id="selectAll">
                                                    <span class="lbl" > 全选</span>
                                                </label>
                                            </div>
                                            <div class="authorization-body">
                                                <ul id="tree" class="ztree" style=" height:400px; overflow:auto;"></ul>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
                                        <div class="col-sm-6">
                                            <button type="button" class="btn btn-blue" id="saveBtn">&nbsp;保存&nbsp;</button>
                                            <button type="button" class="btn btn-white" id="cancelBtn">&nbsp;取消&nbsp;</button>
                                        </div>
                                        <div class="col-sm-4 control-tips">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            
                        </div>
                    </div>
                    <!-- PAGE CONTENT ENDS -->
        <input type="hidden" value="${role.id! }" id="roleId"/>
        <script>
            $(function(){
              if($('.chosen-select')){
                  //$('.chosen-select').chosen({
                   //   allow_single_deselect:true,
                  //    disable_search_threshold: 10
                 // }); 
                  //resize the chosen on window resize
    
                  $(window)
                  .off('resize.chosen')
                  .on('resize.chosen', function() {
                      $('.chosen-select').each(function() {
                          var $this = $(this);
                          $this.next().css({'width': $this.width()});
                      })
                  }).trigger('resize.chosen');
              }
              $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                  $(window).trigger('resize.chosen')
              })
              
              
              <#if memberUserIndexs?exists && memberUserIndexs?size gt 0>
                 <#list memberUserIndexs as index>
                   var index = ${index};
                  // var $choseObj = $(".chosen-results li:eq("+index+")");
                   var name = "test"+index;
                   
                   var optionIndex = index+1;
                   //$choseObj.attr("class","result-selected");
                   var chosenHtml = '<li class="search-choice"><span>'+name+'</span><a class="search-choice-close" data-option-array-index="'+optionIndex+'"></a></li>';
                   $(".chosen-choices .search-field input").removeClass("default");
                   $(".chosen-choices .search-field input").attr("style",'width: 25px; font-family: \"Microsoft Yahei\",\"Segoe UI Emoji\",\"Segoe UI Symbol\", Symbola, EmojiSymbols !important;');
                   $(".chosen-choices .search-field input").data("emoji_font",true);
                   
                   $(".chosen-choices .search-field").before(chosenHtml);
                 </#list>
              </#if>
              
              chose_mult_set_ini('.chosen-select');
              //初始化
              $(".chosen-select").chosen();
              
          });
            
            
          function chose_mult_set_ini(select) {
            <#if memberUsers?exists && memberUsers?size gt 0>
              <#list memberUsers as memberUserId>
                 var userId = "";
                 $(select + " option[value='" + "${memberUserId}" + "']").attr('selected', 'selected');
              </#list>
              $(select).trigger("liszt:updated");
            </#if>
          }
        </script>
        <script type="text/javascript" >
            var zTree;
            var setting = {
                view: {
                    dblClickExpand: false,
                    showLine: true,
                    selectedMulti: false
                },
                data: {
                    simpleData: {
                        enable:true,
                        idKey: "id",
                        pIdKey: "pId",
                        rootPId: ""
                    }
                },
                check: {
                  autoCheckTrigger: true,
                  chkboxType: { "Y": "ps", "N": "ps" },
                  chkStyle:"checkbox",
                  enable:true
                },
                callback: {
                  onCheck: function(){
                    //选中节点数
                    var nodes = zTree.getCheckedNodes(true);
                    $("#treeBtnDiv button").removeClass("btn-grey").removeClass("btn-blue");
                    if(nodes.length>0){
                      $("#treeBtnDiv button").addClass("btn-blue");
                    }else if(nodes.length==0){
                      $("#treeBtnDiv button").addClass("btn-grey");
                    }
                  }
                  
                }
            };
        
            $.ajax({
              url:"${request.contextPath}/system/role/modelZtree",
              data:{
                'roleId':'${role.id!}'
              },
              type:'post',
              dataType:'json',
              success:function(data){
                  if(data.success){
                      $.fn.zTree.init($("#tree"), setting, JSON.parse(data.msg));
                      zTree = $.fn.zTree.getZTreeObj("tree");
                      
                      var nodes = zTree.getCheckedNodes(true);//选中节点
                      if(nodes.length>0){
                        for(var i=0;i<nodes.length;i++){
                          zTree.checkNode(nodes[i], true, true);
                        }
                      }
                      
                      //清空按钮初始化
                      if(nodes.length>0){
                        $("#treeBtnDiv").prepend('<button type="button" id="clearBtn" class="btn btn-blue pull-right">清空</button>');
                      }else if(nodes.length==0){
                        $("#treeBtnDiv").prepend('<button type="button" id="clearBtn"  class="btn btn-grey pull-right">清空</button>');
                      }
                      //清空按钮点击事件
                      $("#clearBtn").on("click",function(){
                        if($("#clearBtn").hasClass("btn-grey")){
                          return false;
                        }else if($("#clearBtn").hasClass("btn-blue")){
                          $("#clearBtn").removeClass("btn-blue").addClass("btn-grey");
                          $("#selectAll").prop("checked", false);
                          zTree.checkAllNodes(false);
                        }
                      });
                  }
              }
          });
            
          //全选
          $("#selectAll").on("click", function(){
            zTree.checkAllNodes($(this).prop("checked"));
            $("#treeBtnDiv button").removeClass("btn-grey").removeClass("btn-blue");
            if($(this).prop("checked")){
              $("#treeBtnDiv button").addClass("btn-blue");
            }else{
              $("#treeBtnDiv button").addClass("btn-grey");
            }
          })
          
          //用户组名称检测
          $("#form-field-1").blur(function(){
            var maxLength = $("#form-field-1").attr("maxLength");
            var value = $("#form-field-1").val();
            
            $("#form-field-1").parent().next().empty();
            if (value && value != "") {
              if (maxLength && maxLength > 0 && getLength(value) > maxLength) {
                $("#form-field-1").parent().next().append('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;用户组名称内容不能超过'+maxLength+'个字节（一个汉字为两个字节）</span>')
                return;
              }
              //检测用户组名称是否有重
              $.ajax({
                url:"${request.contextPath}/system/role/checkRoleName",
                data:{
                  'name':value,
                  'id':$("#roleId").val()
                },
                type:"post",
                dataType: "json",
                success:function(data){
                    $("#form-field-1").parent().next().empty();
                    if(!data.success){
                      $("#form-field-1").parent().next().append('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;'+data.msg+'</span>')
                    }else{
                      $("#form-field-1").parent().next().append('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;正确</span>');
                    }
                }
              });
            }
          });
          
          //备注检测
          $("#form-field-3").blur(function(){
            var maxLength = $("#form-field-3").attr("maxLength");
            var value = $("#form-field-3").val();
            
            $("#form-field-3").parent().next().empty();
            if (value && value != "") {
              if (maxLength && maxLength > 0 && getLength(value) > maxLength) {
                $("#form-field-3").parent().next().append('<span class="has-error"><i class="fa fa-times-circle"></i>&nbsp;备注内容不能超过'+maxLength+'个字节（一个汉字为两个字节）</span>')
                return;
              }else{
                $("#form-field-3").parent().next().append('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;正确</span>');
              }
            }
          });
          
          //保存
          $("#saveBtn").on("click",function(){
            //用户组名称
            var name = $("#form-field-1").val();
            if(name == ""){
              showMsgError("用户组名称不能为空","错误信息",function(index){
                layer.close(index);
              });
              return;
            }
            
            if($("#form-field-1").parent().next().find("span").hasClass("has-error") || $("#form-field-3").parent().next().find("span").hasClass("has-error")){
              showMsgError("提交数据有错误，请修改后再提交","错误信息",function(index){
                layer.close(index);
              });
              return;
            }
            
            
            //备注
            var description = $("#form-field-3").val();
            
            //用户组成员
            var userIdIndexAry = new Array();
            var userIds = "";
            if($(".chosen-choices .search-choice").length){
              $(".chosen-choices .search-choice").each(function(i,e){
                userIdIndexAry.push($(e).find(".search-choice-close").data("option-array-index"));
              });
              
              for(var i=0;i<userIdIndexAry.length;i++){
                userIds += $("#roleUserSelect option:eq("+userIdIndexAry[i]+")").val()+",";
              }
              userIds = userIds.substring(0, userIds.length-1);
            }
            
            //所选模块
            var modelIds="";
            var nodes = zTree.getCheckedNodes(true);//选中节点
            if(nodes.length>0){
              for(var i=0;i<nodes.length;i++){
                var nodeObj = nodes[i];
                if(!nodeObj.isParent && nodeObj.getParentNode()){
                  modelIds += nodeObj.id + ",";
                }
              }
              modelIds = modelIds.substring(0, modelIds.length-1);
            }
            
            var allModelIds="";
            var allNodes = zTree.transformToArray(zTree.getNodes());
            if(allNodes.length>0){
              for(var i=0;i<allNodes.length;i++){
                allModelIds += allNodes[i].id+",";
              }
              allModelIds = allModelIds.substring(0, allModelIds.length-1);
            }
            
            $.ajax({
              url:"${request.contextPath}/system/role/saveRole",
              data:{
                'roleId':$("#roleId").val(),
                'name':name,
                'description':description,
                'userIds':userIds,
                'modelIds':modelIds,
                'allModelIds':allModelIds
              },
              type: "post",
              dataType: "json",
              success:function(data){
                  if(data.success){
                    layer.alert(data.msg,{},function(slayer){
                      layer.close(slayer);
                      moduleContentLoad("${request.contextPath}/system/userPerm/index?tabCode=rolePerm");
                    });
                  }
              }
            });
            
          });
          
          //取消按钮
          $("#cancelBtn").on("click",function(){
            moduleContentLoad("${request.contextPath}/system/userPerm/index?tabCode=rolePerm");
          });
    
        </script>
    </body>
</html>
