<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="box box-default">
					<div class="box-header">
						<h4 class="box-title">2017年浙江省第一次五校联考通知单</h4>
					</div>
					<div class="box-body">
						<div class="filter">
							<div class="filter-item">
								<span class="filter-name">年级：</span>
								<div class="filter-content">
									<select name="" id="" class="form-control">
										<option value=""></option>
										<option value=""></option>
										<option value=""></option>
									</select>
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">班级：</span>
								<div class="filter-content">
									<select name="" id="" class="form-control">
										<option value=""></option>
										<option value=""></option>
										<option value=""></option>
									</select>
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">考生姓名：</span>
								<div class="filter-content">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">准考证号：</span>
								<div class="filter-content">
									<input type="text" class="form-control">
								</div>
							</div>
							<div class="filter-item filter-item-right">
								<button class="btn btn-blue">导出</button>
							</div>
						</div>

						<div class="table-container">
							<div class="table-container-body">
								<table id="example" class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>年级</th>
											<th>班级</th>
											<th>考生姓名</th>
											<th>准考证号</th>
											<th>语文考试场地</th>
											<th>语文考试场号</th>
											<th>数学考试场地</th>
											<th>物理考试场号</th>
											<th>物理考试场地</th>
											<th>物理考试场号</th>
											<th>语数外考试场地</th>
											<th>语数外考试场号</th>
											<th>物理考试场地</th>
											<th>物理考试场号</th>
											<th>物理考试场地</th>
											<th>物理考试场号</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>高一</td>
											<td>02班</td>
											<td>赛一朵</td>
											<td>7160809</td>
											<td>群星楼101</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>群星楼101</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
										</tr>
										<tr>
											<td>高一</td>
											<td>02班</td>
											<td>赛一朵</td>
											<td>群星楼101</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>7160809</td>
											<td>群星楼101</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
											<td>群星楼103</td>
											<td>0106</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->
<script>
	$(function(){
		var table = $('#example').DataTable( {
	        scrollY: "300px",
			info: false,
			searching: false,
			autoWidth: false,
	        scrollX: true,
	        sort: false,
	        scrollCollapse: true,
	        paging: false,
	        fixedColumns: {
	            leftColumns: 4
	        }
	    } );
	})
</script>
