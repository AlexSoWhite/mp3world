package com.nafanya.mp3world.features.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.StatisticListItemBinding

class StatisticListAdapter(
    private val list: MutableList<SongStatisticEntity>
) : RecyclerView.Adapter<StatisticListAdapter.StatisticViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.statistic_list_item, parent, false)
        return StatisticViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StatisticViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = StatisticListItemBinding.bind(itemView)

        @Suppress("ComplexCondition")
        fun bind(
            song: SongStatisticEntity
        ) {
            binding.song = song
            binding.time.text = stringFromDuration(song.time!!)
        }

        private fun stringFromDuration(arg: Long?): String {
            var duration = arg
            if (duration == null) return ""
            var hours = 0
            while (duration >= millisecondsInOneHour) {
                hours++
                duration -= millisecondsInOneHour
            }
            var minutes = 0
            while (duration >= millisecondsInOneMinute) {
                minutes++
                duration -= millisecondsInOneMinute
            }
            var seconds = 0
            while (duration >= millisecondsInOneSecond) {
                seconds++
                duration -= millisecondsInOneSecond
            }
            return if (hours == 0) {
                minutes.toString() + ":" + seconds.toString().padStart(2, '0')
            } else {
                hours.toString() + ":" +
                    minutes.toString().padStart(2, '0') + "" +
                    seconds.toString().padStart(2, '0')
            }
        }

        companion object {
            private const val millisecondsInOneHour = 3600000
            private const val millisecondsInOneMinute = 60000
            private const val millisecondsInOneSecond = 1000
        }
    }
}
