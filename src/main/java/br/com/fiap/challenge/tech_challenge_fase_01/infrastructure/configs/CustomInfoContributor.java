package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> appInfo = new HashMap<>();
        
        // Informações da aplicação
        appInfo.put("name", "Tech Challenge Fase 01");
        appInfo.put("description", "Sistema de Gestão de Restaurantes - Arquitetura Hexagonal");
        appInfo.put("version", "1.0.0");
        appInfo.put("team", "Equipe-3-FIAP-POS-GRAD-ARC-JAVA");
        
        // Informações técnicas
        Map<String, Object> techInfo = new HashMap<>();
        techInfo.put("architecture", "Hexagonal Architecture (Ports & Adapters)");
        techInfo.put("framework", "Spring Boot 3.5.6");
        techInfo.put("javaVersion", "Java 21");
        techInfo.put("database", "PostgreSQL 17");
        techInfo.put("containerization", "Docker + Docker Compose");
        
        // Informações de funcionalidades
        Map<String, Object> features = new HashMap<>();
        features.put("authentication", "JWT Stateless");
        features.put("authorization", "RBAC (CLIENT, OWNER, ADMIN)");
        features.put("validation", "Jakarta Bean Validation");
        features.put("documentation", "OpenAPI 3.0 + Swagger UI");
        features.put("monitoring", "Spring Boot Actuator + Prometheus");
        features.put("errorHandling", "RFC 7807 Problem Details");
        
        // Informações de saúde customizadas
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("lastStartup", LocalDateTime.now().toString());
        healthInfo.put("environment", "development");
        
        builder.withDetail("application", appInfo);
        builder.withDetail("technical", techInfo);
        builder.withDetail("features", features);
        builder.withDetail("health", healthInfo);
    }
}