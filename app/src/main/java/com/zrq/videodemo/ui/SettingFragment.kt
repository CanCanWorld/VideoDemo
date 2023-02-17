package com.zrq.videodemo.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.databinding.FragmentSettingBinding
import com.zrq.videodemo.utils.Constants.PAGE_SETTING
import com.zrq.videodemo.utils.OtherUtils
import com.zrq.videodemo.view.EnsureDialog
import com.zrq.videodemo.view.HistoryDialog
import com.zrq.videodemo.view.ThemeDialog

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    private var historyDialog: HistoryDialog? = null
    private var themeDialog: ThemeDialog? = null
    private var ensureDialog: EnsureDialog? = null
    private var list = mutableListOf<String>()

    override fun initData() {
        mainModel.setSearchHintText("设置页")
    }

    override fun initEvent() {
        mBinding.apply {
            ivBack.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .popBackStack()
            }
            llCheckAll.setOnClickListener {
                mainModel.db?.messageDao()?.let { dao ->
                    dao.queryAll().forEach {
                        list.add(OtherUtils.messageToString(it))
                    }
                    historyDialog?.onItemNotify(list)
                }
                if (historyDialog == null)
                    historyDialog = HistoryDialog(requireContext(), requireActivity(), list)
                historyDialog!!.show()
            }
            llDelete.setOnClickListener {
                if (ensureDialog == null)
                    ensureDialog = EnsureDialog(requireContext(), requireActivity()) { ensure ->
                        if (ensure) {
                            mainModel.db?.messageDao()?.removeAll()
                            Toast.makeText(requireContext(), "删除成功", Toast.LENGTH_SHORT).show()
                        }
                    }
                ensureDialog!!.show()
            }
            llTheme.setOnClickListener {
                if (themeDialog == null) {
                    themeDialog = ThemeDialog(requireContext(), requireActivity()) {
                        requireActivity().apply {
                            finish()
                            startActivity(intent.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
                            overridePendingTransition(0, 0)
                        }
                    }
                    themeDialog!!.show()
                }
            }
        }
    }

    override fun setNowPage(): String = PAGE_SETTING
}
