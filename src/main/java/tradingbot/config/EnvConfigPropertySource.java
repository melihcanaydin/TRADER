package tradingbot.config;

import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

public class EnvConfigPropertySource extends PropertySource<EnvConfig> {

    public EnvConfigPropertySource(String name) {
        super(name, new EnvConfig());
    }

    @Override
    public Object getProperty(@NonNull String name) {
        return EnvConfig.get(name);
    }
}
