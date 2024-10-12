package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPLAnalysis {

    public static void main(String[] args) {
        String matchesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/IPL Matches.csv";
        String deliveriesFile = "deliveries.csv";

        try {
            Map<Integer, Integer> matchesPerYear = getMatchesPlayedPerYear(matchesFile);
            Map<String, Integer> matchesWonByTeams =getMatchesWonByTeam(matchesFile);
            System.out.println("Matches per year: " + matchesPerYear);
            System.out.println("Matches won by teams: "+matchesWonByTeams );

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
                int season = Integer.parseInt(nextLine[1]); // Assuming 'season' is at index 1
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
        catch (Exception e){
            e.printStackTrace();
        }
        return matchesWonByTeam;
    }

}
