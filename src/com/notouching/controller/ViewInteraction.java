package com.notouching.controller;

public interface ViewInteraction {
    void onPlayerMoved(PlayerMove move);

    int getSkipCount();

    boolean isRunning();

    void setSkipCount(int skipCount);
}