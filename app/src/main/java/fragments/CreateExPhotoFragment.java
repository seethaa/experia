package fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.experia.experia.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import models.Creation;
import util.CupboardDBHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class CreateExPhotoFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.ivExperiencePhoto) ImageView experiencePhoto;
    @BindView(R.id.btPhoto)
    Button selectBtn;
    @BindView(R.id.btnSubmitPhoto)
    Button btnSubmit;
    static SQLiteDatabase db;

    // PHOTO related constants
    public final static int PICK_PHOTO_CODE = 1046;
    public final String APP_TAG = "Experia";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";


    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    String mPhotoStringURL;

    private OnPhotoPickCompleteListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnPhotoPickCompleteListener {
        public void onSavePhotosCompleted(String imgString);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoPickCompleteListener) {
            listener = (OnPhotoPickCompleteListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnNameAndDescriptionCompleteListener");
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    public void onSavePhotos(View v) {


        System.out.println("DEBUGGY Exp 4 old: " + mPhotoStringURL);
        listener.onSavePhotosCompleted(mPhotoStringURL);
    }


    public static CreateExPhotoFragment newInstance(int color, int icon) {
        CreateExPhotoFragment fragment = new CreateExPhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        //experiencePhoto.setImageResource(getArguments().getInt(ARG_SECTION_NUMBER));

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePhotos(v);
            }
        });

        CupboardDBHelper dbHelper = new CupboardDBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        return rootView;
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_CODE);
            //startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) getActivity().findViewById(R.id.ivExperiencePhoto);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PICK_PHOTO_CODE) {

            if (data != null) {
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) getActivity().findViewById(R.id.ivExperiencePhoto);
                ivPreview.setImageBitmap(selectedImage);

                //Toast.makeText(getContext(),selectedImage.toString(),Toast.LENGTH_LONG).show();
                saveSelectedPhoto(getView(),selectedImage.toString());
            }
/*
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                ArrayList<Bitmap> mBitmapsSelected = new ArrayList<Bitmap>();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mBitmapsSelected.add(bitmap);
                    // Load the selected image into a preview
                    ImageView ivPreview = (ImageView) getActivity().findViewById(R.id.ivExperiencePhoto);
                    ivPreview.setImageBitmap(bitmap);
                }
            }
*/

        }
    }

    public void saveSelectedPhoto(View view, String photoFileName) {

        //set values obj
        ContentValues values = new ContentValues(1);
        values.put("imgURL", photoFileName);
        // update first record
        cupboard().withDatabase(db).update(Creation.class, values, "_id = ?", "1");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
