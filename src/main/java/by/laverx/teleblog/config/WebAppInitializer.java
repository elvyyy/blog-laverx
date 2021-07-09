package by.laverx.teleblog.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                WebAppConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebAppConfig.class,
                EmailConfig.class,
                JwtConfig.class,
                ModuleMapperConfig.class,
                PersistentJpaConfig.class,
                RedisConfig.class,
                ValidationConfig.class,
                WebSecurityConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
