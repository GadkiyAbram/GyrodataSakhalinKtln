package com.example.gyrodatasakhalin.tool

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.decode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.decode
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gyrodatasakhalin.R
import com.github.chrisbanes.photoview.PhotoView
import java.lang.Byte.decode
import java.net.URLDecoder.decode
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*


class ToolAdapter(private val toolList: List<ToolItem>, context: Context) : RecyclerView.Adapter<ToolAdapter.ToolViewHolder>() {

    private val context: Context = context

    class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Layouts
        val mainToolLayout: LinearLayout = itemView.findViewById(R.id.mainToolLayout)
        val expandableToolLayout: LinearLayout = itemView.findViewById(R.id.expandableToolLayout)
        // Preview Tool Data
        val toolItem: TextView = itemView.findViewById(R.id.tvToolItem)
        val toolAsset: TextView = itemView.findViewById(R.id.tvToolAsset)
        val toolCircHrs: TextView = itemView.findViewById(R.id.tvToolCircHrs)
        val toolArrived: TextView = itemView.findViewById(R.id.tvToolArrived)
        val toolInvoice: TextView = itemView.findViewById(R.id.tvToolInvoice)
        // Precise Tool Data
        val toolImage: ImageView = itemView.findViewById(R.id.imageItemPhoto)
        val toolImagePreview: ImageView = itemView.findViewById(R.id.itemImagePreview)
    }

    private fun getContext() : Context {
        return context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val toolView = LayoutInflater.from(context).inflate(
            R.layout.tool_item,
            parent, false)

        return ToolAdapter.ToolViewHolder(toolView)
    }

    override fun getItemCount(): Int {
        return toolList.size
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val currentTool = toolList[position]
        // Preview Battery Data
        holder.toolItem.text = currentTool.item
        holder.toolAsset.text = currentTool.asset
        holder.toolCircHrs.text = currentTool.circulation.toString()
        holder.toolArrived.text = currentTool.arrived
        holder.toolInvoice.text = currentTool.invoice

        // Precise Tool Data
        if (currentTool.itemImage != ""){
            var decodedImage: Bitmap = prepareImage(currentTool.itemImage)
            holder.toolImage.setImageBitmap(decodedImage)
            holder.toolImagePreview.setImageBitmap(decodedImage)
        }

        val isExpandable: Boolean = toolList[position].expandable
        holder.expandableToolLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.mainToolLayout.setOnClickListener {
            var currentTool = toolList[position]
            currentTool.expandable = !currentTool.expandable
            notifyItemChanged(position)
        }
    }

    private fun prepareImage(imageRecievedInString: String): Bitmap {

        var decodedImageInBytes: Bitmap? = null
//        val imageInBytes = Base64.getDecoder().decode(imageRecievedInString)
        val imageInBytes = android.util.Base64.decode(imageRecievedInString, android.util.Base64.DEFAULT)
        decodedImageInBytes = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.size)

        return decodedImageInBytes
    }
}