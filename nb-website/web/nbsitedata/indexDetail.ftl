<!-- 首页详细信息 -->

<div class="fn-clearfix">
    <div class="tpfd module fn-fl">
        <div class="model">
        <#if articles?exists && articles?size gt 0>
            <#list articles as a>
                <a href="javascript:;" onclick="doView('${a.id!}','');"><img src="${request.contextPath}/sitedata/titleimage.html?titleImageUrl=${a.titleImageUrl}" alt=""/></a>
            </#list>
        </#if>
        </div><!--model-->
    </div><!--module-->
    <div class="yhdl module fn-fr">
        <div class="module-header">
            <h3 class="module-title">用户登录</h3>
        </div>
        <div class="module-body">
            <div class="model">
                <div class="nsy-login-nologin">
                    <form id="loginForm" name="loginForm" type="post">
                        <input type="hidden" value="0" id="loginMode" name="loginMode"/>
                        <input type="hidden" name="verifyCode1" maxlength="4" value=""/>
                        <div class="nsy-login-username"><input  type="text" id="username" placeholder="账号"/></div>
                        <div class="nsy-login-pwd"><input type="password" id="password" placeholder="密码"></div>
                        <div class="nsy-login-forgot"><p class="tips"><span id="error_span" class="tip-error" style="display: block;color:red;"></span></p></div>
                        <div class="nsy-login-group" ><input value="登陆" type="button" id="loginCommit"></div>
                    </form>
                <#--<div class="nsy-login-noname"><span>没有账号？</span><a href="#" target="_blank">立即注册</a></div>-->
                </div>
                <div class="nsy-login-islogin" style="display: none;">
                    <a style="cursor: pointer;" target="_blank" id="systemLogin">系统管理</a><a style="cursor: pointer;"  id="websiteLogin" target="_blank">网站后台管理</a>
                </div>
            </div><!--model-->
        </div>
    </div><!--module-->
</div>
<div class="fn-clearfix">
    <div class="main fn-fl">
        <div class="mtjj module module01">
            <div class="module-header">
                <h3 class="module-title">媒体聚焦</h3>
                <a href="javascript:;" class="module-more" onclick="doMore('04');">更多>></a>
            </div>
            <div class="module-body">
                <div class="model">
                    <img class="img-l" src="${request.contextPath}/nbsitedata/images/mtjj-img.jpg" alt=""/>
                    <ul class="nsy-news-ul">
                    <#if medias?exists && medias?size gt 0>
                        <#list medias as media>
                            <li class="nsy-news-li">
                                <img class="nsy-newsFlag" src="${request.contextPath}/nbsitedata/images/list-icon.png" alt=""/>
                                <a class="nsy-newsTitle" href="javascript:;" onclick="doView('${media.id!}','04')"><@subMyStr str=media.title! length=22 /></a>
                                <p class="nsy-news-meta"><span class="nsy-newsTime">${(media.releaseTime?string('MM-dd'))?if_exists}</span></p>
                            </li>
                        </#list>
                    </#if>
                    </ul>
                </div><!--model-->
            </div>
        </div><!--module-->
        <div class="syqjj module module01">
            <div class="module-header">
                <h3 class="module-title">实验区简介</h3>
            </div>
            <div class="module-body" >
                <div class="model">
                    <img class="syq-img" src="${request.contextPath}/sitedata/titleimage.html?titleImageUrl=${tindu.titleImageUrl!}"" alt=""/>
                <#--     <img class="syq-img" src="${request.contextPath}/nbsitedata/images/syq.jpg" alt=""/> -->
                    <div class="introduce"  >

                        <h3><img src="${request.contextPath}/nbsitedata/images/list-icon.png" alt=""/> ${tindu.title!}</h3>
                        <p> ${tindu.content!} </p>
                    </div>
                </div><!--model-->
            </div>
        </div><!--module-->
        <div class="syqts module module01">
            <div class="module-header">
                <h3 class="module-title">实验区特色项目</h3>
            </div>
            <div class="module-body">
                <div class="model">
                    <ul class="nsy-newsPicture">
                    <#if projects?exists && projects?size gt 0>
                        <#list projects as project>
                            <li class="nsy-newsPicture-li">
                                <span class="nsy-newsPicture-span1"><#if (project.title?length gt 2)>${(project.title)?substring(0,2)}</br>${(project.title)?substring(2)}<#else>${project.title!}</#if></span>
                                <div class="nsy-headlineHd">
                                    <h3>${project.title!}</h3>
                                    <p>${project.introduction!}</p>
                                </div>
                            </li>
                        </#list>
                    </#if>
                    </ul>
                    </ul>
                </div><!--model-->
            </div>
        </div><!--module-->
    </div>
    <div class="side fn-fr">
        <div class="ggtz module module01">
            <div class="module-header">
                <h3 class="module-title">公告通知</h3>
                <a href="javascript:;" class="module-more" onclick="doMore('01')">更多>></a>
            </div>
            <div class="module-body">
                <div class="model">
                    <ul class="nsy-news-ul">
                    <#if bulletins?exists && bulletins?size gt 0>
                        <#list bulletins as bullectin>
                            <li class="nsy-news-li">
                                <img class="nsy-newsFlag" src="${request.contextPath}/nbsitedata/images/ggtz-list-icon0${bullectin_index+1}.png" alt=""/>
                            <#--<span class="nsy-news-span">小学语文 |  </span>-->
                                <a class="nsy-newsTitle" href="javascript:;" onclick="doView('${bullectin.id!}','01')"><@subMyStr str=bullectin.title!></@subMyStr></a>
                                <p class="nsy-news-meta"><span class="nsy-newsTime">${(bullectin.releaseTime?string('MM-dd'))?if_exists}</span></p>
                            </li>
                        </#list>
                    </#if>
                    </ul>
                </div><!--model-->
            </div>
        </div><!--module-->
        <div class="imgLink module fn-clearfix">
        <#if pics?exists && pics?size gt 0>
            <#list pics as pic>
                <a class="item0${pic_index+1}" href="javascript:;" onclick="goRegion('06','${pic.titleLink!}')"><img src="${request.contextPath}/sitedata/titleimage.html?titleImageUrl=${pic.titleImageUrl!}" alt=""/></a>
            </#list>
        </#if>
        </div><!--module-->
        <div class="syqdt module module01">
            <div class="module-header">
                <h3 class="module-title">实验区动态</h3>
                <a href="javascript:;" class="module-more" onclick="doMore('07')">更多>></a>
            </div>
            <div class="module-body">
                <div class="model">
                    <ul class="nsy-news-ul">
                    <#if dynamics?exists && dynamics?size gt 0>
                        <#list dynamics as dynamic>
                            <li class="nsy-news-li">
                                <img class="nsy-newsFlag" src="${request.contextPath}/nbsitedata/images/list-icon01.png" alt=""/>
                            <#--<span class="nsy-news-span">小学语文 |  </span>-->
                                <a class="nsy-newsTitle" href="javascript:;" onclick="doView('${dynamic.id!}','07')"><@subMyStr str=dynamic.title! length=12 /></a>
                                <p class="nsy-news-meta"><span class="nsy-newsTime">${(dynamic.releaseTime?string('MM-dd'))?if_exists}</span></p>
                            </li>
                        </#list>
                    </#if>
                    </ul>
                </div><!--model-->
            </div>
        </div><!--module-->
    </div>
