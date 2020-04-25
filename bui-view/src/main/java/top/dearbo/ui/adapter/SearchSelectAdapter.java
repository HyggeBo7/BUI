package top.dearbo.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import top.dearbo.ui.R;
import top.dearbo.ui.adapter.impl.SpinnerTextFormatter;
import top.dearbo.ui.adapter.list.base.BaseListAdapter;

import java.util.List;


public class SearchSelectAdapter<T> extends BaseListAdapter<T, SearchSelectAdapter.ViewHolder> {
    private SpinnerTextFormatter spinnerTextFormatter;

    public SearchSelectAdapter(Context ctx) {
        this(ctx, null, new SimpleSpinnerTextFormatter());
    }

    public SearchSelectAdapter(Context ctx, List<T> mList) {
        this(ctx, mList, new SimpleSpinnerTextFormatter());
    }

    public SearchSelectAdapter(Context ctx, List<T> mList, SpinnerTextFormatter spinnerTextFormatter) {
        super(ctx, mList);
        this.spinnerTextFormatter = spinnerTextFormatter;
    }

    @Override
    protected ViewHolder newViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_list_select_single;
    }

    @Override
    protected void convert(ViewHolder holder, T item, int position) {
        holder.info.setText(spinnerTextFormatter.format(item).toString());
    }

    static class ViewHolder {
        TextView info;

        public ViewHolder(View view) {
            info = view.findViewById(R.id.tv_select_info);
        }
    }

}
