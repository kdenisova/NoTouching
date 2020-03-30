package com.notouching.controller;

import com.notouching.model.Food;
import com.notouching.model.People;
import com.notouching.model.Sanitizer;

public interface Visitor {
    void interact(People people);
    void interact(Food food);
    void interact(Sanitizer sanitizer);
}
