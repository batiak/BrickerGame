**Bricker - Arkanoid-Style Game**
**Ex2 | OOP | HUJI**

A Java implementation of a brick-breaking game built using the DanoGameLab engine.

**------**

**Game Details**

Bricker is a classic brick-breaking game where the player controls a paddle to keep a ball in play while
destroying all bricks on screen. The game features enhanced mechanics with special brick behaviors.
Core Gameplay

Player-controlled paddle (left/right movement)
Ball physics with collision detection
Destructible bricks with special abilities
Win: destroy all bricks | Lose: ball falls below screen & no lives left

**Special Brick Behaviors (10% chance each)**

Extra Balls (Pucks): Spawn 2 smaller balls that don't affect lives when lost
Additional Paddle: Temporary second paddle at screen center, disappears after 4 hits
Turbo Mode: Main ball becomes red and 1.4x faster for 6 collisions
Life Recovery: Falling heart collectible increases lives (max 4)
Double Behavior: Combines two random special behaviors (max 3 per brick)

**Technical Features**

Strategy Pattern for brick behaviors, Decorator Pattern
for combinations, clean OOP design with extensible architecture.

**Balance**

4 lives max, varied behaviors maintain challenge
while adding strategic depth to classic gameplay.
