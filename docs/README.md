# Lilith

Lilith is a personal task management chatbot with a themed GUI. She helps you manage todos, deadlines, and events.

![Ui](Ui.png)

---

## Download
[Latest Release](https://github.com/Astatyr/ip/releases/latest)

---

## Getting Started

### Prerequisites
- Java 21 ([Download here](https://adoptium.net/temurin/releases/?version=21))
- Note that Jave versions higher than 21 may experience issues due to incompatibility with Gradle.

### Running the app
```
java -jar Lilith.jar
```

---

## Features

### Task Types
Lilith supports three types of tasks:

| Type | Command | Description |
|---|---|---|
| Todo | `todo <task>` | A simple task with no date |
| Deadline | `deadline <task> /by <date>` | A task due by a specific date |
| Event | `event <task> /from <date> /to <date>` | A task with a start and end time |

### Commands

| Command | Description | Example |
|---|---|---|
| `list` | Show all tasks | `list` |
| `find <keyword>` | Search tasks by name | `find book` |
| `mark <index>` | Mark a task as done | `mark 1` |
| `unmark <index>` | Mark a task as not done | `unmark 1` |
| `update <index> [/name] [/by] [/from] [/to]` | Edit an existing task | `update 1 /name new name` |
| `delete <index>` | Delete a task | `delete 1` |
| `/emptyall` | Clear all tasks | `/emptyall` |
| `bye` | Exit the app | `bye` |
| `cheer` | Sends an encouraging message or a surprise! | `cheer` |

### Update Command Fields
The `update` command accepts any combination of these fields:

```
update <index> [/name <new name>] [/by <date>] [/from <date>] [/to <date>]
```

Examples:
```
update 2 /name Submit assignment
update 1 /by 2025-12-01 1800
update 3 /from 2025-01-01 /to 2025-01-02
```

### Date Formats
Lilith accepts a wide range of date formats:

```
2025-01-30 1800
2025/01/30 18:00
30-01-2025 1800
30/01/2025
2025-01-30
```

---

## Data Storage
Tasks are automatically saved to `./LilithData/lilith.txt` relative to where the app is run. The file is created automatically if it does not exist.

---

## Running via Command Line Interface (CLI)
```
java -jar Lilith.jar cli
```

---

## For Developers

### Config File
Can be found in `./src/main/java/lilith/config`.

### Resource Files
Can be found in `./src/main/resources/lilith`.

### Building from source
```
./gradlew clean shadowJar
```
The JAR then can be found in `build/libs/Lilith.jar`.

### Running tests
```
./gradlew test
```

### Tech Stack
- Java 21
- JavaFX 21
- JUnit 5
- Gradle with Shadow plugin for fat JAR

---

## Acknowledgments & Credits

### Character & Aesthetic
* **Source Material:** The character **Lilith**, her name, and the visual theme are inspired by the game *The NOexistenceN of you AND me*.
* **Visual Assets:** The bot's profile picture (`pfp.png`) and thematic icons (like the strawberry cake) are from or adapted from the original game assets for educational and non-commercial use.

### Technical Credits
This project was developed as part of a software engineering module at the **National University of Singapore (NUS)**.

---

## License
This project is for educational purposes. Please respect the original creators of *The NOexistenceN of you AND me* regarding any redistribution of the character-based assets.
