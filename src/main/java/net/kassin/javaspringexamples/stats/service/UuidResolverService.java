package net.kassin.javaspringexamples.stats.service;

import net.kassin.javaspringexamples.stats.resolver.mojang.MojangResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
public class UuidResolverService {

    private static final String MOJANG_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private final RestTemplate restTemplate;

    public UuidResolverService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public UUID getOfflineUuid(String name) {
        String offlineId = "OfflinePlayer:" + name;
        return UUID.nameUUIDFromBytes(offlineId.getBytes(StandardCharsets.UTF_8));
    }

    private Optional<UUID> getOnlineUuid(String userName) {
        String url = MOJANG_URL + userName;
        try {
            ResponseEntity<MojangResponse> response = restTemplate.getForEntity(url, MojangResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.ofNullable(response.getBody().getFormattedUUID());
            }

        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Erro ao consultar API da Mojang para " + userName + ": " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<UUID> resolveUUID(String userName, String mode) {
        if ("offline".equalsIgnoreCase(mode)) {
            return Optional.of(getOfflineUuid(userName));
        }
        return getOnlineUuid(userName);
    }

}
