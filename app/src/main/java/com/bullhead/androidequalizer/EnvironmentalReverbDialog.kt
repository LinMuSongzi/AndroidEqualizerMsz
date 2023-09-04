package com.bullhead.androidequalizer

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bullhead.equalizer.EnableViewModel

class EnvironmentalReverbDialog : BaseDialogFragment() {


    private val viewModel: EnableViewModel?
        get() {
            return if (!isAdded) null else ViewModelProvider(requireActivity())[EnableViewModel::class.java]
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_environmental_reverb, container, false)
    }







    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.id_recycle)?.apply recycleview@{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val size = (viewModel?.environmentalReverbs?.size) ?: 0
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    if (parent.getChildLayoutPosition(view) != 0) {
                        outRect.top = 20//context.resources.getDimensionPixelSize(R.dimen.test_10dp)
                    }
                }
            })
            addItemDecoration(object :RecyclerView.ItemDecoration(){
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if(parent.getChildItemId(view).toInt()  != (parent.adapter?.itemCount!! -1) ){
                        outRect.bottom = 5
                    }
                }
            })
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    return object : ViewHolder(LayoutInflater.from(requireContext()).inflate(R.layout.item_environmental_reverb, parent, false)) {}
                }

                override fun getItemCount(): Int = size
                override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
                    val info = viewModel?.environmentalReverbs!![position]
                    val id_values = holder.itemView.findViewById<TextView>(R.id.id_values)
                    holder.itemView.findViewById<SeekBar>(R.id.verticalSeekBar).apply {
                        progress = info.thisProx
                        max = info.maxProx + info.plusSum
                        textTag(id_values, info)
                        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                if (fromUser) {
                                    id_values.text = "progress = ${progress - info.plusSum}"
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                                Log.d("onStopTrackingTouch", "onStopTrackingTouch: ${info.title} , thisProx =  ${progress - info.plusSum}")
                                info.thisProx = (seekBar.progress * info.multiplying).toInt()
//                                viewModel?.environmentalReverbsLivedata?.value = 1
                                viewModel?.currentEnvironmentalReverbInfo?.value = info
//                                this@recycleview.adapter?.notifyItemChanged(position)
                                textTag(id_values, info)
                            }

                        })
                    }

                    holder.itemView.findViewById<SeekBar>(R.id.verticalSeekBar2).apply{
                        this.max = 30
                        progress = info.multiplying.toInt() * 10
                        textTag(id_values, info)
                        setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar) {
                                info.multiplying = seekBar.progress.toFloat() / 10
                                viewModel?.currentEnvironmentalReverbInfo?.value = info
                                textTag(id_values, info)
                            }

                        })
                    }

                    holder.itemView.findViewById<TextView>(R.id.id_title).text =
                        viewModel?.environmentalReverbs!![position].title + " 范围 [${info.startPox},${info.maxProx}] ${info.tag}"
                    holder.itemView.findViewById<TextView>(R.id.id_detail).text = "${info.detail}"
                }

            }
        }

    }

    private fun textTag(textView: TextView, info: EnableViewModel.EnvironmentalReverbInfo) {
        textView.text = "progress = ${info.thisProx - info.plusSum}/倍率：${info.multiplying}"
    }

}