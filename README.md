# 📚 Player Statistics Dashboard (Minecraft)

## Overview
This project showcases a **Full-Stack Architecture** focused on **Distributed Cache Coherence** and **Performance Optimization (TPS)**. It addresses the common problem of latency ("lag") in game servers (like Minecraft/Paper) caused by slow, blocking database operations by employing an asynchronous, multi-layered caching system.

The system is composed of two primary applications:
1.  **Backend (Java/Spring Boot):** A robust REST API managing player data persistence and serving as the central hub for cache invalidation via Redis Pub/Sub.
2.  **Frontend (React/Vite):** A professional Single Page Application (SPA) designed for searching player statistics with a modern, responsive interface.

## 🛠️ Technologies and Tools

### Backend (Java/Spring Boot - Managed by Gradle)

| Technology | Purpose | Key Files |
| :--- | :--- | :--- |
| **Spring Boot** | Framework for building the REST API. | `JavaSpringExamplesApplication.java` |
| **Spring Data JPA** | Data persistence management (`PlayerStats`). | `PlayerStatsRepository.java` |
| **Redis (Docker)** | Asynchronous notification mechanism for cache invalidation. | `application.yaml` |
| **`RedisConfig`** | Explicit configuration of `RedisTemplate<String, Object>` with `StringRedisSerializer` to ensure Pub/Sub functionality, solving serialization issues. | `RedisConfig.java` |
| **`RedisPublisher`** | Service that triggers the invalidation event to the `stats-invalidation-channel` upon every data update (`@PutMapping`). | `RedisPublisher.java` |
| **`UuidResolverService`**| Logic for resolving player names to UUIDs (Offline and Online via Mojang API). | `UuidResolverService.java` |
| **`@CrossOrigin`**| Configured in `StatsController` to resolve CORS issues with the frontend. | `StatsController.java` |


## ⚙️ Optimization Architecture (The Senior Focus)
The core focus of this project is the architecture implemented to ensure that data query and saving operations **never block the Minecraft server's main thread** (guaranteeing high TPS) and that data remains consistent.

### 1. Multi-Layered Caching System (L1/L2)

* **L1 - Caffeine (In the Paper/Spigot Plugin):** Provides maximum speed (in-memory cache) for the game's core logic.
* **L2 - Redis Pub/Sub:** Acts as a *coherence coordinator*. Any external update via the REST API triggers a message in Redis, forcing the **instant invalidation** of the corresponding key in the Paper's Caffeine cache. This ensures immediate data consistency across all BungeeCord servers.

### 2. Minimum Display Delay (Optimized UX)
To solve the visual "flash" or *jank* in ultra-fast network requests, the React component (`StatsDashboard`) utilizes a **Minimum Delay for Loader** logic:

* The "Loading..." status is only displayed if the request duration exceeds **200ms**.
* The button deactivation is controlled to remain in the "Loading" state for a minimum time (e.g., 500ms), ensuring the user registers the action and making the user experience (UX) smoother and more reliable.
---
