package My_ViewPager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.luckzhang.R;

import java.util.ArrayList;
import java.util.List;

public class DemoPageAdapter extends PagerAdapter {
    private List<View> mViewList = new ArrayList<>();

    public DemoPageAdapter(Context context) {
        View view1=View.inflate(context,R.layout.main_left,null);
        View view2=View.inflate(context,R.layout.main_right,null);
        mViewList.add(view1);
        mViewList.add(view2);

        /*for (int index = 0; index < 2; index++) {
            TextView view = (TextView) View.inflate(context, R.layout.view_pager_text, null);
            view.setText("第" + index + "页");
            mViewList.add(view);
        }*/
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

