package com.bebetterprogrammer.trecox.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.models.Company

class CompanyDetailsAdapter : RecyclerView.Adapter<CompanyDetailsViewHolder>() {

    var data = listOf<Company>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyDetailsViewHolder {
        return CompanyDetailsViewHolder.create(
            parent
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CompanyDetailsViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }

}
