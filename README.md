# JCMD - Java Command Framework

JCMD is a **lightweight Java CLI framework** that allows developers to build interactive command-line applications with modular, extensible commands.  
It features a REPL (Read-Eval-Print Loop) interface, dynamic command registration, and safe error handling. JCMD can be used in **games, developer tools, runtime consoles, or scripting utilities**.

---

## Features
- Modular command registration and execution
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
app.register(new Echo());
app.register(new Exit(app));

// Start CLI
app.run();
```

## Sample console session:
```
JCMD started. Type 'exit' to leave.
> echo Hello World
Hello World
> version
JCMD version 0.1.2
> alias e exit
Alias created: ex -> exit
> ex
Exiting CLI...
```

## Available Base Commands

- `alias` — Create command aliases
- `command` — Register/Unregister commands
- `date` — Displays the current date
- `desc` - Print the project description
- `echo` — Print messages to the console
- `env` — Print environment variables
- `exit` — Stops the CLI
- `help` — Lists available commands
- `time` — Displays the current time
- `version` — Shows JCMD version

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
java -jar target/jcmd-0.1.2.jar
```
