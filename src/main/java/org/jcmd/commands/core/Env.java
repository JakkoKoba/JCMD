package org.jcmd.commands.core;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Env implements Command {
    protected final JCMD engine;

    private final String NAME = "env";
    private final String DESCRIPTION = "Get the current environment variables.";
    private final String CATEGORY = "Core";

    public Env(JCMD engine) {
        this.engine = engine;
    }

    @Override
    public String getName() {
        return NAME;
    }
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
    @Override
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println(
                            "             variables\n" + "===================================\n" +
                    "TIME_FORMAT = " + Variables.TIME_FORMAT + "\n" +
                    "DATE_FORMAT = " + Variables.DATE_FORMAT + "\n" +
                    "VAR_PREFIX = " + Variables.VAR_PREFIX + "\n" +
                            "\n" + "               flags\n" + "===================================\n" +
                    "COMMAND_REGISTER_FLAG = " + Variables.COMMAND_REGISTER_FLAG + "\n" +
                    "COMMAND_UNREGISTER_FLAG = " + Variables.COMMAND_UNREGISTER_FLAG + "\n" +
                            "\n" + "             constants\n" + "===================================\n" +
                    "JAVA_VERSION = " + Variables.JAVA_VERSION + "\n" +
                    "USER_NAME = " + Variables.USER_NAME + "\n" +
                    "OS_NAME = " + Variables.OS_NAME + "\n" +
                    "JCMD_VERSION = " + Variables.JCMD_VERSION + "\n" +
                    "MAVEN_VERSION = " + Variables.MAVEN_VERSION
            );
            return;
        }

        if (args.length >= 3 && args[0].equals("set")) {
            String variableName = args[1];
            String value = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

            try {
                var field = Variables.class.getDeclaredField(variableName);
                if (Modifier.isStatic(field.getModifiers())) {
                    Class<?> type = field.getType();
                    Object converted = convertValue(value, type);
                    field.set(null, converted);
                    System.out.println("Updated " + variableName + " = " + converted);
                } else {
                    System.out.println("Variable is not static: " + variableName);
                }
            } catch (NoSuchFieldException e) {
                System.out.println("No such variable: " + variableName);
            } catch (IllegalAccessException e) {
                System.out.println("Cannot update variable: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid value for variable type: " + e.getMessage());
            }
            return;
        }

        System.out.println("Invalid arguments. Usage: env OR env set <NAME> <value>");
    }

    private Object convertValue(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }
}