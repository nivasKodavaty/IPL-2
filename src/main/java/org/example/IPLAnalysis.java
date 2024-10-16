package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IPLAnalysis {
    static String matchesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/IPL Matches.csv";
    static String deliveriesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/deliveries.csv";


    public static void main(String[] args) {
        ArrayList<Matches> matchesData = getMatchesData();
        ArrayList<Deliveries> deliveriesData = getDeliveriesData();
//        System.out.println(getMatchesPlayed(matchesData));
//        System.out.println(getMatchesWonByTeam(matchesData));
        System.out.println(runsConceded(deliveriesData));



    }

    public static ArrayList<Deliveries> getDeliveriesData() {
        ArrayList<Deliveries> deliveriesArrayList = new ArrayList<>();
        CSVReader deliveriesReader;
        try {
            deliveriesReader = new CSVReader(new FileReader(deliveriesFile));
            deliveriesReader.readNext();
            String[] nextLine;
            while ((nextLine = deliveriesReader.readNext()) != null) {
                Deliveries deliveriesData = new Deliveries();
                deliveriesData.setMatchId(Integer.parseInt(nextLine[0]));
                deliveriesData.setInning(Integer.parseInt(nextLine[1]));
                deliveriesData.setBattingTeam(nextLine[2]);
                deliveriesData.setBowlingTeam(nextLine[3]);
                deliveriesData.setOver(Integer.parseInt(nextLine[4]));
                deliveriesData.setBall(Integer.parseInt(nextLine[5]));
                deliveriesData.setBatsman(nextLine[6]);
                deliveriesData.setNon_striker(nextLine[7]);
                deliveriesData.setBowler(nextLine[8]);
                deliveriesData.setIs_super_over(Integer.parseInt(nextLine[9]));
                deliveriesData.setWide_runs(Integer.parseInt(nextLine[10]));
                deliveriesData.setBye_runs(Integer.parseInt(nextLine[11]));
                deliveriesData.setLegbye_runs(Integer.parseInt(nextLine[12]));
                deliveriesData.setNo_ball_runs(Integer.parseInt(nextLine[13]));
                deliveriesData.setPenalty_runs(Integer.parseInt(nextLine[14]));
                deliveriesData.setBatsmen_runs(Integer.parseInt(nextLine[15]));
                deliveriesData.setExtra_runs(Integer.parseInt(nextLine[16]));
                deliveriesData.setTotal_runs(Integer.parseInt(nextLine[17]));

                deliveriesArrayList.add(deliveriesData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveriesArrayList;
    }

    public static ArrayList<Matches> getMatchesData() {
        ArrayList<Matches> matchesArrayList = new ArrayList<>();
        CSVReader matchesReader;
        try {
            matchesReader = new CSVReader(new FileReader(matchesFile));
            matchesReader.readNext();
            String[] nextLine;
            while ((nextLine = matchesReader.readNext()) != null) {
                Matches matchData = new Matches();
                matchData.setId(Integer.parseInt(nextLine[0]));
                matchData.setSeason(Integer.parseInt(nextLine[1]));
                matchData.setCity(nextLine[2]);
                matchData.setDate(nextLine[3]);
                matchData.setTeam_1(nextLine[4]);
                matchData.setTeam_2(nextLine[5]);
                matchData.setToss_winner(nextLine[6]);
                matchData.setToss_decision(nextLine[7]);
                matchData.setResult(nextLine[8]);
                matchData.setDl_applied(Integer.parseInt(nextLine[9]));
                matchData.setWinner(nextLine[10]);
                matchData.setWin_by_runs(Integer.parseInt(nextLine[11]));
                matchData.setWin_by_runs(Integer.parseInt(nextLine[12]));
                matchData.setPlayer_of_match(nextLine[13]);
                matchData.setVenue(nextLine[14]);
                matchData.setUmpire1(nextLine[15]);
                matchData.setUmpire2(nextLine[16]);
                matchesArrayList.add(matchData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matchesArrayList;
    }

    public static HashMap<Integer, Integer> getMatchesPlayed(ArrayList<Matches> matchesData) {
        HashMap<Integer, Integer> matchesPlayedPerYear = new HashMap<>();
        for (Matches matches : matchesData) {
            int season = matches.getSeason();
            matchesPlayedPerYear.put(season, matchesPlayedPerYear.getOrDefault(season, 0) + 1);
        }
        return matchesPlayedPerYear;

    }

    public static HashMap<String, Integer> getMatchesWonByTeam(ArrayList<Matches> matchesData) {
        HashMap<String, Integer> matchesWonByTeam = new HashMap<>();
        for (Matches matches : matchesData) {
            String winner = matches.getWinner();
            if (winner!="")
            matchesWonByTeam.put(winner, matchesWonByTeam.getOrDefault(winner, 0) + 1);
        }
        return matchesWonByTeam;
    }

    public static Map<String, Integer> runsConceded(ArrayList<Deliveries> deliveriesData) {
        Map<String, Integer> runsConcedeByTeams = new HashMap<>();
        for (Deliveries deliveries : deliveriesData) {
            int match_id = deliveries.getMatchId();
            if (match_id >= 577) {
                int runs_conceded = deliveries.getTotal_runs();
                if (runs_conceded > 0) {
                    String bowling_team = deliveries.getBowlingTeam();
                    if (runsConcedeByTeams.containsKey(bowling_team)){
                        runsConcedeByTeams.put(bowling_team, runsConcedeByTeams.get(bowling_team)+runs_conceded);
                    }
                    else {
                        runsConcedeByTeams.put(bowling_team, 0);
                    }
                }
            }
        }

        return runsConcedeByTeams;
    }

    public static Map<String, Double> economicalBowlers(CSVReader reader) throws IOException, CsvException {
        Map<String, Integer> runs_Conceded = new HashMap<>();
        Map<String, Integer> balls_bowled = new HashMap<>();
        List<String[]> list = reader.readAll();
        list.removeFirst();
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
        int count = 0;
        for (Map.Entry<String, Double> entry : economy.entrySet()) {
            if (count > 10) {
                break;
            }
            sortedAndtrimmed.put(entry.getKey(), entry.getValue());
            count++;

        }

        return sortedAndtrimmed;
    }

}
