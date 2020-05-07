<#if favorites?exists && favorites?size gt 0>
     <#list favorites as item>
           <tr id="favorite_${item.id!}">
               <td style="width: 150px;"><a href="javascript:void(0);" title="${item.favoriteName!}" style="display: block;width: 150px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" onclick="editModelFavorite('${item.id!}')" id="${item.id!}">${item.favoriteName!}</a></td>
               <td style="width: 150px;">
                                    <span style="display: block;width: 150px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                                        <#list item.tags as tag>
                                            ${tag!}&nbsp;
                                        </#list>
                                    </span>
                                </td>
               <td>
                                    <a href="javascript:void(0);" onclick="deleteModelFavorite('${item.id!}', '${item.favoriteName!}')" hidefocus="true">删除</a>
                                </td>
           </tr>
     </#list>
<#else>
    <div class="no-data">
        <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-directory.png"/>
        <div>暂无数据</div>
    </div>
</#if>