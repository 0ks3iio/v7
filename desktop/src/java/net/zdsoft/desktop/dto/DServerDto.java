package net.zdsoft.desktop.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.dto.server.ModelDto;
import net.zdsoft.system.entity.server.Server;

/**
 * Created by shenke on 2017/3/13.
 */
public class DServerDto  extends BaseDto{

    private Server server;
    private String imageUrl;
    private List<ModelDto> modelDtos;
    private List<ModelDto> noDirModelDtos;
    private boolean dir;
    private String urlIndexSecond;
    private String urlIndex;

    public DServerDto() {
        this.modelDtos = new ArrayList<ModelDto>();
    }

    public void addSubModeldto(ModelDto modelDto){
        this.modelDtos.add(modelDto);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ModelDto> getModelDtos() {
        SortUtils.ASC(modelDtos,"model.displayOrder");
        return modelDtos;
    }

    public void setModelDtos(List<ModelDto> modelDtos) {
        this.modelDtos = modelDtos;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public List<ModelDto> getNoDirModelDtos() {
        return noDirModelDtos;
    }

    public void setNoDirModelDtos(List<ModelDto> noDirModelDtos) {
        this.noDirModelDtos = noDirModelDtos;
    }

    public Integer getOpenType() {
        if (server.getOpenType() != null) {
            return server.getOpenType();
        }
        if ( isDir() ) {
            if (CollectionUtils.isNotEmpty(noDirModelDtos)) {
                for (ModelDto dto : noDirModelDtos) {
                    if (dto.getModel() != null && dto.getModel().getOpenType() != null) {
                        return dto.getModel().getOpenType();
                    }
                }
            }

        } else {
            if (CollectionUtils.isNotEmpty(modelDtos)) {
                for (ModelDto dto : modelDtos) {
                    if (dto.getModel() != null && dto.getModel().getOpenType() != null) {
                        return dto.getModel().getOpenType();
                    }
                }
            }
        }
        return DeskTopConstant.MODEL_OPEN_TYPE_IFRAME;
    }

    public String getUrlIndex() {
        return urlIndex;
    }

    public String getUrlIndexSecond() {
        return urlIndexSecond;
    }

    private String getUrlIndexFor(boolean isSecondUrl,String serverUrl) {
        if(StringUtils.isNotBlank(serverUrl) && serverUrl.indexOf("?") != -1) {
        	return serverUrl;
        }
    	StringBuffer urlIndex = new StringBuffer();
        if ( (DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(getOpenType())
                || DeskTopConstant.MODEL_OPEN_TYPE_NEW.equals(getOpenType()))
                && server != null ) {
            if ("/".equals(server.getIndexUrl())) {
                return StringUtils.EMPTY;
            }
            //HttpServletRequest request = Evn.getRequest();
            if(StringUtils.isNotBlank(serverUrl)){
            	String indexUrl = server.getIndexUrl();
            	if (StringUtils.startsWith(indexUrl, "http")) {
                    return indexUrl;
                }
            	if (StringUtils.isNotBlank(indexUrl) && !StringUtils.equals("/", indexUrl)) {
                    return serverUrl + indexUrl;
                }
            }else{
            	return server.getUrlIndex(isSecondUrl);
            }
        }
        if ( server != null && StringUtils.isNotBlank(server.getIndexUrl()) ) {
            if ("/".equals(server.getIndexUrl())) {
                return StringUtils.EMPTY;
            }
            //HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
            if(StringUtils.isBlank(serverUrl)){
            	serverUrl = UrlUtils.ignoreLastRightSlash( isSecondUrl ? server.getSecondUrl() : server.getUrl());
            }
            urlIndex.append(serverUrl)
                    .append("/")
                    .append(DeskTopConstant.UNIFY_LOGIN_URL)
                    .append("?url=")
                    .append(serverUrl)
                    .append("/")
                    .append(UrlUtils.ignoreFirstLeftSlash(server.getIndexUrl()));
                    //.append(StringUtils.contains(server.getIndexUrl(), UrlUtils.QUESTION_MARK) ? "&" : "?")
                    //.append("&Common_Session_ID=")
                    //.append(session==null ? "" : session.getId())
                    //.append("&uid=")
                    //.append(Session.get(session.getId()).getLoginInfo().getUserName());
        }
        return urlIndex.toString();
    }

	public void setUrlIndexSecond(String urlIndexSecond) {
		this.urlIndexSecond = urlIndexSecond;
	}
	public void setUrlIndex(String urlIndex) {
		this.urlIndex = urlIndex;
	}
	
	public void doSetUrlIndex(String urlIndex) {
		this.urlIndex = getUrlIndexFor(false,urlIndex);
	}
	
	public void doSetUrlIndexSecond(String urlIndexSecond) {
		this.urlIndexSecond = getUrlIndexFor(true,urlIndexSecond);
	}
}
