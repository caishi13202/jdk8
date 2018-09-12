package stream.test;

import org.junit.Test;
import stream.pojo.Employee;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stream的终止操作练习
 */
public class StreamTest3 {
    List<Employee> emps = Arrays.asList(
            new Employee("张三", 17, 6666.66),
            new Employee("李四", 20, 7777.77),
            new Employee("王五", 36, 8888.88),
            new Employee("田七", 55, 11111.11),
            new Employee("赵六", 55, 9999.99),
            new Employee("赵六", 45, 12222.22)
    );

    /**
     * 终止操作：查找与匹配 allMatch, anyMatch, noneMatch, findFirst, findAny, count, max, min ,forEach
     */
    @Test
    public void test1() {
        //1.查看是否有员工年龄是否都大于18
        boolean flag1 = emps.stream().allMatch(e -> e.getAge() > 18);
        System.out.println(flag1);   // false
        //2.先去掉张三，然后再判断所有员工年龄是否都大于18
        boolean flag2 = emps.stream().filter(e -> !"张三".equals(e.getName())).allMatch(e -> e.getAge() > 18);
        System.out.println(flag2);  //true
        //3.是否有员工年龄大于50
        boolean flag3 = emps.stream().filter(e -> !"张三".equals(e.getName())).anyMatch(e -> e.getAge() > 50);
        System.out.println(flag3);  //true
        //4.没有员工的年龄大于50？
        boolean flag4 = emps.stream().filter(e -> !"张三".equals(e.getName())).noneMatch(e -> e.getAge() > 50);
        System.out.println(flag4);  //false
        //5.先按照年龄进行排序，然后返回第一个员工。optional是java8用来包装可能出现空指针的对象的对象
        Optional<Employee> op1 = emps.stream().sorted((x, y) -> Integer.compare(x.getAge(), y.getAge())).findFirst();
        System.out.println(op1.get());  //Employee{name='张三', age=17, salary=6666.66}
        //6. 查找任意一名员工的姓名，当使用顺序流时，返回的是第一个对象，当使用并行流时，会随机返回一名员工的姓名
        Optional<String> op2 = emps.parallelStream().map(e -> e.getName()).findAny();
        System.out.println(op2.get()); //会随机获取一名员工
        //7. 查询员工人数
        Long count = emps.stream().count();
        System.out.println(count);
        //8.查询员工工资最大的员工信息。PS: 这个也可以通过先按照工资排序，然后取第一个元素来实现
        Optional<Employee> maxSalary = emps.stream().max((x, y) -> Double.compare(x.getSalary(), y.getSalary()));
        System.out.println(maxSalary.get());
        //9.查询员工最小年龄
        Optional<Employee> minAge = emps.stream().max((x, y) -> -Integer.compare(x.getAge(), y.getAge()));
        System.out.println(minAge.get());
        //10.循环输出所有员工的信息
        emps.stream().forEach(System.out::println);
    }
    /**
     * 终止操作：规约 reduce
     * 规约操作两个重载方法的区别是：
     * 一个未指定起始值，有可能返回null,所以返回的是一个Optional对象
     * 一个指定了起始值，不可能返回null,所以返回值可以确定是一个Employee
     */
    @Test
    public void test2() {
        //1.将所有员工的名字加上 -> 下一个员工的名字： 如 张三 -> 李四
        Optional<Employee> op1 = emps.stream().reduce((x,y) -> {x.setName(x.getName() + "->" + y.getName()); return x;});
        System.out.println(op1.get().getName());  //张三->李四->王五->田七->赵六->赵六
        //2.将所有员工的名字加上 -> 下一个员工的名字，并且开始以王八开始;
        // PS:测试时，请将例子1注释掉，不然会影响emps对象
        Employee emp = emps.stream()
                .reduce(new Employee("王八", 65, 8888.88)
                        , (x,y) -> {
                            x.setName(x.getName() + "->" + y.getName());
                            return x;
                        });
        System.out.println(emp.getName());  //王八->张三->李四->王五->田七->赵六->赵六
    }

    /**
     * 终止操作：收集
     */
    @Test
    public void test3(){
        //1.按年龄排序后收集成一个list并返回
        List<Employee> list = emps.stream().sorted((x, y) -> Integer.compare(x.getAge(), y.getAge()))
                  .collect(Collectors.toList());
        list.forEach(System.out::println);
        //2.按工资高低排序后收集成一个Set返回
        Set<Employee> set = emps.stream().sorted((x, y) -> Integer.compare(x.getAge(), y.getAge()))
                .collect(Collectors.toSet());
        set.forEach(System.out::println);
        //3.按工资排序后收集到指定的集合中，这里指定LinkedList
        LinkedList<Employee> linkedList = emps.stream().sorted((x, y) -> Integer.compare(x.getAge(), y.getAge()))
                .collect(Collectors.toCollection(LinkedList::new));
        linkedList.forEach(System.out::println);
        //4.计算流中元素的个数：
        long count = emps.stream().collect(Collectors.counting());
        System.out.println(count);
        //5.对所有员工的年龄求和：
        int inttotal = emps.stream().collect(Collectors.summingInt(Employee::getAge));
        System.out.println(inttotal);
        //6.计算所有员工工资的平均值：
        Double doubleavg= emps.stream().collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(doubleavg);
        //7.返回一个IntSummaryStatistics，可以通过这个对象获取统计值，如平均值：
        IntSummaryStatistics iss =emps.stream().collect(Collectors.summarizingInt(Employee::getAge));
        System.out.println(iss.getAverage());
        System.out.println(iss.getMax());
        System.out.println(iss.getMin());
        //8.连接所有员工的名字：
        String str= emps.stream().map(Employee::getName).collect(Collectors.joining());
        System.out.println(str);
        //9.相当于先按照工资进行排序，再取出排在第一位的员工
        Optional<Employee> min = emps.stream().collect(Collectors.minBy((x ,y) -> Double.compare(x.getSalary(), y.getSalary())));
        System.out.println(min);
        //10.相当于先按照工资进行排序，再取出排在最后一位的员工
        Optional<Employee> max = emps.stream().collect(Collectors.maxBy((x ,y) -> Double.compare(x.getSalary(), y.getSalary())));
        System.out.println(max);
        //11.从一个作为累加器的初始值开始，利用BinaryOperator与流中元素逐个结合，从而归约成单个值。
        Integer totalAge = emps.stream().collect(Collectors.reducing(0, Employee::getAge, Integer::sum));
        System.out.println(totalAge);
        //12.包裹一个收集器，对其结果进行转换：
        int inthow = emps.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));
        System.out.println(inthow);
        //13.根据某属性对结果进行分组，属性为K，结果为V：
        Map<String, List<Employee>> kv = emps.stream().collect(Collectors.groupingBy(Employee::getName));
        System.out.println(emps);
        //14.根据true或false进行分区，年龄大于30的分在true区，小于30的分在false区
        Map<Boolean,List<Employee>> vd = emps.stream().collect(Collectors.partitioningBy(e -> e.getAge() > 30));
        System.out.println(vd);
    }
}
