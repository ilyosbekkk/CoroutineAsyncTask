package com.example.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    var simpleTask: SimpleTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val numbers = arrayOf(1, 2, 3, 5, 6, 7, 8, 9, 10)

        simpleTask = SimpleTask()
        simpleTask?.execute(*numbers)

    }


}


class SimpleTask : CoroutineAsyncTask<Int, Int, String>() {
    private val TAG = "MainActivity"
    override fun doInBackground(vararg params: Int?): String {
        for (i in params) {
            Thread.sleep(1000)
            Log.e(TAG, "doInBackground: " + i)
        }

        return "Success"
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.e(TAG, "onPostExecute: " + result)
    }

}


abstract class CoroutineAsyncTask<Params, Progress, Result> {


    open fun onPreexecute() {}

    abstract fun doInBackground(vararg params: Params?): Result

    open fun onProgressUpdate(vararg values: Progress?) {}

    open fun onPostExecute(result: Result?) {}

    open fun onCancelled(result: Result?) {}

    protected fun publishProgress(vararg progres: Progress?) {
        GlobalScope.launch(Dispatchers.Main) {
            onProgressUpdate(*progres)
        }
    }

    fun execute(vararg params: Params?) {
        GlobalScope.launch(Dispatchers.Default) {
            val result = doInBackground(*params)
            withContext(Dispatchers.Main) {
                onPostExecute(result = result)
            }
        }
    }

    fun cancel(mayInterruptIfRunning: Boolean) {}

}

