/**
 * @author Hayden Parker and Liam Parker
 * M359 AP Java
 * Mr. Nichols Period 8
 */
package Final_Project;

import java.io.*;
import java.util.*;

public class Game {
	private String word;
	private int category;
	private ArrayList<String> words = new ArrayList<String>();
	private ArrayList<String> categories = new ArrayList<String>();
	private int categoryNum;
	private int wordsPerCategory;
	private char[] letters;
	private int[] guessedLetters;
	private Player[] players;
	private int letterValue = -2;
	private int[] wheel;
	private ArrayList<String> vowels = new ArrayList<String>();
	private ArrayList<String> gLetters = new ArrayList<String>();
	private ArrayList<String> abet = new ArrayList<String>();
	private final int BASE_WINNINGS = 750;

	public Game(String file, ArrayList<String> names) throws FileNotFoundException {
		players = new Player[names.size()];
		for (int i = 0; i < names.size(); i++) {
			players[i] = new Player(names.get(i));
		}
		loadWords(file);
		loadWheel();
		loadVowels();
		loadAbet();
	}

	/**
	 * Description: loads the words used in the game
	 */
	public void loadWords(String file) throws FileNotFoundException {
		Scanner inF = new Scanner(new File(file));
		String temp;
		temp = inF.nextLine();
		while (!temp.isEmpty()) {
			categories.add(temp);
			temp = inF.nextLine();
		}
		temp = inF.nextLine();
		while (!temp.isEmpty()) {
			words.add(temp.toLowerCase());
			if (inF.hasNextLine()) {
				temp = inF.nextLine();
			} else {
				temp = "";
			}
		}
		inF.close();
		categoryNum = categories.size();
		wordsPerCategory = words.size() / categoryNum;
	}

	/**
	 * Description: loads the possible consonants that could be guessed
	 */
	public void loadAbet() {
		abet.add("b");
		abet.add("c");
		abet.add("d");
		abet.add("f");
		abet.add("g");
		abet.add("h");
		abet.add("j");
		abet.add("k");
		abet.add("l");
		abet.add("m");
		abet.add("n");
		abet.add("p");
		abet.add("q");
		abet.add("r");
		abet.add("s");
		abet.add("t");
		abet.add("v");
		abet.add("w");
		abet.add("x");
		abet.add("z");
	}

	/**
	 * Description: loads the possibilities that could be spun from the wheel
	 */
	public void loadWheel() {
		wheel = new int[24];
		wheel[0] = 650;
		wheel[1] = 500;
		wheel[2] = 900;
		wheel[3] = 0; // BANKRUPT
		wheel[4] = 2500;
		wheel[5] = 1000;
		wheel[6] = 600;
		wheel[7] = 700;
		wheel[8] = 600;
		wheel[9] = 650;
		wheel[10] = 1000;
		wheel[11] = 700;
		wheel[12] = 0; //
		wheel[13] = 600;
		wheel[14] = 550;
		wheel[15] = 500;
		wheel[16] = 600;
		wheel[17] = 0; //
		wheel[18] = 650;
		wheel[19] = 1000;
		wheel[20] = 700;
		wheel[21] = -1; // LOSE TURN
		wheel[22] = 800;
		wheel[23] = 1000;
	}

	/**
	 * Description: loads the vowels into a ArrayList
	 */
	public void loadVowels() {
		vowels.add("a");
		vowels.add("e");
		vowels.add("i");
		vowels.add("o");
		vowels.add("u");
		vowels.add("y");
	}

	/**
	 * Description: picks a random word from the list of words in the category
	 */
	public void pickRandomWord() {
		category = ((int) (Math.random() * categoryNum)) + 1;
		word = words.get(((int) (Math.random() * wordsPerCategory)) + (category - 1) * wordsPerCategory);
		convertWordToChars(word);
	}

	/**
	 * Description: converts the word into char array
	 */
	public void convertWordToChars(String w) {
		letters = w.toCharArray();
		guessedLetters = new int[letters.length];
		for (int i = 0; i < letters.length; i++) {
			guessedLetters[i] = 0;
		}
	}

	/**
	 * Description: displays the interface with the correct information
	 */
	public void displayWord(int round, int turn, String situation, int player) {
		for (int i = 0; i < 55; i++)
			System.out.println();
		if (situation.equalsIgnoreCase("incorrect")) {
			System.out.println("Sorry that is not the correct answer " + players[player - 1].getName());
		}
		if (situation.equalsIgnoreCase("correct")) {
			System.out.println("You got it correct " + players[player - 1].getName() + "!");
		}
		if (situation.equalsIgnoreCase("not enough money")) {
			System.out.println("You don't have enough money " + players[player - 1].getName() + "!");
		}
		if (situation.equalsIgnoreCase("exception")) {
			System.out.println("Please input a correct choice " + players[player - 1].getName() + "!");
		}
		System.out.println();
		System.out.println("Winnings:");
		for (int i = 0; i < players.length; i++) {
			System.out.print(players[i].getName() + ": $" + players[i].getGlobalMoney() + "  ");
		}
		System.out.println("\n");
		System.out.println("Round " + round);
		System.out.println("----------------------------");
		for (int i = 0; i < letters.length; i++) {
			if (Character.isLetter(letters[i])) {
				if (guessedLetters[i] == 1) {
					System.out.print(letters[i] + " ");
				} else {
					System.out.print("_ ");
				}
			} else {
				System.out.print(letters[i] + " ");
			}
		}
		System.out.println();
		for (int i = 0; i < players.length; i++) {
			System.out.print(players[i].getName() + ": $" + players[i].getMoney() + "  ");
		}
		System.out.print("\n\nCategory: ");
		System.out.println(categories.get(category - 1));
		if (turn == 1) {
			if (letterValue == 0) {
				System.out.println("\n" + players[players.length - 1].getName() + " went Bankrupt!");
			}
			if (letterValue == -1) {
				System.out.println("\n" + players[players.length - 1].getName() + " lost their turn!");
			}
		} else {
			if (letterValue == 0) {
				System.out.println("\n" + players[turn - 2].getName() + " went Bankrupt!");
			}
			if (letterValue == -1) {
				System.out.println("\n" + players[turn - 2].getName() + " lost their turn!");
			}
		}
		System.out.print("\nCurrent Player's Turn: " + players[turn - 1].getName());
	}

