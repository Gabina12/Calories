package ge.bondx.calories

import android.annotation.SuppressLint
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
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import ge.bondx.calories.database.MyDBHandler

import ge.bondx.calories.objects.Product


class NotificationsFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var list: MutableList<Product> = mutableListOf()
    private lateinit var listView: RecyclerView
    private lateinit var adapter : MyMainItemRecyclerViewAdapter
    private lateinit var itemTotal: TextView
    private lateinit var bottomNavigation: AHBottomNavigation
    private var cnt: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_notifications, container, false)
        initUi(view)
        return view
    }


    private fun initUi(view: View){

        listView = view.findViewById<View>(R.id.notificationList) as RecyclerView
        itemTotal = view.findViewById<View>(R.id.txtTotal) as TextView

        bottomNavigation = activity.findViewById<View>(R.id.navigation) as AHBottomNavigation

        val dbHandler = MyDBHandler(context)
        list = dbHandler.getProducts() as MutableList<Product>
        cnt = list.count { !it.isHeader && it.isChecked }

        adapter = MyMainItemRecyclerViewAdapter(list ,object : MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: Product) {
                if(!item.isChecked) {
                    dbHandler.deleteProduct(item.key!!)
                    list.remove(item)
                    cnt--

                    if(!list.any { it.category == item.category && !it.isHeader }){
                        val header = list.firstOrNull{it.category == item.category}
                        if(header != null)
                            list.remove(header)
                    }

                    var total:Double = list.sumByDouble { it.calory!!.toDouble() }
                    itemTotal.text = total.toString()

                    adapter.notifyDataSetChanged()
                }
                bottomNavigation.setNotification("+" + cnt.toString(),1)
                if(cnt == 0)
                    bottomNavigation.setNotification("",1)
            }
        })

        var total:Double = list.sumByDouble { it.calory!!.toDouble() }
        itemTotal.text = total.toString()

        listView.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
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


