package ex;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class CoffeeMachine {
    private static int portionsForPollution;

    private int actualMilk = 10000;

    private int actualCoffee = 10000;

    private int actualWater = 10000;

    private static final int MAX_PORTIONS = 10;

    static final int MAX_MILK = 10000;

    static final int MAX_COFFEE = 10000;

    static final int MAX_WATER = 10000;

    private final Logger LOGGER = Logger.getLogger(CoffeeMachine.class.getName());

    public static Map<String, Integer> totalPortionsMade = new HashMap<>();

    public int getPortions() {
        return portionsForPollution;
    }


    public void setPortions(int portions) {
        if (0 <= portions && portions <= MAX_PORTIONS) {
            portionsForPollution = portions;
        }
    }

    public int setActualMilk(int actualMilk) {
        if (0 <= actualMilk && actualMilk <= MAX_MILK) {
            this.actualMilk = actualMilk;
        }
        return actualMilk;
    }

    public int setActualCoffee(int actualCoffee) {
        if (0 <= actualCoffee && actualCoffee <= MAX_COFFEE) {
            this.actualCoffee = actualCoffee;
        }
        return actualCoffee;
    }

    public int setActualWater(int actualWater) {
        if (0 <= actualWater && actualWater <= MAX_WATER) {
            this.actualWater = actualWater;
        }
        return actualWater;
    }

    public void start() {
        UserMenu userMenu = new UserMenu();
        userMenu.startMenu();
    }

    public boolean makeCoffee(Recipe recipe, int count) {
        int requiredWater = recipe.getWater() * count;
        int requiredCoffee = recipe.getCoffee() * count;
        int requiredMilk = recipe.getMilk() * count;

        if (actualWater >= requiredWater &&
                actualCoffee >= requiredCoffee &&
                actualMilk >= requiredMilk &&
                portionsForPollution + count <= MAX_PORTIONS) {
            setActualWater(getActualWater() - requiredWater);
            setActualCoffee(getActualCoffee() - requiredCoffee);
            setActualMilk(getActualMilk() - requiredMilk);
            setPortions(getPortions() - count);
            portionsForPollution += count;
            String recipeName = recipe.getName();
            totalPortionsMade.put(recipeName, totalPortionsMade.getOrDefault(recipeName, 0) + count);
            LOGGER.log(Level.INFO, " Было приготовлено: " + count + recipe);
            return true;
        } else {
            LOGGER.log(Level.WARNING, " Не достаточно ингредиентов или кофемашина загрязнена");
            return false;
        }
    }

    static void displayTotalPortions() {
        System.out.println("Приготовленные напитки:");
        for (Map.Entry<String, Integer> entry : totalPortionsMade.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public boolean needsCleaning() {
        boolean cleaning = portionsForPollution >= MAX_PORTIONS && portionsForPollution == 0;
        if (cleaning) {
            LOGGER.log(Level.WARNING, " Не очищена, требуется очистка");
        } else {
            LOGGER.log(Level.INFO, " Очистка не требуется");
        }
        return cleaning;
    }

    public void clean() {
        if (portionsForPollution == 0) {
            LOGGER.log(Level.INFO, " Кофемашина не требует очистки");
        } else if (portionsForPollution > 0 && portionsForPollution < MAX_PORTIONS) {
            LOGGER.log(Level.INFO, " Кофемашина очищена");
            portionsForPollution = 0;
        } else if (portionsForPollution >= MAX_PORTIONS) {
            LOGGER.log(Level.WARNING, " Кофемашина загрязнена, требуется очистка");
            portionsForPollution = 0;
        } else {
            LOGGER.log(Level.INFO, "Кофемашину нельзя очистить повторно");
        }
    }
}
