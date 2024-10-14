package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IPLAnalysis {

    public static void main(String[] args) {
        String matchesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/IPL Matches.csv";
        String deliveriesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/deliveries.csv";
        CSVReader matchesReader;
        CSVReader deliveriesReader ;
        CSVReader matchesReader1;
        CSVReader deliveriesReader2 ;

        try {

            matchesReader = new CSVReader(new FileReader(matchesFile));
            deliveriesReader = new CSVReader(new FileReader(deliveriesFile));
            matchesReader1 = new CSVReader(new FileReader(matchesFile));
            deliveriesReader2 = new CSVReader(new FileReader(deliveriesFile));
            System.out.println(matchesPlayed(matchesReader));
            System.out.println(MatchesWonByTeam(matchesReader1));
            System.out.println(runsConceded(deliveriesReader));
            System.out.println(economicalBowlers(deliveriesReader2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Integer> matchesPlayed(CSVReader reader) throws CsvValidationException, IOException {
        Map<Integer, Integer> matchesPerYear = new HashMap<>();
        String[] nextLine;
        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            int season = Integer.parseInt(nextLine[1]);
            matchesPerYear.put(season, matchesPerYear.getOrDefault(season, 0) + 1);
        }

        return matchesPerYear;

    }

    public static Map<String, Integer> MatchesWonByTeam(CSVReader reader) throws IOException, CsvValidationException {
        Map<String, Integer> matchesWonByTeam = new HashMap<>();
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                String winner = nextLine[10];
                if (!winner.isEmpty()) {
                    matchesWonByTeam.put(winner, matchesWonByTeam.getOrDefault(winner, 0) + 1);
                }
            }


        return matchesWonByTeam;
    }

    public static Map<String, Integer> runsConceded(CSVReader reader) throws IOException, CsvException {
        Map<String, Integer> map = new HashMap<>();
        String[] nextLine;
        reader.readNext();
        while ((nextLine= reader.readNext())!=null){
            if (Integer.parseInt(nextLine[0]) > 577 && Integer.parseInt(nextLine[16]) > 0) {
                String team = nextLine[3];
                int extraruns = Integer.parseInt(nextLine[16]);
                if (map.containsKey(team)) {
                    map.put(team, map.get(team) + extraruns);
                } else {
                    map.put(team, extraruns);
                }
            }
        }
//            List<String[]> list = reader.readAll();
//            list.remove(0);
//            for (String[] nextLine : list) {
//                if (Integer.parseInt(nextLine[0]) > 577 && Integer.parseInt(nextLine[16]) > 0) {
//                    String team = nextLine[3];
//                    int extraruns = Integer.parseInt(nextLine[16]);
//                    if (map.containsKey(team)) {
//                        map.put(team, map.get(team) + extraruns);
//                    } else {
//                        map.put(team, extraruns);
//                    }
//                }
//            }

        return map;
    }

    public static Map<String, Double> economicalBowlers(CSVReader reader) throws IOException, CsvException {
        Map<String, Integer> runs_Conceded = new HashMap<>();
        Map<String, Integer> balls_bowled = new HashMap<>();
            List<String[]> list = reader.readAll();
            list.remove(0);
            for (String[] strings : list) {
                int match_id = Integer.parseInt(strings[0]);
                int total_runs = Integer.parseInt(strings[17]);
                String bowler = strings[8];
                if ((match_id >= 518 && match_id <= 576)) {
                    if (balls_bowled.containsKey(bowler)) {
                        balls_bowled.put(bowler, balls_bowled.get(bowler) + 1);
                    } else {
                        balls_bowled.put(bowler, 1);
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
        TreeMap<String, Double> economy = new TreeMap<>();
        for (String s : balls_bowled.keySet()) {
            double overs = (double) balls_bowled.get(s) / 6;
            economy.put(s, (double) runs_Conceded.get(s) / overs);

        }
        HashMap<String, Double> sortedAndtrimmed = new HashMap<>();
        int count=0;
        for(Map.Entry<String, Double> entry : economy.entrySet()){
            if (count>10){
                break;
            }
            sortedAndtrimmed.put(entry.getKey(), entry.getValue());
            count++;

        }

        return sortedAndtrimmed;
    }

}
