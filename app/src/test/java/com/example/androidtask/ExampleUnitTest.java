package com.example.androidtask;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.Records;

import org.junit.Test;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

//        photoService.getShare(null, null, "1692126434274971648").subscribe(new Observer<BaseResponse<Data<Records>>>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull BaseResponse<Data<Records>> dataBaseResponse) {
//                Log.d("kkx ;",dataBaseResponse.toString());
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                Log.d("kkx ;",e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d("kkx ;","完成");
//            }
//        });
//        photoService.getShare(null, null, "1692126434274971648")
//                .subscribe(new FlowableSubscriber<BaseResponse<Data<Records>>>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE); // 请求数据
//                    }
//
//                    @Override
//                    public void onNext(BaseResponse<Data<Records>> response) {
//                        System.out.println(response.getData().getRecords());
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        // 处理错误情况，比如显示错误信息
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 数据流处理完成，可以执行一些收尾操作
//                    }
//                });

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

        ImageText imageText = new ImageText();
        imageText.setContent("kkx");
        imageText.setImageCode("1703725920025710592");
        imageText.setPUserId("1691444731072090112");
        imageText.setTitle("测试2");

        photoService.uploadAdd(imageText).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (response.body().getCode() == 200) {
                    System.out.println("新增成功!  " + response.body());
                } else if (response.body().getCode() == 500) {
                    System.out.println(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {

            }
        });

        while (true) {
        }
    }
}