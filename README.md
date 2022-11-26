# lombok_explore

> 探索lombok在项目中的应用

## val、var
val和var都表示定义一个变量，并且回进行类型推导
 - val: 定义的是final变量
 - var: 定义的是非final变量
```java
public class ValAndVarExample {

    public void valExample(){
        //相当于final String example = "Hello world";
        val example = "hello world";
//        example = "hello china"; //Cannot assign a value to final variable
        System.out.println(example);
    }

    public void varExample(){
        var example = "hello world";
        example = "hello china";
        System.out.println(example);
    }

    public static void main(String[] args) {
        ValAndVarExample valAndVarExample = new ValAndVarExample();
        valAndVarExample.valExample();
        valAndVarExample.varExample();
    }
}
```

## @NoNull

用来指定某个方法入参不能为null。

```java
public class NoNullExample {

    /**
     * 为空回抛出java.lang.NullPointerException: param is marked non-null but is null
     * @param param
     */
    public void example(@NonNull String param){
        System.out.println(param);
    }

    public static void main(String[] args) {
        NoNullExample noNullExample = new NoNullExample();
        noNullExample.example(null);
    }
}
```
会抛异常：
```java
Exception in thread "main" java.lang.NullPointerException: param is marked non-null but is null
```
> 注意:NonNull仅仅会判断是不是null，而不会判断是不是空字符串。

lombok作用于编译期，此代码编译后的效果：
```java
public class NoNullExample {
    public NoNullExample() {
    }

    public void example(@NonNull String param) {
        if (param == null) {
            throw new NullPointerException("param is marked non-null but is null");
        } else {
            System.out.println(param);
        }
    }

    public static void main(String[] args) {
        NoNullExample noNullExample = new NoNullExample();
        noNullExample.example((String)null);
    }
}
```



## @Cleanup

自动帮我们清理资源，比如InputStream、OutputStream，会自动的调用close方法。

```java
public class CleanupExample {
    public static void main(String[] args) throws IOException {
        @Cleanup InputStream inputStream = new FileInputStream(args[0]);
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
    }
}
```

编译后生成代码：

```java
public class CleanupExample {
    public CleanupExample() {
    }

    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream(args[0]);

        try {
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
        } finally {
            if (Collections.singletonList(inputStream).get(0) != null) {
                inputStream.close();
            }

        }

    }
}
```

> 如果某个资源没有close方法，那么我们可以指定某个方法来关闭资源，value属性默认为close:

```java
@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.SOURCE)
public @interface Cleanup {
	/** @return The name of the method that cleans up the resource. By default, 'close'. The method must not have any parameters. */
	String value() default "close";
}
```

可以使用@Cleanup(value="指定的方法")，调用其他的方法，默认是close

## @ToString

由Lombok重写toString()方法

```java
@ToString
@Getter(value = AccessLevel.PRIVATE)
@Setter
@EqualsAndHashCode
public class CommonExample {
    @ToString.Exclude private Integer id;
    private String name;
    private Integer age;
    @EqualsAndHashCode.Exclude private String grade;
}
```

编译后格式为:

```java
public String toString() {
    return "CommonExample(name=" + this.getName() + ", age=" + this.getAge() + ", grade=" + this.getGrade() + ")";
}
```

>  @ToString.Exclude private Integer id; 已经将id排除
>
>  如果希望某个属性不参与重写的toString()方法中，可以利用`@ToString.Exclude`来标记。

## @Getter、@Setter
由Lombok针对属性提供getter、setter方法，默认生成的方法是public，可以通过value属性来进行修改，比如:

```java
@Getter(value = AccessLevel.PRIVATE)
@Setter
```

## @EqualsAndHashCode
由Lombok重写equals()方法与hashCode()方法。

重写的equals()方法如下(编译后的结果):

```java
public boolean equals(final Object o) {
    	//判断是否是当前对象本身进行比较
        if (o == this) {
            return true;
        } else if (!(o instanceof CommonExample)) {
            //判断当前对象是否属于当前类型
            return false;
        } else {
            CommonExample other = (CommonExample)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                //当前对象是当前类型，开始比较属性值
                //id是否相等
                label47: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label47;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label47;
                    }

                    return false;
                }

                //age是否相等
                Object this$age = this.getAge();
                Object other$age = other.getAge();
                if (this$age == null) {
                    if (other$age != null) {
                        return false;
                    }
                } else if (!this$age.equals(other$age)) {
                    return false;
                }

                //name是否相等
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }
				
                //如果上面的属性都相等，则返回true
                return true;
            }
        }
    }
	
   //判断当前对象是否属于当前类型
    protected boolean canEqual(final Object other) {
        return other instanceof CommonExample;
    }
```



