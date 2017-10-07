package mypackage.marketinventory;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.Provider;
import java.util.regex.Matcher;

import static android.os.Build.PRODUCT;
import static mypackage.marketinventory.MarketContract.CONTENT_LIST_TYPE;

/**
 * Created by Omar on 10/1/17.
 */

public class MarketProvider extends ContentProvider {

    private static final int PRODUCTS = 100;

    private static final int PRODUCT_ID = 101;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {


        matcher.addURI(MarketContract.CONTENT_AUTHORITY, MarketContract.PATH_INVENTORY, PRODUCTS);
        matcher.addURI(MarketContract.CONTENT_AUTHORITY, MarketContract.PATH_INVENTORY + "/#", PRODUCT_ID);

    }


    public static final String LOG_TAG = MarketProvider.class.getSimpleName();

    private MarketHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MarketHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = matcher.match(uri);

        switch (match) {
            case PRODUCTS:
                cursor = database.query(MarketContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case PRODUCT_ID:
                selection = MarketContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MarketContract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = matcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return MarketContract.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return MarketContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = matcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(MarketContract.ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = matcher.match(uri);
        switch (match) {
            case PRODUCTS:

                rowsDeleted = database.delete(MarketContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = MarketContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MarketContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);


        }
        if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = matcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:

                selection = MarketContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                return -1;
        }
    }


    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(MarketContract.ProductEntry.COLUMN_NAME)) {
            String name = values.getAsString(MarketContract.ProductEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(MarketContract.ProductEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(MarketContract.ProductEntry.COLUMN_QUANTITY);
            if (quantity == 0) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }
        }

        if (values.containsKey(MarketContract.ProductEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(MarketContract.ProductEntry.COLUMN_PRICE);
            if (price == 0) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }
        if (values.containsKey(MarketContract.ProductEntry.COLUMN_PIC)) {
            String pic = values.getAsString(MarketContract.ProductEntry.COLUMN_PIC);
            if (pic.equalsIgnoreCase(null) || pic.isEmpty()) {
                throw new IllegalArgumentException("Product requires a picture");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        dbHelper = new MarketHelper(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        return database.update(MarketContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    }

