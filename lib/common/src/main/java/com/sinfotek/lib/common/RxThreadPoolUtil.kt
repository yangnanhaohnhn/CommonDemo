package com.sinfotek.lib.common

import java.util.concurrent.*

/**
 * Create 2020/10/28
 *
 * @author N
 * desc:
 */
class RxThreadPoolUtil private constructor() {
    private var exec: ThreadPoolExecutor? = null
    private val unit = TimeUnit.MILLISECONDS

    /**
     * 初始化ThreadPoolExecutor
     * 双重检查加锁,只有在第一次实例化的时候才启用同步机制,提高了性能
     */
    private fun initThreadPoolExecutor() {
        if (exec == null || isShutDown || isTerminated) {
            synchronized(RxThreadPoolUtil::class.java) {
                if (exec == null || isShutDown || isTerminated) {
                    val workQueue: BlockingQueue<Runnable> = LinkedBlockingDeque()
                    val threadFactory = Executors.defaultThreadFactory()
                    val handler: RejectedExecutionHandler = ThreadPoolExecutor.DiscardPolicy()
                    val keepAliveTime: Long = 3000
                    exec = ThreadPoolExecutor(
                        POOL_SIZE,
                        10,
                        keepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler
                    )
                }
            }
        }
    }

    /**
     * 在未来某个时间执行给定的命令
     *
     *
     * 该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
     *
     * @param command 命令
     */
    fun execute(command: Runnable?) {
        initThreadPoolExecutor()
        exec!!.execute(command)
    }

    /**
     * 在未来某个时间执行给定的命令链表
     *
     *
     * 该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。
     *
     * @param commands 命令链表
     */
    fun execute(commands: List<Runnable?>) {
        initThreadPoolExecutor()
        for (runnable in commands) {
            exec!!.execute(runnable)
        }
    }

    /**
     * 待以前提交的任务执行完毕后关闭线程池
     *
     *
     * 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
     * 如果已经关闭，则调用没有作用。
     */
    fun shutDown() {
        initThreadPoolExecutor()
        exec!!.shutdown()
    }

    /**
     * 试图停止所有正在执行的活动任务
     * 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
     * 无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。
     *
     * @return 等待执行的任务的列表
     */
    fun shutDownNow(): List<Runnable> {
        initThreadPoolExecutor()
        return exec!!.shutdownNow()
    }

