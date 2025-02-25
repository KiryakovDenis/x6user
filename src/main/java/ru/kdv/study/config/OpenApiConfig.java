package ru.kdv.study.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "User",
                description = "API микросервиса пользователей для системы Marketplace",
                version = "1.0.0",
                contact = @Contact(
                        name = "Kiryakov Denis",
                        email = "KiryakovDenis@gmail.com"
                )
        )
)
public class OpenApiConfig {
}
