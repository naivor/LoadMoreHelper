# LoadMoreHelper
Listview，RecyclerView 分页加载的辅助类

地址：
```
compile com.naivor:loadmore:1.0.2
```
用法：

1.初始化一个 LoadMoreHelper
```
LoadMoreHelper helper=new LoadMoreHelper(context);
```
2.设置目标view（仅支持ListView，RecyclerView）
```
helper.target(view);
```
3.加载下一页的回调
```
 helper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int next) {
               //TODO 加载逻辑，next为下一页的索引
            }
        });
```
4.其他

初始化索引起始值，有人喜欢从0开始，有人喜欢从1开始
```
LoadMoreHelper.initIndex(num);
```
设置加载模式
```
helper.setLoadMode(loadMode);  // MODE_AUTO, MODE_CLICK;  //自动加载，点击加载，默认是自动加载
```

设置loadmore的样式
```
   helper.setOriginHint(originHint);
   helper.setErrorHint(errorHint);
   helper.setLoadingHint(loadingHint);
   helper.setNoMoreDataHint(noMoreDataHint);
   helper.setLoadingDrawable(loadingDrawable);

```
加载完成操作
```
 	/**
     * 加载完成
     */
    void loadComplete();

    /**
     * 加载出错
     */
    void loadError();

    /**
     * 重置
     */
    void reset();

    /**
     * 执行加载
     */
    void loading();

    /**
     * 没有更多数据
     */
    void noMoreData();
```

5.效果：


![img](https://github.com/naivor/LoadMoreHelper/blob/master/docs/loadmore.gif)

![img](https://github.com/naivor/LoadMoreHelper/blob/master/docs/loadmore_clickmode.gif)

License
=========

    Copyright (c) 2017. Naivor.All rights reserved. 

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.