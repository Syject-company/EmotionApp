package com.syject.eqally.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

abstract class AppBaseAdapter<T>(
    protected val context: AppCompatActivity,
    val onItemClick: (T) -> Unit
) :
    RecyclerView.Adapter<AppBaseAdapter.BaseItem<T>>() {

    private var items: MutableList<T?> = mutableListOf()
    private var selectedItem: BaseItem<T>? = null
    private var selectedItemPosition = -1

    constructor(context: AppCompatActivity, list: List<T?>, onItemClick: (T) -> Unit) :
            this(context, onItemClick) {
        addItems(list)
    }

    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): BaseItem<T> {
        return createHolder(
            LayoutInflater.from(context).inflate(getLayoutId(), viewGroup, false)
        ).listen { pos, _, holder: BaseItem<T> ->
            val item = items[pos]
            if (item != null && isCallClick(item, pos)) {
                onItemClick(item)
                onItemSelected(item, pos, holder)
            }
        }
    }

    open fun onItemSelected(item: T, pos: Int, holder: BaseItem<T>) {
        holder.onItemSelected()
        selectedItem?.onItemUnSelected()
        selectedItem = holder
        selectedItemPosition = holder.adapterPosition
    }

    open fun isCallClick(item: T?, position: Int) = position != selectedItemPosition

    override fun getItemCount() = items.size

    abstract fun getLayoutId(): Int

    override fun onBindViewHolder(holder: BaseItem<T>, position: Int) {
        items[position]?.let { holder.bind(it) }

    }

    protected fun addItems(items: List<T?>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    abstract fun createHolder(inflate: View): BaseItem<T>

    private fun <R : BaseItem<T>> R.listen(event: (position: Int, type: Int, R: BaseItem<T>) -> Unit): R {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType, this)
        }
        return this
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    abstract class BaseItem<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            setIsRecyclable(false)
        }

        abstract fun bind(t: T)

        open fun onItemSelected() {

        }

        open fun onItemUnSelected() {

        }
    }
}
