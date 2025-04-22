# CS9053 - Java Final Project Proposal
## Project Overview
This project aims to develop a Java-based idle RPG game similar to Melvor Idle, where players can engage in a
variety of tasks such as fighting, resource gathering, crafting, and leveling up, all while the game continues to
progress in the background. The game will allow players to automate tasks, accumulate resources over time, and
unlock new abilities and achievements. The core mechanics will combine elements of idle gameplay with RPG
progression, offering both strategic management and casual play.
## Objectives
The primary goals of the project are as follows:
1. To design and implement an idle RPG game where players can manage multiple tasks and resources.
2. To use inheritance to customize frames for different sections of the game (e.g., main menu, task
management screen, resources dashboard).
3. To utilize GUI components, such as buttons, text areas, and menus, in creating an interactive and engaging
user interface.
4. To apply multithreading for automating background tasks and real-time resource accumulation.
5. To integrate databases for saving player progress, resources, and achievements.
6. To develop a web interface where players can view and manage their game progress remotely.
## Approaches
1. GUI Development:
   a. Swing or JavaFX will be used to build the graphical user interface. These libraries will help create
   interactive elements like buttons, progress bars, labels, and drop-down menus.
2. Database:
   a. MySQL or PostgreSQL will store player data such as game progress, resources, and achievements.
   b. JDBC or Spring Data JPA will be used to interact with the database, ensuring player data is saved
   and retrieved efficiently.
3. Multithreading:
   a. Java’s ExecutorService or Thread will be used to manage concurrent tasks, such as automatically
   executing background jobs (combat, resource gathering, etc.) without interrupting the game’s
   interface.
4. Spring Framework:
   a. b. If the web application is included, Spring Boot will be used to build the backend logic, managing
   tasks and resource accumulation through RESTful APIs.
   The front end will be developed using HTML, CSS, and JavaScript.
## Project Phases
1. Phase 1: Game Design and Planning
2. Phase 2: GUI Development
3. Phase 3: Database Integration
4. Phase 4: Task Management and Multithreading
5. Phase 5: Web Application
6. Phase 6: Testing and Debugging
7. Phase 7: Final Deliverables and Documentation