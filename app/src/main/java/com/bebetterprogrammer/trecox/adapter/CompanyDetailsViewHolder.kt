package com.bebetterprogrammer.trecox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.Company

class CompanyDetailsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val name = view.findViewById<TextView>(R.id.tv_name)
    private var transaction: Company? = null

    fun bind(company: Company, position: Int) {
        name.text = company.cname

    }

    companion object {
        fun create(parent: ViewGroup): CompanyDetailsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.company_details_item, parent, false)
            return CompanyDetailsViewHolder(
                view
            )
        }
    }
}
