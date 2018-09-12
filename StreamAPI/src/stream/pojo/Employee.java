package stream.pojo;

public class Employee implements Comparable{

    private String name;
    private Integer age;
    private Double salary;

    public Employee() {
    }
    public Employee(String name, Integer age, Double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

    /**
     * 自然排序，先按姓名排序，再按年龄排序，最后按工资排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        o = (Employee)o;
        if (this.getName().equals(((Employee) o).getName())) {
            if (this.getAge().equals(((Employee) o).getAge())) {
                return this.getSalary().compareTo(((Employee) o).getSalary());
            }
            return this.getAge().compareTo(((Employee) o).getAge());
        }
        return this.getName().compareTo(((Employee) o).getName());
    }
}
