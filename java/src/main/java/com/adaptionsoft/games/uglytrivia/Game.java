package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    private static final int BOARD_SIZE = 12;
    private static final int QUESTIONS_PER_CATEGORY = 50;

    private final List<Player> players = new ArrayList<>();

    private final LinkedList<String> popQuestions = new LinkedList<>();
    private final LinkedList<String> scienceQuestions = new LinkedList<>();
    private final LinkedList<String> sportsQuestions = new LinkedList<>();
    private final LinkedList<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;

    public Game() {
        for (int i = 0; i < QUESTIONS_PER_CATEGORY; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast("Science Question " + i);
            sportsQuestions.addLast("Sports Question " + i);
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean isPlayable() {
        return howManyPlayers() >= 2;
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        Player player = players.get(currentPlayer);

        System.out.println(player + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (player.isInPenaltyBox()) {
            player.tryToLeavePenaltyBox(roll);

            if (!player.isGettingOutOfPenaltyBox()) {
                System.out.println(player + " is not getting out of the penalty box");
                return;
            }
            System.out.println(player + " is getting out of the penalty box");
        }

        movePlayerAndAskQuestion(player, roll);
    }

    private void movePlayerAndAskQuestion(Player player, int roll) {
        player.move(roll, BOARD_SIZE);

        System.out.println(player + "'s new location is " + player.getPlace());
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void askQuestion() {
        switch (currentCategory()) {
            case "Pop":
                System.out.println(popQuestions.removeFirst());
                break;
            case "Science":
                System.out.println(scienceQuestions.removeFirst());
                break;
            case "Sports":
                System.out.println(sportsQuestions.removeFirst());
                break;
            default:
                System.out.println(rockQuestions.removeFirst());
                break;
        }
    }

    private String currentCategory() {
        int place = players.get(currentPlayer).getPlace();

        if (place % 4 == 0) return "Pop";
        if (place % 4 == 1) return "Science";
        if (place % 4 == 2) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        Player player = players.get(currentPlayer);

        if (player.isInPenaltyBox() && !player.isGettingOutOfPenaltyBox()) {
            nextPlayer();
            return true;
        }

        System.out.println(player.isInPenaltyBox()
                ? "Answer was correct!!!!"
                : "Answer was corrent!!!!");

        player.addCoin();
        System.out.println(player + " now has " + player.getPurse() + " Gold Coins.");

        boolean notAWinner = !player.hasWon();
        nextPlayer();
        return notAWinner;
    }

    public boolean wrongAnswer() {
        Player player = players.get(currentPlayer);

        System.out.println("Question was incorrectly answered");
        System.out.println(player + " was sent to the penalty box");
        player.sendToPenaltyBox();

        nextPlayer();
        return true;
    }

    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }
}