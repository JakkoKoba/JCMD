# JCMD - Java Command Framework

JCMD is a **lightweight Java CLI framework** that allows developers to build interactive commandTemplate-line applications with modular, extensible commands.  
It features a REPL (Read-Eval-Print Loop) interface, dynamic commandTemplate registration, and safe error handling. JCMD can be used in **games, developer tools, runtime consoles, or scripting utilities**.

---

## Features
- Modular commandTemplate registration and execution
- Interactive REPL interface
- Extensible design: easily add new commands
- Error handling for safe execution
- Ideal for games, admin consoles, testing, or scripting tools

---

## Requirements (tested)
- Java 24.0.2
- Maven 3.9.11

---

## Example Usage

```java
JCMD app = new JCMD();

// Register commands
app.registerCoreCommands();
app.register(new Echo());

// Start CLI
app.run();
```

## Sample console session:
```
JCMD started. Type 'exit' to leave.
> echo Hello ${date yyyy}!
Hello World 2025!
> commandTemplate -reg org.jcmd.commands.version
Command registered: version
> version
JCMD version 0.1.3
> alias ex exit
Alias created: ex -> exit
> ex
Exiting CLI...
```

## Available Core/Base Commands

#### Core:
- `alias` — Create commandTemplate aliases
- `commandTemplate` — Register/Unregister commands
- `env` — Print environment variables
- `exit` — Stops the CLI
- `help` — Lists available commands
- `version` — Shows JCMD version
#### Base:
- `date` — Displays the current date
- `desc` - Print the project description
- `echo` — Print messages to the console
- `name` — Displays the project name
- `time` — Displays the current time

---

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/JakkoKoba/jcmd.git
```

2. Build with maven:
```bash
cd jcmd
mvn clean package
```

3. Run the CLI:
```bash
java -jar target/jcmd-0.1.3.jar
```
