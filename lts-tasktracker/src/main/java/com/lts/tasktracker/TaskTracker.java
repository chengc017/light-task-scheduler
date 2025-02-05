package com.lts.tasktracker;

import com.lts.core.cluster.AbstractClientNode;
import com.lts.core.constant.Level;
import com.lts.remoting.netty.NettyRequestProcessor;
import com.lts.tasktracker.domain.TaskTrackerApplication;
import com.lts.tasktracker.domain.TaskTrackerNode;
import com.lts.tasktracker.monitor.Monitor;
import com.lts.tasktracker.processor.RemotingDispatcher;
import com.lts.tasktracker.runner.JobRunner;
import com.lts.tasktracker.runner.RunnerFactory;
import com.lts.tasktracker.runner.RunnerPool;
import com.lts.tasktracker.support.JobPullMachine;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         任务执行节点
 */
public class TaskTracker extends AbstractClientNode<TaskTrackerNode, TaskTrackerApplication> {

    @Override
    protected void innerStart() {
        // 设置 线程池
        application.setRunnerPool(new RunnerPool(application));
        application.setJobPullMachine(new JobPullMachine(application));
        application.setMonitor(new Monitor(application));
    }

    @Override
    protected void injectRemotingClient() {
        application.setRemotingClient(remotingClient);
    }

    @Override
    protected NettyRequestProcessor getDefaultProcessor() {
        return new RemotingDispatcher(remotingClient, application);
    }

    public <JRC extends JobRunner> void setJobRunnerClass(Class<JRC> clazz) {
        application.setJobRunnerClass(clazz);
    }

    public void setWorkThreads(int workThreads) {
        config.setWorkThreads(workThreads);
    }

    /**
     * 设置业务日志记录级别
     */
    public void setBizLoggerLevel(Level level) {
        if (level != null) {
            application.setBizLogLevel(level);
        }
    }

    /**
     * 设置JobRunner工场类，一般用户不用调用
     */
    public void setRunnerFactory(RunnerFactory factory) {
        application.setRunnerFactory(factory);
    }
}
