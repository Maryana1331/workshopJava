package ex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;
import static ex.CoffeeMachine.MAX_COFFEE;
import static ex.CoffeeMachine.MAX_MILK;
import static ex.CoffeeMachine.MAX_WATER;

class CoffeePreference {

    Recipe coffeeRecipe;
    int count;

    CoffeePreference(Recipe coffeeRecipe, int count) {
        this.coffeeRecipe = coffeeRecipe;
        this.count = count;
    }
}

public class UserMenu {

    private int check() {

        int userChoice;

        while (true) {
            try {
                System.out.print("Введите команду: ");
                String input = scanner.nextLine().trim();

                if (input.contains(" ")) {
                    System.out.println("Ошибка: пробел не является командой. Пожалуйста, введите команду еще раз.");
                    continue;
                }

                userChoice = Integer.parseInt(input);

                if (userChoice >= 0) {
                    return userChoice;
                } else {
                    System.out.println("Ошибка: число должно быть неотрицательным. Пожалуйста, введите команду еще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректный ввод. Пожалуйста, введите число.");
            }
        }
    }

    private static int userChoice;

    Scanner scanner = new Scanner(System.in);

    CoffeeMachine coffeeMachine = new CoffeeMachine();

    Map<String, List<CoffeePreference>> userProfiles = new HashMap<>();

    public static void makingCoffeeProcess(String message, int countOfCups, long timeInterval) {
        int length = 15 * countOfCups;
        char incomplete = '░'; // U+2591 Unicode Character
        char complete = '█'; // U+2588 Unicode Character
        StringBuilder builder = new StringBuilder();
        Stream.generate(() -> incomplete).limit(length).forEach(builder::append);
        System.out.println(message);
        for (int i = 0; i < length; i++) {
            builder.replace(i, i + 1, String.valueOf(complete));
            String progressBar = "\r" + builder;
            System.out.print(progressBar);
            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException ignored) {

            }
        }
        System.out.println("\nГотово " + countOfCups + " " + message);
    }

