####接入钉钉单点通用配置说明

* 1、只需在/resource/conf目录下增加dingAp-common.properties即可
* 2、配置文件需一下参数
	```
	部署地区.ding.coprId=***
	部署地区.ding.cporSecret=***
	部署地区.ding.agentId=***
	```
* 说明：部署地区参数为sys_option 表 sys_deploy_region 参数