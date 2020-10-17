package com.notouching.controller;

import com.notouching.model.*;

//Visitor Pattern
public interface PlayerInteraction {
    void interact(Customer customer);

    void interact(Food food);

    void interact(Sanitizer sanitizer);
}