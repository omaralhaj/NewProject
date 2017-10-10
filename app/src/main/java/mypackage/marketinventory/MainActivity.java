package mypackage.marketinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int MARKET_LOADER = 0;
    MarketCursorAdapter marketCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(intent);
            }
        });

        marketCursorAdapter = new MarketCursorAdapter(this, null);

        ListView listView = (ListView) findViewById(R.id.products);

        listView.setAdapter(marketCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductDetails.class);
                Uri currentUri = ContentUris.withAppendedId(MarketContract.ProductEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);

            }
        });

        listView.setEmptyView(findViewById(android.R.id.empty));
        getLoaderManager().initLoader(MARKET_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String projection[] = {
                MarketContract.ProductEntry._ID,
                MarketContract.ProductEntry.COLUMN_NAME,
                MarketContract.ProductEntry.COLUMN_PIC,
                MarketContract.ProductEntry.COLUMN_PRICE,
                MarketContract.ProductEntry.COLUMN_QUANTITY

        };

        return new CursorLoader(this,
                MarketContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        marketCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        marketCursorAdapter.swapCursor(null);

    }
}

