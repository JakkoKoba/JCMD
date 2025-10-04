package org.jcmd.commands.base;

import org.jcmd.core.Command;
import org.jcmd.core.Variables;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Echo implements Command {

    private final Variables var = new Variables();

    private final String NAME = "echo";
    private final String DESCRIPTION = "Prints back the input text.";
    private final String CATEGORY = "Base";

    @Override
    public String getName() { return NAME; }
    @Override
    public String getDescription() { return DESCRIPTION; }
    @Override
    public String getCategory() { return CATEGORY; }

    @Override
    public void execute(String[] args) {
        String prefix = Variables.VAR_PREFIX;
        String VAR_PREFIX_OPEN;
        String VAR_PREFIX_CLOSE;

        String trimmed = prefix.trim(); // Trim whitespace
        int spaceIndex = trimmed.indexOf(' ');

        if (spaceIndex == -1 || spaceIndex == 0 || spaceIndex == trimmed.length() - 1) {
            throw new IllegalArgumentException("Prefix must contain a space separating open and close delimiters");
        }

        VAR_PREFIX_OPEN = trimmed.substring(0, spaceIndex);
        VAR_PREFIX_CLOSE = trimmed.substring(spaceIndex + 1).trim();

        if (args.length == 0) {
            System.out.println();
            return;
        }

        String input = String.join(" ", args);
        StringBuilder result = new StringBuilder();
        int start = 0;

        while (true) {
            int open = input.indexOf(VAR_PREFIX_OPEN, start);
            if (open == -1) {
                result.append(input.substring(start));
                break;
            }

            result.append(input, start, open);

            int close = input.indexOf(VAR_PREFIX_CLOSE, open + VAR_PREFIX_OPEN.length());
            if (close == -1) {
                // No closing brace: append rest literally
                result.append(input.substring(open));
                break;
            }

            String commandStr = input.substring(open + VAR_PREFIX_OPEN.length(), close).trim();
            if (commandStr.isEmpty()) {
                result.append(prefix);
                start = close + VAR_PREFIX_CLOSE.length();
                continue;
            }

            // Split method name and optional arguments
            String[] parts = commandStr.split("\\s+");
            String methodName = parts[0];
            String[] methodArgs = Arrays.copyOfRange(parts, 1, parts.length);

            try {
                // Find a method in Variables with matching name and argument count
                Method method = findVariableMethod(methodName);
                if (method != null) {
                    // Invoke the method
                    Object output = method.invoke(var, new Object[]{ methodArgs });
                    result.append(output.toString());
                } else {
                    // Unknown method, keep placeholder
                    result.append(VAR_PREFIX_OPEN).append(commandStr).append(VAR_PREFIX_CLOSE);
                }
            } catch (Exception e) {
                result.append("Error in ").append(methodName).append(": ").append(e.getMessage());
            }

            start = close + VAR_PREFIX_CLOSE.length();
        }

        System.out.println(result.toString());
    }

    private Method findVariableMethod(String name) {
        // Variables methods take a String[] as a single argument
        try {
            return var.getClass().getMethod(name, String[].class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}