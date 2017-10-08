package mypackage.marketinventory;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Omar on 10/1/17.
 */

public class MarketContract {


        private MarketContract() {}

        /**
         +     * The "Content authority" is a name for the entire content provider, similar to the
         +     * relationship between a domain name and its website.  A convenient string to use for the
         +     * content authority is the package name for the app, which is guaranteed to be unique on the
         +     * device.
         +     */
     public static final String CONTENT_AUTHORITY = "mypackage.marketinventory";

     public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

      public static final String PATH_INVENTORY = "products";

    public static final String CONTENT_LIST_TYPE =
                          ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;


                 public static final String CONTENT_ITEM_TYPE =
                           ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final class ProductEntry implements BaseColumns {


     public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);


            public final static String TABLE_NAME = "products";


            public final static String _ID = BaseColumns._ID;


            public final static String COLUMN_NAME ="name";


            public final static String COLUMN_PRICE = "price";


            public final static String COLUMN_QUANTITY = "quantity";


            public final static String COLUMN_PIC = "picture";



        }

    }


