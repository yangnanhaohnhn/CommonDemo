package com.example.commondemo

import java.io.Serializable

/**
 *
 * @author Y&N
 * date: 2021/9/29
 * desc:
 */
class SingleTest {
    /**
     * 饿汉式单例饿汉式单例模式是实现单例模式比较简单的一种方式，
     * 它有个特点就是不管需不需要该单例实例，该实例对象都会被实例化。
     * 在kotlin中实现起来非常简单，只需要定义一个object对象表达式即可，
     * 无需手动去设置构造器私有化和提供全局访问点，这一点Kotlin编译器全给你做好了
     */
    object KSingleton : Serializable {

        private fun readResolve(): Any {//防止单例对象在反序列化时重新生成对象
            return KSingleton//由于反序列化时会调用readResolve这个钩子方法，只需要把当前的KSingleton对象返回而不是去创建一个新的对象
        }

        fun doSomething() {
            println("KSingleton do some thing")
        }
    }

    /**
     * 线程安全的懒汉式单例
    当类加载的时候就去创建这个单例实例，当我们使用这个实例的时候才去初始化它
     */
    class KLazilySingleton private constructor() : Serializable {
        companion object {
            private var mInstance: KLazilySingleton? = null
                get() {
                    return field ?: KLazilySingleton()
                }

            @JvmStatic
            @Synchronized
            fun getInstance(): KLazilySingleton {
                return requireNotNull(mInstance)
            }
        }

        //防止单例对象在反序列化时重新生成对象
        private fun readResolve(): Any {
            return getInstance()
        }

        fun doSomething() {
            println("KLazilySingleton do some thing")
        }
    }

    /**
     * DCL(double check lock)改造懒汉式单例线程安全的单例模式直接是使用synchronized同步锁，
     * 锁住getInstance方法，每一次调用该方法的时候都得获取锁，但是如果这个单例已经被初始化了，
     * 其实按道理就不需要申请同步锁了，直接返回这个单例类实例即可。
     * 于是就有了DCL实现单例方式而且在kotlin中，可以支持线程安全DCL的单例，
     * 可以说也是非常非常简单，就仅仅3行代码左右，那就是Companion Object + lazy属性代理，一起来看下吧
     */
    class KLazilyDclSingleton private constructor() : Serializable {
        fun doSomething() {
            println("KLazilyDclSingleton do some thing")
        }

        private fun readResolve(): Any {
            //防止单例对象在反序列化时重新生成对象
            return instance
        }

        companion object {
            //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样
            //类似KLazilyDCLSingleton.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
            @JvmStatic
            //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
            val instance: KLazilyDclSingleton by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                KLazilyDclSingleton()
            }
        }
    }


    /**
     * 静态内部类
     * 多线程环境下一般不推荐DCL的单例模式
     */
    class KOptimizeSingleton private constructor() : Serializable {
        companion object {
            @JvmStatic
            fun getInstance(): KOptimizeSingleton {
                return SingletonHolder.mInstance
            }
        }

        fun doSomething() {
            println("KOptimizeSingleton do some thing")
        }

        /**
         * 静态内部类
         */
        private object SingletonHolder {
            val mInstance: KOptimizeSingleton = KOptimizeSingleton()
        }

        /**
         * //防止单例对象在反序列化时重新生成对象
         * 这是为了反序列化会重新创建对象而使得原来的单例对象不再唯一
         * 通过序列化一个单例对象将它写入到磁盘中，然后再从磁盘中读取出来，从而可以获得一个新的实例对象，
         * 即使构造器是私有的，反序列化会通过其他特殊途径创建单例类的新实例。然而为了让开发者能够控制反序列化，
         * 提供一个特殊的钩子方法那就是readResolve方法，这样一来我们只需要在readResolve直接返回原来的实例即可，就不会创建新的对象。
         */
        private fun readResolve(): Any {
            return SingletonHolder.mInstance
        }
    }

    /**
     * 枚举单例，就是为了防止反序列化，因为我们都知道枚举类反序列化是不会创建新的对象实例的。
     * Java的序列化机制对枚举类型做了特殊处理，一般来说在序列枚举类型时，只会存储枚举类的引用和枚举常量名称，
     * 反序列化的过程中，这些信息被用来在运行时环境中查找存在的枚举类型对象，枚举类型的序列化机制保证只会查找已经存在的枚举类型实例，而不是创建新的实例。
     */
    enum class KEnumSingleton{
        INSTANCE;

        fun doSomething() {
            println("KEnumSingleton do some thing")
        }
    }

}