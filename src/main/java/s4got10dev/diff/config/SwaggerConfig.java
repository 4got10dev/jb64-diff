package s4got10dev.diff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Serhii Homeniuk
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {
        //@formatter:off
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("v1")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("s4got10dev.diff"))
                .paths(PathSelectors.any())
                .build();
        //@formatter:on
    }

    private ApiInfo apiInfo() {
        //@formatter:off
        return new ApiInfoBuilder()
                .title("JSON Base64 Diff")
                .description("Application accept two JSON Base64 encoded binary data on left and right side "
                        + "and check difference witch can be accessed after")
                .contact(new Contact("Serhii Homeniuk", "", ""))
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
        //@formatter:on
    }

}
