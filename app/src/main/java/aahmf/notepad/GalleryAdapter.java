package aahmf.notepad;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ArrayList<Uri> mArrayUri;
    private final Activity mActivity = null;
    private OnClickThumbListener mOnClickThumbListener;
    private Cursor mMediaStoreCursor;
    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }



    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    public Object getUri() {
        return mArrayUri;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);

        ivGallery.setImageURI(mArrayUri.get(position));

        return itemView;
    }

    private interface OnClickThumbListener {
        void OnClickImage(Uri imageUri);
    }

    private void getOnClickUri(int position) {
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);
        String dataString = mMediaStoreCursor.getString(dataIndex);
        String authorities = mActivity.getPackageName() + ".fileprovider";
        Uri mediaUri = FileProvider.getUriForFile(mActivity, authorities, new File(dataString));
//        Uri mediaUri = Uri.parse("file://" + dataString);

        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                mOnClickThumbListener.OnClickImage(mediaUri);
                break;
            default:
        }
}
}