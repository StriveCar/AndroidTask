package com.example.androidtask;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.androidtask.network.RetrofitClient;
import com.example.androidtask.network.service.PhotoService;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.User;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
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
//        photoService.getShare(1).subscribe(new Consumer<BaseResponse<Data<Records>>>() {
//            @Override
//            public void accept(BaseResponse<Data<Records>> dataBaseResponse) throws Throwable {
//                System.out.println(dataBaseResponse);
//            }
//        });

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


        while (true) {
        }
    }
}