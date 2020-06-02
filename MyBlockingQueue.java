package lesson7;

/**
 * 阻塞式队列
 * （1）基于数组的循环队列实现
 * （2）提供一个队列，取元素消费时，如果队列为空，阻塞等待。如果队列满了，存元素时阻塞等待
 */
public class MyBlockingQueue<T> {

    private Object[] table;
    //取元素的索引
    private int takeIdnex;
    //存放元素的索引
    private int putIndex;
    //保存的元素大小
    private int size;

    public MyBlockingQueue(int capacity){
        table = new Object[capacity];
    }

    public synchronized int size(){
        return size;
    }

    public synchronized void put(T element) throws InterruptedException {
        while(size == table.length)
            wait();
        table[putIndex] = element;//存放元素
        putIndex = (putIndex + 1) % table.length;
        size++;
        notifyAll();
        Thread.sleep(500);
    }

    public synchronized T take() throws InterruptedException {
        while(size == 0)
            wait();
        Object element = table[takeIdnex];//取元素
        takeIdnex = (takeIdnex + 1) % table.length;
        size--;
        notifyAll();
        return (T) element;
    }

    public static void main(String[] args) {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue(100);
        for(int i=0; i<5 ; i++){
            new Thread(()->{
                try {
                        while(true){
                            synchronized (queue) {
                                queue.put(1);
                                System.out.println("存放面包+1:"+queue.size());
                            }
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for(int i=0; i<20 ; i++){
            new Thread(()->{
                try {
                        while(true){
                            synchronized (queue) {
                                Integer e = queue.take();
                                System.out.println("消费面包-1:"+queue.size());
                            }
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
