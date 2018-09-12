package stream.test;

import org.junit.Test;
import stream.pojo.Employee;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Stream的中间操作练习
 */
public class StreamTest2 {
    List<Employee> emps = Arrays.asList(
            new Employee("张三", 18, 6666.66),
            new Employee("李四", 20, 7777.77),
            new Employee("王五", 36, 8888.88),
            new Employee("田七", 55, 11111.11),
            new Employee("赵六", 55, 9999.99),
            new Employee("赵六", 45, 12222.22)
    );
    /**
     * 筛选与切片 filter, distinct limit skip
     */
    @Test
    public void test1() {
        //1.过滤掉年龄小于25的员工
        emps.stream().filter((e) -> e.getAge() > 25).forEach(System.out::println);
        //2.过滤掉姓名重复的员工
        emps.stream().distinct().forEach(System.out::println);
        //3.获取前三名员工
        emps.stream().limit(3).forEach(System.out::println);
        //4.获取第三名以后的员工
        emps.stream().skip(3).forEach(System.out::println);
        //5.先获取前3名员工，再获取其中年龄大于25的员工。（中间操作可以任意次）
        emps.stream().limit(3).filter(e -> e.getAge() > 25);
    }
    /**
     * 映射操作 map, mapToDouble, mapToInt, mapToLong, flatMap
     */
    @Test
    public void test2() {
        //1. 获取所有员工的姓名
        emps.stream().map(e -> e.getName()).forEach(System.out::println);
        //2. 获取所有员工的工资，这里工资是Double类型，我们可以用mapToDouble方法
        emps.stream().mapToDouble(e -> e.getSalary()).forEach(System.out::println);
        //3. 获取所有员工的年龄，用mapToInt方法
        emps.stream().mapToInt(e -> e.getAge()).forEach(System.out::println);
        //4. 将员工年龄转换成Long型并输出,PS：这里就算不用longValue系统也会自动将getAge转换成Long类型
        emps.stream().mapToLong(e -> e.getAge().longValue()).forEach(System.out::println);
        /**
         * 5.将所有员工的 姓名，年龄，工资转换成一个流并返回
         * 首先我们用map方法来处理，这种方式返回的是六个Stream对象，数据结构可以类似于：
         * [{"张三",18, 6666.66},{"李四",20, 7777.77}, {"王五", 36, 8888.88}, {"赵六", 24, 9999.99}, {"田七", 55, 11111.11}, {"赵六", 45, 12222.22}]
         * 然后我们用flatMap方法来处理，返回的是一个Stream对象，将所有元素连接到了一起，数据结构类似于：
         * ["张三",18, 6666.66,"李四",20, 7777.77, "王五", 36, 8888.88, "赵六", 24, 9999.99, "田七", 55, 11111.11, "赵六", 45, 12222.22]
         */
        emps.stream().map(e -> {
            return Stream.of(e.getName(), e.getAge(), e.getSalary());
        }).forEach(System.out::println);
        emps.stream().flatMap(e -> {
            return Stream.of(e.getName(), e.getAge(), e.getSalary());
        }).forEach(System.out::println);
    }
    /**
     * 排序操作 sorted
     */
    @Test
    public void test3() {
        //1.按照自然排序，注意，需要进行自然排序则对象必须实现Comparable接口
        emps.stream().sorted().forEach(System.out::println);
        //2.按照给定规则进行排序,(按照工资高低进行排序)
        emps.stream().sorted((x,y) -> Double.compare(x.getSalary(),y.getSalary())).forEach(System.out::println);
    }
}