仔细看并不难，就是比较两个对象的各个属性是否相等，属性全部相等，两个对象才相等。

重写的hashCode()方法如下（编译后的结果）:

```java
public int hashCode() {
    int PRIME = true;
    int result = 1;
    Object $id = this.getId();
    int result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $age = this.getAge();
    result = result * 59 + ($age == null ? 43 : $age.hashCode());
    Object $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    return result;
}
```

就是利用各个属性的hashCode来生成整个对象的hashCode。

> 如果希望某个属性不参与重写的equals()方法hashCode()方法中，可以利用`@EqualsAndHashCode.Exclude`来标记。





## @NoArgsConstructor、@AllArgsConstructor

由Lombok生成无参的构造方法、全部参数的构造方法，比如:

```java
@NoArgsConstructor
@AllArgsConstructor
public class ConstructorExample {
    @NonNull private Integer x;
    @NonNull private Integer y;
    private String description;

    public static void main(String[] args) {
        //Exception in thread "main" java.lang.NullPointerException: x is marked non-null but is null
//        new ConstructorExample(null, null, "");
    }
}

```

编译后的代码：

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adun.explore.example;

import lombok.NonNull;

public class ConstructorExample {
    @NonNull
    private Integer x;
    @NonNull
    private Integer y;
    private String description;

    public static void main(String[] args) {
    }
	//无参构造器
    public ConstructorExample() {
    }

    //全参构造器
    public ConstructorExample(@NonNull final Integer x, @NonNull final Integer y, final String description) {
        if (x == null) {
            throw new NullPointerException("x is marked non-null but is null");
        } else if (y == null) {
            throw new NullPointerException("y is marked non-null but is null");
        } else {
            this.x = x;
            this.y = y;
            this.description = description;
        }
    }
}

```

加在属性上的@NonNull会影响到构造方法，会在构造方法中判断对应参数是否为null。



## @RequiredArgsConstructor

会针对必要属性，也就是加了@NonNull的属性生成一个构造方法，比如:

```java
@RequiredArgsConstructor
public class ConstructorExample {
    @NonNull private Integer x;
    @NonNull private Integer y;
    private String description;

    public static void main(String[] args) {
        //Exception in thread "main" java.lang.NullPointerException: x is marked non-null but is null
//        new ConstructorExample(null, null, "");
    }
}

```

编译后的代码：

```java
public class ConstructorExample {
    @NonNull
    private Integer x;
    @NonNull
    private Integer y;
    private String description;

    public static void main(String[] args) {
    }
    
    //必要属性的构造器
    public ConstructorExample(@NonNull final Integer x, @NonNull final Integer y) {
        if (x == null) {
            throw new NullPointerException("x is marked non-null but is null");
        } else if (y == null) {
            throw new NullPointerException("y is marked non-null but is null");
        } else {
            this.x = x;
            this.y = y;
        }
    }
}

```

我们还可以利用@RequiredArgsConstructor的staticName来指定生成一个static方法，该方法可以用来构造出一个对象，比如:

```java
@RequiredArgsConstructor(staticName = "of")
public class ConstructorExample {
    @NonNull private Integer x;
    @NonNull private Integer y;
    private String description;

    public static void main(String[] args) {
        ConstructorExample.of(1, 2);
    }
}
```

编译后生成的代码：

```java
public class ConstructorExample {
    @NonNull
    private Integer x;
    @NonNull
    private Integer y;
    private String description;

    public static void main(String[] args) {
        of(1, 2);
    }

    private ConstructorExample(@NonNull final Integer x, @NonNull final Integer y) {
        if (x == null) {
            throw new NullPointerException("x is marked non-null but is null");
        } else if (y == null) {
            throw new NullPointerException("y is marked non-null but is null");
        } else {
            this.x = x;
            this.y = y;
        }
    }

    public static ConstructorExample of(@NonNull final Integer x, @NonNull final Integer y) {
        return new ConstructorExample(x, y);
    }
}
```



## @Data
@Data等价于@ToString、@EqualsAndHashCode、@Getter、@Setter、@RequiredArgsConstructor，所以一个类加了@Data注解，那么Lombok就会︰

1. 重写toString()方法

2. 重写equals()和hashCode()方法

3. 生成getter、setter方法

4. 根据@NonNull标记的属性生成对应的构造方法



比如：

```java
@Data
public class DataExample {
    @NonNull private final String name;
    private int age;
    private double score;
}
```

编译后生成：

```java
public class DataExample {
    @NonNull
    private final String name;
    private int age;
    private double score;

    //@RequiredArgsConstructor
    public DataExample(@NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else {
            this.name = name;
        }
    }
	
