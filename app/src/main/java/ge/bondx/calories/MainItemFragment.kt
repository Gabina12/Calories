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
import com.google.firebase.database.*
import ge.bondx.calories.Objects.Product

class MainItemFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private var list: MutableList<Product>? = mutableListOf<Product>()
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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        val ref = database.getReference("Products")
        ref.keepSynced(true)
        ref.orderByKey().addListenerForSingleValueEvent(itemListener)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
            adapter = MyMainItemRecyclerViewAdapter(this!!.list!!, mListener)
            view.adapter = adapter
        }
        return view
    }

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {

        for (item : DataSnapshot in dataSnapshot.children) {
            var prod = Product.create()
            prod.name = item.child("Name").value as String?
            prod.calory = item.child("Calory").value as Number?
            prod.category = item.child("Category").value as String?
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

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Product)
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







