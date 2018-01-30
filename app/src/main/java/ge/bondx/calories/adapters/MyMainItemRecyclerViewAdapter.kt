package ge.bondx.calories.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import ge.bondx.calories.R

import ge.bondx.calories.objects.Product

class MyMainItemRecyclerViewAdapter(private var mValues: MutableList<Product>,
                                    private val mListener: OnListFragmentInteractionListener?,
                                    var ctx: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?
        return if (viewType == 1){
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_section_header, parent, false)
            ViewHolderHeader(view)
        } else{
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_mainitem, parent, false)

            ViewHolderItem(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(mValues[position].isHeader) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == 1){
            val header : ViewHolderHeader = holder as ViewHolderHeader
            header.mSeparator.text = mValues[position].name
        } else{
            val item: ViewHolderItem = holder as ViewHolderItem
            item.bind(mValues[position], ctx, mListener!!)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    fun animateTo(models: List<Product>) {
        applyAndAnimateRemovals(models)
        applyAndAnimateAdditions(models)
        applyAndAnimateMovedItems(models)

    }

    private fun applyAndAnimateRemovals(newModels: List<Product>) {

        for (i in mValues.size - 1 downTo 0) {
            val model = mValues[i]
            if (!newModels.contains(model)) {
                removeItem(i)
            }
        }
    }

    private fun applyAndAnimateAdditions(newModels: List<Product>) {

        var i = 0
        val count = newModels.size
        while (i < count) {
            val model = newModels[i]
            if (!mValues.contains(model)) {
                addItem(i, model)
            }
            i++
        }
    }

    private fun applyAndAnimateMovedItems(newModels: List<Product>) {

        for (toPosition in newModels.indices.reversed()) {
            val model = newModels[toPosition]
            val fromPosition = mValues.indexOf(model)
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition)
            }
        }
    }

    private fun removeItem(position: Int): Product {
        val item = mValues[position]
        mValues.remove(item)
        notifyItemRemoved(position)
        return item
    }

    private fun addItem(position: Int, model: Product) {
        mValues.add(position, model)
        notifyItemInserted(position)
    }

    private fun moveItem(fromPosition: Int, toPosition: Int) {
        val item = mValues[fromPosition]
        mValues.remove(item)
        mValues.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolderItem(val mView: View) : RecyclerView.ViewHolder(mView) {
        var txtContent: TextView = mView.findViewById<View>(R.id.txtContent) as TextView
        var txtCalory: TextView = mView.findViewById<View>(R.id.txtCalory) as TextView
        var mCheckBox: CheckBox = mView.findViewById<View>(R.id.checkBox) as CheckBox

        fun bind(item: Product, ctx: Context, listener: OnListFragmentInteractionListener) {
            txtCalory.text = "${ctx.resources.getText(R.string.calorie_text)} ${item.calory.toString()} " + if(item.Count!! > 100) " / " +
                    "${ctx.resources.getText(R.string.sum)}${(item.Count!! * item.calory!!.toDouble())/ 100} ${ctx.resources.getText(R.string.calorie_unit)}" else " "
            txtContent.text = item.name

            mCheckBox.tag = item.key
            mCheckBox.isChecked = item.isChecked

            mView.setOnClickListener {
                item.isChecked = !mCheckBox.isChecked
                mCheckBox.isChecked = item.isChecked
                listener.onListFragmentInteraction(item)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + txtContent.text + "' "
        }
    }

    class ViewHolderHeader(val mView: View) : RecyclerView.ViewHolder(mView){
        var mSeparator: TextView = mView.findViewById<View>(R.id.separator) as TextView

    }
}

