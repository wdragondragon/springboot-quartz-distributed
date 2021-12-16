1、master负责推送任务。cluster负责执行调度任务。

2、引入engine_group的概念。master从`dc_collect_engine_group`获取所有的`groupName`作为schedulerName来创建并持有对应的scheduler对象，以master为中心，master根据engine_group向所有的scheduler添加或删除任务。如下图所示。

3、从`dc_collect_job`中获取对应scheduler中添加或删除的任务。

4、master默认属于`engine-master`组，并在启动时添加负责执行（2）（3）中的更新scheduler和job的定时任务。



![image-20211216144955281](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202112161450404.png)



