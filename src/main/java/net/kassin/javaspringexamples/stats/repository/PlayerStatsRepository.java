package net.kassin.javaspringexamples.stats.repository;

import net.kassin.javaspringexamples.stats.model.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, UUID> {
}
