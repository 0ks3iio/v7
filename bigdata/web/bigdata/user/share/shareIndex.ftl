<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<div  id="share-dim-div" class="filter-item active"  onclick="showList(1);">
        <span>我分享的资源</span>
    </div>
    <div id="be-share-rule-div" class="filter-item" onclick="showList(2);">
        <span>分享给我的资源</span>
    </div>
</div>
<div class="box box-default clearfix no-margin" id="contentDiv"></div>
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css"/>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="share-div">
    <div class="form-horizontal" id="myForm-three">
        <div class="form-group">
            <div class="col-sm-7">
                <p class="choose-num">选择分享用户&nbsp;</p>
                <!--<p class="no-padding-right bold">选择授权对象&nbsp;</p>　-->
                <div class="bs-callout bs-callout-danger">
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#aa" data-toggle="tab">本单位用户</a>
                        </li>
                    </ul>
                    <div class="tab-content tree-wrap tree-tab width-1of1">
                        <div class="row no-margin">
                            <div class="filter-item col-sm-8 no-margin-right">
                                <div class="pos-rel pull-left width-1of2">
                                    <input id="tree_search_keywords" type="text"
                                           class="typeahead scrollable form-control width-1of1" autocomplete="off"
                                           data-provide="typeahead" placeholder="请输入关键词">
                                </div>
                                <div class="input-group-btn">
                                    <button id="tree_search" type="button" class="btn btn-default">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-sm-2 switch-button text-right">
                                <span>是否级联:</span>
                            </div>
                            <div class="col-sm-2 switch-button">
                                <input id="tree_cascade" name="switch-field-1" class="wp wp-switch" type="checkbox">
                                <span class="lbl"></span>
                            </div>
                        </div>
                        <div class="tab-pane active" id="aa">
                            <ul id="treeDemo-one" class="ztree"></ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-sm-5">
                <div class="bs-callout bs-callout-danger">
                    <p class="choose-num">已选（<span>0</span>）</p>
                    <div class="choose-item">
                        <div class="no-data">
                            <#--<img src="../images/big-data/no-choice.png"/>-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
	function showList(type){
		if(!type)
			type=$("#type").val();
	    if(type ==1){
	    	$('#share-dim-div').addClass('active').siblings().removeClass('active');			
	    }else if (type == 2){
	    	$('#be-share-rule-div').addClass('active').siblings().removeClass('active');
	    }
		$("#type").val(type);
		var url =  "${request.contextPath}/bigdata/share/list?type="+type;
		$("#contentDiv").load(url);
	}

	$(document).ready(function(){
		showList(${type?default(1)});
	});
</script>