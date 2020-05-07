package net.zdsoft.bg;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 上午11:47
 */
public class ExceptionMatcher<T extends Throwable> extends BaseMatcher<T> {

    private final Matcher<?> exceptionMatcher;
    private final Matcher<?> causeExceptionMatcher;
    private final Matcher<?> messageMatcher;
    private final Class<? extends Throwable> cause;

    public ExceptionMatcher(String... messages) {
        this(null, messages);
    }

    public ExceptionMatcher(Class<? extends Throwable> cause, String... messages) {
        exceptionMatcher = CoreMatchers.allOf(CoreMatchers.notNullValue(),
                CoreMatchers.instanceOf(Throwable.class));
        if (cause != null) {
            causeExceptionMatcher = CoreMatchers.allOf(CoreMatchers.notNullValue(),
                    CoreMatchers.instanceOf(cause));
        } else {
            causeExceptionMatcher = null;
        }

        if (messages != null && messages.length > 0) {
            LinkedList<Matcher<?>> matchers = new LinkedList<>();
            for (String message : messages) {
                matchers.add(CoreMatchers.containsString(message));
            }
            messageMatcher = CoreMatchers.allOf(matchers.toArray(new Matcher[matchers.size()]));
        } else {
            messageMatcher = null;
        }
        this.cause = cause;
    }

    @Override
    public boolean matches(Object item) {
        if (!exceptionMatcher.matches(item)) {
            return false;
        }
        Throwable top = (Throwable) item;
        Throwable t = top;
        if (causeExceptionMatcher != null) {
            Set<Throwable> vs = new HashSet<>();
            for (; t != null && !cause.isInstance(t) && !vs.contains(t); t = t.getCause()) {
                vs.add(t);
            }
            if (!causeExceptionMatcher.matches(t)) {
                return false;
            }
        }
        if (messageMatcher == null) {
            return true;
        }
        String message = t.getMessage();
        if (t != top) {
            //why is \n
            message += "\n" + message;
        }
        return messageMatcher.matches(message);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("An exception that is ").appendDescriptionOf(exceptionMatcher);
        if (causeExceptionMatcher != null) {
            description.appendText("\n and its cause exception is ").appendDescriptionOf(causeExceptionMatcher);
        }
        if (messageMatcher != null) {
            description.appendText("\n and its message is ").appendDescriptionOf(messageMatcher);
        }
    }
}
