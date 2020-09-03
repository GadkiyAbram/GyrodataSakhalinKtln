package com.example.gyrodatasakhalin.fragments.tool

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.tool.ToolItem
import com.example.gyrodatasakhalin.tool.ToolService
import kotlinx.android.synthetic.main.activity_add_tool.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


private lateinit var toolService: ToolService
private lateinit var errorsItemArray: MutableMap<String, String>

class AddToolFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_tool, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolService = RetrofitInstance.getRetrofitInstance()
            .create(ToolService::class.java)

        fabAddItemImage.setOnClickListener {
            val imageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(imageIntent, 101)
        }

        getItemsComponents()

        addItemBtn.setOnClickListener {
            addItem()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 101 && resultCode == RESULT_OK){
            val takenItemImage = data?.extras?.get("data") as Bitmap
            imageItemPhoto.setImageBitmap(takenItemImage)
        }
    }

    private fun prepareImage(itemImage: Bitmap?): String {
        var preparedImage = ""
        if (itemImage != null) {
            val baos = ByteArrayOutputStream()
            itemImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes: ByteArray = baos.toByteArray()
//            preparedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            preparedImage = Base64.getEncoder().encodeToString(imageBytes)
        }
        return preparedImage
    }

    private fun addItem(){

        val gwdItem = spinnerToolGWD.selectedItem.toString()
        val gwdAsset = edItemAsset.text.toString()
        val gwdArrived = edAItemArrived.text.toString()
        val gwdInvoice = edItemInvoice.text.toString()
        val gwdCCD = edItemCCD.text.toString()
        val gwdCCDRus = edItemNameRussian.text.toString()
        val gwdCCDPosition = edItemPosition.text.toString()
        val gwdLocation = edItemStatus.text.toString()
        val gwdBox = edItemBox.text.toString()
        val gwdContainer = edItemContainer.text.toString()
        val gwdComments = edItemComments.text.toString()
        var itemImage = ""
        val gwdId = 0
        val gwdCreated = ""
        val gwdUpdated = ""
        val gwdExpandable = false

        if (imageItemPhoto.getDrawable() != null) {
            itemImage = prepareImage((imageItemPhoto.getDrawable() as BitmapDrawable).bitmap)
        }

        val gwdItemToInsert = ToolItem(
            gwdArrived,
            gwdAsset,
            gwdBox,
            gwdCCD,
            0,
            gwdComments,
            gwdContainer,
            gwdCreated,
            gwdId,
            gwdInvoice,
            gwdItem,
            itemImage,
            gwdLocation,
            gwdCCDRus,
            gwdCCDPosition,
            gwdUpdated
        )

        val responseLiveData : LiveData<Response<Int>> = liveData {
            val response = toolService.addNewItem(gwdItemToInsert)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val result = it.body()
            if (result != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                Toast.makeText(context!!.applicationContext, "${gwdItem} ${gwdAsset} added", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getItemsComponents(){

        pbWaiting.bringToFront()
        pbWaiting.visibility = View.VISIBLE

        val responseLiveData : LiveData<Response<List<String>>> = liveData {
            val response = toolService.getItemsComponents()
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val componentsList = it.body()?.listIterator()
            var componentsFinal = ArrayList<String>()
            if (componentsList != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (componentsList.hasNext()){
                    val item = componentsList.next()
                    componentsFinal.add(item)
                }
                spinnerToolGWD.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_clients, componentsFinal)
                Log.i("TOOL", "items: ${componentsFinal.toString()}")
            }
        })

    }
}