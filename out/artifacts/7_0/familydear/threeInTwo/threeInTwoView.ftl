
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div id="myDiv">
    <form id="subForm" >
        
        <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="box box-default">

                    <div class="box-body">
                       <div class="form-horizontal mt20">
		                   <div class="form-group">
		                       <label class="col-sm-2 control-label no-padding-right">年度：</label>
		                       <div class="col-sm-3">
		                           <select class="form-control" id="year" name="year" disabled="disabled">
							           <#if acadyearList?exists && (acadyearList?size>0)>
							               <#list acadyearList as item>
							                   <option value="${item!}" <#if year?default(nowYear)==item?default('b')>selected</#if>>${item!}</option>
							               </#list>
							           </#if>
							       </select>
	                       		</div>
	                       </div>
	                       <div class="form-group">
	                       	<label class="col-sm-2 control-label no-padding-right">标题：</label>
	                          	<div class="col-sm-3">
	                           	<input type="text" class="form-control" id="title" name="title" disabled="disabled" value="${famDearThreeInTwo.title!}" maxlength="200" style="width:100%;"/>
	                       	</div>
	                       </div>
	                       <div class="form-group">
	                           <label class="col-sm-2 control-label no-padding-right">内容：</label>
	                           <div class="col-sm-8">
	                           	<textarea class="form-control" cols="30" rows="5" maxlength="2000" disabled="disabled" id="content" name="content"   style="width:100%;">${famDearThreeInTwo.content!}</textarea>
	                           </div>
	                       </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">图片：</label>
                                <div class="col-sm-8">
                                    <div class="clearfix">
                                    	<div id="ticketImages" class="js-layer-photos">
											<#if actDetails?exists&& (actDetails?size > 0)>
											    <#list actDetails as ad>
											        <span class="position-relative float-left mr10 mb10">
													<a class="pull-left">
											             <img id ="" style="width: 94px;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/threeInTwo/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/familydear/threeInTwo/showPic?id=${ad.id!}&showOrigin=0" alt="">
											         </a>
											        </span>
											    </#list>
											</#if>
                                    	</div>
										<a href="javascript:void(0);" class="form-file pull-left mb10" id="ticket">
											<i class="fa fa-plus"></i>
										</a>
									</div>
								<div class="color-999"><i class="fa fa-exclamation-circle color-yellow"></i> 尺寸建议：178*108 ，图片大小不得超过10M</div>
                           		</div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right">文件：</label>
                                <div class="col-sm-8">
                                    <p></p>
									<div id="desFile">
										<#if fileAtts?exists&& (fileAtts?size > 0)>
											    <#list fileAtts as ad>
											        <div><span>${ad.filename!}</span><a class="color-blue ml10" target="_blank" href="${request.contextPath}/familydear/threeInTwo/downFile?id=${ad.id!}&showOrigin=1">下载</a><span class="color-blue ml10"></span></div>
											    </#list>
											</#if>
  									</div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label no-padding-right"></label>
                                <div class="col-sm-8">
                                    <a class="btn btn-blue" onclick="goBack();" href="javascript:;">返回</a>
                                </div>
                            </div>
                      </div>

                    </div>
                </div>
            </div>
        </div>
    	</div>
        
    </form>
</div>
<script>

	$(function () {
	     layer.photos({
	     	closeBtn:1,
	         shade: .6,
	         photos:'.js-layer-photos',
	         shift:1
	     });
    });

	function goBack(){
		url = "${request.contextPath}/familydear/threeInTwo/edu/index/page";
		$(".model-div-show").load(url);
	}
	
</script>