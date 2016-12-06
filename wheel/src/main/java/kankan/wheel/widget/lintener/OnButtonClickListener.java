package kankan.wheel.widget.lintener;

import kankan.wheel.widget.model.DataModel;

/**
 * Created by wang
 * on 2016/12/6
 */

public interface OnButtonClickListener {
    void onClick(DataModel province, DataModel city, DataModel area);

    void onCancel();
}
