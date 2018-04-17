package com.concurency.practice.semaphore;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreParking {

    private static final int CAR_NUMBER = 10;
    private static final int PLACE_NUMBER = 5;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(PLACE_NUMBER, true);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Random random = new Random();

        for (int i = 0; i < CAR_NUMBER; ++i) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    int beforeAcquireTime = 1 + random.nextInt(2);
                    TimeUnit.SECONDS.sleep(beforeAcquireTime);
                    System.out.println("Car number " + finalI + " tries to acquire!");
                    semaphore.acquire();
                    System.out.println("Car number " + finalI + " is parking...");
                    int sleepTime = 5 + random.nextInt(5);
                    TimeUnit.SECONDS.sleep(sleepTime);
                    System.out.println("Car number " + finalI + " took " + sleepTime + "s to park!");
                    semaphore.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

}
