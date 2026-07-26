# CONTINUE.md - Tank Hell Project Guide

## 1. Project Overview

### Purpose
Tank Hell is a 2D artillery game inspired by classic tank battle games. Players control tanks that shoot projectiles at each other across a destructible terrain. The game features physics-based projectile movement, explosion effects, and terrain deformation.

### Key Technologies
- **Language**: Kotlin
- **Build System**: Gradle
- **UI Framework**: Java Swing
- **Architecture**: Multi-module Gradle project with separation of concerns

### High-Level Architecture
The project follows a clean separation of concerns with three main modules:
- **core**: Contains domain logic, game entities, and physics calculations
- **ui**: Handles rendering, user interface, and input handling
- **main**: Entry point and application bootstrap

## 2. Getting Started

### Prerequisites
- Java 17+ JDK
- Gradle 8.2+
- Kotlin 1.9.0

### Installation
1. Clone the repository
2. Ensure Java and Gradle are installed
3. No additional dependencies needed - all are managed by Gradle

### Running the Application
```bash
./gradlew run
```

Or run directly from your IDE by executing the `Main.kt` file.

### Basic Usage
- Click anywhere on the game field to fire a bullet
- Watch tanks fall when the terrain beneath them is destroyed
- Observe explosions that create craters in the terrain

### Running Tests
Currently, the project does not contain automated tests. Consider adding unit tests for core game logic.

## 3. Project Structure

### Main Directories
```
├── core/           # Domain logic and game entities
├── ui/             # User interface and rendering
├── main/           # Application entry point
├── diagrams/       # Architecture and domain diagrams
└── .continue/      # Continue configuration and documentation
```

### Key Components

#### Core Module (`core/`)
- **domain/GameField.kt**: Represents the destructible terrain with sine wave landscape
- **domain/Tank.kt**: Tank entity with position and falling mechanics
- **domain/Bullet.kt**: Projectile with physics-based movement
- **domain/Explosion.kt**: Explosion effect with radius expansion
- **domain/Area.kt**: Bounding box for collision detection

#### UI Module (`ui/`)
- **ui/GamePanel.kt**: Main game rendering surface with game loop
- **ui/Frame.kt**: Application window container
- **widgets/**: Visual representations of game entities

### Important Configuration Files
- **settings.gradle**: Defines multi-project structure
- **build.gradle**: Root build configuration
- **gradle.properties**: Gradle properties including Kotlin version

## 4. Development Workflow

### Coding Standards
- Follow Kotlin idioms and best practices
- Use descriptive function and variable names
- Include documentation for public APIs
- Keep functions focused and small

### Build and Deployment
The project uses Gradle for building. The shadow plugin is configured for creating fat JARs.

To build:
```bash
./gradlew build
```

To run:
```bash
./gradlew run
```

### Contribution Guidelines
1. Make changes in small, focused commits
2. Update documentation when adding new features
3. Consider performance implications of changes
4. Test changes manually before committing

## 5. Key Concepts

### Domain Terminology
- **GameField**: The destructible terrain represented as a boolean matrix
- **Tank**: Player-controlled unit that can move and shoot
- **Bullet**: Projectile with parabolic trajectory
- **Explosion**: Circular effect that destroys terrain
- **FallingDustBlock**: Debris that falls after an explosion

### Core Abstractions
- **Entity-Component Pattern**: Game entities (Tank, Bullet) separate from their visual representation (TankWidget, BulletWidget)
- **Tick-Based Game Loop**: All game state updates occur in tick() methods
- **Separation of Concerns**: Domain logic is completely separate from UI rendering

### Design Patterns
- **Observer Pattern**: Mouse events trigger game actions
- **Strategy Pattern**: Different entities implement their own tick() behavior
- **Factory Pattern**: Explosion widgets create falling dust blocks

## 6. Common Tasks

### Adding a New Tank
1. In `GamePanel.kt`, modify the `addTanks()` method:
```kotlin
private fun addTanks() {
    tankWidgets.add(TankWidget(100, 50, Color.orange))
    tankWidgets.add(TankWidget(300, 50, Color.gray))
    tankWidgets.add(TankWidget(600, 50, Color.magenta))
    // Add new tank here with x, y coordinates and color
    tankWidgets.add(TankWidget(400, 50, Color.blue))
}
```

### Modifying Projectile Physics
1. In `domain/Bullet.kt`, adjust the physics formula in the `tick()` method:
```kotlin
fun tick() {
    t += 1
    x = startX + power * t * cosa
    y = startY - power * t * sina + 0.1 * t * t  // Adjust gravity coefficient (0.1)
}
```

### Changing Terrain Generation
1. In `domain/GameField.kt`, modify the `buildSineField()` method:
```kotlin
private fun buildSineField() {
    for (x in 0..<width) {
        for (y in 0..<height) {
            // Modify the sine wave parameters
            if (y > 200 * sin(0.01 * x) + 300) matrix[x][y] = true
        }
    }
}
```

### Adding Sound Effects
1. Create a new `AudioManager` class in the `ui` package
2. Load sound files in `GamePanel.init`
3. Trigger sounds in relevant methods (e.g., when bullet fires or explosion occurs)

## 7. Troubleshooting

### Common Issues

#### Game Window Not Appearing
- Ensure Java is properly installed
- Check that the `Frame` class is being instantiated
- Verify that `isVisible = true` is set

#### Tanks Not Falling
- Check that `processTankFalling()` is being called in the game loop
- Verify that the game field collision detection is working
- Ensure the tank's `startFalling()` method is being called

#### Bullets Not Colliding
- Verify that `collided()` method in `Bullet.kt` is correctly checking the game field
- Check that `processBullets()` is calling collision detection
- Ensure the bullet's coordinates are being updated in `tick()`

### Debugging Tips
- Use the time display (top left) to monitor frame rate
- Add logging to track entity positions and states
- Use IDE debugging to step through the game loop
- Comment out sections of the rendering to isolate issues

## 8. References

### Documentation
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

### Relevant Files
- `diagrams/domain.puml`: Domain model diagram
- `core/src/main/kotlin/domain/`: Core game logic
- `ui/src/main/kotlin/`: User interface code

### Future Improvements
- Add multiplayer support
- Implement power-up system
- Add different weapon types
- Create level editor
- Add sound effects and music
- Implement scoring system
- Add particle effects
- Create menu system
- Add save/load functionality
- Implement AI opponents
```