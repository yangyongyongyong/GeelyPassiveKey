package com.luca.geelykeyradar;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;

/** 只读暴露应用私有更新缓存，供系统安装器通过临时 URI 读取 APK。 */
public class UpdateApkProvider extends ContentProvider {
    private static final String UPDATE_DIR = "updates";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return "application/vnd.android.package-archive";
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        File file = resolve(uri);
        MatrixCursor cursor = new MatrixCursor(
                projection == null ? new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE} : projection);
        if (file == null || !file.isFile()) return cursor;
        Object[] values = new Object[cursor.getColumnCount()];
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);
            if (OpenableColumns.DISPLAY_NAME.equals(column)) values[i] = file.getName();
            else if (OpenableColumns.SIZE.equals(column)) values[i] = file.length();
        }
        cursor.addRow(values);
        return cursor;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (!"r".equals(mode)) throw new FileNotFoundException("更新文件只读");
        File file = resolve(uri);
        if (file == null || !file.isFile()) throw new FileNotFoundException("更新文件不存在");
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    private File resolve(Uri uri) {
        if (getContext() == null || uri == null) return null;
        String name = uri.getLastPathSegment();
        if (name == null || name.isEmpty()) return null;
        try {
            File root = new File(getContext().getCacheDir(), UPDATE_DIR).getCanonicalFile();
            File file = new File(root, name).getCanonicalFile();
            return root.equals(file.getParentFile()) ? file : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("read-only provider");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("read-only provider");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("read-only provider");
    }
}
