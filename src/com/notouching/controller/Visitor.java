package com.notouching.controller;

import com.notouching.model.Food;
import com.notouching.model.People;
import com.notouching.model.Sanitizer;
import com.notouching.model.Virus;

public interface Visitor {
    void interact(Virus virus);
    void interact(People people);
    void interact(Food food);
    void interact(Sanitizer sanitizer);
}
