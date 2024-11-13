package ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Recipe {
    CAPPUCCINO(220, 150, 200),
    ESPRESSO(0, 100, 100);

    private final int milk;

    private final int coffee;

    private final int water;

    public String getName(){
        return name();
    }
}
