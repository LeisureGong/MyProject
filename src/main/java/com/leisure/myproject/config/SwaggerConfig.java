package com.leisure.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gonglei
 * @date 2020/4/24 9:33
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.pathMapping("/")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.leisure.myproject.controller"))
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder()
						.title("SpringBoot整合Swagger")
						.description("SpringBoot整合Swagger，详细信息......")
						.version("9.0")
						.contact(new Contact("Leisure","blog.csdn.net","leisure_gong@foxmail.com"))
						.license("The Apache License")
						.licenseUrl("http://www.baidu.com")
						.build());
	}
}
