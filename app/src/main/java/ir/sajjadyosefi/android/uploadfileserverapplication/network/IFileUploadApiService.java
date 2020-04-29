package ir.sajjadyosefi.android.uploadfileserverapplication.network;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created on : Dec 30, 2018
 * Author     : AndroidWave
 * Website    : https://androidwave.com/
 */
public interface IFileUploadApiService {
    //profile
    @Multipart
    @POST("UserAvatarProfileImage")
    Single<ResponseBody> onFileUpload(
            @Part("UserId") RequestBody mUserId,
            @Part("Type") RequestBody mType,
            @Part MultipartBody.Part avatar);

    @Multipart
    @POST("UserAvatarProfileImage")
    Single<ResponseBody> onFileUpload2(@PartMap Map<String, RequestBody> params);




    //multifile upload
//    @Multipart
//    @POST("fileUpload.php")
//    Single<ResponseBody> onFileUploadInService(@Part("email") RequestBody mEmail,
//                                      @Part MultipartBody.Part file);

    @Multipart
    @POST("UserAvatarProfileImage")
    Single<ResponseBody> onFileUploadInService(@Part("email") RequestBody mEmail,
                                      @Part MultipartBody.Part file);
    @Multipart
    @POST("UserAvatarProfileImage")
    Single<ResponseBody> onFileUploadInService2(@PartMap Map<String, RequestBody> params);

}
