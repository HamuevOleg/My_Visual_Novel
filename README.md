# My_Visual_Novel

My_Visual_Novel is a visual novel game that was made with Java 23 and LibGDX engine.
This game was created as an individual project for a Java OOP course.

## 🎮 About the Game
This visual novel features an engaging story with multiple paths and endings.

## 🎯 Game endings
The game has 3 different endings:
1. **Romantic** - Find love
2. **Friendship** - Build lasting friendships  
3. **Loneliness** - A solitary path

## 📸 Screenshots
![image](https://github.com/user-attachments/assets/4844aeac-a7d0-4b98-860e-87b83284a818)
![image](https://github.com/user-attachments/assets/82420091-f2d5-4837-90d5-e86c910d317a)
![image](https://github.com/user-attachments/assets/bf69f21f-8196-4f2c-96a6-4bcc2b89c5e0)
![image](https://github.com/user-attachments/assets/fe97f00b-ac0e-43fe-bdb9-2b62f833d3dd)


## 🚀 How to Run
To start the game, run the `Lwjgl3Launcher` file:
```bash
./gradlew lwjgl3:run
```
### 🏗️ Project Architecture
## Core Classes

```
Main – Main application class where execution begins. Initializes the game and sets initial parameters.
Lwjgl3Launcher – Entry point for desktop version using Lwjgl3. Configures window parameters and desktop-specific settings.
StartupHelper – Helper class responsible for resource preloading, initial setup and environment preparation before launching the main screen.
```
## Game Screens

```
GameScreen – Main game screen where scenes, dialogs, choices and animations are displayed. Manages rendering logic and user input processing during gameplay.
MainMenuScreen – Main menu screen where users start interacting with the game. Implements UI elements for starting new game, continuing and exiting.
```

## Story Management

```
StoryManager – Management class responsible for scene transitions logic, story choice processing and current story state management.
StoryScene – Represents individual visual novel scene. Contains dialog lines list, available choices and other information related to specific story episode.
DialogLine – Model representing single character or narrator line. May include text, speaker name, emotions or other parameters affecting display.
StoryChoice – Class describing choice options provided to player. Each choice can lead to new scene or affect story development.
```
## System Requirements
- **Java 23** or higher (required)
- At least 512MB RAM

⚠️⚠️⚠️ **Important**: This project requires Java 23! So be careful.

## 🙏 Credits & Assets
This project was created with assistance from various tools and resources:

### 🎵 Music: Generated using Riffusion
### 🎨 Design: Created by professional designer & ChatGPT
### 💻 Code: Some parts developed with help of Claude AI

Special thanks to all the tools and contributors that made this project possible!
--------------------------------------------------------------------------------
## © 2025 Hamuev Oleg. All rights reserved.
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.
