package net.zdsoft.dataimport.biz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2017.08.01
 */
public abstract class QImportError {

    private Map<String,ImportFieldError> errorFiledCache = new HashMap<>();

    private volatile boolean hasError;

    protected ImportFieldError createFieldError(String name) {
        return new ImportFieldError(name);
    }

    public QImportError error(final String name, String ... error) {
        initCache();
        hasError = Boolean.TRUE;
        ImportFieldError importFieldError = errorFiledCache.get(name);
        importFieldError.addErrors(Arrays.stream(error).collect(Collectors.toList()));
        return this;
    }

    public void value(String name, Object value) {
        initCache();
        ImportFieldError importFieldError = errorFiledCache.get(name);
        importFieldError.setValue(value);
    }

    private void initCache() {
        if ( errorFiledCache.isEmpty() ) {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field e : fields) {
                try {
                    errorFiledCache.put(e.getName(), (ImportFieldError) BeanUtils.getProperty(this, e));
                } catch (Exception e1) {

                }
            }
        }
    }

    public class ImportFieldError {
        private List<String> errors;
        private String name;

        private Object value;   //该字段对应的Excel中的值

        public ImportFieldError(String name) {
            this.errors = new ArrayList<>();
            this.name = name;
        }

        public void addError(String error) {
            setHasError(Boolean.TRUE);
            this.errors.add(error);
        }

        public void addErrors(List<String> errors){
            this.errors.addAll(errors);
            setHasError(Boolean.TRUE);
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    private void setHasError(boolean error) {
        hasError = error;
    }

    public boolean isHasError() {
        return hasError;
    }
}
