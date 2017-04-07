package com.naivor.loadmorehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.naivor.loadmore.LoadMoreHelper;
import com.naivor.loadmore.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecySimpleActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private TestRecySimpleAdapter adapter;

    private LoadMoreHelper helper;

    private boolean isError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recycler LoadMore  Simple");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new TestRecySimpleAdapter(getApplicationContext());

        rvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvContent.setAdapter(adapter);

        helper = new LoadMoreHelper(getApplicationContext());
        helper.target(rvContent);
        helper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int next) {
                loadData(next);
            }

            @Override
            public void onReload(int index) {
                loadData(index);
            }
        });

        loadData(0);
    }

    private void loadData(final int next) {
        new Thread() {
            @Override
            public void run() {

                final List<SimpleItem> datas = new ArrayList<>();

                SimpleItem item = new SimpleItem();
                item.setContent("我是第 " + next + " 页的 Item");
                for (int i = 0; i < 10; i++) {
                    datas.add(item);
                }

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (next < 4) {
                                if (helper.isLoadMore()) {
                                    adapter.addItems(datas);
                                } else {
                                    adapter.setItems(datas);
                                }
                                helper.loadComplete();

                            } else if (!isError && next == 4) {
                                helper.loadError();

                                isError = true;
                            } else {
                                helper.setHasMore(false);
                            }

                        }
                    });
                }

            }
        }.start();
    }
}
