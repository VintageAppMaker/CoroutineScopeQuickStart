package com.example.coroutinescopequickstart.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jsoup.Jsoup

class MainViewModel : ViewModel() {
    var imgList     = MutableLiveData<MutableList<String>>()
    private val lst = mutableListOf<String>()

    var status      = MutableLiveData<String>()
    var nProgress   = 0

    // Flow처리 완료시 UI에 리스트 값을 전송
    var onComplete :() -> Unit  = {
        imgList.postValue(lst)
    }

    init {
        status.postValue("시작")
        getImageInfoFromNetwork("http://vintageappmaker.com", onComplete)
    }

    fun getImageInfoFromNetwork(url : String, onComplete : () -> Unit) {

        // 통신처리를 안전하게 CoroutineScope를 사용함
        // ViewModel이 메모리 삭제시
        // 모든작업 취소 및 종료
        CoroutineScope(Dispatchers.IO).launch {

            // flow는 통신처리를
            // 순차적으로 하기위함
            // 진행상황을 UI에 리포팅
            flow{
                var doc = Jsoup.connect(url).get()
                var lst = doc.getElementsByTag("img")
                lst.forEach {
                    var sUrl = it.attr("src")
                    emit(sUrl) // collect로 보냄
                    delay(400)
                }

                // 모두 처리했음
                emit(true)
            }.collect {

                // 임의적으로 bool 값이면 종료라고 판단
                if (it is String ) {
                    Log.d(this.javaClass.toString(), "[network] image complete => $it")
                    lst.add(it)

                    // UI 쓰레드로 진행정보 전송
                    status.postValue("추가 - ${nProgress++} ")

                } else {
                    // 종료 callback 호출
                    status.postValue("종료")
                    onComplete()
                }
            }
        }
    }
}