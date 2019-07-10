/**
 * @author Hayden Parker and Liam Parker
 * M359 AP Java
 * Mr. Nichols Period 8
 */
package Final_Project;

public class Player {
	private int globalMoney;
	private int money;
	private String name;

	public Player(String name) {
		globalMoney = 0;
		money = 0;
		this.name = name;
	}

	/**
	 * Description: Adds m to player's money
	 * 
	 * @param int m
	 */
	public void addMoney(int m) {
		money += m;
	}

	/**
	 * Description: Subtracts m from player's money
	 * 
	 * @param int m
	 */
	public void deductMoney(int m) {
		money -= m;
	}

	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the globalMoney
	 */
	public int getGlobalMoney() {
		return globalMoney;
	}

	/**
	 * @param globalMoney
	 *            the globalMoney to set
	 */
	public void setGlobalMoney(int globalMoney) {
		this.globalMoney = globalMoney;
	}
}
