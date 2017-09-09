# home
## Home web project
> ### 1 登陆接口: /api/login  POST
```     
     接口参数: {"username":"siwang","password":"xuyans"}
     成功:{"status":200,"msg":"success","data":     {"id":"59aad95f21d07310f8e8d453","username":"siwang","password":"xuyan","type":1}}
     失败:{"status":201,"msg":"fail","data":null}
```

> ### 2 创建房产信息接口: /api/hourse/create POST
```
     接口参数:{"title":"正商新蓝钻","price":200,"acreage":123,"status":1,"houseOwnerName":"李坤","houseOwnerPhone":"18239926789",
     	"address":"中原区新园区","infomation":"面积是123平米","userId":"59aad72021d0731974afcabf","images":[{"imageUrl":"www.baidu.com"},
     	{"imageUrl":"www.google.com"}],"state":1
     }
成功:{
       "status": 200,
       "msg": "success",
       "data": {
           "id": "59b38d2e21d07319cc3d06e6",
           "title": "正商新蓝钻",
           "price": 200,
           "acreage": 123,
           "status": 1,
           "houseOwnerName": "李坤",
           "houseOwnerPhone": "18239926789",
           "address": "中原区新园区",
           "infomation": "面积是123平米",
           "userId": "59aad72021d0731974afcabf",
           "state": 1,
           "images": [
               {
                   "id": null,
                   "imageUrl": "www.baidu.com"
               },
               {
                   "id": null,
                   "imageUrl": "www.google.com"
               }
           ]
       }
   }
```
> ### 3 注册用户接口: /api/account POST
```
接口参数:{"username":"siwang","password":"xuyan","type":1}
成功:{
       "status": 200,
       "msg": "success",
       "data": {
           "id": "59b3919f21d07319cc3d06e7",
           "username": "siwng",
           "password": "xuyan",
           "type": 1
       }
   }
失败:{
       "status": 201,
       "msg": "fail",
       "data": "账号已存在"
   }
```
> ### 4 获取房产详细信息接口: /api/hourse/{hourseId}  GET
```
成功：{
       "status": 200,
       "msg": "success",
       "data": {
           "id": "59b38d2e21d07319cc3d06e6",
           "title": "正商新蓝钻",
           "price": 200,
           "acreage": 123,
           "status": 1,
           "houseOwnerName": "李坤",
           "houseOwnerPhone": "18239926789",
           "address": "中原区新园区",
           "infomation": "面积是123平米",
           "userId": "59aad72021d0731974afcabf",
           "state": 1,
           "images": [
               {
                   "id": null,
                   "imageUrl": "www.baidu.com"
               },
               {
                   "id": null,
                   "imageUrl": "www.google.com"
               }
           ]
       }
   }
失败：{
       "status": 201,
       "msg": "fail",
       "data": null
   }
```

> ### 5 获取用户可看到的所有信息接口：/api/hourses/{userId} POST
```
参数:{"pageSize":10,"pageNumber":0}
成功:{
       "status": 200,
       "msg": "success",
       "data": [
           {
               "id": "59b39abb21d073191403e50f",
               "title": "正商新蓝钻A区",
               "price": 160,
               "acreage": 118,
               "status": 1,
               "houseOwnerName": "张永亮",
               "houseOwnerPhone": "18239926789",
               "address": "中原区航海东路",
               "infomation": "急用钱",
               "userId": "59aad72021d0731974afcabf",
               "createDate": 1504942779345,
               "updateDate": null,
               "state": 1,
               "images": [
                   {
                       "imageUrl": "www.baidu.com"
                   },
                   {
                       "imageUrl": "www.google.com"
                   }
               ]
           },
           {
               "id": "59b38d2e21d07319cc3d06e6",
               "title": "正商新蓝钻",
               "price": 200,
               "acreage": 123,
               "status": 1,
               "houseOwnerName": "李坤",
               "houseOwnerPhone": "18239926789",
               "address": "中原区新园区",
               "infomation": "面积是123平米",
               "userId": "59aad72021d0731974afcabf",
               "createDate": null,
               "updateDate": null,
               "state": 1,
               "images": [
                   {
                       "imageUrl": "www.baidu.com"
                   },
                   {
                       "imageUrl": "www.google.com"
                   }
               ]
           },
           {
               "id": "59b3951921d07319cc3d06e8",
               "title": "正商新蓝钻A区",
               "price": 160,
               "acreage": 118,
               "status": 1,
               "houseOwnerName": "张永亮",
               "houseOwnerPhone": "18239926789",
               "address": "中原区航海东路",
               "infomation": "急用钱",
               "userId": "59aad72021d0731974afcabf",
               "createDate": null,
               "updateDate": null,
               "state": 1,
               "images": [
                   {
                       "imageUrl": "www.baidu.com"
                   },
                   {
                       "imageUrl": "www.google.com"
                   }
               ]
           }
       ]
   }
```
   
> ### 6 删除房产信息接口: /api/delete/{hourseId}  DELETE
```
成功:{
          "status": 200,
          "msg": "success",
          "data": "success"
      }
      
更新房产信息接口: /api/update  PUT
参数:{"id":"id",title":"正商新蓝钻","price":200,"acreage":123,"status":1,"houseOwnerName":"李坤","houseOwnerPhone":"18239926789",
        	"address":"中原区新园区","infomation":"面积是123平米","userId":"59aad72021d0731974afcabf","images":[{"imageUrl":"www.baidu.com"},
        	{"imageUrl":"www.google.com"}],"state":1
        }
成功:{
       "status": 200,
       "msg": "success",
       "data": {
           "id": "59b39abb21d073191403e50f",
           "title": "正商新蓝钻C区",
           "price": 160,
           "acreage": 108,
           "status": 1,
           "houseOwnerName": "张永亮",
           "houseOwnerPhone": "18239926789",
           "address": "管城区航海东路",
           "infomation": "急用钱",
           "userId": "59aad72021d0731974afcabf",
           "createDate": null,
           "updateDate": 1504944010241,
           "state": 1,
           "images": [
               {
                   "imageUrl": "www.baidu.com"
               },
               {
                   "imageUrl": "www.google.com"
               }
           ]
       }
   }
```
> ### 7 删除用户接口: /api/deleteUser/{userId}  DELETE
```
成功:{
             "status": 200,
             "msg": "success",
             "data": null
         }
```
