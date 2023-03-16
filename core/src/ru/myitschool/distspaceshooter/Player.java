package ru.myitschool.distspaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Arrays;
import java.util.Comparator;

public class Player {
    String name;
    long kills;

    public Player(String name, long kills) {
        this.name = name;
        this.kills = kills;
    }

    static void sortTableOfRecords(Player[] players){
        class Cmp implements Comparator<Player> {
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.kills<p2.kills) return 1;
                if(p1.kills>p2.kills) return -1;
                return 0;
            }
        }
        Arrays.sort(players, new Cmp());
		/*boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 0; i < players.length - 1; i++) {
				if (players[i].kills > players[i + 1].kills) {
					flag = true;
					Player p = players[i];
					players[i] = players[i + 1];
					players[i + 1] = p;
				}
			}
		}*/
    }

    static String tableOfRecordsToString(Player[] players){
        String s = "Рекорды:\n\n";
        for (int i = 0; i < players.length-1; i++) {
            s += i+1+" "+players[i].name+"......."+players[i].kills+"\n";
        }
        return s;
    }

    static void saveTableOfRecords(Player[] players){
        try {
            Preferences prefs = Gdx.app.getPreferences("Records");
            for (int i = 0; i < players.length; i++) {
                prefs.putString("name" + i, players[i].name);
                prefs.putLong("kills" + i, players[i].kills);
            }
            prefs.flush();
        } catch (Exception ignored){
        }
    }

    static void loadTableOfRecords(Player[] players){
        try {
            Preferences prefs = Gdx.app.getPreferences("Table Of Records");
            for (int i = 0; i < players.length; i++) {
                players[i].name = prefs.getString("name" + i, "No info");
                players[i].kills = prefs.getLong("kills" + i, 0);
            }
        } catch (Exception ignored){
        }
    }
}
