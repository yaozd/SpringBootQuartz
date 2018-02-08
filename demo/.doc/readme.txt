
安装：
创建数据库：
hello_test
创建表：
tables_mysql_innodb.sql
表说明：
QRTZ_CALENDARS 以 Blob 类型存储 Quartz 的 Calendar 信息
QRTZ_CRON_TRIGGERS 存储 Cron Trigger，包括 Cron表达式和时区信息
QRTZ_FIRED_TRIGGERS 存储与已触发的 Trigger 相关的状态信息，以及相联 Job的执行信息 QRTZ_PAUSED_TRIGGER_GRPS 存储已暂停的 Trigger 组的信息
QRTZ_SCHEDULER_STATE 存储少量的有关 Scheduler 的状态信息，和别的 Scheduler实例(假如是用于一个集群中)
QRTZ_LOCKS 存储程序的悲观锁的信息(假如使用了悲观锁)
QRTZ_JOB_DETAILS 存储每一个已配置的 Job 的详细信息
QRTZ_JOB_LISTENERS 存储有关已配置的 JobListener 的信息
QRTZ_SIMPLE_TRIGGERS 存储简单的Trigger，包括重复次数，间隔，以及已触的次数
QRTZ_BLOG_TRIGGERS Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
QRTZ_TRIGGER_LISTENERS 存储已配置的 TriggerListener 的信息
QRTZ_TRIGGERS 存储已配置的 Trigger 的信息
==
界面：
http://localhost:8080/JobManager.html
==
在线Cron表达式生成器
http://cron.qqe2.com/
例：
"30 * * * * ?" 每半分钟触发任务
"30 10 * * * ?" 每小时的10分30秒触发任务
"30 10 1 * * ?" 每天1点10分30秒触发任务
"30 10 1 20 * ?" 每月20号1点10分30秒触发任务
"30 10 1 20 10 ? *" 每年10月20号1点10分30秒触发任务
"30 10 1 20 10 ? 2011" 2011年10月20号1点10分30秒触发任务
"30 10 1 ? 10 * 2011" 2011年10月每天1点10分30秒触发任务
"30 10 1 ? 10 SUN 2011" 2011年10月每周日1点10分30秒触发任务
"15,30,45 * * * * ?" 每15秒，30秒，45秒时触发任务
"15-45 * * * * ?" 15到45秒内，每秒都触发任务
"15/5 * * * * ?" 每分钟的每15秒开始触发，每隔5秒触发一次
"15-30/5 * * * * ?" 每分钟的15秒到30秒之间开始触发，每隔5秒触发一次
"0 0/3 * * * ?" 每小时的第0分0秒开始，每三分钟触发一次
"0 15 10 ? * MON-FRI" 星期一到星期五的10点15分0秒触发任务
"0 15 10 L * ?" 每个月最后一天的10点15分0秒触发任务
"0 15 10 LW * ?" 每个月最后一个工作日的10点15分0秒触发任务
"0 15 10 ? * 5L" 每个月最后一个星期四的10点15分0秒触发任务
"0 15 10 ? * 5#3" 每个月第三周的星期四的10点15分0秒触发任务
==

==
增加示例：
com.example.demo.job.HelloJob  0/10 * * * * ?  所在分组：1
==

==
参考文章：
Spring Boot集成持久化Quartz定时任务管理和界面展示
http://blog.csdn.net/u012907049/article/details/73801122
定时任务框架Quartz的新玩法
https://yq.aliyun.com/articles/62959
1，RAMJobStore
RAMJobStore是默认的数据存储方式，其把数据存在本地内存中，官方宣称这是最有效率的方式（在本地内存当然快啦）。但是在宕机或者重启的时候数据就会丢失。
//配置方式
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore

2，JDBCJobStore
JDBCJobStore终于把数据给持久化起来了，这个也是使用最广泛的数据存储方式。在于Spring集成后都不需要自己再配置一遍数据库连接了。建表脚本在官方包里面可以找到（http://www.quartz-scheduler.org/downloads/）
//配置方式
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.dataSource = myDS
3，TerracottaJobStore
在Quartz被Software AG收购后免不了要夹带一些私货。这个就是专门给Terracotta配置的。我也就懒得讲了，大家网上搜搜吧。
说了这么多，其实都是Quartz的标准用法。在使用实践中因为各种原因导致没有办法使用官方提供的三种数据存储方式。所以只有自己动手丰衣足食，开辟新的玩法了。
3，Quartz 定时器动态修改执行时间修改后出现立即执行情况
step-01
问题已解决：CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobExpression()).withMisfireHandlingInstructionDoNothing();
withMisfireHandlingInstructionDoNothing
——不触发立即执行
——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
==
step-02
quartz.properties
#这个时间大于10000（10秒）会导致MISFIRE_INSTRUCTION_DO_NOTHING不起作用。
org.quartz.jobStore.misfireThreshold = 5000
======================================


