# 如何添加网络请求

## 添加请求链接

在network软件包下的service软件包下的PhotoService类中添加，例如接口http://47.107.52.7:88/member/photo/share 格式如下：

```java
	@GET("share")
    Observable<BaseResponse<Data<Records>>> getShare(@Query("userId") int userId);
```

#### **添加注解**

图片分享后端提供api中除了GET就是POST，是哪个就写哪个

链接就写除了BASE_URL(http://47.107.52.7:88/member/photo/)  以外的部分，上面的链接就是share

**注意：链接前面是没有/的，因为BASE_URL是以/结尾的**

#### **选择数据源类型**

常使用类型有Retrofit的Call，RxJava的Observable和Flowable

优缺点分析：

Call的优缺点:

​	简单性：Call 是 Retrofit 的默认数据流类型，使用起来非常简单直观。如果你只需要执行异步请求并获取响应，而不需要进行复杂	的数据流操作，那么使用 Call 可能更加简单和直接。

​	较小的数据量：如果你的响应数据量相对较小，并且不需要进行背压处理，那么使用 Call 可以满足需求。

​	不需要响应式编程：如果你不需要利用响应式编程的特性，如操作符链式调用、线程调度、错误处理等，那么使用 Call 可以更加简	洁

​	使用场景：登陆、注册、忘记密码等请求

`Observable`的优缺点：

​	简单易用：`Observable`是RxJava的基础类型，使用起来非常简单和直观。

​	轻量级：相对于`Flowable`，`Observable`在内存消耗方面较少。

​	缺乏背压支持：`Observable`不支持背压，当生产者产生数据速度大于消费者处理速度时，可能会导致内存溢出或数据丢失的问	        	题。

​	不适用于处理大量数据：当需要处理大量数据或者涉及到潜在的速度不匹配问题时，`Observable`可能不是最佳选择。

​	使用场景：适用于简单的数据流处理和不需要背压支持的场景。

`Flowable`的优缺点：

​	背压支持：`Flowable`提供了背压支持，可以有效地处理生产者和消费者之间速度不匹配的问题。

​	适用于处理大量数据：对于大量数据的处理，`Flowable`可以通过背压策略来控制数据的流量，避免内存溢出或数据丢失的问题。

​	复杂性增加：相对于`Observable`，`Flowable`具有更多的概念和操作符，因为它需要支持背压机制，这可能会增加学习和使用的	复杂性。

​	内存消耗增加：由于需要缓存数据以处理背压，`Flowable`在处理大量数据时可能会占用更多的内存。

​	使用场景：适用于处理大量数据或需要背压支持的场景。

#### 范型选择

复制返回数据到BeJson网站（[在线JSON字符串转Java实体类(JavaBean、Entity)-BeJSON.com](https://www.bejson.com/json2javapojo/new/)） ，即可生成实体类。

需要注意的是：生成的实体类可以把某些数据类型改为范型，这样可以重用这些实体类

例如，http://47.107.52.7:88/member/photo/share 返回的数据中data是一个Json，里面又有一个records列表，列表里面还是Json对象

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "records": [
      {
        "id": "4821",
        "pUserId": "1688853878336000000",
        "imageCode": "1689123137066766336",
        "title": "宝宝补钙",
        "content": "啦啦啦",
        "createTime": "1691553313995",
        "imageUrlList": [
          "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2023/08/09/f5cdd6bf-a829-4756-91e6-51407b634852.jpg"
        ],
        "likeId": null,
        "likeNum": 0,
        "hasLike": false,
        "collectId": null,
        "collectNum": 0,
        "hasCollect": false,
        "hasFocus": false,
        "username": "user1"
      },
      {
        "id": "4820",
        "pUserId": "1688853878336000000",
        "imageCode": "1689123063481896960",
        "title": "将计就计",
        "content": "777",
        "createTime": "1691553297580",
        "imageUrlList": [
          "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2023/08/09/6dbbea50-8733-4c65-9f75-5634e60beab0.jpg"
        ],
        "likeId": null,
        "likeNum": 0,
        "hasLike": false,
        "collectId": null,
        "collectNum": 0,
        "hasCollect": false,
        "hasFocus": false,
        "username": "user1"
      },
    ],
    "total": 2580,
    "size": 10,
    "current": 1
  }
}
```

因此我们使用Bejson生成之后需要修改范型

```java
public class Records { //对应返回的Records中的每一项
    private String id;
    private String pUserId;
    private String imageCode;
    private String title;
    private String content;
    private String createTime;
    private List<String> imageUrlList;
    private String likeId;
    private int likeNum;
    private boolean hasLike;
    private String collectId;
    private int collectNum;
    private boolean hasCollect;
    private boolean hasFocus;
    private String username;
    public void setId(String id) {
         this.id = id;
     }
}

public class Data<T>{  //对应返回结果的Data
    private List<T> records;
    private int total;
    private int size;
    private int current;
}

public class BaseResponse <T> {  //对应返回结果
    private int code;
    private String msg;
    public final static int RESPONSE_SUCCESS = 0;
    private T data;

}
```

那么此时我们的范型选择为**Observable<BaseResponse<Data<Records>>>**，刚好跟返回结果层层对应。

#### 参数注解选择

普通参数：比如请求地址:http://47.107.52.7:88/member/photo/collect?userId=1 那么userId作为参数传递时选择注解@Query

Json参数：比如注册请求：http://47.107.52.7:88/member/photo/user/register ，但参数是

{
  "password": "123456",
  "username": "admin"
}

的Json对象，此时我们建立一个java类的数据对应Json对象，使用注解@Body

```java
public class User {
    private String password;
    private String username;

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }
}

	@POST("user/register")
    Call<BaseResponse<Object>> userRegister(@Body User user);
```

可选参数：

#### 如何调用获取返回结果

在com.example.androidtask(test)中的ExampleUnitTest类中也有响应代码参考

```
		//获取photoService对象固定写法
		PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
		
		//数据源类型为Flowable
        photoService.getShare(1).subscribe(new Consumer<BaseResponse<Data<Records>>>() {
            @Override
            public void accept(BaseResponse<Data<Records>> dataBaseResponse) throws Throwable {
                System.out.println(dataBaseResponse);
            }
        });

		//数据源类型为Observable
        photoService.getShare(1).subscribe(new Observer<BaseResponse<Data<Records>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {  //接收到数据时调用

            }

            @Override
            public void onNext(@NonNull BaseResponse<Data<Records>> dataBaseResponse) { //接收到数据时调用
                System.out.println(dataBaseResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) { //发生错误时调用
                System.out.println("注册失败");
            }

            @Override
            public void onComplete() { //完成时调用
                System.out.println("注册成功");
            }
        });
        
        //数据源类型为Call
        photoService.userRegister(new User("admin", "admin")).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (response.body().getCode() == 200) {
                    System.out.println("注册成功!  "+response.body());
                } else if (response.body().getCode() == 500) {
                    System.out.println(response.body().getMsg());
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                System.out.println("注册失败");
            }
        });
```

