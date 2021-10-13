# # guli 在线教育项目

该项目是一个前后端分离项目，前端包括后台管理系统和前台用户系统。

后台管理系统 **guli-admin** 基于 vue.js 和 vue-admin-template 模板进行页面的搭建，引入 Echarts 进行数据的图表显示；前台用户系统 **guli-front** 基于vue.js 和 nuxt 框架进行页面的搭建。都采用 axios 进行 Ajax 请求调用。

后端 **guli-parent** 使用 SpringBoot + SpringCloud 进行微服务架构，使用 Nacos 进行服务注册和作为配置中心，使用 OpenFeign 进行服务调用，使用 Hystrix 进行服务降级和熔断，使用 Gateway 进行负载均衡，使用 MybatisPlus 进行持久层的操作，使用 OAuth2 + JWT 实现分布式的访问，引入 SpringSecurity 进行权限控制。整合了 EasyExcel 实现对 Excel 的读写，使用 Redis 缓存首页数据、订单信息、短信验证码等。



**部分模块的配置文件中的相关内容需要根据自己的实际信息进行相应配置：**

- `service-order` 订单模块，微信支付相关配置

```properties
wxpay.appid=<your appid>
wxpay.partner=<your mch_id>
wxpay.partnerkey=<your key>
wxpay.notifyurl=<your notify_url>
```

- `service-oss` 对象存储模块，阿里云OSS相关配置

```properties
aliyun.oss.file.endpoint=<your endpoint>
aliyun.oss.file.access-key-id=<your assessKeyId>
aliyun.oss.file.access-key-secret=<your assessKeySecret>
aliyun.oss.file.bucket-name=<your bucketName>
```

- `service-user` 用户模块，微信登录相关配置

```properties
wx.open.app_id=<your appid>
wx.open.app_secret=<your appsecret>
wx.open.redirect_url=<your redirect_url>
```

- `service-vod` 视频点播模块，阿里云视频点播相关配置

```properties
aliyun.oss.file.access-key-id=<your accessKeyId>
aliyun.oss.file.access-key-secret=<your accessKeySecret>
```



