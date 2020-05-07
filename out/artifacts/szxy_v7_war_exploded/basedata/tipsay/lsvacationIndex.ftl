<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
			<div class="main-content">
				<div class="main-content-inner">
					<div class="page-content">	
						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="box box-default">
									<div class="box-header">
										<div class="filter">
											<div class="filter-item">
												<span class="filter-name">年级：</span>
												<div class="filter-content">
													<select class="form-control">
														<option>高一</option>
														<option>高二</option>
														<option>高三</option>
													</select>
												</div>
											</div>
										    <div class="filter-item filter-item-right">
												<button class="btn btn-blue" onclick="setChoiceDefault()">保存</button>
											</div>
										</div>
									</div>
									<div class="box-body">
										<table class="table table-bordered table-striped table-hover">
											<thead>
												<tr>
													<th width="16%">日期</th>
													<th width="16%">周次</th>
													<th width="16%">星期</th>
													<th width="16%">是否工作日</th>
													<th width="18%">节假日名称</th>
													<th width="18%">调课时间</th>
												</tr>
											</thead>
											<tbody>
												<tr>
        <td>2018-06-15</td>
        <td>16</td>
        <td>5</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-16</td>
        <td>16</td>
        <td>6</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-17</td>
        <td>16</td>
        <td>7</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-18</td>
        <td>17</td>
        <td>1</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-19</td>
        <td>17</td>
        <td>2</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-20</td>
        <td>17</td>
        <td>3</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-21</td>
        <td>17</td>
        <td>4</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-22</td>
        <td>17</td>
        <td>5</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-23</td>
        <td>17</td>
        <td>6</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-24</td>
        <td>17</td>
        <td>7</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-25</td>
        <td>18</td>
        <td>1</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>



<tr>
        <td>2018-06-26</td>
        <td>18</td>
        <td>2</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-27</td>
        <td>18</td>
        <td>3</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-28</td>
        <td>18</td>
        <td>4</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-29</td>
        <td>18</td>
        <td>5</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-06-30</td>
        <td>18</td>
        <td>6</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-07-01</td>
        <td>18</td>
        <td>7</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-07-02</td>
        <td>19</td>
        <td>1</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-07-03</td>
        <td>19</td>
        <td>2</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
<tr>
        <td>2018-07-04</td>
        <td>19</td>
        <td>3</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-05</td>
        <td>19</td>
        <td>4</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-06</td>
        <td>19</td>
        <td>5</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-07</td>
        <td>19</td>
        <td>6</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-08</td>
        <td>19</td>
        <td>7</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock">
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-09</td>
        <td>20</td>
        <td>1</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr><tr>
        <td>2018-07-10</td>
        <td>20</td>
        <td>2</td>
        <td>
        <label>
        <input type="checkbox" class="wp wp-switch js-toggleLock" checked>
        <span class="lbl"></span>
        </label>
        </td>
        <td>
        <input type="text" class="form-control" placeholder="">
        </td>
        <td>
        <div class="input-group">
        <input class="form-control date-picker" type="text">
        <span class="input-group-addon">
        <i class="fa fa-calendar"></i>
        </span>
        </div>
        </td>
</tr>
											</tbody>
										</table>
									</div>
								</div>
								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->
		<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
		<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

		<script type="text/javascript">
			$(function(){
				$('.js-toggleLock').on('change', function(){
		    		if($(this).prop('checked') === true){
		    			$(this).closest('.table').find('.date-picker').attr('disabled', false)
		    		}else{
		    			$(this).closest('.table').find('.date-picker').attr('disabled', true)
		    		}
		    	})
                // 时间
                $('.date-picker').datepicker({
					language: 'zh-CN',
					autoclose: true,
					todayHighlight: true,
					format: 'yyyy-mm-dd'
				})
				//show datepicker when clicking on the icon
				.next().on('click', function(){
					$(this).prev().focus();
				});
                var _this = $('.date-picker').siblings('.input-group-addon');
					_this.on('click', function(){
						$(this).prev().focus();
					}); 
					 
            })
            
         //设置默认值
		function setChoiceDefault(){
			layer.msg("保存成功！", {offset: 't',time: 2000});
		}
            
		</script>