</div>
<div class="row module01 fn-clearfix">
    <div class="xxjy module fn-fl">
        <div class="module-header">
            <h3 class="module-title">学校经验</h3>
            <a href="javascript:;" class="module-more" onclick="doMore('02')">更多>></a>
        </div>
        <div class="module-body">
            <div class="model">
                <ul class="nsy-news-ul">
                <#if experiences?exists && experiences?size gt 0>
                    <#list experiences as experience>
                        <li class="nsy-news-li">
                            <img class="nsy-newsFlag" src="${request.contextPath}/nbsitedata/images/list-icon02.png" alt=""/>
                        <#--<span class="nsy-news-span">小学语文 |  </span>-->
                            <a class="nsy-newsTitle" href="javascript:;" onclick="doView('${experience.id!}','02')"><@subMyStr str=experience.title! length=25 /></a>
                            <p class="nsy-news-meta"><span class="nsy-newsTime">${(experience.releaseTime?string('MM-dd'))?if_exists}</span></p>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div><!--model-->
        </div>
    </div><!--module-->
    <div class="cgzs module fn-fr">
        <div class="module-header">
            <h3 class="module-title">成果展示</h3>
            <a href="javascript:;" class="module-more" onclick="doMore('03')">更多>></a>
        </div>
        <div class="module-body">
            <div class="model">
                <ul class="nsy-newsPhoto">
                <#if shows?exists && shows?size gt 0>
                    <#list shows as show>
                        <li class="nsy-newsPhoto-li${show_index+1}">
                            <a class="nsy-newsTitle" href="javascript:;" onclick="doView('${show.id!}','03')" ><img width="195" height="133" src="${request.contextPath}/sitedata/titleimage.html?titleImageUrl=${show.titleImageUrl!}"/><span>${show.title!}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div><!--model-->
        </div>
    </div><!--module-->
</div>
<div class="banner module">
    <a href="javascript:;"><img src="${request.contextPath}/nbsitedata/images/banner01.jpg" alt="数据中心"/></a>
