package ge.bondx.calories

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ge.bondx.calories.database.MyDBHandler

import ge.bondx.calories.objects.Product


class NotificationsFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var list: MutableList<Product>? = null

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
        val dbHandler = MyDBHandler(context)
        list = dbHandler.getProducts() as MutableList<Product>?

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
