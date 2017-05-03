package me.roybailey.springboot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Required because graphql created data fetcher objects using newInstance() outside spring.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static AtomicReference<ApplicationContext> ApplicationContext = new AtomicReference<>();

    public static ApplicationContext getApplicationContext() {
        return ApplicationContext.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContext.set(applicationContext);
    }
}
