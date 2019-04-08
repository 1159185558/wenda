package com.example.wenda;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author: weilei
 * @CreateTime: 2019/4/1  10:31
 * @Description:
 */

public class MultipleThread {
    private static Object object = new Object();

    public static void test1() throws InterruptedException {
        synchronized (object) {
            Thread.sleep(500);
            System.out.println("synchronize 1  ");

        }
    }

    public static void test2() throws InterruptedException {
        synchronized (new Object()) {
            Thread.sleep(500);
            System.out.println("synchronize 2  ");

        }
    }

    public static void synchronizedTest() {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        test1();
                        test2();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    class Producer implements Runnable {
        private BlockingQueue<String> queue;

        private Producer(BlockingQueue<String> q) {
            queue = q;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; ++i) {
                    queue.put(String.valueOf(i));
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                    Thread.sleep(300);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Consumer implements Runnable {
        private BlockingQueue<String> queue;

        private Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int test = 0;
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void testLocal() {
        for (int i = 0; i < 10; i++) {
            final int tmp = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        test = tmp;
                        Thread.sleep(300);
                        System.out.println("test: " + test);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }
        for (int i = 0; i < 10; i++) {
            final int tmp = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocal.set(tmp);
                    try {
                        Thread.sleep(300);
                        System.out.println("Local: " + threadLocal.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void executorTest(){
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;++i){
                    try {
                        Thread.sleep(200);
                        System.out.println("first: "+i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;++i){
                    try {
                        Thread.sleep(200);
                        System.out.println("second: "+i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void test() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(queue), "producer").start();
        new Thread(new Consumer(queue), "consumer 1").start();
        new Thread(new Consumer(queue), "consumer 2").start();
    }

    public static void main(String[] args) {
        //synchronizedTest();
        //new MultipleThread().test();
        //testLocal();
        long start = new Date().getTime();
        executorTest();
        long end = new Date().getTime();
        System.out.println(end-start);
    }
}
