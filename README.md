Bricker - Arkanoid-Style Game
Ex2 | OOP | Huji
A Java implementation of an Arkanoid-style brick-breaking game built using the DanoGameLab engine (version 1.1.0).

Overview
Bricker is a classic brick-breaking game where the player controls a paddle to keep a ball in play while destroying all bricks on screen.
The game features enhanced mechanics with special brick behaviors that add strategic depth and excitement to the traditional gameplay.
Game Features

Core Gameplay
Player-controlled paddle that moves left and right
Ball physics with realistic collision detection
Destructible bricks with various special abilities
Win condition: destroy all bricks
Lose condition: ball falls below screen

Special Brick Behaviors
Each brick has a probability-based behavior when destroyed of one of the following behaviors.

Extra Balls (Pucks):
Spawn at the destroyed brick's center with random upward trajectories
Smaller size (75% of main ball) with same collision properties
Don't affect player lives when lost - automatically removed when exiting screen

Additional Paddle:
Appears at screen center, half-screen height
Moves independently but follows player input
Disappears after taking 4 hits from any ball
Only one additional paddle can exist at a time

Turbo Mode:
Activated only when the main ball (not Pucks) hits a turbo brick
Ball becomes red and moves 1.4x faster
Effect lasts for exactly 6 collisions with any object
Cannot be activated if already in turbo mode

Life Recovery:
Heart-shaped collectible falls from destroyed brick center
Must be caught by the original paddle (not additional paddle)
Increases lives up to maximum of 4
Falls at constant speed downward

Double Behavior:
Randomly selects and combines two special behaviors
Can stack up to 3 total special behaviors per brick maximum
Same behavior can be selected multiple times (e.g., 4 Puck balls)
Uses decorator pattern for flexible behavior combination

Technical Implementation
The game utilizes the Strategy Pattern for different brick collision behaviors, the Decorator Pattern for combining multiple special effects,
and Polymorphism for clean object interactions. The architecture follows the Open-Close principle with extensible design, 
maintains clean separation of concerns with minimal API exposure, and provides a flexible behavior system that allows easy addition of new special effects.

Game Balance
Maximum 4 lives total (starting with 3)
Special behaviors distributed to maintain challenge while providing variety
Puck balls add chaos without punishment
Additional paddle provides temporary assistance
Turbo mode increases difficulty temporarily

This implementation demonstrates advanced Java OOP concepts while creating an engaging and varied gaming experience that extends the classic Arkanoid formula.
