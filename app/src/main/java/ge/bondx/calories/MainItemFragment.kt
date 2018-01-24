package ge.bondx.calories

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import ge.bondx.calories.MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener
import ge.bondx.calories.objects.Product
import ge.bondx.calories.database.DatabaseUtil
import ge.bondx.calories.database.MyDBHandler


class MainItemFragment : Fragment() {

    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private var list: MutableList<Product>? = mutableListOf()
    private var adapter: MyMainItemRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_mainitem_list, container, false)

        val ref = DatabaseUtil().database.getReference("Products")
        ref.keepSynced(true)
        ref.orderByKey().addListenerForSingleValueEvent(itemListener)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)

            adapter = MyMainItemRecyclerViewAdapter(list!!, object : MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener {
                override fun onListFragmentInteraction(item: Product) {
                    val dbHandler = MyDBHandler(context)
                    if(item.isChecked) {
                        dbHandler.addProduct(item)
                    }
                    else {
                        dbHandler.deleteProduct(item.key!!)
                    }
                }
            })
            view.adapter = adapter
        }
        return view
    }



    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {

        var prevCategory: String? = ""
        val dbHandler = MyDBHandler(context)
        var selected: List<Product> = dbHandler.getProducts()!!

        for (item : DataSnapshot in dataSnapshot.children.sortedWith(compareBy({it.child("Category ").value.toString()})).toList()) {
            var prod = Product.create()

            if(prevCategory!!.trim() != (item.child("Category ").value as String?)!!.trim()){
                prevCategory = (item.child("Category ").value as String?)!!.trim()
                var pheader = Product.create()
                pheader.name = prevCategory
                pheader.isHeader = true
                list!!.add(pheader)
            }

            prod.name = item.child("Name").value as String?
            prod.calory = item.child("Calorie").value as Number?
            prod.category = item.child("Category ").value as String?
            prod.key = item.key

            if(selected.any{it.key == prod.key}){
                prod.isChecked = true
            }

            list!!.add(prod)
        }

        adapter!!.notifyDataSetChanged()

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {

        private val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): MainItemFragment {
            val fragment = MainItemFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}





