package voxspell.engine;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the gold that the player has earned and handles transactions
 * Created by nateeo on 24/10/16.
 */
public class Money implements Serializable {
    private ArrayList<Integer> money;

    public Money() {
        money = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            money.add(0);
        }
    }

    public void addGold(int amount) {
        money.set(0, money.get(0) + amount);
    }

    public void addSilver(int amount) {
        money.set(1, money.get(0) + amount);
    }

    public void addBronze(int amount) {
        money.set(2, money.get(0) + amount);
    }

    public int getGold() {
        return money.get(0);
    }

    public int getSilver() {
        return money.get(1);
    }

    public int getBronze() {
        return money.get(2);
    }

    /**
     * deduct the price, returns true for successful, otherwise cancels transaction
     * int code for currency (0 = gold, 1 = silver, 3 = bronze
     */
    public boolean deduct(int amount, int currency) {
        if (enoughFor(amount, currency)) {
            money.set(currency, money.get(currency) - amount);
            return true;
        } else {
            return false;
        }
    }

    public boolean enoughFor(int amount, int currency) {
        if (currency > 2 || amount > money.get(currency)) {
            return false;
        } else {
            return true;
        }
    }
}
