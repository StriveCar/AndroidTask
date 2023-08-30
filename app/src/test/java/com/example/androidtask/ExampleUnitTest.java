package com.example.androidtask;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;

import org.junit.Test;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        PhotoService photoService = RetrofitClient.getInstance().getService(PhotoService.class);
//        ArtWordService artWordService = RetrofitClient.getInstance().getService("https://apis.tianapi.com/", ArtWordService.class);
//
//
//        artWordService.getArtWord().enqueue(new Callback<WordResponse>() {
//            @Override
//            public void onResponse(Call<WordResponse> call, Response<WordResponse> response) {
//                if (response.body().getCode() == 200){
//                    System.out.println(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WordResponse> call, Throwable t) {
//
//            }
//        });
//        photoService.getShare(1).subscribe(new Consumer<BaseResponse<Data<Records>>>() {
//            @Override
//            public void accept(BaseResponse<Data<Records>> dataBaseResponse) throws Throwable {
//                System.out.println(dataBaseResponse);
//            }
//        });

        photoService.getShare(null, null, "1692126434274971648")
                .subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE); // 请求数据
                    }

                    @Override
                    public void onNext(BaseResponse<Data<Records>> response) {
                        System.out.println(response.getData().getRecords());
                    }

                    @Override
                    public void onError(Throwable t) {
                        // 处理错误情况，比如显示错误信息
                    }

                    @Override
                    public void onComplete() {
                        // 数据流处理完成，可以执行一些收尾操作
                    }
                });

//        photoService.userRegister(new User("admin", "admin")).enqueue(new Callback<BaseResponse<Object>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
//                if (response.body().getCode() == 200) {
//                    System.out.println("注册成功!  "+response.body());
//                } else if (response.body().getCode() == 500) {
//                    System.out.println(response.body().getMsg());
//                }
//            }
//            @Override
//            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
//                System.out.println("注册失败");
//            }
//        });


//        photoService.userLogin("admin", "admin").enqueue(new Callback<BaseResponse<LoginData>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<LoginData>> call, Response<BaseResponse<LoginData>> response) {
//                if (response.body().getCode() == 200) {
//                    System.out.println("登录成功!  "+response.body().getData());
//                } else if (response.body().getCode() == 500) {
//                    System.out.println(response.body().getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<LoginData>> call, Throwable t) {
//                System.out.println("登陆失败");
//            }
//        });

        while (true) {
        }
    }
}