    //@Getter
    @NonNull
    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public double getScore() {
        return this.score;
    }

    //@Setter
    public void setAge(final int age) {
        this.age = age;
    }

    public void setScore(final double score) {
        this.score = score;
    }

    //@EqualsAndHashCode
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DataExample)) {
            return false;
        } else {
            DataExample other = (DataExample)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getAge() != other.getAge()) {
                return false;
            } else if (Double.compare(this.getScore(), other.getScore()) != 0) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name == null) {
                        return true;
                    }
                } else if (this$name.equals(other$name)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DataExample;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        int result = result * 59 + this.getAge();
        long $score = Double.doubleToLongBits(this.getScore());
        result = result * 59 + (int)($score >>> 32 ^ $score);
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    //ToString
    public String toString() {
        return "DataExample(name=" + this.getName() + ", age=" + this.getAge() + ", score=" + this.getScore() + ")";
    }
}

```

## @Value
将一个类变得不可变，不能被继承、类中的属性也不能被修改。

1. 会使得类变成final的
2. 会使得没有声明访问权限的属性变为私有的
3. 会使得属性变为final的、可以通过@NonFinal来标记某个属性不变成final
4. 同时还会生成getter、equals()、 hashCode()、toString()方法
5. 还会生成一个全属性的构造方法

```java
@Value
public class ValueExample {
    String name;
    @NonFinal public int age;
    public double score;
}
```

编译后生成的代码：

```java
//会使得类变成final的
public final class ValueExample {
    //会使得没有声明访问权限的属性变为私有的
    private final String name;
    //会使得属性变为final的、可以通过@NonFinal来标记某个属性不变成final
    public int age;
    public final double score;

    //全参构造器
    public ValueExample(final String name, final int age, final double score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }
	
    //Getter
    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public double getScore() {
        return this.score;
    }

    //equals()、 hashCode()
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ValueExample)) {
            return false;
        } else {
            ValueExample other = (ValueExample)o;
            if (this.getAge() != other.getAge()) {
                return false;
            } else if (Double.compare(this.getScore(), other.getScore()) != 0) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                return true;
            }
        }
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        int result = result * 59 + this.getAge();
        long $score = Double.doubleToLongBits(this.getScore());
        result = result * 59 + (int)($score >>> 32 ^ $score);
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    //toString
    public String toString() {
        return "ValueExample(name=" + this.getName() + ", age=" + this.getAge() + ", score=" + this.getScore() + ")";
    }
}
```



## @Builder

可以让我们通过构造者模式来构造对象，比如:

```java
@Builder
@ToString
public class BuilderExample {

    @Builder.Default private long id=3;
    private String name;
    private int age;
    @Singular private Set<String> friends;


    public static void main(String[] args) {
        BuilderExample example = BuilderExample.builder()
                .age(18)
                .friend("tom")
                .friend("jack")
                .build();
        System.out.println(example);
    }

}

```

- @Builder.Default使得默认值生效，不然id的值不会是默认的3
- @Singular使得可以向对应集合中多次添加元素

编译后的代码：

```java
//使用建造者模式
public class BuilderExample {
    private long id;
    private String name;
    private int age;
    private Set<String> friends;

    public static void main(String[] args) {
        BuilderExample example = builder().age(18).friend("tom").friend("jack").build();
        System.out.println(example);
    }

    private static long $default$id() {
        return 3L;
    }

    BuilderExample(final long id, final String name, final int age, final Set<String> friends) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.friends = friends;
    }

    public static BuilderExample.BuilderExampleBuilder builder() {
        return new BuilderExample.BuilderExampleBuilder();
    }

    public String toString() {
        return "BuilderExample(id=" + this.id + ", name=" + this.name + ", age=" + this.age + ", friends=" + this.friends + ")";
    }

    public static class BuilderExampleBuilder {
        private boolean id$set;
        private long id$value;
        private String name;
        private int age;
        private ArrayList<String> friends;

        BuilderExampleBuilder() {
        }

        public BuilderExample.BuilderExampleBuilder id(final long id) {
            this.id$value = id;
            this.id$set = true;
            return this;
        }

        public BuilderExample.BuilderExampleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public BuilderExample.BuilderExampleBuilder age(final int age) {
            this.age = age;
            return this;
        }

        public BuilderExample.BuilderExampleBuilder friend(final String friend) {
            if (this.friends == null) {
                this.friends = new ArrayList();
            }

            this.friends.add(friend);
            //返回对象的本身
            return this;
        }

        public BuilderExample.BuilderExampleBuilder friends(final Collection<? extends String> friends) {
            if (friends == null) {
                throw new NullPointerException("friends cannot be null");
            } else {
                if (this.friends == null) {
                    this.friends = new ArrayList();
                }

                this.friends.addAll(friends);
                return this;
            }
        }

        public BuilderExample.BuilderExampleBuilder clearFriends() {
            if (this.friends != null) {
                this.friends.clear();
            }

            return this;
        }

        public BuilderExample build() {
            Set friends;
            switch(this.friends == null ? 0 : this.friends.size()) {
            case 0:
                friends = Collections.emptySet();
                break;
            case 1:
                friends = Collections.singleton(this.friends.get(0));
                break;
            default:
                Set<String> friends = new LinkedHashSet(this.friends.size() < 1073741824 ? 1 + this.friends.size() + (this.friends.size() - 3) / 3 : 2147483647);
                friends.addAll(this.friends);
                friends = Collections.unmodifiableSet(friends);
            }

            long id$value = this.id$value;
            if (!this.id$set) {
                id$value = BuilderExample.$default$id();
            }

            return new BuilderExample(id$value, this.name, this.age, friends);
        }

        public String toString() {
            return "BuilderExample.BuilderExampleBuilder(id$value=" + this.id$value + ", name=" + this.name + ", age=" + this.age + ", friends=" + this.friends + ")";
        }
    }
}
```



## @SneakyThrows

自动捕获并抛出异常，比如:

```java
public class SneakyThrowsExample {

