/**
 * @author Hayden Parker and Liam Parker
 * M359 AP Java
 * Mr. Nichols Period 8
 */
package Final_Project;

import java.io.FileNotFoundException;
import java.util.*;

public class GameDriver {
	private static Game g;
	private static int choice;
	private static String letter;
	private static String word;
	private static Scanner inF = new Scanner(System.in);
	private static final int MAX_PLAYERS = 3;
	private static String situation;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("How many people are playing? (Max of " + MAX_PLAYERS + ")");
		int numPlayers = 0;
		while (numPlayers == 0 || numPlayers > MAX_PLAYERS) {
			try {
				numPlayers = inF.nextInt();
			} catch (Exception e) {
				System.out.println("Please input a correct option");
			}
			inF.nextLine();
			if (numPlayers == 0 || numPlayers > MAX_PLAYERS) {
				System.out.println("Please input a correct option");
			}
		}
		int numCPUs = MAX_PLAYERS - numPlayers;
		ArrayList<String> players = new ArrayList<String>();
		for (int i = 1; i <= numPlayers; i++) {
			System.out.println("What is player " + i + "'s name?");
			String playerName = inF.nextLine();
			players.add(playerName);
		}
		for (int i = 1; i <= numCPUs; i++) {
			players.add("CPU" + i);
		}
		g = new Game("FortuneWords.txt", players);
		int playerTurn = 1, round = 1, player = 1;
		situation = "";
		boolean game = true, round_ = true, AITurn;
		g.pickRandomWord();
		while (game) {
			while (round_) {
				g.displayWord(round, playerTurn, situation, player);
				situation = "";
				AITurn = false;
				int check = 0;
				if (playerTurn > MAX_PLAYERS - numCPUs) {
					AITurn = true;
				}
				if (AITurn) { // CPU
					giveOptions(playerTurn);
					int unGuessedLetters = 0;
					for (int i = 0; i < g.getLetters().length; i++) {
						if (Character.isLetter(g.getLetters()[i])) {
							if (g.getGuessedLetters()[i] == 0) {
								unGuessedLetters++;
							}
						}
					}
					if (unGuessedLetters < 3) {
						CPUAskAnswer();
						boolean correct = g.checkAnswer(word);
						if (correct) {
							System.out.println("You got it correct!");
							player = playerTurn;
							g.giveWinnings(playerTurn);
							break;
						}
					}
					if (g.getPlayer(playerTurn).getMoney() >= 1000 && check == 0) {
						System.out.println("CPU chose to purchase a vowel!\n");
						g.getPlayer(playerTurn).deductMoney(250);
						while (g.checkConsonant(letter) || letter.length() > 2
								|| letter.equalsIgnoreCase("") && check == 0) {
							CPUGuessVowel();
						}
						if (!g.checkLetter(letter, playerTurn)) {
							playerTurn++;
						} else {
							check = 1;
						}
					} else {
						System.out.println("CPU chose to spin!");
						g.spin();
						System.out.println("");
						if (g.getLetterValue() > 0) {
							System.out.println("\n\nYou spun $" + g.getLetterValue());
							letter = "a";
							while (!g.checkConsonant(letter) || letter.length() > 2 || letter.equalsIgnoreCase("")
									|| g.getGLetters().contains(letter)) {
								CPUGuessConsonant();
							}
							if (!g.checkLetter(letter, playerTurn)) {
								playerTurn++;
							}
						} else if (g.getLetterValue() == 0) {
							System.out.println("BANKRUPT!");
							g.getPlayer(playerTurn).setMoney(0);
							playerTurn++;
						} else if (g.getLetterValue() == -1) {
							System.out.println("LOSE A TURN!");
							playerTurn++;
						}
					}
				} else { // PLAYER
					giveOptions(playerTurn);
					choice = 0;
					try {
						choice = inF.nextInt();
					} catch (Exception e) {
						System.out.println("Please input a correct Option");
						situation = "exception";
						player = playerTurn;
					}
					inF.nextLine();
					if (choice == 1) { // Solve
						askAnswer();
						boolean correct = g.checkAnswer(word);
						if (correct) {
							System.out.println("You got it correct!");
							player = playerTurn;
							g.giveWinnings(playerTurn);
							break;
						} else {
							System.out.println("Sorry that isn't correct!");
							player = playerTurn;
							playerTurn++;
							situation = "incorrect";
						}
					} else if (choice == 2) { // Buy Vowel
						if (g.getPlayer(playerTurn).getMoney() >= 250) {
							g.getPlayer(playerTurn).deductMoney(250);
							letter = "z";
							while (g.checkConsonant(letter) || letter.length() > 2 || letter.equalsIgnoreCase("")
									|| g.getGLetters().contains(letter)) { // check loop
								guessVowel();
							}
							if (!g.checkLetter(letter, playerTurn)) {
								playerTurn++;
							}
						} else {
							System.out.println("You don't have enough money!");
							situation = "not enough money";
							player = playerTurn;
						}
					} else if (choice == 3) { // Spin to Play
						g.spin();
						if (g.getLetterValue() > 0) {
							System.out.println("You spun $" + g.getLetterValue());
							letter = "a";
							while (!g.checkConsonant(letter) || letter.length() > 2 || letter.equalsIgnoreCase("")
									|| g.getGLetters().contains(letter)) { // check loop
								guessConsonant();
							}
							if (!g.checkLetter(letter, playerTurn)) {
								playerTurn++;
							}
						} else if (g.getLetterValue() == 0) { // checks if you rolled bankruptcy
							System.out.println("BANKRUPT!");
							g.getPlayer(playerTurn).setMoney(0);
							playerTurn++;
						} else if (g.getLetterValue() == -1) { // checks if you rolled lose a turn
							System.out.println("LOSE A TURN!");
							playerTurn++;
						}
					}
				}
				if (playerTurn > MAX_PLAYERS) { // loops players turn
					playerTurn = 1;
				}
				round_ = g.checkWord();
			}
			round++;
			playerTurn = 1;
			g.clearEarnings();
			situation = "correct";
			g.pickRandomWord();
			if (round == 5) { // ends game
				game = false;
			}
			round_ = true;
		}
		System.out.println("This is the end of the game!");
		int score = 0, j = 0;
		for (int i = 0; i < g.getPlayers().length; i++) { // finds the player with the highest score
			if (g.getPlayers()[i].getGlobalMoney() > score) {
				score = g.getPlayers()[i].getGlobalMoney();
				j = i;
			}
		}
		System.out.println("An the winner is " + g.getPlayers()[j].getName() + " with a score of " + score + "!");
		for (int i = 0; i < g.getPlayers().length; i++) { // prints the rest of the players score
			if (i != j) {
				System.out.println(
						g.getPlayers()[i].getName() + " has a score of " + g.getPlayers()[i].getGlobalMoney() + "!");
			}
		}
		System.out.println("\nThanks for Playing!");
	}

	public static void giveOptions(int turn) {
		System.out.println("\n");
		System.out.println("1. Solve");
		if (g.getPlayer(turn).getMoney() > 250) {
			System.out.println("2. Buy Vowel -$250");
			System.out.println("3. Spin");
		} else {
			System.out.println("\n3. Spin");
		}
		for (int i = 0; i < 38; i++) // Lines to separate main text from input lines
			System.out.println();
	}

	public static void guessConsonant() {
		System.out.println();
		System.out.println("Guess Consonant");
		letter = inF.nextLine();
		letter.toLowerCase();
	}

	public static void CPUGuessConsonant() {
		System.out.println();
		System.out.println("Guess Consonant");
		int guess = (int) (Math.random() * g.getAbet().size());
		letter = g.getAbet().get(guess);
		System.out.println("CPU guessed " + letter);
	}

	public static void guessVowel() {
		System.out.println();
		System.out.println("Guess Vowel");
		letter = inF.nextLine();
		letter.toLowerCase();
	}

	public static void CPUGuessVowel() {
		System.out.println();
		System.out.println("Guess Vowel");
		int guess = (int) (Math.random() * g.getVowels().size());
		letter = g.getVowels().get(guess);
	}

	public static void askAnswer() {
		System.out.println();
		System.out.println("What is your solution?");
		word = inF.nextLine();
		word.toLowerCase();
	}

	public static void CPUAskAnswer() {
		System.out.println();
		System.out.println("What is your solution?");
		word = g.getWord();
		word.toLowerCase();
		System.out.println("CPU guessed: " + g.getWord());
	}
}
