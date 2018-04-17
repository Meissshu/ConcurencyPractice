package com.concurency.practice.cyclicBarrier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicPrefix {

    private static final int THREADS_NUMBER = 4;
    private static final int N = 16;
    private static CyclicBarrier cyclicBarrier;
    private static List<Integer> arr = new ArrayList<>();
    private static int maxChunk[] = new int[THREADS_NUMBER];
    private static int toSum[] = new int[THREADS_NUMBER];

    public static void main(String[] args) throws InterruptedException {
        List<Worker> workers = new ArrayList<>();

        arr = Arrays.asList(
                3, 5, 2, 5,
                7, 9, -4, 6,
                7, -3, 1, 7,
                6, 8, -1, 2);
//        for(int i = 0; i < N; i++) {
//            arr.add(i, 5);
//        }

        System.out.println("start barrier");
        cyclicBarrier = new CyclicBarrier(THREADS_NUMBER);

        System.out.println("counting");
        for (int i = 0; i < THREADS_NUMBER; ++i) {
            System.out.println("add worker");
            workers.add(i, new Worker(i));
            workers.get(i).start();
            System.out.println("worker ran");
        }

        for (Worker worker : workers) {
            worker.join();
        }

        toSum[0] = 0;
        for (int i = 1; i < THREADS_NUMBER; i++) {
            toSum[i] = toSum[i - 1] + maxChunk[i - 1];
        }

        for (int i = 0; i < N; i++) {
            System.out.println(arr.get(i));
        }

    }

    private static class Worker extends Thread {


        private int local_id;

        public Worker(int local_id) {
            super("Worker " + local_id);
            this.local_id = local_id;
        }

        // private static final Object MONITOR = new Object();

        @Override
        public void run() {
            System.out.println("ENTERING THE BARRIER");

            int start = local_id * N / THREADS_NUMBER;
            int end = start + N / THREADS_NUMBER;
            for (int i = start + 1; i < end; ++i) {
                arr.set(i, arr.get(i) + arr.get(i - 1));
            }

            maxChunk[local_id] = arr.get(end - 1);

            try {
                System.out.println("AWAIT");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("After await");
            for (int i = 0; i < local_id; i++) {
                toSum[local_id] += maxChunk[i];
            }

            for (int i = start; i < end; i++) {
                arr.set(i, arr.get(i) + toSum[local_id]);
            }
            System.out.println("Leaving!");
        }
    }
}