package me.yokeyword.indexablerv;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoKey on 16/10/16.
 */

abstract class AbstractHeaderFooterAdapter<T> {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    ArrayList<EntityWrapper<T>> mEntityWrapperList = new ArrayList<>();
    protected OnItemClickListener<T> mListener;
    protected OnItemLongClickListener<T> mLongListener;

    /**
     * 不想显示哪个就传null
     *
     * @param index      IndexBar的字母索引
     * @param indexTitle IndexTitle
     * @param datas      数据源
     */
    public AbstractHeaderFooterAdapter(String index, String indexTitle, List<T> datas) {
        if (indexTitle != null) {
            EntityWrapper<T> wrapper = wrapEntity(index, indexTitle);
            wrapper.setItemType(EntityWrapper.TYPE_TITLE);
        }
        if (datas == null) {
            EntityWrapper<T> wrapper = wrapEntity(index, indexTitle);
            wrapper.setItemType(getItemViewType());
        } else {
            for (int i = 0; i < datas.size(); i++) {
                EntityWrapper<T> wrapper = wrapEntity(index, indexTitle);
                wrapper.setData(datas.get(i));
            }
        }
    }

    private EntityWrapper<T> wrapEntity(String index, String indexTitle) {
        EntityWrapper<T> wrapper = new EntityWrapper<>();
        wrapper.setIndex(index);
        wrapper.setIndexTitle(indexTitle);
        mEntityWrapperList.add(wrapper);
        return wrapper;
    }

    public abstract int getItemViewType();

    public abstract RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent);

    public abstract void onBindContentViewHolder(RecyclerView.ViewHolder holder, T entity);

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    OnItemClickListener<T> getOnItemClickListener() {
        return mListener;
    }


    OnItemLongClickListener getOnItemLongClickListener() {
        return mLongListener;
    }

    ArrayList<EntityWrapper<T>> getDatas() {
        for (EntityWrapper<T> wrapper : mEntityWrapperList) {
            if (wrapper.getItemType() == EntityWrapper.TYPE_CONTENT) {
                wrapper.setItemType(getItemViewType());
            }
        }
        return mEntityWrapperList;
    }

    void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    interface OnItemClickListener<T> {
        void onItemClick(View v, int currentPosition, T entity);
    }

    interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View v, int currentPosition, T entity);
    }
}
