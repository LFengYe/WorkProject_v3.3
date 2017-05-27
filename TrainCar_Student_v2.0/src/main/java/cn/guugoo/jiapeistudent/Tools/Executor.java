package cn.guugoo.jiapeistudent.Tools;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/10.
 */
public class Executor {
    private static final String TAG = "Executor";
    public static void main(String args[]) {
        Random random = new Random();
        // 建立一个容量为5的固定尺寸的线程池
         ExecutorService executor = Executors.newFixedThreadPool(5);
        // 判断可是线程池可以结束
        int waitTime = 500;
        for (int i = 0; i < 10; i++) {
            String name = "线程 " + i;
            int time = random.nextInt(1000);
            waitTime += time;
            System.out.println("增加: " + name + " / " + time);
            executor.execute(new Thread());
        }
        try {
            Thread.sleep(waitTime);
            executor.shutdown();
            executor.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {

        }
    }
}
