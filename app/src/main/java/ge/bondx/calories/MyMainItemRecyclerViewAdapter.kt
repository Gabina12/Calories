package ge.bondx.calories

import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import ge.bondx.calories.MainItemFragment.OnListFragmentInteractionListener
import ge.bondx.calories.Objects.Product


class MyMainItemRecyclerViewAdapter(private val mValues: List<Product>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<MyMainItemRecyclerViewAdapter.ViewHolder>() {

    private var prevCategory: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_mainitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        if(prevCategory != mValues[position].category){
            holder.mSepHeader.visibility = View.VISIBLE
            holder.mSeparator.text = mValues[position].category
        }else{
            holder.mSepHeader.visibility = View.GONE
        }
        prevCategory = holder.mItem!!.category



        holder.txtCalory.text = mValues[position].calory.toString()
        holder.txtContent.text = mValues[position].name

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)

        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtContent: TextView
        val txtCalory: TextView
        val mSeparator: TextView
        val mSepHeader: LinearLayout
        var mItem: Product? = null

        init {
            txtContent = mView.findViewById<View>(R.id.txtContent) as TextView
            txtCalory = mView.findViewById<View>(R.id.txtCalory) as TextView
            mSeparator = mView.findViewById<View>(R.id.separator) as TextView
            mSepHeader = mView.findViewById<View>(R.id.separatorHeader) as LinearLayout
        }

        override fun toString(): String {
            return super.toString() + " '" + txtContent.text + "' [" + mItem!!.calory.toString() + "]"
        }
    }
}
