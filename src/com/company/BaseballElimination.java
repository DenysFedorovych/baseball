package com.company;

import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination {
    private BinarySearchST<String,int[]> teams = new BinarySearchST<>();
    private BinarySearchST<Integer,String> bckw = new BinarySearchST<>();
    private int[][] matches;
    private int max=0;
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        String num = in.readLine();
        int b = Integer.parseInt(num);
        matches = new int[b][b];
        while(in.hasNextLine()){
            String[] current = in.readLine().split(" ");
            int[] a = new int[3];
            for(int i=1;i<4;i++){a[i-1]=Integer.parseInt(current[i]);}
            for(int i=4;i<4+matches.length;i++){
                matches[teams.size()][i-4]=Integer.parseInt(current[i]);
            }
            bckw.put(teams.size(),current[0]);
            teams.put(current[0],a);
        }
    }
// create a baseball division from given filename in format specified below

    public int numberOfTeams(){return teams.size();}
// number of teams

    public Iterable<String> teams(){return teams.keys();}
// all teams

    public int wins(String team){return teams.get(team)[0];}
// number of wins for given team

    public int losses(String team){return teams.get(team)[1];}
// number of losses for given team

    public int remaining(String team){return teams.get(team)[2];}
// number of remaining games for given team

    public int against(String team1, String team2){return matches[teams.get(team1)[0]][teams.get(team2)[0]];}
// number of remaining games between team1 and team2

    public boolean isEliminated(String team)
// is given team eliminated?

    public Iterable<String> certificateOfElimination(String team)
// subset R of teams that eliminates given team; null if not eliminated
}