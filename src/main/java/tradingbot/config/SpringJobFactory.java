package tradingbot.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringJobFactory extends SpringBeanJobFactory {
    private final AutowireCapableBeanFactory beanFactory;

    public SpringJobFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("null")
    @Override
    protected Object createJobInstance(@SuppressWarnings("null") TriggerFiredBundle bundle)
            throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        beanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