    /**
     * 移除任务
     */
    fun remove(task: Runnable?) {
        initThreadPoolExecutor()
        exec!!.remove(task)
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isShutDown: Boolean
        get() = exec!!.isShutdown

    /**
     * 关闭线程池后判断所有任务是否都已完成
     *
     *
     * 注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isTerminated: Boolean
        get() = exec!!.isTerminated
    //
    //    /**
    //     * 请求关闭、发生超时或者当前线程中断
    //     * <p>
    //     * 无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。
    //     *
    //     * @param timeout 最长等待时间
    //     * @param unit    时间单位
    //     * @return `true`: 请求成功<br></br>`false`: 请求超时
    //     * @throws InterruptedException 终端异常
    //     */
    //    public boolean awaitTermination(Long timeout, TimeUnit unit) throws InterruptedException {
    //        return exec.awaitTermination(timeout, unit);
    //    }
    //
    /**
     * 提交一个Callable任务用于执行
     *
     *
     * 如果想立即阻塞任务的等待，则可以使用`result = exec.submit(aCallable).get();`形式的构造。
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回该任务的结果。
    </T> *
     */
    fun <T> submit(task: Callable<T>): Future<T> {
        initThreadPoolExecutor()
        return exec!!.submit(task)
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @param <T>    泛型
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回该任务的结果。
    </T> *
     */
    fun <T> submit(task: Runnable?, result: T): Future<T> {
        initThreadPoolExecutor()
        return exec!!.submit(task, result)
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的`get`方法在成功完成时将会返回null结果。
     */
    fun submit(task: Runnable?): Future<*> {
        initThreadPoolExecutor()
        return exec!!.submit(task)
    } //

    //    /**
    //     * 执行给定的任务
    //     * <p>
    //     * 当所有任务完成时，返回保持任务状态和结果的Future列表。
    //     * 返回列表的所有元素的[Future.isDone]为`true`。
    //     * 注意，可以正常地或通过抛出异常来终止已完成任务。
    //     * 如果正在进行此操作时修改了给定的 collection，则此方法的结果是不确定的。
    //     *
    //     * @param tasks 任务集合
    //     * @param <T>   泛型
    //     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
    //     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
    //     *                              </T>
    //     */
    //    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks) throws InterruptedException {
    //        return exec.invokeAll(tasks);
    //    }
    //
    //    /**
    //     * 执行给定的任务
    //     * <p>
    //     * 当所有任务完成或超时期满时(无论哪个首先发生)，返回保持任务状态和结果的Future列表。
    //     * 返回列表的所有元素的[Future.isDone]为`true`。
    //     * 一旦返回后，即取消尚未完成的任务。
    //     * 注意，可以正常地或通过抛出异常来终止已完成任务。
    //     * 如果此操作正在进行时修改了给定的 collection，则此方法的结果是不确定的。
    //     *
    //     * @param tasks   任务集合
    //     * @param timeout 最长等待时间
    //     * @param unit    时间单位
    //     * @param <T>     泛型
    //     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
    //     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
    //     *                              </T>
    //     */
    //    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
    //        return exec.invokeAll(tasks, timeout, unit);
    //    }
    //
    //    /**
    //     * 执行给定的任务
    //     * <p>
    //     * 如果某个任务已成功完成（也就是未抛出异常），则返回其结果。
    //     * 一旦正常或异常返回后，则取消尚未完成的任务。
    //     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。
    //     *
    //     * @param tasks 任务集合
    //     * @param <T>   泛型
    //     * @return 某个任务返回的结果
    //     * @throws InterruptedException 如果等待时发生中断
    //     * @throws ExecutionException   如果没有任务成功完成
    //     *                              </T>
    //     */
    //    public <T> T invokeAny(Collection<Callable<T>> tasks) throws InterruptedException, ExecutionException {
    //        return exec.invokeAny(tasks);
    //    }
    //
    //    /**
    //     * 执行给定的任务
    //     * <p>
    //     * 如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。
    //     * 一旦正常或异常返回后，则取消尚未完成的任务。
    //     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。
    //     *
    //     * @param tasks   任务集合
    //     * @param timeout 最长等待时间
    //     * @param unit    时间单位
    //     * @param <T>     泛型
    //     * @return 某个任务返回的结果
    //     * @throws InterruptedException 如果等待时发生中断
    //     * @throws ExecutionException   如果没有任务成功完成
    //     * @throws TimeoutException     如果在所有任务成功完成之前给定的超时期满
    //     *                              </T>
    //     */
    //    public <T> T invokeAny(Collection<Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    //        return exec.invokeAny(tasks, timeout, unit);
    //    }
    //
    //    /**
    //     * 延迟执行Runnable命令
    //     *
    //     * @param command 命令
    //     * @param delay   延迟时间
    //     * @param unit    单位
    //     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在完成后将返回`null`
    //     */
    //    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    //        return scheduleExec.schedule(command, delay, unit);
    //    }
    //
    //    /**
    //     * 延迟执行Callable命令
    //     *
    //     * @param callable 命令
    //     * @param delay    延迟时间
    //     * @param unit     时间单位
    //     * @param <V>      泛型
    //     * @return 可用于提取结果或取消的ScheduledFuture
    //     * </V>
    //     */
    //    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
    //        return scheduleExec.schedule(callable, delay, unit);
    //    }
    //
    //    /**
    //     * 延迟并循环执行命令
    //     *
    //     * @param command      命令
    //     * @param initialDelay 首次执行的延迟时间
    //     * @param period       连续执行之间的周期
    //     * @param unit         时间单位
    //     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在取消后将抛出异常
    //     */
    //    public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
    //        return scheduleExec.scheduleAtFixedRate(command, initialDelay, period, unit);
    //    }
    //
    //    /**
    //     * 延迟并以固定休息时间循环执行命令
    //     *
    //     * @param command      命令
    //     * @param initialDelay 首次执行的延迟时间
    //     * @param delay        每一次执行终止和下一次执行开始之间的延迟
    //     * @param unit         时间单位
    //     * @return 表示挂起任务完成的ScheduledFuture，并且其`get()`方法在取消后将抛出异常
    //     */
    //    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    //        return scheduleExec.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    //    }
    //    public enum Type {
    //        /**
    //         * 创建一个线程池，根据需要创建新的线程，但是将重用之前构建的线程可用。
    //         * 这些池通常会提高性能,执行许多短期的异步任务的程序。
    //         * 调用{@code execute}将重用之前构造的线程(如果可用)。
    //         * 如果没有可用的线程，则使用新线程,线程将被创建并添加到池中。线程未使用60秒将终止并删除缓存。
    //         * 因此，一个足够长时间保持空闲的池将会不消耗任何资源。
    //         * 注意，有类似的池属性，但不同的细节(例如，超时参数)
    //         * 返回新创建的线程池
    //         */
    //        CachedThread,
    //        /**
    //         * 创建重用固定数量线程的线程池,操作一个共享的无界队列。
    //         * 在任何点，最多线程将是活动的处理任务。
    //         * 如果在所有线程都处于活动状态时提交了额外的任务，它们将在队列中等待，直到线程可用。
    //         * 如果任何线程在执行过程中由于失败而终止,关机前，如有需要，将更换一个新的执行后续任务。
    //         * 池中的线程将存在直到它显式为{@link ExecutorService#shutdown shutdown}。
    //         * 线程池中的线程数
    //         * 返回新创建的线程池
    //         * @抛出IllegalArgumentException如果{@code nThreads <= 0}
    //         */
    //        FixedThread,
    //        /**
    //         * 创建使用单个工作线程操作的执行器,关闭一个无界队列。
    //         * (注意，如果这个单线程在执行之前由于失败而终止关机时，如果需要执行，会有一个新的替换后续任务。)
    //         * 任务被保证执行按顺序排列，任何活动的任务不超过一个。
    //         * 不像其他等价的{@code newFixedThreadPool(1)}
    //         * 保证不被重新配置使用额外的线程。
    //         * 返回新创建的单线程执行器
    //         */
    //        SingleThread
    //    }
    companion object {
        private const val POOL_SIZE = 5
        @JvmStatic
        fun instance(): RxThreadPoolUtil {
            return SingletonHolder.mInstance
        }
    }

    /**
     * 静态内部类
     */
    private object SingletonHolder {
        val mInstance = RxThreadPoolUtil()
    }

    init {
        initThreadPoolExecutor()
    }
}