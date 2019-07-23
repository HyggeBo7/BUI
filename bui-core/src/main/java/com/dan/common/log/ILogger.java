package com.dan.common.log;

/**
 * <pre>
 *     desc   : 简易的日志记录接口
 *     author : Bo
 *     time   : 2019年7月22日17:01:28
 * </pre>
 */
public interface ILogger {
    /**
     * 打印信息
     *
     * @param priority 优先级
     * @param tag      标签
     * @param message  信息
     * @param t        出错信息
     */
    void log(int priority, String tag, String message, Throwable t);
}
