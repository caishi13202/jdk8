package test;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class TestForkJoin {
    /**
     * 测试用forkjoin框架计算0到一百亿的和
     */
    @Test
    public void test1(){
        long start = System.currentTimeMillis();
        //1.ForkJoin框架也需要一个ForkJoin池来启动
        ForkJoinPool pool = new ForkJoinPool();
        //2.创建一个ForkJoinTask，RecursiveTask也是继承自ForkJoinTask，所以我们new自己写的那个计算类
        ForkJoinTask<Long> task = new ForkJoinTest(0L, 10000000000L);
        //3.执行计算
        long sum = pool.invoke(task);
        System.out.println(sum);  //7611

        long end = System.currentTimeMillis();

        System.out.println("耗费的时间为: " + (end - start));
    }

    /**
     * 测试用for循环计算0到1一亿的和
     */
    @Test
    public void test2(){
        long start = System.currentTimeMillis();

        long sum = 0L;

        for (long i = 0L; i <= 10000000000L; i++) {
            sum += i;
        }

        System.out.println(sum);

        long end = System.currentTimeMillis();

        System.out.println("耗费的时间为: " + (end - start));  //7670
    }
    /**
     * 测试用并行流计算0到1一亿的和
     */
    @Test
    public void test3(){
        long start = System.currentTimeMillis();

        Long sum = LongStream.rangeClosed(0L, 10000000000L).parallel().sum();

        System.out.println(sum);

        long end = System.currentTimeMillis();

        System.out.println("耗费的时间为: " + (end - start)); //3864
    }
}
