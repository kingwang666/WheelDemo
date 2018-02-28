/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wang.wheel.adapters;

import android.content.Context;

import java.util.List;

import com.wang.wheel.model.DataModel;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter<T> {
    
    // items
    private List<T> items;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ArrayWheelAdapter(Context context, List<T> items) {
        super(context);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            T item = items.get(index);
            return getItemText(item);
        }
        return null;
    }

    @Override
    public CharSequence getItemText(T item) {
        if (item == null){
            return "";
        }
        if (item instanceof CharSequence) {
            return (CharSequence) item;
        }
        if (item instanceof DataModel){
            return ((DataModel) item).Name;
        }
        return item.toString();
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public T getItem(int index) {
        return items.get(index);
    }

    @Override
    public int indexOf(T o) {
        return items.indexOf(o);
    }
}
