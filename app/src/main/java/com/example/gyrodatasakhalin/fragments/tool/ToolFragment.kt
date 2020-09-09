package com.example.gyrodatasakhalin.fragments.tool

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.TITEMSASSETS
import com.example.gyrodatasakhalin.tool.Tool
import com.example.gyrodatasakhalin.tool.ToolAdapter
import com.example.gyrodatasakhalin.tool.ToolItem
import com.example.gyrodatasakhalin.tool.ToolService
import kotlinx.android.synthetic.main.activity_tool.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var toolService: ToolService
private lateinit var progressBar: ProgressBar

class ToolFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val toolView: View = inflater.inflate(R.layout.fragment_tool, container, false)
        progressBar = toolView.findViewById(R.id.pbWaiting)

        return toolView
//        return inflater.inflate(R.layout.fragment_tool, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolService = RetrofitInstance.getRetrofitInstance()
            .create(ToolService::class.java)

        getTools(API_KEY, "", "")

        searchToolByKey(edSearchTool)
    }

    private fun searchToolByKey(search: EditText){
        search.addTextChangedListener(object : TextWatcher {

            var where: String = ""

            override fun afterTextChanged(searchQuery: Editable?) {

                if (radioToolItemButton.isChecked){where = "Item"}
                if (radioToolAssetButton.isChecked){where = "Asset"}
                if (radioToolCCDButton.isChecked){where = "CCD"}
                if (radioToolInvoiceButton.isChecked){where = "Invoice"}

                Handler().postDelayed({
                    search(searchQuery, where)
                }, 1500)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            private fun search(searchWhat: Editable?, searchWhere: String){
                var searchWhat : String = searchWhat.toString()
                getTools(API_KEY, searchWhat, searchWhere)
            }

        })
    }

    private fun getTools(token: String, what: String, where: String){

//        if (pbWaiting != null){
//            pbWaiting.bringToFront()
//            pbWaiting.visibility = View.VISIBLE
//        }

        if (progressBar != null){
            progressBar.bringToFront()
            progressBar.visibility = View.VISIBLE
        }

        val responseLiveData : LiveData<Response<Tool>> = liveData {
            val response = toolService.getCustomItems(what, where)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val toolList = it.body()?.listIterator()
            var tools = ArrayList<ToolItem>()
            TITEMSASSETS.clear()
            if (toolList != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (toolList.hasNext()){
                    val toolItem = toolList.next()
                    tools.add(toolItem)
                    TITEMSASSETS.put(toolItem.asset, toolItem.item)
                }
                toolRecyclerView.adapter = ToolAdapter(tools, context!!.applicationContext)
                (toolRecyclerView.adapter as ToolAdapter).notifyDataSetChanged()
                toolRecyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
                toolRecyclerView.setHasFixedSize(true)
            }
        })
    }
}