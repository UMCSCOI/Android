package com.stable.scoi.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R


class MyPageFragment : Fragment(R.layout.fragment_mypage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_menu_list)

        val menuData = listOf(
            MenuData("계정 정보", R.drawable.ic_user02),
            MenuData("API 연동 설정", R.drawable.database),
            MenuData("간편 비밀번호 변경", R.drawable.credit_card)
        )

        val myAdapter = MyPageAdapter(menuData) { menu ->
            when (menu.title) {
                "계정 정보" -> {
                    startActivity(Intent(context, AccountInfoActivity::class.java))
                }
                "API 연동 설정" -> {
                    startActivity(Intent(context, ApiSettingsActivity::class.java))
                }
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = myAdapter
        }
    }
}
