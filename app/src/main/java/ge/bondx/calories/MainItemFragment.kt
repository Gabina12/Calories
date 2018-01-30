package ge.bondx.calories

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.google.firebase.database.*
import com.miguelcatalan.materialsearchview.MaterialSearchView
import ge.bondx.calories.MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener
import ge.bondx.calories.objects.Product
import ge.bondx.calories.database.DatabaseUtil
import ge.bondx.calories.database.MyDBHandler
import android.widget.LinearLayout
import android.widget.TextView
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.text.InputType






class MainItemFragment : Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private var list: MutableList<Product> = mutableListOf()
    private var full_data: MutableList<Product> = mutableListOf()
    private var adapter: MyMainItemRecyclerViewAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: MaterialSearchView
    private lateinit var bottomNavigation: AHBottomNavigation
    private var cnt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_mainitem_list, container, false)

        bottomNavigation = activity.findViewById<View>(R.id.navigation) as AHBottomNavigation

        bottomNavigation.setNotificationBackgroundColor(resources.getColor(R.color.colorBadge))

        searchView = activity.findViewById<View>(R.id.search_view) as MaterialSearchView
        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                adapter!!.animateTo(full_data)
                recyclerView.scrollToPosition(0)
            }
        })

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerView.scrollToPosition(0)
                val data = filter(newText!!)
                adapter!!.animateTo(data)
                return true
            }

        })

        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.keepSynced(true)
        ref.orderByKey().addListenerForSingleValueEvent(itemListener)

        val context = view.context
        recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MyMainItemRecyclerViewAdapter(list!!, object : MyMainItemRecyclerViewAdapter.OnListFragmentInteractionListener {
            override fun onListFragmentInteraction(item: Product) {
                val dbHandler = MyDBHandler(context)
                if(item.isChecked) {

                    val alertDialogBuilder = AlertDialog.Builder(
                            context)

                    alertDialogBuilder.setTitle(resources.getText(R.string.dialog_title))
                    alertDialogBuilder.setMessage(resources.getText(R.string.dialog_message))

                    val input = EditText(context)
                    input.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)
                    input.setText("100")
                    input.requestFocus()
                    input.selectAll()

                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)

                    val lp = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT)
                    input.layoutParams = lp
                    alertDialogBuilder.setView(input)

                    alertDialogBuilder.setCancelable(true)
                            .setPositiveButton(resources.getText(R.string.Ok), {
                                    _, _ ->  item.Count = input.text.toString().toDouble()
                                cnt++
                                setNotification(cnt)
                                dbHandler.addProduct(item)
                            }
                            ).setNegativeButton(resources.getText(R.string.Cancel),{
                                _, _ -> item.isChecked = false
                                setNotification(cnt)
                                adapter!!.notifyDataSetChanged()
                            })

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
                else {
                    cnt--
                    setNotification(cnt)
                    dbHandler.deleteProduct(item.key!!)
                }
            }
        })

        recyclerView.adapter = adapter

        return view
    }

    fun setNotification(cnt: Int){
        bottomNavigation.setNotification("+" + cnt.toString(),1)
        if(cnt == 0)
            bottomNavigation.setNotification("",1)
    }

    private fun filter(text: String) : MutableList<Product> {
        val filteredProduct = ArrayList<Product>()

        list = full_data

        if(text.isNullOrBlank()) return list

        list.filterTo(filteredProduct) {
            (it.name!!.toLowerCase().contains(text.toLowerCase()) || it.category!!.toLowerCase().contains(text.toLowerCase())
                    || it.calory!!.toString().contains(text))
        }
        return  filteredProduct
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

        for (item: DataSnapshot in dataSnapshot.children.sortedWith(compareBy({ it.child("Category").value.toString() })).toList()) {

            try {
                var prod = Product.create()

                if (prevCategory!!.trim() != (item.child("Category").value as String?)!!.trim()) {
                    prevCategory = (item.child("Category").value as String?)!!.trim()
                    var pheader = Product.create()
                    pheader.name = prevCategory
                    pheader.category = prevCategory
                    pheader.calory = -1
                    pheader.isHeader = true
                    list.add(pheader)
                    full_data.add(pheader)
                }

                prod.name = item.child("Name").value as String?
                prod.calory = item.child("Calorie").value as Number?
                prod.category = item.child("Category").value as String?
                prod.key = item.key

                if (selected.any { it.key == prod.key }) {
                    prod.isChecked = true
                }

                list!!.add(prod)
                full_data.add(prod)
                cnt = full_data.count { it.isChecked && !it.isHeader }
            } catch (ex: Throwable) {
                Log.wtf("ERROR", ex)
            }
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

        fun newInstance(): MainItemFragment {
            val fragment = MainItemFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}