	/**
	 * Description: spins the "wheel" returning a random spin
	 */
	public void spin() {
		letterValue = wheel[((int) (Math.random() * (wheel.length - 1))) + 1];
	}

	/**
	 * Description: returns a boolean if the letter guessed is valid
	 * 
	 * @return return a boolean if letter guessed is valid
	 */
	public boolean checkLetter(String c, int turn) {
		if (c.equalsIgnoreCase("")) {
			return false;
		}
		gLetters.add(c);
		int check = 0;
		for (int j = 0; j < word.length(); j++) {
			if (word.charAt(j) == c.toCharArray()[0] && guessedLetters[j] == 0) {
				guessedLetters[j] = 1;
				if (checkConsonant(c)) {
					players[turn - 1].addMoney(letterValue);
				}
				check = 1;
			}
		}
		if (check == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Description: checks if word is solved
	 * 
	 * @return boolean
	 */
	public boolean checkWord() {
		for (int i = 0; i < guessedLetters.length; i++) {
			if (guessedLetters[i] == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Description: gets the player playing
	 * 
	 * @param int
	 *            p which is the player playing at turn p
	 * @return returns the player in the arrayList players
	 */
	public Player getPlayer(int p) {
		return players[p - 1];
	}

	/**
	 * Description: checks the players answer
	 * 
	 * @param String
	 *            sees if the guessed word is equal to the round word
	 * @return returns true if true
	 */
	public boolean checkAnswer(String word) {
		return word.equals(this.word);
	}

	/**
	 * Description: gives the winnings to the player who guesses the word correctly
	 * 
	 * @param int
	 *            player number
	 */
	public void giveWinnings(int p) {
		players[p - 1].setGlobalMoney(players[p - 1].getMoney() + BASE_WINNINGS);
	}

	/**
	 * Description: clears the round earning, not the global winnings
	 */
	public void clearEarnings() {
		for (int i = 0; i < players.length; i++) {
			players[i].setMoney(0);
		}
	}

	/**
	 * Description: checks if the guesses vowel is a vowel
	 * 
	 * @param String
	 *            c is the guessed vowel
	 * @return returns true if valid guess
	 */
	public boolean checkConsonant(String c) {
		return !vowels.contains(c);
	}

	// ---------------------------------------------------

	/**
	 * @return returns the int[] guessedLetters
	 */
	public int[] getGuessedLetters() {
		return guessedLetters;
	}

	/**
	 * @param list
	 *            guessedLetters
	 */
	public void setGuessedLetters(int[] guessedLetters) {
		this.guessedLetters = guessedLetters;
	}

	/**
	 * @return Players[] players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * @param players
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}

	/**
	 * @return returns category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param int
	 *            category
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * @return returns the letter value
	 */
	public int getLetterValue() {
		return letterValue;
	}

	/**
	 * @param letterValue
	 */
	public void setLetterValue(int letterValue) {
		this.letterValue = letterValue;
	}

	/**
	 * @returns int[] wheel
	 */
	public int[] getWheel() {
		return wheel;
	}

	public void setWheel(int[] wheel) {
		this.wheel = wheel;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the letters
	 */
	public char[] getLetters() {
		return letters;
	}

	/**
	 * @param letters
	 *            the letters to set
	 */
	public void setLetters(char[] letters) {
		this.letters = letters;
	}

	/**
	 * @return the words
	 */
	public ArrayList<String> getWords() {
		return words;
	}

	/**
	 * @param words
	 *            the words to set
	 */
	public void setWords(ArrayList<String> words) {
		this.words = words;
	}

	/**
	 * @return the categories
	 */
	public ArrayList<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the categoryNum
	 */
	public int getCategoryNum() {
		return categoryNum;
	}

	/**
	 * @param categoryNum
	 *            the categoryNum to set
	 */
	public void setCategoryNum(int categoryNum) {
		this.categoryNum = categoryNum;
	}

	/**
	 * @return the wordsPerCategory
	 */
	public int getWordsPerCategory() {
		return wordsPerCategory;
	}

	/**
	 * @param wordsPerCategory
	 *            the wordsPerCategory to set
	 */
	public void setWordsPerCategory(int wordsPerCategory) {
		this.wordsPerCategory = wordsPerCategory;
	}

	/**
	 * @return the vowels
	 */
	public ArrayList<String> getVowels() {
		return vowels;
	}

	/**
	 * @param vowels
	 *            the vowels to set
	 */
	public void setVowels(ArrayList<String> vowels) {
		this.vowels = vowels;
	}

	/**
	 * @return the gLetters
	 */
	public ArrayList<String> getGLetters() {
		return gLetters;
	}

	/**
	 * @param gLetters
	 *            the gLetters to set
	 */
	public void setGLetters(ArrayList<String> gLetters) {
		this.gLetters = gLetters;
	}

	/**
	 * @return the abet
	 */
	public ArrayList<String> getAbet() {
		return abet;
	}

	/**
	 * @param abet
	 *            the abet to set
	 */
	public void setAbet(ArrayList<String> abet) {
		this.abet = abet;
	}
}
