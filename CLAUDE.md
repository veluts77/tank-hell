# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- `./gradlew build` — build all modules
- `./gradlew run` — run the application
- `./gradlew :main:build` — build specific module

## Architecture

**Multi-module Gradle project** with three modules:
- `main` — application entry point (MainKt)
- `core` — domain layer (game logic, no UI dependencies)
- `ui` — Swing-based UI layer (depends on core)

**Layer separation**: Domain objects (Tank, Bullet, Explosion, GameField, FallingDustBlock) live in `core/` and are rendered by widgets in `ui/`. Widgets wrap domain objects and handle Swing rendering.

**Game loop** lives in `GamePanel` (ui module) — it orchestrates tick/update cycles for tanks, bullets, explosions, and falling blocks.

**Key domain classes**:
- `GameField` — terrain matrix with sine-wave hill landscape, supports sub-matrix extraction for explosions
- `Tank` — falls under gravity until hitting terrain or field bottom
- `Bullet` — ballistic trajectory (angle + power), collides with terrain or tanks
- `Explosion` — expanding circular area, converts terrain to falling dust blocks
- `FallingDustBlock` — matrix of dirt that falls cell-by-cell after explosion

**Rendering pipeline**: GamePanel.double-buffered rendering via BufferedImage. Draw order: terrain → falling blocks → tanks → explosions → bullets. Frame runs at ~40 FPS with 25ms sleep per frame.