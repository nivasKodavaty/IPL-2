package org.example;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;

public class IPLAnalysis {
    static String matchesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/IPL Matches.csv";
    static String deliveriesFile = "/Users/nivaskodavaty/Desktop/IPL-2/src/data/deliveries.csv";


    public static void main(String[] args) {
        ArrayList<Matches> matchesData = getMatchesData();
        ArrayList<Deliveries> deliveriesData = getDeliveriesData();

        System.out.println(getMatchesPlayed(matchesData));
        System.out.println(getMatchesWonByTeam(matchesData));
        System.out.println(getRunsConceded(deliveriesData));
        System.out.println(getEconomicalBowlers(deliveriesData));

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
        }
        catch (Exception e) {
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

    public static Map<String, Integer> getRunsConceded(ArrayList<Deliveries> deliveriesData) {
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

    public static Map<String, Double> getEconomicalBowlers(ArrayList<Deliveries> deliveriesData) { // Map to store total balls bowled by each bowler
        HashMap<String, Integer> balls_bowled = new HashMap<>();
        HashMap<String, Integer> runs_conceded = new HashMap<>();
        for (Deliveries delivery : deliveriesData) {
            int match_id = delivery.getMatchId();
            if (match_id > 517 && match_id < 577) {
                String bowler = delivery.getBowler();
                int total_runs = delivery.getTotal_runs();
                balls_bowled.put(bowler, balls_bowled.getOrDefault(bowler, 0) + 1);
                runs_conceded.put(bowler, runs_conceded.getOrDefault(bowler, 0) + total_runs);
            }
        }
        TreeMap<String, Double> bowlers_economy = new TreeMap<>();
        for (String bowler : balls_bowled.keySet()) {
            int balls = balls_bowled.get(bowler);
            int runs = runs_conceded.getOrDefault(bowler, 0);

            double economyRate = (double) runs / (balls / 6.0);
            bowlers_economy.put(bowler, economyRate);
        }
        List<Map.Entry<String, Double>> sortedBowlers = new ArrayList<>(bowlers_economy.entrySet());
        sortedBowlers.sort(Map.Entry.comparingByValue());
        HashMap<String, Double> top10EconomicalBowlers = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            Map.Entry<String, Double> entry = sortedBowlers.get(i);
            top10EconomicalBowlers.put(entry.getKey(), entry.getValue());
        }
        return top10EconomicalBowlers;
    }

}
