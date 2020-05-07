/**
 * FileName: TagVO.java
 * Author:   shenke
 * Date:     2018/5/17 上午11:11
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

/**
 * @author shenke
 * @since 2018/5/17 上午11:11
 */
public class TagVO {

    private String tagName;
    private String tagId;
    private Boolean selected;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    public static final class Builder {
        private String tagName;
        private String tagId;
        private Boolean selected;

        private Builder() {
        }

        public static Builder aTagVO() {
            return new Builder();
        }

        public Builder tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public Builder tagId(String tagId) {
            this.tagId = tagId;
            return this;
        }

        public Builder selected(Boolean selected) {
            this.selected = selected;
            return this;
        }

        public Builder but() {
            return aTagVO().tagName(tagName).tagId(tagId).selected(selected);
        }

        public TagVO build() {
            TagVO tagVO = new TagVO();
            tagVO.setTagName(tagName);
            tagVO.setTagId(tagId);
            tagVO.setSelected(selected);
            return tagVO;
        }
    }
}
