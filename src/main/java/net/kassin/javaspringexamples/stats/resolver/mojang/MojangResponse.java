package net.kassin.javaspringexamples.stats.resolver.mojang;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MojangResponse {

    private String id;
    private String name;

    public UUID getFormattedUUID() {
        if (id == null || id.length() != 32) return null;
        return UUID.fromString(
                id.substring(0, 8) + "-" +
                        id.substring(8, 12) + "-" +
                        id.substring(12, 16) + "-" +
                        id.substring(16, 20) + "-" +
                        id.substring(20, 32)
        );
    }

}
