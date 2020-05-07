package net.zdsoft.framework.entity;

import java.util.ArrayList;
import java.util.List;

public class Menu extends BaseEntity<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private String url;

    private List<Menu> subMenu = new ArrayList<Menu>();
    
    public boolean isDir() {
        return subMenu.size() > 0;
    }

    public void addSubMenu(Menu menu) {
        subMenu.add(menu);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getSubMenu() {
        return subMenu;
    }

    @Override
    public String fetchCacheEntitName() {
        return "menu";
    }

}
