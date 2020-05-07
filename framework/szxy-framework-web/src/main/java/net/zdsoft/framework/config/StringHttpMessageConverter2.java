package net.zdsoft.framework.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

public class StringHttpMessageConverter2 extends StringHttpMessageConverter {
        
    @Override
    protected List<Charset> getAcceptedCharsets() {
        // TODO Auto-generated method stub
        return super.getAcceptedCharsets();
    }

    @Override
    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        // TODO Auto-generated method stub
        super.setWriteAcceptCharset(writeAcceptCharset);
    }

    @Override
    protected void addDefaultHeaders(HttpHeaders arg0, String arg1, MediaType arg2)
            throws IOException {
        // TODO Auto-generated method stub
        super.addDefaultHeaders(arg0, arg1, arg2);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        // TODO Auto-generated method stub
        return super.getSupportedMediaTypes();
    }

    @Override
    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        // TODO Auto-generated method stub
        super.setSupportedMediaTypes(supportedMediaTypes);
    }
    

}
