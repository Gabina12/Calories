package ge.bondx.calories

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import ge.bondx.calories.database.MyDBHandler

import ge.bondx.calories.objects.Product


class NotificationsFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var list: List<Product>? = null
    private lateinit var listView: RecyclerView
    private lateinit var adapter : MyMainItemRecyclerViewAdapter
    private lateinit var itemTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //mParam1 = arguments.getString(ARG_PARAM1)
            //mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_notifications, container, false)
        listView = view.findViewById<View>(R.id.notificationList) as RecyclerView
        itemTotal = view.findViewById<View>(R.id.txtTotal) as TextView

        val dbHandler = MyDBHandler(context)
        list = dbHandler.getProducts()

        adapter = MyMainItemRecyclerViewAdapter(list!! ,object : MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: Product) {

            }
        })

        var total:Double = list!!.sumByDouble { it.calory!!.toDouble() }
        itemTotal.text = total.toString()

        listView.adapter = adapter

        adapter.notifyDataSetChanged()

        Toast.makeText(this.context,list!!.size.toString(),Toast.LENGTH_SHORT).show()

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    fun addList(data: List<Product>?){
        //list = data
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        fun newInstance(): NotificationsFragment {
            val fragment = NotificationsFragment()
            val args = Bundle()
            //args.putString(ARG_PARAM1, param1)
            //args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}


