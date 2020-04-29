package ir.sajjadyosefi.android.uploadfileserverapplication.network;


import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.ResponseBody;


public class FileUploaderContract {
    public interface View {
        void showErrorMessage(String message);

        void uploadCompleted();

        void setUploadProgress(int progress);
    }

    interface Presenter {
        void onFileSelected(String selectedFile, String userId, String type);

        void onFileSelectedWithoutShowProgress(String selectedFilePath, String userId, String type);

        void cancel();
    }

    interface Model {
        Flowable<Double> uploadFile(String selectedFilePath, String userId, String type);

        Single<ResponseBody> uploadFileWithoutProgress(String filePath, String userId, String type);
    }

}
