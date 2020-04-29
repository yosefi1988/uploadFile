package ir.sajjadyosefi.android.uploadfileserverapplication.picker;


import android.net.Uri;
import java.io.File;

public class ImageContract {
    public interface View {

        boolean checkPermission();

        void showPermissionDialog(boolean isGallery);

        File getFilePath();

        void openSettings();

        void startCamera(File file);

        void chooseGallery();

        File newFile();

        void showErrorDialog();

        void displayImagePreview(File mFile);

        String getRealPathFromUri(Uri contentUri);
    }

    interface Presenter {

        void cameraClick();

        void chooseGalleryClick();

        void saveImage(String filePath);

        String getImage();

        void showPreview(File mFile);
    }
}
