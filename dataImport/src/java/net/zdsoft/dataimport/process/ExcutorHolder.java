package net.zdsoft.dataimport.process;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author shenke
 * @since 2017.08.04
 */
@Component
public class ExcutorHolder {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void run(Thread importThread) {
        executorService.submit(importThread);
    }

    public <T> Future<T> run(Callable<T> callable) {
        return executorService.submit(callable);
    }

    public Future<?> run (Runnable runnable) {
        return executorService.submit(runnable);
    }

    public void clearCancel() {

    }
}
