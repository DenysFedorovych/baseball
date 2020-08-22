//package com.company;

import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;
public class BaseballElimination {
    private BinarySearchST<String, int[]> teams = new BinarySearchST<>();
    private BinarySearchST<Integer, String> bckw = new BinarySearchST<>();
    private int[][] matches;
    private int max = 0;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        String num = in.readLine();
        int b = Integer.parseInt(num);
        matches = new int[b][b];
        while (!in.isEmpty()) {
            String current = in.readString();
            int[] a = new int[4];
            a[0] = teams.size();
            for (int i = 1; i < 4; i++) {
                a[i] = in.readInt();
            }
            for (int i = 0; i < matches.length; i++) {
                matches[teams.size()][i] = in.readInt();
            }
            teams.put(current, a);
            bckw.put(a[0], current);
            max = Math.max(max, a[1]);
        }
    }
// create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return teams.size();
    }
// number of teams

    public Iterable<String> teams() {
        return teams.keys();
    }
// all teams

    public int wins(String team) {
        check(team);
        return teams.get(team)[1];
    }
// number of wins for given team

    public int losses(String team) {
        check(team);
        return teams.get(team)[2];
    }
// number of losses for given team

    public int remaining(String team) {
        check(team);
        return teams.get(team)[3];
    }
// number of remaining games for given team

    public int against(String team1, String team2) {
        check(team1);
        check(team2);
        return matches[teams.get(team1)[0]][teams.get(team2)[0]];
    }
// number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        check(team);
        if ((wins(team) + remaining(team)) < max) {
            return true;
        }
        int a = matches.length;
        FlowNetwork FN = new FlowNetwork(a * a);
        int n = teams.get(team)[0];
        int c = 1;
        for (int i = 0; i < a; i++) {
            int k = wins(team) + remaining(team) - wins(bckw.get(i));
            FN.addEdge(new FlowEdge(FN.V() - a + i, FN.V() - 1, k));
            if (i == n) {
                continue;
            }
            for (int j = i; j < a; j++) {
                if (j == n) {
                    continue;
                }
                FN.addEdge(new FlowEdge(0, c, matches[i][j]));
                FN.addEdge(new FlowEdge(c, FN.V() - a + i, Double.POSITIVE_INFINITY));
                FN.addEdge(new FlowEdge(c, FN.V() - a + j, Double.POSITIVE_INFINITY));
                c++;
            }
        }
        FordFulkerson FF = new FordFulkerson(FN, 0, FN.V() - 1);
        for (FlowEdge each : FN.adj(0)) {
            if (each.flow() != each.capacity()) {
                return true;
            }
        }
        return false;
    }
// is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        check(team);
        int a = matches.length;
        FlowNetwork FN = new FlowNetwork(a * a);
        int n = teams.get(team)[0];
        int c = 1;
        ArrayList<String> b = new ArrayList<>();
        if (!isEliminated(team)) {
            return null;
        } else {
            for (int i = 0; i < a; i++) {
                int k = wins(team) + remaining(team) - wins(bckw.get(i));
                if (k < 0) {
                    b.add(bckw.get(i));
                } else {
                    FN.addEdge(new FlowEdge(FN.V() - a + i, FN.V() - 1, k));
                    if (i == n) {
                        continue;
                    }
                    for (int j = i; j < a; j++) {
                        if (j == n) {
                            continue;
                        }
                        FN.addEdge(new FlowEdge(0, c, matches[i][j]));
                        FN.addEdge(new FlowEdge(c, FN.V() - a + i, Double.POSITIVE_INFINITY));
                        FN.addEdge(new FlowEdge(c, FN.V() - a + j, Double.POSITIVE_INFINITY));
                        c++;
                    }
                }
            }
            FordFulkerson FF = new FordFulkerson(FN, 0, FN.V() - 1);
            for (int i = 0; i < a; i++) {
                if (FF.inCut(FN.V() - a + i)) {
                    b.add(bckw.get(i));
                }
            }
            return b;
        }
    }

    private void check(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException("team doesn't exist");
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams4.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
// subset R of teams that eliminates given team; null if not eliminated
}