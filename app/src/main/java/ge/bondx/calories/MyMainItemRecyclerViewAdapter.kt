package ge.bondx.calories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

import ge.bondx.calories.objects.Product




class MyMainItemRecyclerViewAdapter(private val mValues: List<Product>,
                                    private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Product)
    }

    private var prevCategory: String? = null
    private var checkedItems: ArrayList<String?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view:View? = null;
        checkedItems = arrayListOf()
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
            item.bind(mValues[position], mListener!!)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolderItem(val mView: View) : RecyclerView.ViewHolder(mView) {

        var txtContent: TextView
        var txtCalory: TextView
        var mCheckBox: CheckBox

        init {
            txtContent = mView.findViewById<View>(R.id.txtContent) as TextView
            txtCalory = mView.findViewById<View>(R.id.txtCalory) as TextView
            mCheckBox = mView.findViewById<View>(R.id.checkBox) as CheckBox


        }

        fun bind(item: Product, listener: OnListFragmentInteractionListener) {
            txtCalory!!.text = "კალორია - " + item.calory.toString()
            txtContent!!.text = item.name

            mCheckBox.tag = item.key
            mCheckBox.isChecked = item.isChecked

            mView.setOnClickListener {
                item!!.isChecked = !mCheckBox!!.isChecked
                mCheckBox.isChecked = item!!.isChecked
                listener.onListFragmentInteraction(item)
            }

        }

        override fun toString(): String {
            return super.toString() + " '" + txtContent.text + "' "
        }
    }

    class ViewHolderHeader(val mView: View) : RecyclerView.ViewHolder(mView){
        var mSeparator: TextView

        init {
             mSeparator = mView.findViewById<View>(R.id.separator) as TextView
        }
    }
}

