package com.tamic.map.mapdemo.map.thirdmap;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinganfang.haofangtuo.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ex-zhangpanpan002
 */
public class HftBottomSheetUtil {

    public static BottomSheetDialog createTextBottomSheetDialog(Context context, HashMap<String, View.OnClickListener> hashMap) {

        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.Dialog_NoTitle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_text, null);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.ll_bottom_sheet);

        // 地图按钮
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, View.OnClickListener> entry = (Map.Entry) iterator.next();
            String key = entry.getKey();
            final View.OnClickListener listener = entry.getValue();
            if (!TextUtils.isEmpty(key)) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_item_text, null);
                TextView tvItem = (TextView) itemView.findViewById(R.id.tv_bottom_sheet_item);

                tvItem.setText(key);
                tvItem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });
                llContent.addView(itemView);
            }
        }

        // 取消按钮
        View itemCancelView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_item_text, null);
        TextView itemCancel = (TextView) itemCancelView.findViewById(R.id.tv_bottom_sheet_item);
        itemCancel.setText("取消");
        itemCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llContent.addView(itemCancelView);

        dialog.setContentView(view);
        return dialog;
    }
}