    @SneakyThrows
    public String utf8ToString(byte[] bytes){
        return new String(bytes,"UTF-8");
    }
}
```

我们手动抛出异常：

```java
public class SneakyThrowsExample {
    public String utf8ToString(byte[] bytes){
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

```

那么用Lombok，就只需要加上@SneakyThrows就可以了，它会来帮我们抛出异常，比如:

编译后生成的代码：

> 生成代码会抛出Throwable ，慎用

```java
public class SneakyThrowsExample {
    public SneakyThrowsExample() {
    }

    public String utf8ToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (Throwable var3) {
            throw var3;
        }
    }
}
```

使用时切记需要指定异常

```java
public class SneakyThrowsExample {

    @SneakyThrows(UnsupportedEncodingException.class)
    public String utf8ToString(byte[] bytes){
        return new String(bytes,"UTF-8");
    }
}
```

编译生成：

```java
public class SneakyThrowsExample {
    public SneakyThrowsExample() {
    }

    public String utf8ToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            throw var3;
        }
    }
}

```



## @Synchronized

其实就是帮我们加锁，比如:

```java
public class SynchronizedExample {
    private  final Object readLock = new Object();

    @Synchronized
    public static void hello(){
        System.out.println("world");
    }

    @Synchronized
    public int answerToLife(){
        return 42;
    }

    @Synchronized("readLock")
    public void foo(){
        System.out.println("bar");
    }
}
```

编译生成的代码：

```java
package com.adun.explore.example;

public class SynchronizedExample {
    //三个锁对象
    private static final Object $LOCK = new Object[0];
    private final Object $lock = new Object[0];
    private final Object readLock = new Object();

    public SynchronizedExample() {
    }

    public static void hello() {
        synchronized($LOCK) {
            System.out.println("world");
        }
    }

    public int answerToLife() {
        synchronized(this.$lock) {
            return 42;
        }
    }

    public void foo() {
        synchronized(this.readLock) {
            System.out.println("bar");
        }
    }
}

```



## @With

一旦修改对应的属性，就会生成一个新的对象，比如:

```java
@AllArgsConstructor
public class WithExample {
    private String name;
    @With private final int age;

    public static void main(String[] args) {
        WithExample example = new WithExample("adun", 18);
        System.out.println(example);
        //age相同
        WithExample withExample = example.withAge(18);
        System.out.println(withExample);
    }

}
```

运行结果：

```shell
com.adun.explore.example.WithExample@2f0e140b
com.adun.explore.example.WithExample@2f0e140b
```

```java
@AllArgsConstructor
public class WithExample {
    private String name;
    @With private final int age;

    public static void main(String[] args) {
        WithExample example = new WithExample("adun", 18);
        System.out.println(example);
        //age不同
        WithExample withExample = example.withAge(19);
        System.out.println(withExample);
    }
}
```

```shell
com.adun.explore.example.WithExample@2f0e140b
com.adun.explore.example.WithExample@7440e464
```



编译生成的代码：

```java
package com.adun.explore.example;

public class WithExample {
    private String name;
    private final int age;

    public static void main(String[] args) {
        WithExample example = new WithExample("adun", 18);
        System.out.println(example);
        WithExample withExample = example.withAge(19);
        System.out.println(withExample);
    }

    public WithExample(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    //@With生成的方法
    public WithExample withAge(final int age) {
        return this.age == age ? this : new WithExample(this.name, age);
    }
}
```

