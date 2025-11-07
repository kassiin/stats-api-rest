package net.kassin.javaspringexamples.stats.controller;


import net.kassin.javaspringexamples.stats.redis.RedisPublisher;
import net.kassin.javaspringexamples.stats.repository.PlayerStatsRepository;
import net.kassin.javaspringexamples.stats.model.PlayerStats;
import net.kassin.javaspringexamples.stats.service.UuidResolverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin("*")
public class StatsController {

    private final PlayerStatsRepository repository;
    private final UuidResolverService uuidResolver;
    private final RedisPublisher publisher;

    public StatsController(PlayerStatsRepository repository, UuidResolverService uuidResolver, RedisPublisher publisher) {
        this.repository = repository;
        this.uuidResolver = uuidResolver;
        this.publisher = publisher;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PlayerStats> getStats(@PathVariable UUID uuid) {
        System.out.println("Procurando informacoes de: " + uuid);
        return repository.findById(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PlayerStats> saveStats(@PathVariable UUID uuid, @RequestBody PlayerStats stats) {
        if (!uuid.equals(stats.getUuid())) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Salvando informacoes de: " + uuid);
        PlayerStats saved = repository.save(stats);
        publisher.publishInvalidation(uuid);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/name/{playerName}")
    public ResponseEntity<PlayerStats> getStatsByName(
            @PathVariable String playerName,
            @RequestParam(name = "mode", defaultValue = "online") String mode) {

        System.out.println("Procurando informacoes de: " + playerName);

        Optional<UUID> uuidOptional = uuidResolver.resolveUUID(playerName, mode);

        if (uuidOptional.isEmpty()) {
            System.out.println("Nenhuma informacao sobre: " + playerName + " foi encontrada");
            return ResponseEntity.notFound().build();
        }
        System.out.println("Jogador: " + playerName + " foi encontrado");
        return getStats(uuidOptional.get());
    }

}