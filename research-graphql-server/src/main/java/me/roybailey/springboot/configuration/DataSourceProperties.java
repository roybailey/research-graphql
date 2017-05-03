package me.roybailey.springboot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "data-source")
public class DataSourceProperties {

    @NotNull
    String urlProductService;

    @NotNull
    String urlUserService;

    @NotNull
    String urlOrderService;

}
