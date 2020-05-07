#!/usr/bin/env bash

ln -s /Users/shenke/nnn7/basedata/web/*  /Users/shenke/nnn7/basedata/szxy-basedata-web/src/main/resources/templates
ln -s /Users/shenke/nnn7/desktop/web/*  /Users/shenke/nnn7/desktop/src/resources/templates
ln -s /Users/shenke/nnn7/framework/web/fw  /Users/shenke/nnn7/framework/szxy-framework-web/src/main/resources/templates
ln -s /Users/shenke/nnn7/framework/web/static  /Users/shenke/nnn7/framework/szxy-framework-web/src/main/resources/public
ln -s /Users/shenke/nnn7/newgkelective/web/*  /Users/shenke/nnn7/newgkelective/src/resources/templates
ln -s /Users/shenke/nnn7/studevelop/web/*  /Users/shenke/nnn7/studevelop/src/resources/templates


#desktop
ln -s /Users/shenke/nnn7/desktop/src/java/net/zdsoft/desktop/remote/service/UserSubscribeRemoteService.java /Users/shenke/nnn7/desktop/desktop-remote/src/main/java/net/zdsoft/desktop/remote/service
ln -s /Users/shenke/nnn7/desktop/src/java/* /Users/shenke/nnn7/desktop/desktop-business/src/main/java
ln -s /Users/shenke/nnn7/desktop/src/resources/* /Users/shenke/nnn7/desktop/desktop-business/src/main/resources
ln -s /Users/shenke/nnn7/desktop/web/* /Users/shenke/nnn7/desktop/desktop-business/src/main/resources/templates
ln -s /Users/shenke/nnn7/desktop/src/java/net/zdsoft/desktop/entity/UserSubscribe.java /Users/shenke/nnn7/desktop/desktop-remote/src/main/java/net/zdsoft/desktop/entity


## main
ln -s /Users/shenke/nnn7/newgkelective/src/java  /Users/shenke/nnn7/newgkelective/src/main
ln -s /Users/shenke/nnn7/newgkelective/src/resources/*  /Users/shenke/nnn7/newgkelective/src/main/resources
ln -s /Users/shenke/nnn7/newgkelective/web/*  /Users/shenke/nnn7/newgkelective/src/main/resources/templates

## career main
ln -s /Users/shenke/nnn7/career/src/java/* /Users/shenke/nnn7/career/src/main/java
ln -s /Users/shenke/nnn7/career/web/*  /Users/shenke/nnn7/career/src/main/resources/templates

## desktop main
ln -s /Users/shenke/nnn7/desktop/src/java/* /Users/shenke/nnn7/desktop/src/main/java
ln -s /Users/shenke/nnn7/desktop/src/resources/* /Users/shenke/nnn7/desktop/src/main/resources
ln -s /Users/shenke/nnn7/desktop/web/* /Users/shenke/nnn7/desktop/src/main/resources/templates

## careerplan main
ln -s /Users/shenke/nnn7/careerplan/src/java/* /Users/shenke/nnn7/careerplan/src/main/java
ln -s /Users/shenke/nnn7/careerplan/src/resources/* /Users/shenke/nnn7/careerplan/src/main/resources
ln -s /Users/shenke/nnn7/careerplan/web/* /Users/shenke/nnn7/careerplan/src/main/resources/templates

## datacollection main
ln -s /Users/shenke/nnn7/datacollection/src/java/* /Users/shenke/nnn7/datacollection/src/main/java
ln -s /Users/shenke/nnn7/datacollection/web/* /Users/shenke/nnn7/datacollection/src/main/resources/templates

## datareport main
ln -s /Users/shenke/nnn7/datareport/src/java/* /Users/shenke/nnn7/datareport/src/main/java
ln -s /Users/shenke/nnn7/datareport/web/* /Users/shenke/nnn7/datareport/src/main/resources/templates

##diathdsis
ln -s /Users/shenke/nnn7/diathesis/src/java/* /Users/shenke/nnn7/diathesis/src/main/java

##exammanage
ln -s /Users/shenke/nnn7/exammanage/src/java/* /Users/shenke/nnn7/exammanage/exammanage-business/src/main/java
ln -s /Users/shenke/nnn7/exammanage/web/* /Users/shenke/nnn7/exammanage/exammanage-business/src/main/resources/templates
ln -s /Users/shenke/nnn7/exammanage/src/java/net/zdsoft/exammanage/remote/service/ExamManageRemoteService.java /Users/shenke/nnn7/exammanage/exammanage-remote/src/main/java/net/zdsoft/exammanage/remote/service
ln -s /Users/shenke/nnn7/exammanage/src/java/net/zdsoft/exammanage/data/entity/EmScoreInfo.java /Users/shenke/nnn7/exammanage/exammanage-remote/src/main/java/net/zdsoft/exammanage/data/entity
ln -s /Users/shenke/nnn7/exammanage/src/java/net/zdsoft/exammanage/data/entity/EmSubjectInfo.java /Users/shenke/nnn7/exammanage/exammanage-remote/src/main/java/net/zdsoft/exammanage/data/entity

##stuwork
ln -s /Users/shenke/nnn7/stuwork/src/java/* /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java
ln -s /Users/shenke/nnn7/stuwork/web/* /Users/shenke/nnn7/stuwork/stuwork-business/src/main/resources/templates
ln -s /Users/shenke/nnn7/stuwork/src/java/net/zdsoft/tutor/remote/service/TutorRemoteService.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/tutor/remote/service

ln -s /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java/net/zdsoft/stuwork/data/dto/DyBusinessOptionDto.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/dto
ln -s /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java/net/zdsoft/stuwork/data/entity/DyStuEvaluation.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java/net/zdsoft/stuwork/data/entity/DyBusinessOption.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java/net/zdsoft/stuwork/data/entity/DyStuHealthResult.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/stuwork-business/src/main/java/net/zdsoft/stuwork/remote/service/StuworkRemoteService.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/remote/service

ln -s /Users/shenke/nnn7/stuwork/src/java/net/zdsoft/stuwork/data/entity/DyDormBed.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/src/java/net/zdsoft/stuwork/data/entity/DyDormRoom.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/src/java/net/zdsoft/stuwork/data/entity/DyDormCheckResult.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity
ln -s /Users/shenke/nnn7/stuwork/src/java/net/zdsoft/stuwork/data/entity/DyDormCheckRemind.java /Users/shenke/nnn7/stuwork/stuwork-remote/src/main/java/net/zdsoft/stuwork/data/entity

##scoremanage
ln -s /Users/shenke/nnn7/scoremanage/src/java/* /Users/shenke/nnn7/scoremanage/scoremanage-business/src/main/java
ln -s /Users/shenke/nnn7/scoremanage/web/* /Users/shenke/nnn7/scoremanage/scoremanage-business/src/main/resources/templates

ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/remote/service/ExamInfoRemoteService.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/remote/service
ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/remote/service/ScoreInfoRemoteService.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/remote/service
ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/remote/service/SubjectInfoRemoteService.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/remote/service

ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/comprehensive/remote/service/ComStatisticsRemoteService.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/comprehensive/remote/service
ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/data/entity/ExamInfo.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/data/entity
ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/data/entity/ScoreInfo.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/data/entity
ln -s /Users/shenke/nnn7/scoremanage/src/java/net/zdsoft/scoremanage/data/entity/SubjectInfo.java /Users/shenke/nnn7/scoremanage/scoremanage-remote/src/main/java/net/zdsoft/scoremanage/data/entity
##newstusys

ln -s /Users/shenke/nnn7/newstusys/src/java/* /Users/shenke/nnn7/newstusys/newstusys-business/src/main/java
ln -s /Users/shenke/nnn7/newstusys/src/resources/* /Users/shenke/nnn7/newstusys/newstusys-business/src/main/resources
ln -s /Users/shenke/nnn7/newstusys/web/* /Users/shenke/nnn7/newstusys/newstusys-business/src/main/resources/templates

ln -s /Users/shenke/nnn7/newstusys/src/java/net/zdsoft/newstusys/remote/service/StudentAbnormalFlowRemoteService.java /Users/shenke/nnn7/newstusys/newstusys-remote/src/main/java/net/zdsoft/newstusys/remote/service
ln -s /Users/shenke/nnn7/newstusys/src/java/net/zdsoft/newstusys/remote/service/StusysStudentBakRemoteService.java /Users/shenke/nnn7/newstusys/newstusys-remote/src/main/java/net/zdsoft/newstusys/remote/service

ln -s /Users/shenke/nnn7/newstusys/src/java/net/zdsoft/newstusys/entity/StusysStudentBak.java /Users/shenke/nnn7/newstusys/newstusys-remote/src/main/java/net/zdsoft/newstusys/entity
ln -s /Users/shenke/nnn7/newstusys/newstusys-business/src/main/java/net/zdsoft/newstusys/entity/StudentGraduate.java /Users/shenke/nnn7/newstusys/newstusys-remote/src/main/java/net/zdsoft/newstusys/entity

#eclasscard
ln -s /Users/shenke/nnn7/eclasscard/src/java/* /Users/shenke/nnn7/eclasscard/eclasscard-business/src/main/java
ln -s /Users/shenke/nnn7/eclasscard/web/* /Users/shenke/nnn7/eclasscard/eclasscard-business/src/main/resources/templates

ln -s /Users/shenke/nnn7/eclasscard/src/java/net/zdsoft/eclasscard/remote/service/EclasscardRemoteService.java /Users/shenke/nnn7/eclasscard/eclasscard-remote/src/main/java/net/zdsoft/eclasscard/remote/service
ln -s /Users/shenke/nnn7/eclasscard/src/java/net/zdsoft/eclasscard/data/constant/* /Users/shenke/nnn7/eclasscard/eclasscard-remote/src/main/java/net/zdsoft/eclasscard/data/constant

ln -s /Users/shenke/nnn7/eclasscard/src/java/net/zdsoft/eclasscard/data/dto/AttanceSearchDto.java /Users/shenke/nnn7/eclasscard/eclasscard-remote/src/main/java/net/zdsoft/eclasscard/data/dto
ln -s /Users/shenke/nnn7/eclasscard/src/java/net/zdsoft/eclasscard/data/dto/DormBuildingDto.java /Users/shenke/nnn7/eclasscard/eclasscard-remote/src/main/java/net/zdsoft/eclasscard/data/dto
ln -s /Users/shenke/nnn7/eclasscard/src/java/net/zdsoft/eclasscard/data/entity/EccStudormAttence.java /Users/shenke/nnn7/eclasscard/eclasscard-remote/src/main/java/net/zdsoft/eclasscard/data/entity

#officework
ln -s /Users/shenke/nnn7/officework/src/java/* /Users/shenke/nnn7/officework/officework-business/src/main/java

ln -s /Users/shenke/nnn7/officework/officework-business/src/main/java/net/zdsoft/officework/dto/OfficeHealthInfoDto.java /Users/shenke/nnn7/officework/officework-remote/src/main/java/net/zdsoft/officework/dto
ln -s /Users/shenke/nnn7/officework/officework-business/src/main/java/net/zdsoft/officework/entity/OfficeHealthDoinoutInfo.java /Users/shenke/nnn7/officework/officework-remote/src/main/java/net/zdsoft/officework/entity

ln -s /Users/shenke/nnn7/officework/src/java/net/zdsoft/officework/remote/service/OfficeAttanceRemoteService.java /Users/shenke/nnn7/officework/officework-remote/src/main/java/net/zdsoft/officework/remote/service
ln -s /Users/shenke/nnn7/officework/src/java/net/zdsoft/officework/remote/service/OfficeHealthDoinoutInfoRemoteService.java /Users/shenke/nnn7/officework/officework-remote/src/main/java/net/zdsoft/officework/remote/service

#studevelop
ln -s /Users/shenke/nnn7/studevelop/src/java/* /Users/shenke/nnn7/studevelop/studevelop-business/src/main/java
ln -s /Users/shenke/nnn7/studevelop/web/* /Users/shenke/nnn7/studevelop/studevelop-business/src/main/resources/templates


ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/studevelop/studevelop/remote/service/StudevelopRemoteService.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/studevelop/remote/service
ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/stutotality/remote/service/StutotalityRemoteService.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/stutotality/remote/service

ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/stutotality/data/dto/StutotalityOptionDto.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/stutotality/data/dto
ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/stutotality/data/dto/StutotalityResultDto.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/stutotality/data/dto
ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/stutotality/data/dto/StutotalityTypeDto.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/stutotality/data/dto

ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/studevelop/data/constant/StuDevelopConstant.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/studevelop/data/constant
ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/studevelop/data/entity/StudevelopDutySituation.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/studevelop/data/entity
ln -s /Users/shenke/nnn7/studevelop/src/java/net/zdsoft/studevelop/data/entity/StudevelopPermission.java /Users/shenke/nnn7/studevelop/studevelop-remote/src/main/java/net/zdsoft/studevelop/data/entity

#teaexam

ln -s /Users/shenke/nnn7/teaexam/src/java/* /Users/shenke/nnn7/teaexam/src/main/java
ln -s /Users/shenke/nnn7/teaexam/web/* /Users/shenke/nnn7/teaexam/src/main/resources/templates

#familydear
ln -s /Users/shenke/nnn7/familydear/src/java/* /Users/shenke/nnn7/familydear/src/main/java
ln -s /Users/shenke/nnn7/familydear/web/* /Users/shenke/nnn7/familydear/src/main/resources/templates

#evaluation

ln -s /Users/shenke/nnn7/evaluation/src/java/* /Users/shenke/nnn7/evaluation/src/main/java
ln -s /Users/shenke/nnn7/evaluation/web/* /Users/shenke/nnn7/evaluation/src/main/resources/templates

#infrastructure

ln -s /Users/shenke/nnn7/infrastructure/src/java/* /Users/shenke/nnn7/infrastructure/src/main/java
ln -s /Users/shenke/nnn7/infrastructure/web/* /Users/shenke/nnn7/infrastructure/src/main/resources/templates

#datacenter
ln -s /Users/shenke/nnn7/datacenter/src/java/* /Users/shenke/nnn7/datacenter/src/main/java
ln -s /Users/shenke/nnn7/datacenter/web/* /Users/shenke/nnn7/datacenter/src/main/resources/templates

#openapi
ln -s /Users/shenke/nnn7/openapi/src/java/* /Users/shenke/nnn7/openapi/src/main/java
ln -s /Users/shenke/nnn7/openapi/web/* /Users/shenke/nnn7/openapi/src/main/resources/templates

