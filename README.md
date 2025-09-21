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
JCMD version 0.1.0
> exit
Exiting CLI...
```

## Available Base Commands

- `echo` — Print messages to the console
- `exit` — Stops the CLI
- `help` — Lists available commands
- `version` — Shows JCMD version

---

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/USERNAME/jcmd-cli.git
```

2. Build with Maven:
```bash
cd jcmd-cli
mvn clean package
```

3. Run the CLI:
```bash
java -jar target/jcmd-cli-0.1.0.jar
```
