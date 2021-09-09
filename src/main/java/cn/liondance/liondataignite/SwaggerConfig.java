package cn.liondance.liondataignite;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Swagger config.
 *
 * @author sunwei
 */
@Data
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {
  private String apiInfoTitle;
  private String apiInfoVersion;
  private String apiInfoDescription;
  private String apiInfoTermsOfServiceUrl;
  private String apiInfoLicense;
  private String apiInfoLicenseUrl;

  /**
   * Create rest api docket.
   *
   * @return the docket
   */
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(apiInfoTitle)
        .version(apiInfoVersion)
        .description(apiInfoDescription)
        .termsOfServiceUrl(apiInfoTermsOfServiceUrl)
        .license(apiInfoLicense)
        .licenseUrl(apiInfoLicenseUrl)
        .build();
  }
}
