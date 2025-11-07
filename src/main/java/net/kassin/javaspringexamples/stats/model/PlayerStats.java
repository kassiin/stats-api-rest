package net.kassin.javaspringexamples.stats.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats {

    @Id
    private UUID uuid;
    private int kills;
    private int deaths;

}
