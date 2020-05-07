<div class="box-header clearfix">
    <div class="filter-made">
		<div class="filter-item">
        	<div class="form-group">
        		<label for="c2">是否记录问题数据:</label>
                <label class="switch" for="isSaveDetail">
                    <input type="checkbox" id="isSaveDetail" name="switch-field-1" />
                 	<span class="switch-name">显示</span>
            	</label>
    		</div>
		</div>
    	<div class="filter-item filter-item-right clearfix">
            <button class="btn btn-blue" onclick="stat();">数据统计</button>
            <button class="btn btn-default" onclick="deleteStat();">清除统计结果</button>
        </div>
    </div>
</div> 
	<#if resultList?exists && resultList?size gt 0>
            <div class="box-body height-calc-32">
                            <div class="wrap-full scrollBar4">
			<table class="tables">
			    <thead>
			        <tr> 
			            <th>统计时间</th>
			            <th>
			            	质量指数&nbsp;
			            	<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip" data-placement="right" title="由各个维度（权重*正确记录数的占比）计算而来">
			            </th>
			            <th>
			             	质量等级&nbsp;
			             	<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip" data-placement="right" title="优秀(>=90),良好(>=80),中等(>=70),及格(>=60),差(>=30),极差(<30)">
						</th>
			             <th>
			             	趋势(%)&nbsp;
			             	<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip" data-placement="right" title="和上次统计对比">
						</th>
			             <th>操作</th>
			        </tr>
			    </thead>
			    <tbody class="kanban-content">
			    	<#list resultList as result>
			    	<tr>
				         <td>${result.statTime!}</td>
				         <td>${result.result!}</td>
				         <td>${result.grade!}</td>         
				         <td><#if result.trend?default(0) gt 0><span style="color:red">${result.trend?default(0)}<i class="wpfont icon-arrow-up"></span><#elseif result.trend?default(0) ==0>未变化<#else><span style="color:green">${result.trend?abs?default(0)}<i class="wpfont icon-arrow-down"></span></#if></td>
				  		<td>
							<#if result.status?default(1) ==1 && result.isSaveDetail?default(0) ==1 >
							<a href="javascript:;" class="look-over js-log-show" onclick="exportData();">导出明细记录</a>
							</#if>
						</td>
				  	</tr>
				  	</#list>
			    </tbody> 
			</table>
			</div>
			</div>
	<#else>
	<div class="no-data-common">
		<div class="text-center">
			<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
			<p class="color-999">
				暂无统计结果
			</p>
		</div>
	</div>
	</#if>
<script>
	$(document).ready(function(){
		$('[data-toggle="tooltip"]').tooltip();
	});
	
  	function stat() {
        showConfirmTips('warn',"提示","您确定要统计吗（统计可能需要一段时间，请务必不要关闭页面）",function(){
       		layer.closeAll();
       		var qualityStatIndex=layer.msg('数据统计中,请不要关闭页面......', {
			      icon: 16,
			      time:0,
			      shade: 0.01
		    });
			var saveDetail = $('#isSaveDetail').is(':checked') ? 1 : 0;
			$.ajax({
		            url: '${request.contextPath}/bigdata/setting/quality/stat/computer',
		            data:{
						'saveDetail':saveDetail
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
				 		layer.close(qualityStatIndex);
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 			showLayerTips('success',data.message,'t');
				 			showList('stat');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
	}
	
	function deleteStat() {
        showConfirmTips('warn',"提示","您确定要清除统计结果(无法恢复)吗?",function(){
			$.ajax({
		            url: '${request.contextPath}/bigdata/setting/quality/stat/delete',
		            data:{
		            },
		            type:"post",
		            dataType: "json",
		            success:function(data){
				 		layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 			showLayerTips('success',data.message,'t');
				 			showList('stat');
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
    }

	function exportData() {
		$.ajax({
            url: '${request.contextPath}/bigdata/setting/quality/stat/export',
            data:{
            },
            type:"post",
            dataType: "json",
            success:function(data){
		 		layer.closeAll();
		 		if(!data.success){
		 			showLayerTips4Confirm('error',data.message);
		 		}else{
		 			showLayerTips('success',data.message,'t');
					var url='${request.contextPath}/common/download/file?filePath='+data.data;
					window.location.href =url;
    			}
          },
          error:function(XMLHttpRequest, textStatus, errorThrown){}
    });
   }
</script>