package My_ViewPager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.luckzhang.Check_Activity;
import com.example.luckzhang.R;
import com.example.luckzhang.Record_detail_Activity;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Data_Class.Report_item;
import Data_Class.User_Info;
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
    private Button mainImportantButton;
    private ImageView imageView;
    private TextView  textView1;
    private TextView  textView2;
    private TextView  textView3;
    private TextView  textView4;
    private TextView  textView5;
    private TextView  textView_day;
    public MyPageAdapter(Context context1) {
        this.context=context1;
        view1=View.inflate(context,R.layout.main_left,null);
        view2=View.inflate(context,R.layout.main_right,null);
        mViewList.add(view1);
        mViewList.add(view2);
        listView=(ListView) view2.findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) view2.findViewById(R.id.refresh);
        imageView=view1.findViewById(R.id.imageView_left);
        textView1=view1.findViewById(R.id.tt1);
        textView2=view1.findViewById(R.id.tt2);
        textView3=view1.findViewById(R.id.tt3);
        textView4=view1.findViewById(R.id.tt4);
        textView5=view1.findViewById(R.id.tt5);
        textView_day=view1.findViewById(R.id.textViewl2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_left_grid();
            }
        });
        init_left_button();
        init_left_grid();

        init_right_listview();
        init_right_refresh();
    }

    //初始化网格布局下方的按钮
    private void init_left_button(){
        mainImportantButton=(Button)view1.findViewById(R.id.button_begin_check);
        mainImportantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context, Check_Activity.class);
                context.startActivity(intent);
            }
        });
    }

    //初始化网格信息
    private void init_left_grid()  {
        User_Info user_info=LitePal.findFirst(User_Info.class);
        if(user_info==null){
            Log.d("MyPageAdapter","用户账户出错");
        }else{
            textView1.setText(user_info.getUser_five_fen());
            textView2.setText(user_info.getUser_five_ci());
            textView3.setText(user_info.getUser_five_zheng());
            textView4.setText(user_info.getUser_five_yi());
            textView5.setText(user_info.getUser_five_yu());
            SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy-MM-dd");
            Date dated= null;
            try {
                dated = sdf11.parse(user_info.getUser_useDay());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(System.currentTimeMillis());
            int i= (int) ((date.getTime()-dated.getTime())/(60*60*1000*24));
            textView_day.setText(i+1+"");
        }
    }

    //初始化右侧的列表
    private void init_right_listview(){
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
        List<Report_item> items= LitePal.findAll(Report_item.class);
        /*if(items.size()==0){
            return;
        }*/
        MyItemAdapter adapter=new MyItemAdapter(context,R.layout.record_item_layout,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tev=view.findViewById(R.id.textViewtete);
                TextView tet=view.findViewById(R.id.textView7);
                if(tev.getText().equals("完成")){
                    Intent intent=new Intent();
                    intent.putExtra("id",tet.getText());
                    intent.setClass(context, Record_detail_Activity.class);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "请下拉刷新重试", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    //初始化右侧的下拉刷新界面
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

