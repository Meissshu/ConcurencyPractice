package com.concurency.practice.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LatchRace {

    private static final int SECONDS_BEFORE_START = 3;
    private static final int RACER_NUMBER = 5;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(SECONDS_BEFORE_START);
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < RACER_NUMBER; ++i) {
            int finalI = i;
            executorService.submit(() -> {
                System.out.println("Racer " + finalI + " is ready...");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Racer " + finalI + " GO!");
            });
        }

        executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Counting...");
                while (countDownLatch.getCount() > 0) {
                    System.out.println(countDownLatch + "!");
                    TimeUnit.SECONDS.sleep(1);
                    countDownLatch.countDown();
                }
                System.out.println("GOOOOOOO!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }
}
