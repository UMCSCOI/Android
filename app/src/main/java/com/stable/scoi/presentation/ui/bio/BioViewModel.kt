package com.stable.scoi.presentation.ui.bio

import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class BioViewModel @Inject constructor():
    BaseViewModel<BioState, BioEvent>(BioState()) {

    fun onBiometricSuccess(){
        emitEvent(BioEvent.NavigationToMain)
    }

    fun onCountOver(count:Int){
        if(count>=5){
            emitEvent(BioEvent.NavigationToPin) // dialog 뜨게 하기
        }
        else{

        }
    }



}