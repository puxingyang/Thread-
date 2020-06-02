package lesson7;

/**
 * 三个线程
 * 第一个只能打印A，第二个只能打印B，第三个只能打印C
 * 要求的打印结果：ABC;同时执行
 * 升级版：循环打印
 * ABC
 * ABC
 * 共计10次
 */
public class SequencePrint {

    public static void print1() {
        Thread t1 = new Thread(new Print("A",null));
        Thread t2 = new Thread(new Print("B",t1));
        Thread t3 = new Thread(new Print("c",t2));
        t3.start();
        t2.start();
        t1.start();
    }

    private static class Print implements Runnable {
        private String content;
        private Thread t;

        public Print(String content, Thread t) {
            this.content = content;
            this.t = t;
        }

        @Override
        public void run() {
                try {
                    if (t != null)
                        t.join();
                        System.out.println(content);
                }catch (InterruptedException e) {
                        e.printStackTrace();
            }
        }


        /**
         * 升级版要求：
         * （1）同时执行
         * （2）打印结果：
         * ABC
         * ABC
         * 循环打印共计10次
         * 思路：
         * 每个线程循环10次来打印A、B、C
         * 每次打印之后，等待其余线程打印完，再往下执行
         */
        public static void print2() {
            for (int i = 0; i < Print2.ARRAY.length; i++) {
                new Thread(new Print2(i)).start();
            }
        }

        private static class Print2 implements Runnable {
            private int idx;
            public static String[] ARRAY = {"A", "B", "C"};
            private static int INDEX;

            public Print2(int idx) {
                this.idx = idx;
            }

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        synchronized (ARRAY) {
                            while (idx != INDEX) {
                                ARRAY.wait();
                            }
                            System.out.print(ARRAY[idx]);
                            if (INDEX == ARRAY.length - 1)
                                System.out.println();
                            INDEX = (INDEX + 1) % ARRAY.length;
                            ARRAY.notifyAll();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            print1();
            print2();
        }
    }
}
