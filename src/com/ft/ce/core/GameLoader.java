package com.ft.ce.core;

import com.ft.ce.tools.IGame;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

public class GameLoader {
    /**
     * Used to return a class that implements Game from a jar file.
     * @param game File to a jar file that contains a class implementing Game.
     * @return Returns a Game that is found in the jar file.
     */
    private IGame loadGame(File game) {
        try {
            ClassLoader loader = URLClassLoader.newInstance(new URL[] {game.toURI().toURL()}, getClass().getClassLoader());
            JarFile jarFile = new JarFile(game);
            for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext(); ) {
                JarEntry entry = it.next();
                if (entry.toString().contains(".class")) {
                    if(!entry.toString().endsWith("/Game.class")) {
                        String gameToLoad = entry.toString().replace('/', '.').substring(0, entry.toString().length() - 6);
                        Class<?> importedGame = Class.forName(gameToLoad, true, loader);
                        if(IGame.class.isAssignableFrom(importedGame)) {
                            Class<? extends IGame> extendedGame = importedGame.asSubclass(IGame.class);

                            Constructor<? extends IGame> gameConstructor = extendedGame.getConstructor();
                            return gameConstructor.newInstance();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error [loadGame/com.ft.ce.core.GameLoader]");
        }
        return null;
    }

    /**
     * Used to load all Game implementing classes from a given folder.
     * @param gamesDir Path to a folder containing jar files with classes that implement Game.
     * @return Returns an array of classes that implement Game.
     */
    static public IGame[] loadAllGames(String gamesDir) {
        GameLoader gl = new GameLoader();
        File gamesFolder = new File(gamesDir);
        if(gamesFolder.exists()) {
            IGame[] games = new IGame[Objects.requireNonNull(gamesFolder.listFiles()).length];
            int i = 0;
            for (File jar : Objects.requireNonNull(gamesFolder.listFiles())) {
                IGame game = gl.loadGame(jar);
                if(game != null) {
                    games[i] = game;
                    i++;
                } else {
                    System.out.println("No class implementing Game found in " + jar);
                }
            }
            return games;
        } else {
            System.out.println(gamesDir + " does not exist!");
            return null;
        }
    }

    public static void main(String[] args) {
        IGame[] games = loadAllGames(System.getProperty("user.dir") + "/games");
        assert games != null;
        if(games.length > 0) {
            games[0].init();
        }
    }
}