package controller;

import java.util.ArrayList;
import java.util.Scanner;
import model.MutableReversiModel;
import model.RegularReversiModel;
import model.RepresentativeColor;
import model.ReversiModelStatus;
import strategy.CompleteStrategy;
import strategy.InfallibleStrategy;
import strategy.MinimaxStrategy;
import view.ReversiTextualView;
import model.RowColPair;

/**
 * A simple controller, used to test and let others understand how this code works easier.
 * User will play with a standard reversi game with side length 6. User need to enter two numbers
 * as the coordinators for the placement position, or P to pass and Q to quit the game. When
 * the game is over, winner will be the player who has higher score which means the player who
 * has more cells in its color. Or we can choose to let two ai player to against each other.
 */
public class SimpleController {
  /**
   * entry point of this program.
   *
   * @param args default parameter
   */
  public static void main(String[] args) {
    strategyCompetition(new CompleteStrategy(new MinimaxStrategy()),
        new CompleteStrategy(new MinimaxStrategy()));
  }

  /**
   * Using two strategy against each other in the text view.
   *
   * @param strategy1 strategy for black user
   * @param strategy2 strategy for white user
   */
  public static void strategyCompetition(InfallibleStrategy strategy1,
                                         InfallibleStrategy strategy2) {
    MutableReversiModel model = new RegularReversiModel(new ArrayList<>(),
        new ReversiModelStatus());
    model.startGame();
    ReversiTextualView view = new ReversiTextualView(model);
    while (!model.isGameOver()) {
      System.out.println(view);
      RepresentativeColor turn = model.getTurn();
      RowColPair pair;
      if (model.hasToPass()) {
        model.makePass(model.getTurn());
        continue;
      }
      if (turn == RepresentativeColor.BLACK) {
        pair = strategy1.choosePosition(model, turn);
      } else {
        pair = strategy2.choosePosition(model, model.getTurn());
      }
      if (pair == null) {
        model.makePass(model.getTurn());
      } else {
        model.placeMove(pair, model.getTurn());
      }
      System.out.println();
    }
    setGameOverState(model);
  }

  /**
   * Interact with the controller by typing command.
   */
  public static void tryToPlay() {
    MutableReversiModel model = new RegularReversiModel(new ArrayList<>(),
        new ReversiModelStatus());
    ReversiTextualView view = new ReversiTextualView(model);
    Scanner scanner = new Scanner(System.in);
    while (!model.isGameOver()) {
      RepresentativeColor turn = model.getTurn();
      System.out.println("Now is " + turn);
      System.out.println(view);
      if (model.hasToPass()) {
        System.out.println("No valid move, you have to pass");
        String m1 = getInput(scanner);
        while (!m1.equals("P")) {
          System.out.println("No valid move, you have to pass");
          m1 = getInput(scanner);
        }
        model.makePass(turn);
        continue;
      }
      String m1 = getInput(scanner);
      if (m1.equals("P")) {
        model.makePass(turn);
        continue;
      } else if (m1.equals("Q")) {
        return;
      }
      String m2 = getInput(scanner);
      if (m2.equals("P")) {
        model.makePass(turn);
        continue;
      } else if (m2.equals("Q")) {
        return;
      }
      try {
        model.placeMove(new RowColPair(Integer.parseInt(m1), Integer.parseInt(m2)), turn);
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.err.println("Invalid move, try again");
      }
    }
    setGameOverState(model);
  }

  /**
   * Print message to notify the user game is over.
   *
   * @param model the current model
   */
  private static void setGameOverState(MutableReversiModel model) {
    System.out.println("Game Over");
    String winner = " ";
    if (model.getWinner() == RepresentativeColor.BLACK) {
      winner = "BLACK Wins";
    } else if (model.getWinner() == RepresentativeColor.WHITE) {
      winner = "WHITE Wins";
    } else {
      winner = "Tie Game";
    }
    System.out.println(winner);
  }

  /**
   * Get parameter from user's input.
   *
   * @param scanner given scanner
   * @return An integer represent coordinator in string form
   */
  private static String getInput(Scanner scanner) {
    String s = " ";
    while (scanner.hasNext()) {
      s = scanner.next();
      if (s.equals("P") || s.equals("Q")) {
        return s;
      }
      try {
        return String.valueOf(Integer.parseInt(s));
      } catch (NumberFormatException e) {
        System.err.println("Enter again");
      }
    }
    return s;
  }
}
