package SomeTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luckzhang.R;

import java.util.List;

import Data_Class.Report_item;
import Data_Class.Ten_Items;

public class ThirdItemAdapter extends ArrayAdapter<Ten_Items> {
    private int resourceId;
    private Context context;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public ThirdItemAdapter(Context context, int textViewResourceId, List<Ten_Items> objects) {
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
        Ten_Items ten_items=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView textView26=view.findViewById(R.id.textView26);
        TextView textView28=view.findViewById(R.id.textView28);
        textView26.setText(ten_items.getTitle());
        textView28.setText(ten_items.getData());
        return view;
    }
}
