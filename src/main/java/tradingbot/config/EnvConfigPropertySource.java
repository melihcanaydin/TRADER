package tradingbot.config;

import org.springframework.core.env.PropertySource;

public class EnvConfigPropertySource extends PropertySource<EnvConfig> {

    public EnvConfigPropertySource(String name) {
        super(name, new EnvConfig());
    }

    @Override
    public Object getProperty(String name) {
        return EnvConfig.get(name);
    }
}
