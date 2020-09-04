package com.notouching.controller;

import com.notouching.model.*;

public interface Visitor {
    void interact(People people);
    void interact(Food food);
    void interact(Sanitizer sanitizer);
}