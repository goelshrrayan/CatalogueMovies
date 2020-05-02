package com.example.shrrmoviecat.function;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.shrrmoviecat.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import static com.example.shrrmoviecat.database.DatabaseContact.CONTENT_URI;


public class FavoriteFragment extends AppCompatActivity {

    private Context context;
    private Unbinder unbinder;


    RecyclerView rv_favorite;

    private Cursor list;
    private FavoriteAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorite);
        context = FavoriteFragment.this;
rv_favorite=(RecyclerView) findViewById(R.id.rv_favorite);
        setTitle("Favorite Movies");
        Log.i("dsdsd",rv_favorite+"");
        unbinder = ButterKnife.bind(this, this);

       // setupList();

        new loadDataAsync().execute();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadDataAsync().execute();
    }

    private void setupList() {
        adapter = new FavoriteAdapter(list);
        rv_favorite.setLayoutManager(new LinearLayoutManager(context));
        rv_favorite.setAdapter(adapter);
    }

    private class loadDataAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return FavoriteFragment.this.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            adapter = new FavoriteAdapter(list);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(FavoriteFragment.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            Log.i("sd",rv_favorite+" "+FavoriteFragment.this);

            rv_favorite.setLayoutManager(layoutManager);
            rv_favorite.setAdapter(adapter);
            adapter.replaceAll(list);
        }
    }
}
