package org.jcmd.commands.admin;

import org.jcmd.core.Command;
import org.jcmd.core.JCMD;
import org.jcmd.core.Variables;
import org.jquill.Debug;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Var implements Command {
    protected final JCMD engine;

    private final String NAME = "var";
    private final String DESCRIPTION = "View or modify runtime variables.";
    private final String CATEGORY = "Admin";

    public Var(JCMD engine) {
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
            Debug.warn("Incorrect usage. Usage: var list | var <name> | var set <name> <value>");
            return;
        }

        switch (args[0]) {
            case "list" -> listVariables();
            case "set" -> {
                if (args.length < 3) {
                    Debug.warn("Incorrect usage. Usage: var set <NAME> <value>");
                } else {
                    setVariable(args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                }
            }
            default -> showVariable(args[0]);
        }
    }

    private void listVariables() {
        System.out.println("Variables and constants:\n===================================");
        for (Field field : Variables.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                try {
                    System.out.println(field.getName() + " = " + field.get(null));
                } catch (IllegalAccessException e) {
                    Debug.warn(field.getName() + " = <inaccessible>");
                }
            }
        }
    }

    private void showVariable(String name) {
        try {
            Field field = Variables.class.getDeclaredField(name);
            if (!Modifier.isStatic(field.getModifiers())) {
                Debug.warn("Variable is not static: " + name);
                return;
            }
            System.out.println(name + " = " + field.get(null));
        } catch (NoSuchFieldException e) {
            Debug.warn("No such variable: " + name);
        } catch (IllegalAccessException e) {
            Debug.error("Cannot access variable: " + e.getMessage());
        }
    }

    private void setVariable(String variableName, String value) {
        try {
            Field field = Variables.class.getDeclaredField(variableName);
            if (!Modifier.isStatic(field.getModifiers())) {
                Debug.warn("Variable is not static: " + variableName);
                return;
            }
            Object converted = convertValue(value, field.getType());
            field.set(null, converted);
            Debug.success("Updated " + variableName + " = " + converted);
        } catch (NoSuchFieldException e) {
            Debug.warn("No such variable: " + variableName);
        } catch (IllegalAccessException e) {
            Debug.error("Cannot update variable: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Debug.error("Invalid value for variable type: " + e.getMessage());
        }
    }

    private Object convertValue(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);

        String msg = "Unsupported type: " + type.getName();
        Debug.log(msg);
        throw new IllegalArgumentException(msg);
    }
}