<#import "/fw/macro/webmacro.ftl" as w>
<div class="row">
	<div class="col-xs-12" id="interfaceCountContainer">
	   <table class="table table-striped table-hover no-margin">
	              <thead>
	                  <tr>
	                      <th>开发者</th>
	                      <th>接口方法</th>
	                      <th>接口名称</th>
	                      <th>接口类型</th>
	                      <th>调用时间</th>
	                  </tr>
	              </thead>
	              <tbody>
	                  <#if interfaceCountDtos?exists &&(interfaceCountDtos?size gt 0)>
	                      <#list interfaceCountDtos as dto>
	                          <tr data-interid="${dto.id!}">
	                              <td>${dto.developerName!}</td>
	                              <td>${dto.method!}</td>
	                              <td>${dto.interfaceName!}</td>
	                              <td>${dto.type!}</td>
	                              <td>${dto.creationTime!}</td>
	                          </tr>
	                      </#list>
	                  </#if>
	              </tbody>
	      </table>
	      <#if interfaceCountDtos?exists && interfaceCountDtos?size gt 0>
              <@w.pagination2  container="#interfaceCountContainer" pagination=pagination page_index=2/>
          </#if>
	 </div>
</div>