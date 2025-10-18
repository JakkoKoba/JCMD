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
- JQuill 1.0.0

---

## Example Usage

```java
import org.jcmd.commands.core.Echo;
import org.jcmd.core.JCMD;

public class Main {

    final static JCMD engine = new JCMD();

    public static void main(String[] args) {
        engine.registerPackage("core");
        engine.register(new Echo(), "base");
        engine.run();
    }
}
```

## Sample console session:
```
JCMD started. Type 'exit' to leave.
> echo Hello ${date yyyy}!
Hello World 2025!
> command -reg base.date
Command registered: date
> alias year date YYYY
alias created: year -> date YYYY
> year
2025
> exit
Exiting CLI...
```

## Available Commands

#### Core Package:
- `exit` — Stops the CLI
- `help` — Get information about commands
- `list` — Lists available commands
#### Base Package:
- `alias` — Create command aliases
- `date` — Displays the current date
- `echo` — Print messages to the console
- `time` — Displays the current time
#### Admin Package:
- `command` — Register/Unregister commands
- `desc` - Print the project description
- `env` — Print environment variables
- `name` — Displays the project name
- `version` — Shows JCMD version

---

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/JakkoKoba/jcmd.git
```
2. Navigate to the project directory:
```bash
cd jcmd
```
3. Building with maven:
```bash
mvn clean package
```
4. Run the CLI:
```bash
java -jar target/jcmd-0.1.3.jar
```
