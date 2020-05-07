package net.zdsoft.desktop.jscrop.upload;

/**
 * 配置
 * @author shenke
 */
public class JsCropConfigure {

    //裁剪目标宽度
    private int bigDescWidth;
    //裁剪目标高度
    private int bigDescHeight;

    private int smallerDescWidth;
    private int smallerDescHeight;

    //源图片缩放目标宽度
    private int originZoomWidth ;
    //源图片缩放目标高度
    private int originZoomHeight;

    private int x;
    private int x2;
    private int y;
    private int y2;

    private boolean isOriginZoom;

    /**
     *
     * @param isOriginZoom 源图片是否需要缩放
     */
    public JsCropConfigure(boolean isOriginZoom) {
        this.isOriginZoom = isOriginZoom;
    }

    public JsCropConfigure(){
        this.isOriginZoom = Boolean.FALSE;
    }

    public int getBigDescWidth() {
        return bigDescWidth;
    }

    public void setBigDescWidth(int bigDescWidth) {
        this.bigDescWidth = bigDescWidth;
    }

    public int getBigDescHeight() {
        return bigDescHeight;
    }

    public void setBigDescHeight(int bigDescHeight) {
        this.bigDescHeight = bigDescHeight;
    }

    public int getSmallerDescWidth() {
        return smallerDescWidth;
    }

    public void setSmallerDescWidth(int smallerDescWidth) {
        this.smallerDescWidth = smallerDescWidth;
    }

    public int getSmallerDescHeight() {
        return smallerDescHeight;
    }

    public void setSmallerDescHeight(int smallerDescHeight) {
        this.smallerDescHeight = smallerDescHeight;
    }

    public int getOriginZoomWidth() {
        return originZoomWidth;
    }

    public void setOriginZoomWidth(int originZoomWidth) {
        this.originZoomWidth = originZoomWidth;
    }

    public int getOriginZoomHeight() {
        return originZoomHeight;
    }

    public void setOriginZoomHeight(int originZoomHeight) {
        this.originZoomHeight = originZoomHeight;
    }

    public boolean isOriginZoom() {
        return isOriginZoom;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
