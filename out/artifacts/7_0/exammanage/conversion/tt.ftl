<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>成绩分析管理</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="../components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="../components/font-awesome/css/font-awesome.css" />

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="../components/layer/skin/layer.css">
		<link rel="stylesheet" href="../components/chosen/chosen.min.css">
		<link rel="stylesheet" href="../components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
        <link rel="stylesheet" href="../components/zTree/css/zTreeStyle.css">
		
		<link rel="stylesheet" href="../css/iconfont.css">
		<link rel="stylesheet" href="../css/components.css">
		<link rel="stylesheet" href="../css/page-desktop.css">
		<link rel="stylesheet" href="../css/pages.css">
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="../css/pages-ie.css" />
		<![endif]-->
		
		<!-- inline styles related to this page -->
		
		<!-- ace settings handler -->
		<script src="../assets/js/ace-extra.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
		<!--[if lte IE 8]>
			<script src="../components/html5shiv/dist/html5shiv.min.js"></script>
			<script src="../components/respond/dest/respond.min.js"></script>
		<![endif]-->
	</head>

	<body>
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default navbar-fixed-top">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container clearfix" id="navbar-container">		
				<div class="navbar-header">
					<!-- #section:basics/navbar.layout.brand -->
					<a href="#" class="navbar-brand">
						<img class="logo" src="../images/logo.png" alt="">
						<span>浙江万朋教育 · 数字校园</span>
					</a>
					<!-- /section:basics/navbar.layout.brand -->
				</div>
				<!-- #section:basics/navbar.dropdown -->			
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="#" class="js-dropbox-msg js-dropbox-toggle">
							<i class="wpfont icon-bell"></i>
							<span class="badge badge-yellow">5</span>
						</a>
						<div class="dropbox dropbox-msg">
							<div class="drapbox-container">
								<ul class="nav nav-tabs nav-tabs-1" role="tablist">
									<li role="presentation" class="active"><a href="#msg" role="tab" data-toggle="tab">消息</a></li>
									<li role="presentation"><a href="#post" role="tab" data-toggle="tab">公告</a></li>
									<li role="presentation"><a href="#other" role="tab" data-toggle="tab">其他</a></li>
								</ul>
								<!-- Tab panes -->
								<div class="tab-content">
									<div role="tabpanel" class="tab-pane active" id="msg">
										<ul class="msg-list">
											<li class="msg-item">
												<i class="msg-icon fa fa-comment"></i>
												<span class="msg-text">这里可以放下十六个字个字个字个字这里可以放下十六个字个字个字个字</span>
												<span class="msg-time">12-15</span>
											</li>
											<li class="msg-item">
												<i class="msg-icon wpfont icon-clock-fill"></i>
												<span class="msg-text">这里可以放下十六个字个字个字个字</span>
												<span class="msg-time">12-15</span>
											</li>
											<li class="msg-item">
												<i class="msg-icon fa fa-refresh"></i>
												<span class="msg-text">这里可以放下十六个字个字个字个字</span>
												<span class="msg-time">12-15</span>
											</li>
										</ul>
									</div>
									<div role="tabpanel" class="tab-pane" id="post">公告</div>
									<div role="tabpanel" class="tab-pane" id="other">其他</div>
								</div>
							</div>
						</div>
					</li>
					<li>
						<a class="js-dropbox-mailList js-dropbox-toggle" href="#">
							<i class="wpfont icon-directory"></i>
						</a>
						<div class="dropbox dropbox-mailList">
							<div class="dropbox-container">
								<ul class="nav nav-tabs nav-tabs-1" role="tablist">
									<li role="presentation" class="active">
										<a href="#tabpanel-mail" role="tab" data-toggle="tab">通讯录</a>
									</li>
								</ul>
								<!-- Tab panes -->
								<div class="tab-content">
									<div role="tabpanel" class="tab-pane active" id="tabpanel-mail">
										<div class="mail-search">
											<div class="input-group">
											    <input type="text" class="form-control">
											    <span class="input-group-addon"><i class="fa fa-search"></i></span>
											</div>
											<div class="mail-search-result">
												<ul class="mail-list">
													<li>
														<span>阮小天</span>
														<div class="mail-list-btns">
															<a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
															<a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
														</div>
													</li>
													<li>
														<span>阮小天</span>
														<div class="mail-list-btns">
															<a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
															<a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
														</div>
													</li>
												</ul>
											</div>
										</div>
										<ul class="nav nav-tabs nav-justified nav-tabs-1 nav-mail-list" role="tablist">
											<li role="presentation" class="active">
												<a href="#a" role="tab" data-toggle="tab">
													<i class="fa fa-users"></i>
												</a>
											</li>
											<li role="presentation">
												<a href="#b" role="tab" data-toggle="tab">
													<i class="fa fa-sitemap"></i>
												</a>
											</li>
										</ul>
										<!-- Tab panes -->
										<div class="tab-content">
											<div role="tabpanel" class="tab-pane active" id="a">
												<div class="panel-group" role="tablist" aria-multiselectable="true">
													<div class="panel panel01">
														<div class="panel-heading" role="tab">
															<h4 class="panel-title">
																<a data-toggle="collapse" href="#m01" aria-expanded="true" aria-controls="m01"><i class="fa fa-caret-right"></i>最近联系人</a>
															</h4>
														</div>
														<div id="m01" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
															<div class="panel-body">
																<ul class="mail-list">
																	<li>
																		<span>阮小天</span>
																		<div class="mail-list-btns">
																			<a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
																			<a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
																		</div>
																	</li>
																	<li>
																		<span>阮小天</span>
																		<div class="mail-list-btns">
																			<a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
																			<a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
																		</div>
																	</li>
																</ul>
															</div>
														</div>
													</div>
													<div class="panel panel01">
														<div class="panel-heading" role="tab">
															<h4 class="panel-title">
																<a data-toggle="collapse" href="#m02" aria-expanded="true" aria-controls="m02"><i class="fa fa-caret-right"></i>数学组</a>
															</h4>
														</div>
														<div id="m02" class="panel-collapse collapse" role="tabpanel">
															<div class="panel-body">
																<ul class="mail-list">
																	<li>
																		<span>阮小天</span>
																		<div class="mail-list-btns">
																			<a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
																			<a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
																		</div>
																	</li>
																</ul>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div role="tabpanel" class="tab-pane" id="b">公告</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</li>
					<li>
						<a class="js-dropbox-code js-dropbox-toggle" href="#">
							<i class="wpfont icon-qr-code"></i>
						</a>
						<div class="dropbox dropbox-code">
							<div class="dropbox-container text-center">
								<p>手机扫码下载移动OA</p>
								<img src="../images/qrcode.png" alt="">
							</div>
						</div>
					</li>
					<li>
						<a class="js-dropbox-setting js-dropbox-toggle" href="#">
							<i class="wpfont icon-cog-fill"></i>
						</a>
						<div class="dropbox dropbox-setting">
							<div class="list-group">
								<a class="list-group-item" href="#">统计图</a>
								<a class="list-group-item js-common-module-set" href="#">设置常用操作</a>
								<a class="list-group-item" href="#">备用功能链接二</a>
								<a class="list-group-item" href="#">备用功能链接三</a>
							</div>
						</div>	
					</li>                   
					<!-- #section:basics/navbar.user_menu -->
					<li class="navbar-item-user">
						<a href="#" class="js-dropbox-user js-dropbox-toggle">
							<img class="nav-user-photo" src="../images/user.png" alt="Jason's Photo" />
							<span class="user-info">阮小天</span>
							<i class="wpfont icon-caret-down"></i>
						</a>
						<div class="dropbox dropbox-user">
							<div class="drapbox-container">
								<div class="user-card">
									<a class="user-card-img" href="#">
										<img src="../images/user-photo.png" alt="...">
									</a>
									<div class="user-card-body">
										<a class="user-sign-in" href="javascript:void(0);">签到</a>
										<h4 class="user-card-name">阮小天</h4>
										<p class="user-login-lastTime">上次登录时间：2016-02-24 14:35</p>
									</div>
								</div>
								<div class="user-role-list">
									<table class="table table-noborder">
										<tbody>
											<tr>
												<td>阮小天ruanxiaort</td>
												<td>教师</td>
												<td><a href="">切换</a></td>
											</tr>
											<tr>
												<td>ruanxiaort</td>
												<td>学生</td>
												<td><a href="">切换</a></td>
											</tr>
											<tr>
												<td>阮小天</td>
												<td>家长</td>
												<td><a href="">切换</a></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="btn-group btn-group-justified dropbox-user-btns">
									<div class="btn-group">
										<a class="btn btn-link js-user-setting">个人设置</a>
									</div>
									<div class="btn-group">
										<a class="btn btn-link">退出</a>
									</div>
								</div>
							</div>
						</div>
					</li>
					<!-- /section:basics/navbar.user_menu -->
				</ul>
				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>
		<!-- /section:basics/navbar.layout -->
		
		<div class="main-container" id="main-container">
			
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			
			<!-- #section:basics/sidebar -->
			<div id="sidebar" class="sidebar responsive sidebar-fixed">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>
				<div class="sidebar-shortcuts">
					<button class="btn btn-block btn-tohome">
						<i class="wpfont icon-home-fill"></i>
						<span class="btn-tohome-text">主页</span>
					</button>
				</div>
				<div class="nav-wrap js-nav-wrap">
					<div class="common-module">
						<h4 class="common-module-name">常用操作</h4>
						<ul class="nav nav-list">
							<li class="active">
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon01-sm.png" alt="">
									<span class="menu-text">学科模块管理</span>
								</a>
							</li>

							<li>
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon02-sm.png" alt="">
									<span class="menu-text">课程开设</span>
								</a>
							</li>

							<li>
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon03-sm.png" alt="">
									<span class="menu-text">课程表维护</span>
								</a>
							</li>

							<li>
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon04-sm.png" alt="">
									<span class="menu-text">选修课设置</span>
								</a>
							</li>

							<li>
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon05-sm.png" alt="">
									<span class="menu-text">教师课程表</span>
								</a>
							</li>

							<li>
								<a href="#">
									<img class="menu-icon" src="../images/icons/app-icon/icon06-sm.png" alt="">
									<span class="menu-text">高考成绩分析</span>
								</a>
							</li>
						</ul>
					</div>
					<ul class="nav nav-list">
						<li>
							<a href="#" data-show="a">
								<img class="menu-icon" src="../images/icons/app-icon/icon07-sm.png" alt="">
								<span class="menu-text">学科模块管理</span>
							</a>
								
						</li>

						<li>
							<a href="#" data-show="b">
								<img class="menu-icon" src="../images/icons/app-icon/icon08-sm.png" alt="">
								<span class="menu-text">课程开设</span>
							</a>
							
						</li>

						<li>
							<a href="#" data-show="c">
								<img class="menu-icon" src="../images/icons/app-icon/icon01-sm.png" alt="">
								<span class="menu-text">课程表维护</span>
							</a>
						</li>

						<li>
							<a href="#" data-show="d">
								<img class="menu-icon" src="../images/icons/app-icon/icon02-sm.png" alt="">
								<span class="menu-text">选修课设置</span>
							</a>
						</li>

						<li>
							<a href="#" data-show="e">
								<img class="menu-icon" src="../images/icons/app-icon/icon03-sm.png" alt="">
								<span class="menu-text">教师课程表</span>
							</a>
						</li>

						<li>
							<a href="#" data-show="f">
								<img class="menu-icon" src="../images/icons/app-icon/icon04-sm.png" alt="">
								<span class="menu-text">高考成绩分析</span>
							</a>
						</li>
					</ul>
				</div>
				<div class="subNav-modal js-subNav-modal">
					<div class="subNav-modal-container">
						
					</div>
				</div>
				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse js-siderbar-collapse" id="sidebar-collapse">
					<i class="wpfont icon-angle-double-left"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>
			<!-- /section:basics/sidebar -->

			<div class="main-content">
				<div class="main-content-inner">
					<ol class="breadcrumb">
						<li><a href="#">成绩分析</a></li>
						<li class="active">学生个体分析-纵向分析</li>
					</ol>
					<div class="page-content">   
						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="pos-abs" style="width: 250px;">
									<div class="box box-default">
										<div class="box-body">
											<div class="accordionContainer">
												<div class="panel-group">
						                            <div class="panel panel-default">
						                                <div class="panel-collapse in">
						                                    <ul id="tree" class="ztree"></ul>
						                                </div>
						                            </div>
						                        </div>
											</div>
										</div>
									</div>
								</div>
								<div class="row" style="margin-left: 260px;">
									<div class="col-xs-12">
										<div class="box box-default">
										    <div class="box-body">
										    	<div class="filter">
													<div class="filter-item">
														<span class="filter-name">学年：</span>
														<div class="filter-content">
															<select class="form-control"></select>
														</div>
													</div>
													<div class="filter-item">
														<span class="filter-name">学期：</span>
														<div class="filter-content">
															<select class="form-control"></select>
														</div>
													</div>
													<div class="filter-item filter-item-right">
														<button class="btn btn-blue" disabled="disabled">导出</button>
													</div>
													<div class="filter-item filter-item-right">
														<div class="btn-group" role="group">
															<a type="button" class="btn btn-blue" href="#">报表</a>
															<a type="button" class="btn btn-white" href="#">图表</a>
														</div>
													</div>
												</div>
												<div>
													<div class="filter">
														<div class="filter-item">
															<label><input type="radio" name="layer-score-3" checked="" class="wp"><span class="lbl"> 考试分</span></label>
														</div>
														<div class="filter-item">
															<label><input type="radio" name="layer-score-3" class="wp"><span class="lbl"> 赋分</span></label>
														</div>
													</div>
													<div id="mychart02" style="height:400px;"></div>
												</div>
												<div>
													<div class="table-container">
														<div class="table-container-body" style="overflow-x: auto;">
															<table class="table table-bordered table-striped table-hover">
																<thead>
																	<tr>
																		<th>学科</th>
																		<th>维度</th>
																		<th>考试1</th>
																		<th>考试2</th>
																		<th>考试3</th>
																		<th>考试4</th>
																	</tr>
																</thead>
																<tbody>
													 				<tr>
																	    <th rowspan="3">语文</th>
																	    <th>成绩</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																	<tr>
																	    <th>班级排名</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																	<tr>
																	    <th>年级排名</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																	<tr>
																	    <th rowspan="3">数学</th>
																	    <th>成绩</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																	<tr>
																	    <th>班级排名</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																	<tr>
																	    <th>年级排名</th>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	    <td>90</td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>
													<div class="no-data-container">
														<div class="no-data">
															<span class="no-data-img">
																<img src="../images/public/nodata6.png" alt="">
															</span>
															<div class="no-data-body">
																<p class="no-data-txt">暂无记录</p>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script src="../components/jquery/dist/jquery.js"></script>
		<!-- <![endif]-->
		<!--[if IE]>
		<script src="../components/jquery.1x/dist/jquery.js"></script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='../components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="../components/bootstrap/dist/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="../components/layer/layer.js"></script>
		<script src="../components/zTree/js/jquery.ztree.core.js"></script>
		<script src="../components/echarts/echarts.min.js"></script>
		<script src="../components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
		<script src="../components/chosen/chosen.jquery.min.js"></script>
		<script src="../components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
		<script src="../components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

		<script src="../js/desktop.js"></script>
		<!-- inline scripts related to this page -->
		<script type="text/javascript">
	        // 基于准备好的dom，初始化echarts实例
	        var myChart = echarts.init(document.getElementById('mychart02'));

	        // 指定图表的配置项和数据
	        var option = {
			    tooltip: {
			        trigger: 'axis'
			    },
			    legend: {
			        data:['语文','英语','数学','物理','合计']
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis: {
			        type: 'category',
			        boundaryGap: false,
			        data: ['2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3','2018-2019 第一学期入学考试3'].map(function (str) {
		                return str.replace(' ', '\n')
		            })
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: [
			        {
			            name:'语文',
			            type:'line',
			            data:[1.2, 1.3, 1.0, 1.3, 0.9, 2.3, 2.1]
			        },
			        {
			            name:'英语',
			            type:'line',
			            data:[2.2, 1.8, 1.9, 2.3, 2.9, 3.3, 3.1]
			        },
			        {
			            name:'数学',
			            type:'line',
			            data:[1.5, 2.3, 2.0, 1.5, 1.9, 3.3, 4.1]
			        },
			        {
			            name:'物理',
			            type:'line',
			            data:[3.2, 3.3, 3.0, 3.3, 3.9, 3.3, 3.2]
			        },
			        {
			            name:'合计',
			            type:'line',
			            data:[8.2, 9.3, 9.0, 9.3, 1.2, 1.3, 1.3]
			        }
			    ]
			};
	        // 使用刚指定的配置项和数据显示图表。
	        myChart.setOption(option);
	    </script>
	    <script type="text/javascript">
			var zTree;
			var demoIframe;
		
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
				callback: {
					beforeClick: function(treeId, treeNode) {
						var zTree = $.fn.zTree.getZTreeObj("tree");
						if (treeNode.isParent) {
							zTree.expandNode(treeNode);
							return false;
						} else {
							demoIframe.attr("src",treeNode.file + ".html");
							return true;
						}
					}
				}
			};
		
			var zNodes =[
				{"id":1, "pId":0, "name":"test1"},   
		        {"id":11, "pId":1, "name":"test11"},   
		        {"id":12, "pId":1, "name":"test12"},   
		        {"id":111, "pId":11, "name":"test111"},   
			];
		
			$(document).ready(function(){
				var t = $("#tree");
				t = $.fn.zTree.init(t, setting, zNodes);
				demoIframe = $("#testIframe");
				demoIframe.bind("load", loadReady);
				var zTree = $.fn.zTree.getZTreeObj("tree");
				zTree.selectNode(zTree.getNodeByParam("id", 101));
		
			});
	    </script>

		
	</body>
</html>
