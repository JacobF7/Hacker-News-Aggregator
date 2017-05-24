package com.uom.assignment.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * The {@link Configuration} for the {@link TaskScheduler}.
 * Note that the Pool Size is configurable from properties file.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {

    @Value("${scheduler.pool.size}")
    private int poolSize;

    @Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.initialize();
        taskRegistrar.setTaskScheduler(scheduler);
    }
}
