package com.example.bookhub2.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bookhub2.R

class AboutUsFragment : Fragment() {
    lateinit var whatsapp:TextView
    lateinit var gmail:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_about_us, container, false)
        whatsapp=view.findViewById(R.id.whatsapp)
        gmail=view.findViewById(R.id.gmail)
        whatsapp.movementMethod=LinkMovementMethod.getInstance()
        whatsapp.setLinkTextColor(Color.BLACK)
        gmail.movementMethod=LinkMovementMethod.getInstance()
        gmail.setLinkTextColor(Color.BLACK)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}