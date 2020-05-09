package My_ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.luckzhang.ExerciseActivity;
import com.example.luckzhang.R;
import com.example.luckzhang.Record_detail_Activity;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Data_Class.Report_item;
import Data_Class.Ten_Items;
import Data_Class.User_Info;
import SomeTools.MyItemAdapter;
import SomeTools.ThirdItemAdapter;
import Toolar_toNext.Help_Activity;

/*
* viewpager页的初始化
* 包括了右侧的list列表的刷新
* */
public class MyPageAdapter extends PagerAdapter {
    private User_Info user_info;
    private List<View> mViewList = new ArrayList<>();
    private View view1;
    private View view2;
    private View view3;
    private ListView listView;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button mainImportantButton;
    private TextView  textView1;
    private TextView  textView2;
    private TextView  textView3;
    private TextView  textView4;
    private TextView  textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView  textView_day;
    private TextView textView15;
    private ImageView imageView13;

    private ListView third_list;

    public MyPageAdapter(final Context context1) {
        user_info=LitePal.findFirst(User_Info.class);
        this.context=context1;
        view1=View.inflate(context,R.layout.main_left,null);
        view2=View.inflate(context,R.layout.main_right,null);
        view3=View.inflate(context,R.layout.main_third,null);

        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        listView=(ListView) view2.findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) view2.findViewById(R.id.refresh);

        textView1=view1.findViewById(R.id.tt1);
        textView2=view1.findViewById(R.id.tt2);
        textView3=view1.findViewById(R.id.tt3);
        textView4=view1.findViewById(R.id.tt4);
        textView5=view1.findViewById(R.id.tt5);
        textView7=view1.findViewById(R.id.tt6);

        textView_day=view1.findViewById(R.id.textViewl2);
        textView6=view1.findViewById(R.id.sdfaadf);
        textView15=view1.findViewById(R.id.textView15);
        textView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentasfs=new Intent(context, Help_Activity.class);
                context.startActivity(intentasfs);
            }
        });
        third_list=view3.findViewById(R.id.third_list);

        init_left_button();
        init_left_grid();
        init_right_listview();
        init_right_refresh();
        init_third_data();
        init_third();
    }

    //初始化网格布局下方的按钮
    private void init_left_button(){
        imageView13=view1.findViewById(R.id.imageView13);
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_info.getEye()==0){//可见
                    user_info.setEye(1);
                    user_info.save();
                    Toast.makeText(context, "已隐藏数据", Toast.LENGTH_SHORT).show();
                }else if(user_info.getEye()==1){//不可见
                    user_info.setEye(0);
                    user_info.save();
                    Toast.makeText(context, "已显示数据", Toast.LENGTH_SHORT).show();
                }
                init_left_grid();
            }
        });
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
        if(user_info==null){
            Log.d("MyPageAdapter","用户账户出错");
        }else{
            if(user_info.getEye()==0){
                Double d= Double.valueOf(user_info.getUser_five_fen());
                DecimalFormat decimalFormat=new DecimalFormat("0.00");
                String fen=decimalFormat.format(d);
                textView1.setText(fen);
                textView2.setText(user_info.getUser_five_ci());
                textView7.setText(user_info.getUser_parcent()+"%");
                textView3.setText(user_info.getUser_five_zheng());
                textView4.setText(user_info.getUser_five_yi());
                textView5.setText(user_info.getUser_five_yu());
                textView6.setText("检测时间："+user_info.getUser_five_id_time());
            }else if(user_info.getEye()==1){
                textView1.setText("**");
                textView2.setText("**");
                textView7.setText("**%");
                textView3.setText("**");
                textView4.setText("**");
                textView5.setText("**");
                textView6.setText("检测时间：****-**-** **:**");
            }
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
    public void init_right_listview(){
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

        List<Report_item> items=LitePal.findAll(Report_item.class);

        Iterator<Report_item> iterator=items.iterator();
        while (iterator.hasNext()){
            Report_item re=iterator.next();
            if(!re.isJudge()){
                iterator.remove();
            }
        }
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                final TextView time=view.findViewById(R.id.textView7);
                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setTitle("注意")
                        .setMessage("确定要删除"+time.getText().toString()+"的记录吗？")
                        .setPositiveButton("确定",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Report_item> report_itemList=LitePal.findAll(Report_item.class);
                                for(Report_item s:report_itemList){
                                    if(s.getItem_time().equals(time.getText().toString())){
                                        s.setJudge(false);
                                        s.save();
                                        init_right_listview();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消",new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                return true;
            }
        });


    }
    //初始化右侧的下拉刷新界面
    private void init_right_refresh(){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                 android.R.color.holo_orange_light);
        swipeRefreshLayout.setDistanceToTriggerSync(400);
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

    //初始化第三个界面的数据
    private void init_third_data(){
        if(LitePal.count(Ten_Items.class)!=10){
            LitePal.deleteAll(Ten_Items.class);
            Ten_Items ten_items0=new Ten_Items();
            ten_items0.setTitle("使用须知");
            ten_items0.setData("尚无记录");
            ten_items0.save();

            Ten_Items ten_items1=new Ten_Items();
            ten_items1.setTitle("头部侧倾");
            ten_items1.setData("尚无记录");
            ten_items1.save();

            Ten_Items ten_items2=new Ten_Items();
            ten_items2.setTitle("头部前倾");
            ten_items2.setData("尚无记录");
            ten_items2.save();

            Ten_Items ten_items3=new Ten_Items();
            ten_items3.setTitle("颈椎异位");
            ten_items3.setData("尚无记录");
            ten_items3.save();

            Ten_Items ten_items4=new Ten_Items();
            ten_items4.setTitle("肩部侧倾");
            ten_items4.setData("尚无记录");
            ten_items4.save();

            Ten_Items ten_items5=new Ten_Items();
            ten_items5.setTitle("脊柱异位");
            ten_items5.setData("尚无记录");
            ten_items5.save();

            Ten_Items ten_items6=new Ten_Items();
            ten_items6.setTitle("髋部侧倾");
            ten_items6.setData("尚无记录");
            ten_items6.save();

            Ten_Items ten_items7=new Ten_Items();
            ten_items7.setTitle("髋部异位");
            ten_items7.setData("尚无记录");
            ten_items7.save();

            Ten_Items ten_items8=new Ten_Items();
            ten_items8.setTitle("腿部异常");
            ten_items8.setData("尚无记录");
            ten_items8.save();

            Ten_Items ten_items9=new Ten_Items();
            ten_items9.setTitle("膝盖异位");
            ten_items9.setData("尚无记录");
            ten_items9.save();


        }
    }
    //初始化第三个界面的列表
    public void init_third(){
        List<Ten_Items> item3=LitePal.findAll(Ten_Items.class);
        ThirdItemAdapter thirdItemAdapter=new ThirdItemAdapter(context,R.layout.third_item_layout,item3);
        third_list.setAdapter(thirdItemAdapter);
        third_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentdfadsf=new Intent();
                intentdfadsf.putExtra("clicknumber",position+"");
                intentdfadsf.setClass(context, ExerciseActivity.class);
                context.startActivity(intentdfadsf);
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

