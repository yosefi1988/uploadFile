package ir.sajjadyosefi.android.uploadfileserverapplication.multifile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ir.sajjadyosefi.android.uploadfileserverapplication.network.CountingRequestBody;
import ir.sajjadyosefi.android.uploadfileserverapplication.network.IFileUploadApiService;
import ir.sajjadyosefi.android.uploadfileserverapplication.network.ServiceGenerator;
import ir.sajjadyosefi.android.uploadfileserverapplication.utils.MIMEType;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService";
    Disposable mDisposable;
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 102;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /**
         * Download/Upload of file
         * The system or framework is already holding a wake lock for us at this point
         */

        // get file file here
        String mFilePath = intent.getStringExtra("mFilePath");
        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        IFileUploadApiService apiService = ServiceGenerator.createService();

        Flowable<Double> fileObservable = Flowable.create(emitter -> {


            Map<String, RequestBody> map = new HashMap<>();
            map.put("UserId", toRequestBody("userId000000000"));
            map.put("Type", toRequestBody("type0000000000"));

            if (mFilePath != null) {
                File file = new File(mFilePath);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                map.put("avatar\"; filename=\"" + file.getName() + "\"", fileBody);
            }




            //fistCode
//            apiService.onFileUploadInService(
//                    toRequestBody("info@androidwave.com"),
//                    createMultipartBody(mFilePath, emitter))
//                    .blockingGet();
//            emitter.onComplete();


            //newCode
            ResponseBody response = apiService.onFileUploadInService2(map).blockingGet();
            emitter.onComplete();


        }, BackpressureStrategy.LATEST);

        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        progress -> onProgress(progress),
                        throwable -> onErrors(throwable),
                        () -> onSuccess());
    }

    private void onErrors(Throwable throwable) {
        sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }

    private void onProgress(Double progress) {
        sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }

    private void onSuccess() {
        sendBroadcastMeaasge("File uploading successful ");
        Log.i(TAG, "onSuccess: File Uploaded");
    }

    public void sendBroadcastMeaasge(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }



    /**
     * return multi part body in format of FlowableEmitter
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("myFile", file.getName(),
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }

    private RequestBody createCountingRequestBody(File file, String mimeType,FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }





    private RequestBody toRequestBody(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
        //or
//        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
//        return body ;
    }
}
