1、master中的scheduler的auto-start设置为false，只负责推送任务。cluster负责执行调度任务。
2、引入engine_group的概念。master加入到所有的scheduler。为engine_group与scheduler建立映射关系，由master根据engine_group向所有的scheduler推送任务。如下图所示。

![image-20211216004146877](https://gitee.com/wdragondragon/picture-bed/raw/master/imgs/202112160041924.png)
