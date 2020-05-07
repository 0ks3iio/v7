package net.zdsoft.system.dto.server;

import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModelDto extends BaseDto {
    private static final long serialVersionUID = 3713795833422521214L;
    private Model model;
    private String preUrl;
    private String fullUrl;
    private Server server;
    
    private String imageUrl;
    
    private List<ModelDto> subModelDtos = new ArrayList<ModelDto>();

    public void addModelDto(ModelDto model) {
        subModelDtos.add(model);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<ModelDto> getSubModelDtos() {
        if (subModelDtos != null && !subModelDtos.isEmpty()) {
            subModelDtos.sort((o1, o2) -> {
                Integer displayOrder1 = null;
                if (o1.getModel() == null) {
                    displayOrder1 = 0;
                } else {
                    displayOrder1 = Optional.ofNullable(o1.getModel().getDisplayOrder()).orElse(0);
                }
                Integer displayOrder2 = null;
                if (o2.getModel() == null) {
                    displayOrder2 = 0;
                } else {
                    displayOrder2 = Optional.ofNullable(o2.getModel().getDisplayOrder()).orElse(0);
                }
                return displayOrder1.compareTo(displayOrder2);
            });
        }
        return subModelDtos;
    }

    public void clearSubModels() {
        subModelDtos.clear();
    }

    public String getPreUrl() {
        return preUrl;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
