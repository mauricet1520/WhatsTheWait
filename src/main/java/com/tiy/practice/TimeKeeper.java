package com.tiy.practice;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by crci1 on 2/15/2017.
 */
public class TimeKeeper {
    private int secondsPassed = 0;

    Timer myTimer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            secondsPassed++;
            setSecondsPassed(secondsPassed);
            System.out.println("seconds passed " + secondsPassed);
        }
    };

    public void start() {
        myTimer.scheduleAtFixedRate(timerTask, 1000, 1000);

    }

    TimeKeeper(){

    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void setSecondsPassed(int secondsPassed) {
        this.secondsPassed = secondsPassed;
    }
}