    void startMenu() {
        while (true) {
            System.out.println("--- Стартовое меню ---\n" +
                    "0. Включить кофемашину\n" +
                    "Нажми 10 " + "для выхода из программы" +
                    "\n" + "Выберите пункт меню");
            userChoice = check();

            switch (userChoice) {
                case 0:
                    System.out.println("Запуск основного меню.");
                    mainMenu();
                    break;
                case 10:
                    System.exit(0);
                    return;
                default:
                    System.out.println("Неверный пункт меню: " + userChoice);
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    void mainMenu() {
        while (true) {
            System.out.println("--- Главное меню ---\n" +
                    "1. Приготовить кофе\n" + "2. Выбрать профиль\n" +
                    "3. Создать профиль\n" + "4. Рецепты\n" +
                    "5. Проверка чистки кофемашины\n" + "6. История \n" +
                    "7. Колличество ингредиентов\n" +
                    "8. Очистка Кофемашины\n" +
                    "0. Выключить кофемашину\n" + "Выберите пункт меню");
            userChoice = check();
            if (userChoice == 0) {
                break;
            }
            switch (userChoice) {
                case 1:
                    makeCoffeeMenu();
                    break;
                case 2:
                    userProfile();
                    break;
                case 3:
                    createUserProfile();
                    break;
                case 4:
                    recipe();
                    break;
                case 5:
                    coffeeMachine.needsCleaning();
                    break;
                case 6:
                    history();
                    break;
                case 7:
                    checkIngredients();
                    break;
                case 8:
                    coffeeMachine.clean();
                    break;
                default:
                    System.out.println("Введите корректную команду! ");
                    break;
            }
        }
    }

    void history() {
        CoffeeMachine.displayTotalPortions();
    }

    void checkIngredients() {
        System.out.println("---Выберите меню---\n" +
                "1. Вывести актуальное количество ингредиентов\n" +
                "2. Пополнить запасы молока\n" +
                "3. Пополнить запасы кофе\n" +
                "4. Пополнить запасы воды\n" +
                "0. Вернуться в меню\n");
        userChoice = check();
        System.out.println("Пользователь выбрал пункт меню проверки ингредиентов: " + userChoice);

        switch (userChoice) {
            case 0: break;
            case 1:
                System.out.println("Молока: " + coffeeMachine.getActualMilk() + " Кофе: " +
                        coffeeMachine.getActualCoffee() + " Воды: " + coffeeMachine.getActualWater() + "\n");
                System.out.println("Вывод актуального количества ингредиентов.");
                break;
            case 2:
                System.out.println("Введите количество молока для пополнения:");
                int milk = check();  // Предполагается, что check() возвращает int
                int newMilkLevel = coffeeMachine.setActualMilk(coffeeMachine.getActualMilk() + milk);
                if (newMilkLevel > MAX_MILK) {
                    System.out.println("Угроза перелива");
                } else {
                    System.out.println("Пополнены запасы молока на: " + milk);
                    coffeeMachine.setActualMilk(coffeeMachine.getActualMilk() + newMilkLevel);
                }
                break;

            case 3:
                System.out.println("Введите количество кофе для пополнения:");
                int coffee = check();
                int newCoffeeLevel = coffeeMachine.setActualCoffee(coffeeMachine.getActualCoffee() + coffee);
                if (newCoffeeLevel > MAX_COFFEE) {
                    System.out.println("Слишком много кофе");
                } else {
                    System.out.println("Пополнены запасы кофе на: " + coffee);
                    coffeeMachine.setActualCoffee(coffeeMachine.getActualCoffee() + newCoffeeLevel);
                }
                break;
            case 4:
                System.out.println("Введите количество воды для пополнения:");
                int water = check();
                int newWaterLevel = coffeeMachine.setActualWater(coffeeMachine.getActualWater() + water);
                if (newWaterLevel > MAX_WATER) {
                    System.out.println("Угроза перелива");
                } else {
                    System.out.println("Пополнены запасы воды на: " + water);
                    coffeeMachine.setActualWater(coffeeMachine.getActualWater() + newWaterLevel);
                }
                break;
            case 5:
            default:
                System.out.println("Неверный выбор, попробуйте снова.");
        }
    }

    void recipe() {
        System.out.println("--- Доступные рецепты ---");
        System.out.println("Выберите тип кофе:");
        System.out.println("1. Капучино\n" + "2. Эспрессо\n" + "0. Вернуться в меню\n");
        userChoice = check();
        switch (userChoice) {
            case 0: break;
            case 1:
                System.out.println("Капучино: ингредиенты:\n" +
                        "\n" +
                        "Эспрессо – 1 порция (30 мл)\n" +
                        "Молоко – 100 мл\n" +
                        "Инструкции:\n" +
                        "\n" +
                        "Приготовьте эспрессо в кофемашине.\n" +
                        "Нагрейте молоко и вспеньте его с помощью парового сопла кофемашины или вспенивателя молока.\n" +
                        "Вылейте готовый эспрессо в чашку.\n" +
                        "Добавьте вспененное молоко в чашку с эспрессо, удерживая пену ложкой, а затем выложите пену сверху.\n");
                break;
            case 2:
                System.out.println("Эспрессо: ингредиенты:\n" +
                        "\n" +
                        "Кофе молотый – 7-9 г (для одной порции)\n" +
                        "Вода – 25-30 мл\n" +
                        "Инструкции:\n" +
                        "\n" +
                        "Подготовьте кофемашину или эспрессо-машину.\n" +
                        "Засыпьте молотый кофе в фильтр-ручку, утрамбуйте.\n" +
                        "Поместите фильтр-ручку в кофемашину.\n" +
                        "Запустите процесс заваривания. Дайте эспрессо стечь в чашку в течение 25-30 секунд.\n");
                break;
            default: System.out.println("Неверный выбор, попробуйте снова.");
        }
    }

    void createUserProfile() {
        System.out.println("--- Создать нового пользователя ---");
        System.out.print("--- Введите имя пользователя: \n");
        String userChoiceName = scanner.nextLine();
        List<CoffeePreference> preferences = new ArrayList<>();

        while (true) {
            System.out.println("Выберите тип кофе: \n" +
                    "1. CAPPUCCINO\n" +
                    "2. ESPRESSO\n" +
                    "0. Завершить выбор\n" +
                    "Выберите пункт меню");
            int userChoiceType = check();
            if (userChoiceType == 0) {
                break;
            }
            Recipe coffeeRecipe;
            if (userChoiceType == 1) {
                coffeeRecipe = Recipe.CAPPUCCINO;
            } else if (userChoiceType == 2) {
                coffeeRecipe = Recipe.ESPRESSO;
            } else {
                System.out.println("Неверный выбор.");
                continue;
            }

            System.out.println("Введите количество чашек для " + coffeeRecipe + ":");
            int count = check();
            preferences.add(new CoffeePreference(coffeeRecipe, count));
        }

        userProfiles.put(userChoiceName, preferences);
        System.out.println("Профиль пользователя " + userChoiceName + " создан.");
    }

    void userProfile() {
        if (userProfiles.isEmpty()) {
            System.out.println("Нет доступных профилей.");
            return;
        }

        System.out.println("--- Доступные профили ---");
        userProfiles.forEach((name, preferences) -> {
            System.out.println("Имя: " + name);
            for (CoffeePreference preference : preferences) {
                System.out.println("Тип кофе: " + preference.coffeeRecipe + ", Количество: " + preference.count);
            }
        });

        System.out.println("Введите имя пользователя, чтобы выбрать профиль, или 0 для выхода:");
        String userChoiceName = scanner.nextLine();

        if (userChoiceName.equals("0")) {
            return;
        }

        if (userProfiles.containsKey(userChoiceName)) {
            System.out.println("Профиль " + userChoiceName + " выбран.");
            List<CoffeePreference> preferences = userProfiles.get(userChoiceName);
            for (CoffeePreference preference : preferences) {
                if (coffeeMachine.makeCoffee(preference.coffeeRecipe, preference.count)) {
                    makingCoffeeProcess("Процесс приготовления " + preference.count + " " + preference.coffeeRecipe, preference.count, 60);
                    System.out.println("\nГотово " + preference.count + " " + preference.coffeeRecipe.name());
                } else {
                    System.out.println("Не хватает ингредиентов для " + preference.coffeeRecipe);
                }
            }
        } else {
            System.out.println("Профиль не найден.");
        }
    }

    void makeCoffeeMenu() {
        while (true) {
            System.out.println("--- Выбор кофе ---\n" +
                    "1. CAPPUCCINO\n" +
                    "2. ESPRESSO\n" +
                    "0. Вернуться назад\n" +
                    "Выберите пункт меню");
            userChoice = check();
            if (userChoice == 0) {
                break;
            }
            switch (userChoice) {
                case 1:
                    choiceCountOfCoffeeCupsMenu(Recipe.CAPPUCCINO);
                    break;
                case 2:
                    choiceCountOfCoffeeCupsMenu(Recipe.ESPRESSO);
                    break;
            }
        }
    }

    private void choiceCountOfCoffeeCupsMenu(Recipe coffee) {
        while (true) {
            System.out.println("--- Выбор кол-ва кофе ---\n" +
                    "1. 1 чашка\n" + "2. 3 чашки\n" +
                    "3. Свое кол-во чашек\n" + "0. Вернуться назад\n" +
                    "Выберите пункт меню");
            userChoice = check();
            if (userChoice == 0) {
                break;
            }
            switch (userChoice) {
                case 1:
                    if (coffeeMachine.makeCoffee(coffee, 1)) {
                        makingCoffeeProcess("Процесс приготовления 1 " + coffee, 1, 60);
                        System.out.println("\nГотово 1 " + coffee.name());
                    } else {
                        System.out.println("Не хватает ингредиентов\n");
                    }
                    break;
                case 2:
                    if (coffeeMachine.makeCoffee(coffee, 3)) {
                        makingCoffeeProcess("Процесс приготовления 3 " + coffee, 3, 60);
                        System.out.println("\nГотово 3 " + coffee.name());
                    } else {
                        System.out.println("Не хватает ингредиентов");
                    }
                    break;
                case 3:
                    enterUserCountMenu(coffee);
                    break;
            }
        }
    }

    void enterUserCountMenu(Recipe coffee) {
        while (true) {
            System.out.println("--- Выбор кол-ва кофе ---\n" +
                    "Введите ваше кол-во кружек кофе или 0 для выхода назад.");
            userChoice = check();
            if (userChoice == 0) {
                break;
            }
            if (coffeeMachine.makeCoffee(coffee, userChoice)) {
                makingCoffeeProcess("Процесс приготовления " +
                        userChoice + " " + coffee, userChoice, 60);
                System.out.println("\nГотово " + userChoice + " " + coffee.name());
            } else {
                System.out.println("Не хватает ингредиентов");
            }
            break;
        }
    }
}
