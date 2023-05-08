package kr.co.testapp0501.viewmodel

import androidx.lifecycle.MutableLiveData
import kr.co.testapp0501.base.BaseViewModel

class ProfileViewModel: BaseViewModel() {

    val tvYear = MutableLiveData<String>() // 생년월일
    val tvSolarMonthAndDay = MutableLiveData<String>() // 양력 월,일
    val tvLunarMonthAndDay = MutableLiveData<String>() // 음력 월,일
    val tvDateOfDeath = MutableLiveData<String>() // 사망일
    val tvAge = MutableLiveData<String>() // 나이
    val tvGender = MutableLiveData<String>() // 성별
    val tvZodiacSign = MutableLiveData<String>() // 띠
    val tvContactNum = MutableLiveData<String>() // 연락처
    val tvAddress1 = MutableLiveData<String>() // 주소 1
    val tvAddress2 = MutableLiveData<String>() // 주소 2

    init {
        tvYear.value = "1999년"
        tvSolarMonthAndDay.value = "1월 18일"
        tvLunarMonthAndDay.value = "2월 1일"
        tvDateOfDeath.value = "-"
        tvAge.value = "25살"
        tvGender.value = "여자"
        tvZodiacSign.value = "토끼띠"
        tvContactNum.value = "010-4054-0064"
        tvAddress1.value = "경기 수원시 망포동 신원빌라 103동"
        tvAddress2.value = "1006호"
    }
}