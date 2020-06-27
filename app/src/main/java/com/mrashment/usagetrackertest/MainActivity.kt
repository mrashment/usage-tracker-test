package com.mrashment.usagetrackertest

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.Comparator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)

        val manager: UsageStatsManager = getSystemService(UsageStatsManager::class.java) as UsageStatsManager

        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        val queryUsageStats: MutableList<UsageStats> = manager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(),
                System.currentTimeMillis()
            )
        Log.d("MainActivity", "List of stats: $queryUsageStats")

        queryUsageStats.sortWith(Comparator { o1, o2 -> if (o1.totalTimeInForeground < o2.totalTimeInForeground) 1 else -1 })

        for (us in queryUsageStats) {
            Log.d("MainActivity", "Next UsageStats: ${us.packageName}")
            if (us.totalTimeInForeground > 0 && !us.packageName.contains("nexuslauncher")) {
                tvData.append("${us.packageName}\t${us.totalTimeInForeground}\n")
            }
        }
    }
}
