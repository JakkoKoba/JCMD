package org.jcmd.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageReg {

    public final Map<String, String> names = new HashMap<>();
    public final Map<String, String[]> registry = new HashMap<>();

    private static String packRoot = "org.jcmd.commands";

    public PackageReg() {
        try {
            String path = packRoot.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    // Running in IDE or exploded classes
                    scanDirectory(new java.io.File(resource.getFile()));
                } else if ("jar".equals(protocol)) {
                    // Running from a JAR
                    JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                    try (JarFile jarFile = jarConnection.getJarFile()) {
                        scanJar(jarFile, path);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanDirectory(java.io.File baseDir) {
        for (java.io.File folder : Objects.requireNonNull(baseDir.listFiles(java.io.File::isDirectory))) {
            String key = folder.getName().toLowerCase();
            names.put(key, packRoot + "." + folder.getName());

            String[] commands = Arrays.stream(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".class"))))
                    .map(f -> f.getName().replace(".class", ""))
                    .filter(name -> !name.contains("$"))
                    .toArray(String[]::new);

            registry.put(key, commands);
        }
    }

    private void scanJar(JarFile jarFile, String path) {
        Map<String, List<String>> packageCommands = new HashMap<>();

        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (name.startsWith(path) && name.endsWith(".class") && !name.contains("$")) {
                String relative = name.substring(path.length() + 1); // +1 to remove the '/'
                int slash = relative.indexOf('/');
                if (slash > 0) {
                    String pkgKey = relative.substring(0, slash).toLowerCase();
                    String className = relative.substring(slash + 1).replace(".class", "");

                    names.putIfAbsent(pkgKey, packRoot + "." + relative.substring(0, slash));
                    packageCommands.computeIfAbsent(pkgKey, k -> new ArrayList<>()).add(className);
                }
            }
        }

        // Convert lists to arrays
        packageCommands.forEach((key, list) -> registry.put(key, list.toArray(new String[0])));
    }

    public static void setPackRoot(String input) {
        packRoot = input;
    }

    public static String getPackRoot() {
        return packRoot;
    }
}
