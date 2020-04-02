import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/*
对Collection接口进行代理，
以前的remove(Object obj)方法是删除集合中第一次出现的元素
(比如集合中有多个“abc”,调用remove(“abc”)后只会删除一个元素)。
代理后，要求在调用remove(Object obj)方法后，
能够删除集合中所有匹配的元素。【动态代理】
 */
public class Test {
    public static void main(String[] args) {
        //创建被代理对象
        Collection<String> coll = new ArrayList<>();

        //往coll集合中添加几个元素
        Collections.addAll(coll, "abc", "aaa", "bbb", "abc", "ccc", "abc");

        //创建代理对象
        Collection<String> list = (Collection<String>) Proxy.newProxyInstance(coll.getClass().getClassLoader(), coll.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //判断是否是删除的方法，如果是，对其进行代理
                        String name = method.getName();
                        if ("remove".equals(name)) {
                            //使用迭代器对元素进行删除
                            Iterator<String> it = coll.iterator();
                            while (it.hasNext()) {
                                //args[0]代表的 就是每一个被删除的元素，也是集合中remove的参数。
                                if (it.next().equals(args[0])) {
                                    it.remove();
                                }
                            }
                        }
                        return method.invoke(coll, args);
                    }
                });

        //调用代理对象的集合
        list.remove("abc");
        //打印代理对象
        System.out.println(coll);
    }
}
/*
输出
[aaa, bbb, ccc]
 */
