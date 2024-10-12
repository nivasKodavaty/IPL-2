package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IPLAnalysis {

    public static void main(String[] args) {
        String matchesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/IPL Matches.csv";
        String deliveriesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/deliveries.csv";

        try {

            System.out.println(getMatchesPlayedPerYear(matchesFile));
            System.out.println(getMatchesWonByTeam(matchesFile));
            System.out.println(runsConceded(deliveriesFile));
            System.out.println(economicalBowlers(deliveriesFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Integer> getMatchesPlayedPerYear(String matchesFile) throws IOException, CsvValidationException {
        Map<Integer, Integer> matchesPerYear = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(matchesFile))) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                int season = Integer.parseInt(nextLine[1]);
                matchesPerYear.put(season, matchesPerYear.getOrDefault(season, 0) + 1);
            }
        }
        return matchesPerYear;
    }

    public static Map<String, Integer> getMatchesWonByTeam(String matchesFile) throws IOException, CsvValidationException {
        Map<String, Integer> matchesWonByTeam = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(matchesFile))) {
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String winner = nextLine[10];
                if (!winner.isEmpty()) {
                    matchesWonByTeam.put(winner, matchesWonByTeam.getOrDefault(winner, 0) + 1);
                }
            }
        }

        return matchesWonByTeam;
    }

    public static Map<String, Integer> runsConceded(String delieversFile) {
        Map<String, Integer> map = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(delieversFile))) {
            List<String[]> list = reader.readAll();
            list.remove(0);
            for (String[] strings : list) {
                if (Integer.parseInt(strings[0]) > 577 && Integer.parseInt(strings[16]) > 0) {
                    String team = strings[3];
                    int extraruns = Integer.parseInt(strings[16]);
                    if (map.containsKey(team)) {
                        map.put(team, map.get(team) + extraruns);
                    } else {
                        map.put(team, extraruns);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Double> economicalBowlers(String delieversFile) {
        Map<String, Integer> runs_Conceded = new HashMap<>();
        Map<String, Integer> ball_bowled = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(delieversFile))) {
            List<String[]> list = reader.readAll();
            list.remove(0);

            for (String[] strings : list) {
                int match_id = Integer.parseInt(strings[0]);
                int total_runs = Integer.parseInt(strings[17]);
                String bowler = strings[8];
                if ((match_id >= 518 && match_id <= 576)) {
                    if (ball_bowled.containsKey(bowler)) {
                        ball_bowled.put(bowler, ball_bowled.get(bowler) + 1);
                    } else {
                        ball_bowled.put(bowler, 1);
                    }
                }

                if ((match_id >= 518 && match_id <= 576) && total_runs > 0) {
                    if (runs_Conceded.containsKey(bowler)) {
                        runs_Conceded.put(bowler, runs_Conceded.get(bowler) + total_runs);
                    } else {
                        runs_Conceded.put(bowler, total_runs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Double> economy = new HashMap<>();
        for (String s : ball_bowled.keySet()) {
            double overs = (double) ball_bowled.get(s) / 6;
            economy.put(s, (double) runs_Conceded.get(s) / overs);

        }
        HashMap<String,Double> sortedAndtrimmed = new HashMap<>();
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<>(economy.entrySet());
        list.sort(Map.Entry.comparingByValue());
        for (int i = 0; i < Math.min(10, list.size()); i++) {
            Map.Entry<String, Double> entry = list.get(i);
            sortedAndtrimmed.put(entry.getKey(), entry.getValue());
        }
        return sortedAndtrimmed;
    }

}
