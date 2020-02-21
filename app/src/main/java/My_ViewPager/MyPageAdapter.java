package My_ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.luckzhang.R;
import com.example.luckzhang.Record_detail_Activity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import Data_Class.Report_item;
import SomeTools.MyItemAdapter;

/*
* viewpager页的初始化
* 包括了右侧的list列表的刷新
* */
public class MyPageAdapter extends PagerAdapter {
    private List<View> mViewList = new ArrayList<>();
    private View view1;
    private View view2;
    private ListView listView;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MyPageAdapter(Context context1) {
        this.context=context1;
        view1=View.inflate(context,R.layout.main_left,null);
        view2=View.inflate(context,R.layout.main_right,null);
        mViewList.add(view1);
        mViewList.add(view2);
        listView=(ListView) view2.findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) view2.findViewById(R.id.refresh);
        init_right_listview();
        init_right_refresh();

    }

//初始化右侧的列表
    private void init_right_listview(){
        //LitePal.deleteAll(Report_item.class);
        Report_item report_item=new Report_item();
        report_item.setItem_id(1);
        report_item.setItem_time("2020-02-20");
        report_item.setItem_title("体态检测报告");
        report_item.setItem_name("卑微小张");
        report_item.setItem_detail_id(1);
        report_item.save();
        List<Report_item> items= LitePal.findAll(Report_item.class);
        if(items.size()==0){
            return;
        }
        MyItemAdapter adapter=new MyItemAdapter(context,R.layout.record_item_layout,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",position+"");
                intent.setClass(context, Record_detail_Activity.class);
                context.startActivity(intent);
            }
        });
        //下面这个方法是为了让refresh和listview不冲突
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }});
    }

    private void init_right_refresh(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                 android.R.color.holo_orange_light);
        swipeRefreshLayout.setDistanceToTriggerSync(500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 init_right_listview();
                 swipeRefreshLayout.setRefreshing(false);
             }
        });
        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
             @Override
             public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                 return false;
             }
        });
    }

    // 返回界面数量
    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 添加界面，一般会添加当前页和左右两边的页面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    // 去除页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

}

