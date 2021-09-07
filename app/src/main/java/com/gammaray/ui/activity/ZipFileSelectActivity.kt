package com.gammaray.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.gammaray.R
import com.gammaray.adapter.FileListAdapter
import com.gammaray.base.BaseActivity
import com.gammaray.base.ToolBarOptions
import com.gammaray.databinding.ActivityZipSelectBinding
import com.zp.z_file.async.ZFileAsyncImpl
import com.zp.z_file.content.ZFileBean
import kotlinx.android.synthetic.main.activity_zip_select.*

class ZipFileSelectActivity : BaseActivity<ActivityZipSelectBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_zip_select
    }

    override fun initPresenter() {

    }

    override fun initView() {
        val mToolBarOptions = ToolBarOptions()
        mToolBarOptions.titleString = "请选择需要上传的文件"
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions)
        showFileList()
    }

    fun showFileList() {

        ZFileAsyncImpl(this) {
            if (isNullOrEmpty()) {
                ToastUtils.showShort("未找到zip文件")
            } else {
                initList(this as ArrayList<ZFileBean>)
            }
        }.start(arrayOf("zip"))
    }

    private fun changeList(oldList: MutableList<ZFileBean>): ArrayList<ZFileBean> {
        val list = ArrayList<ZFileBean>()
        var index = 1
        oldList.forEach forEach@{
            if (index >= 100) {
                return@forEach
            }
            list.add(it)
            index++
        }
        return list
    }

    private var fileAdapter: FileListAdapter? = null
    private fun initList(list: ArrayList<ZFileBean>) {
        fileAdapter = FileListAdapter(list)
        fileList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fileAdapter
        }
        fileAdapter!!.setEmptyView(R.layout.layout_entrust_empty, fileList)

        fileAdapter!!.setOnItemClickListener { adapter, view, position -> backResult(list.get(position).filePath) }
    }

    private fun backResult(path: String) {
        val intent = Intent()
        intent.putExtra("scan_result", path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}