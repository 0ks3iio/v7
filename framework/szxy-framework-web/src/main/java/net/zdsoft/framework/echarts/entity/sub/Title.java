package net.zdsoft.framework.echarts.entity.sub;

public class Title {
    private String text;
    private String subtext;

    public Title() {
        
    }
    
    public Title(String text) {
        this.text = text;
    }
    public Title(String text, String subtext) {
        this.text = text;
        this.subtext = subtext;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }
}
