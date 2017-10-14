## 交互式的钉钉机器人

* 支持人机互动
* 支持以聊天的方式向应用发起指令，动态执行任务
* 本应用实现了一个简单的值班系统，自带sqlite数据库
* 可以通过SpringEL表达式来添加值班人等信息
* 定时更换值班人，每天自动提醒

## 操作方法
* 查询指令:select:sql
* 更新指令:execute:sql
* 排班列表
* 当前值班人
* 执行表达式:spel:expression

* 获取每日提醒 @robot spel:#dailyNoteDao.getDailyNoteStr()
* 获取值班列表 @robot allDailyMan
* @robot execute:update dailyjob set clazz='org.jplus.quartz.ShowDailyNote' where id=2
* @robot select:select * from dailyjob
