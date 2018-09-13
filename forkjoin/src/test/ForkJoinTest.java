package test;

import java.util.concurrent.RecursiveTask;

/**
 * forkjoin框架使用示例: 利用ForkJoin框架求一个区间段的和
 */
public class ForkJoinTest extends RecursiveTask<Long> {
    private static final long serialVersionUID = 123134564L;
    //计算的起始值
    private Long start;
    //计算的终止值
    private Long end;
    //做任务拆分时的临界值
    private static final Long THRESHOLD = 100000L;

    public ForkJoinTest(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 计算代码，当计算区间的长度大于临界值时，继续拆分，当小于临界值时，进行计算
     *
     * @return
     */
    @Override
    protected Long compute() {
        Long length = this.end - this.start;
        if (length > THRESHOLD) {
            long middle = (start + end) / 2;
            ForkJoinTest left = new ForkJoinTest(start, middle);
            left.fork();
            ForkJoinTest right = new ForkJoinTest(middle + 1, this.end);
            right.fork();
            return left.join() + right.join();
        } else {
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }
    }
}
