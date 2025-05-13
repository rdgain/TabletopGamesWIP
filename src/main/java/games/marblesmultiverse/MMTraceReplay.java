package games.marblesmultiverse;

import com.google.gson.Gson;
import core.AbstractParameters;
import core.AbstractPlayer;
import core.Game;
import evaluation.listeners.IGameListener;
import games.GameType;
import gui.AbstractGUIManager;
import gui.GUI;
import gui.GamePanel;
import players.TraceReplay;
import players.human.ActionController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static core.Game.runOne;

public class MMTraceReplay {
    public static void main(String[] args) throws IOException {
        String playTracePath = "MMPlayTraceTest/MM-trace-2.json";
        boolean useGUI = true;
        int turnPause = 10;
        ActionController ac = new ActionController();

        String jsonFile =  new String(Files.readAllBytes(Paths.get(playTracePath)));
        Gson gson = new Gson();
        long[] playTraces = gson.fromJson(jsonFile, long[].class); // This is Long due to also saving the random seed of the game.
        long seed = playTraces[playTraces.length-1];

        /* Set up players for the game */
        ArrayList<Integer> actionTraces = new ArrayList<>();
        for (int i = 0; i < playTraces.length-1; i++) {   // Convert playTrace into integer array
            actionTraces.add(Math.toIntExact(playTraces[i]));
        }
        ArrayList<AbstractPlayer> players = new ArrayList<>();
        players.add(new TraceReplay(actionTraces));
        players.add(new TraceReplay(actionTraces));

        /* Run! */
        MMParameters params = new MMParameters();
        params.saveTraceEnabled = false;
        runOne(GameType.valueOf("MultiverseMarbles"), params, players, seed, false, null, useGUI ? ac : null, turnPause);
    }
}
