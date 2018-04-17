package com.concurency.practice.cyclicBarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CyclicEndlessPrint {

    private static final int BARRIER_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Random random = new Random();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(BARRIER_COUNT, () -> {
            System.out.println("Releasing barrier...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Released!");
        });

        while (true) {
            System.out.println("Add new thread...");
            TimeUnit.SECONDS.sleep(2);
            executorService.submit(() -> {
                System.out.println(Thread.currentThread() + " is waiting for barrier...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread continues!");
            });
        }
    }
}
