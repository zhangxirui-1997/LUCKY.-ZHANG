package SomeTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luckzhang.R;

import java.util.List;

import Data_Class.Report_item;

public class MyItemAdapter extends ArrayAdapter<Report_item> {
    private int resourceId;
    private Context context;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public MyItemAdapter(Context context, int textViewResourceId, List<Report_item> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        this.context=context;
        resourceId = textViewResourceId;
    }

    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Report_item report_item = getItem(position);  //获取当前项的Fruit实例
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView textView_title=view.findViewById(R.id.textView3);
        TextView textView_name=view.findViewById(R.id.textView5);
        TextView textView_data=view.findViewById(R.id.textView7);
        TextView textView_statue=view.findViewById(R.id.textViewtete);
        textView_title.setText(report_item.getItem_title());
        textView_name.setText(report_item.getItem_name());
        textView_data.setText(report_item.getItem_time());
        if(report_item.getStatue_now().equals("未收到")){
            textView_statue.setTextColor(context.getResources().getColor(R.color.color_errorRed,null));
        }
        textView_statue.setText(report_item.getStatue_now());
        return view;
    }
}
