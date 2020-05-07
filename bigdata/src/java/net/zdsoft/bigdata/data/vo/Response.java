/**
 * FileName: Response
 * Author:   shenke
 * Date:     2018/4/18 下午4:18
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2018/4/18 下午4:18
 */
public class Response {

    private boolean success;
    private String message;
    private Object data;

    public static ResponseBuilder ok() {
        return new ResponseBuilderImpl().ok();
    }

    public static ResponseBuilder error() {
        return new ResponseBuilderImpl().error();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public interface ResponseBuilder {

        ResponseBuilder ok();

        ResponseBuilder error();

        ResponseBuilder message(String message);

        ResponseBuilder message(String format, Object ...args);

        ResponseBuilder data(Object data);

        ResponseBuilder data(String key, Object value);

        Response build();
    }

    public static class ResponseBuilderImpl implements ResponseBuilder {

        private boolean success;
        private String message;
        private Object data;
        private Map<String, Object> datas;

        @Override
        public ResponseBuilder ok() {
            this.success = true;
            return this;
        }

        @Override
        public ResponseBuilder error() {
            this.success = false;
            return this;
        }

        @Override
        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public ResponseBuilder message(String format, Object... args) {
            this.message = String.format(format, args);
            return this;
        }

        @Override
        public ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        @Override
        public ResponseBuilder data(String key, Object value) {
            if (this.datas == null) {
                this.datas = new HashMap<>(16);
            }
            this.datas.put(key, value);
            return this;
        }

        @Override
        public Response build() {
            Response response = new Response();
            response.data = data;
            response.message = message;
            response.success = success;
            return response;
        }
    }
}