</div><!--module-->
<#--<script type="text/javascript" src="${request.contextPath}/nbsitedata/js/jquery.js"></script>-->
<script src="${request.contextPath}/static/js/login/jquery.cookies.js" type="text/javascript"></script>
<script src="${request.contextPath}/static/js/login/md5.js" type="text/javascript"></script>
<script src="${request.contextPath}/static/js/login/sha1.js" type="text/javascript"></script>
<script src="${request.contextPath}/static/js/login/constants.js" type="text/javascript"></script>
<script src="${request.contextPath}/static/js/login/login.js" type="text/javascript"></script>
<script>
    var systemRoot ;
    var websiteRoot;
    function doMore(thisId) {
        $(".container").load(encodeURI('${request.contextPath}/sitedata/model.html?thisId='+thisId+'&modelName=首页'),function () {
            t = $("#model"+thisId).offset().top;
            $(window).scrollTop(t);
        });
        //go2Model(thisId,'container');

    }
    function doView(id,thisId){
        $(".container").load(encodeURI('${request.contextPath}/sitedata/webarticle/detail.html?id='+id+'&thisId='+thisId+'&modelName=首页'),function () {
            t = $("#model"+thisId).offset().top;
            $(window).scrollTop(t);

        });

    }
    function goRegion(thisId,params){
        $(".container").load('${request.contextPath}/sitedata/model.html'+params+"&thisId="+thisId,function () {
            t = $("#model"+thisId).offset().top;
            $(window).scrollTop(t);
        });
//        t = $("#model"+thisId).offset().top;
//        $(window).scrollTop(t);
    }
    $(".tpfd .model").slick({
        dots: true,
        infinite: true,
        speed: 300,
        slidesToShow: 1,
        slidesToScroll: 1,
        mobileFirst:true,
        autoplay: true,
        arrows:false
    });

    $("#systemLogin").unbind("click").bind("click",function(){
        loginDisplay();
        doRealLogin('${frameworkEnv.getString("passport_system_login_url")}','${frameworkEnv.getString("passport_system_server_id")}',systemRoot);
    });

    $("#websiteLogin").unbind("click").bind("click",function () {
        loginDisplay();
        doRealLogin('${frameworkEnv.getString("passport_sitedata_login_url")}','${frameworkEnv.getString("passport_sitedata_server_id")}',websiteRoot);
    });


    function verify(){
        //$("#loginCommit").attr("disabled", "true");
        var params = new Object();
        params.username = $("#username").val();
        if(params.username == ""){
            alert("请输入登陆账号信息！");
            $("#loginCommit").removeAttr("disabled");
        }
        params.password = $("#password").val();
        if(params.password == ""){
            alert("请输入密码！");
            $("#loginCommit").removeAttr("disabled");
        }
        ajaxSubmit($.param(params),"${request.contextPath}/homepage/verify/login",function (data) {
            var jsonO = JSON.parse(data);
            if(jsonO.success){
                systemRoot = jsonO.systemContext;
                websiteRoot = jsonO.websiteContext;
                if(!jsonO.containSystem && jsonO.containSitedata){
                    doRealLogin('${frameworkEnv.getString("passport_sitedata_login_url")}','${frameworkEnv.getString("passport_sitedata_server_id")}',websiteRoot);
                    return ;
                }
                else if(jsonO.containSystem && !jsonO.containSitedata){
                    doRealLogin('${frameworkEnv.getString("passport_system_login_url")}','${frameworkEnv.getString("passport_system_server_id")}',systemRoot);
                    return ;
                }
                else if(!jsonO.containSystem && !jsonO.containSitedata){
                    showError("您没有权限！");
                    return ;
                }
                loginShow();
            }else{
                login.showError(jsonO.msg);
            }
        });
    }

    function doRealLogin(loginUrl,serverId,isRoot){
    <#if frameworkEnv.passport?default(true)>
        login.doLogin('${frameworkEnv.getString("passport_url")}',serverId,loginUrl,isRoot);
        return ;
    </#if>

    }

    function loginShow(){
        $(".nsy-login-nologin").css("display","none");
        $(".nsy-login-islogin").css("display","");
    }
    function loginDisplay() {
        $(".nsy-login-nologin").css("display","");
        $(".nsy-login-islogin").css("display","none");
    }

    $("#loginCommit").on("click", function(){
        verify();
    });
    function ajaxSubmit(data,url,success) {
        //"${request.contextPath}/homepage/loginUser/page"
        $.ajax({
            url:url,
            data:data,
            type:"post",
            success:success,
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                $("#loginCommit").attr("disabled", "false");
                var text = syncText(XMLHttpRequest);
                swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
            }
        });
    }
</script>
<!--截取字符串-->
<#macro subMyStr str length=12>
    <#if str?? && str?length gt length>
    ${str?substring(0,length)}...
    <#else>
    ${str!}
    </#if>
</#macro>