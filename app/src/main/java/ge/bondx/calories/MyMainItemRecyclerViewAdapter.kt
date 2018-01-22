package ge.bondx.calories

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

import ge.bondx.calories.MainItemFragment.OnListFragmentInteractionListener
import ge.bondx.calories.Objects.Product


class MyMainItemRecyclerViewAdapter(private val mValues: List<Product>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var prevCategory: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view:View? = null;
        return if (viewType == 1){
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_section_header, parent, false)
            ViewHolderHeader(view)
        }else{
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
        }else{
            val item: ViewHolderItem = holder as ViewHolderItem
            item.mItem = mValues[position]

            item.txtCalory!!.text = mValues[position].calory.toString()
            item.txtContent!!.text = mValues[position].name

            item.mView.setOnClickListener {
                mListener?.onListFragmentInteraction(item.mItem!!)
                item.mCheckBox!!.isChecked = !item.mCheckBox!!.isChecked
            }
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolderItem(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        override fun onClick(p0: View?) {
            mCheckBox!!.isChecked = !mCheckBox!!.isChecked
        }

        var txtContent: TextView
        var txtCalory: TextView
        var mItem: Product? = null
        var mCheckBox: CheckBox

        init {
            txtContent = mView.findViewById<View>(R.id.txtContent) as TextView
            txtCalory = mView.findViewById<View>(R.id.txtCalory) as TextView
            mCheckBox = mView.findViewById<View>(R.id.checkBox) as CheckBox
            txtContent!!.setOnClickListener(this)
            txtCalory!!.setOnClickListener (this)
        }

        override fun toString(): String {
            return super.toString() + " '" + txtContent.text + "' [" + mItem!!.calory.toString() + "]"
        }
    }

    class ViewHolderHeader(val mView: View) : RecyclerView.ViewHolder(mView){
        var mSeparator: TextView

        init {
             mSeparator = mView.findViewById<View>(R.id.separator) as TextView
        }
    }
}

