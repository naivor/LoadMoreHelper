package com.naivor.loadmorehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.naivor.loadmore.LoadMoreHelper;
import com.naivor.loadmore.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity {

    @Bind(R.id.lv_content)
    ListView lvContent;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private TestListAdapter adapter;

    private LoadMoreHelper helper;

    private boolean isError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ListView  LoadMore");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new TestListAdapter(getApplicationContext());
        lvContent.setAdapter(adapter);

        helper = new LoadMoreHelper(getApplicationContext());
        helper.target(lvContent);
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
                for (int i = 0; i < 5; i++) {
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
