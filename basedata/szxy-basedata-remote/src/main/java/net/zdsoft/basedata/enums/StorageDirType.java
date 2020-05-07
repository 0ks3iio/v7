package net.zdsoft.basedata.enums;

public enum StorageDirType {
	PUBLIC(0), ATTACHMENT(1), PICTURE(2);

    private int value;

    StorageDirType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return String.valueOf(value);
    }

    public static StorageDirType valueOf(int value) {
        StorageDirType type = null;
        switch (value) {
        case 0:
            type = PUBLIC;
            break;
        case 1:
            type = ATTACHMENT;
            break;
        case 2:
            type = PICTURE;
            break;
        default:
            type = PUBLIC;
            break;
        }
        return type;
    }

    public String getDescription() {
        String desc = null;
        switch (this) {
        case PUBLIC:
            desc = "公共";
            break;
        case ATTACHMENT:
            desc = "附件";
            break;
        case PICTURE:
            desc = "图片";
            break;
        default:
            desc = "公共";
            break;
        }
        return desc;
    }

    /**
     * 获得子目录
     * 
     * @return
     */
    public String getSubdirectory() {
        String desc = null;
        switch (this) {
        case PUBLIC:
            desc = "public";
            break;
        case ATTACHMENT:
            desc = "attachment";
            break;
        case PICTURE:
            desc = "photo";
            break;
        default:
            desc = "public";
            break;
        }
        return desc;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
