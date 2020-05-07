package net.zdsoft.desktop.jscrop.define;

import java.io.File;
import java.io.Serializable;

/**
 * @author shenke
 */
public class JsCropFile implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] bytes;
    private JsCropFileType type;
    private File file;

    public JsCropFile(JsCropFileType type) {
        this.type = type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public JsCropFileType getType() {
        return type;
    }

    public void setType(JsCropFileType type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
