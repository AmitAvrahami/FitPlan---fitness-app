package com.example.fitplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitplan.model.Exercise
import com.example.fitplan.model.Plan

class SharedViewModel : ViewModel() {
    private val _selectedPlan = MutableLiveData<Plan>()
    val selectedPlan: LiveData<Plan> get() = _selectedPlan

    private val _selectedExercise = MutableLiveData<Exercise>()
    val selectedExercise: LiveData<Exercise> get() = _selectedExercise

    private val _exerciseToPlan = MutableLiveData<List<Exercise>>()
    val exerciseToPlan: LiveData<List<Exercise>> get() = _exerciseToPlan

    fun setSelectedPlan(plan: Plan) {
        _selectedPlan.value = plan
    }
    fun addExerciseToPlan(exercise: Exercise) {
        val currentList = _exerciseToPlan.value?.toMutableList() ?: mutableListOf()
        currentList.add(exercise)
        _exerciseToPlan.value = currentList
    }
    fun setSelectedExercise(exercise: Exercise) {
        _selectedExercise.value = exercise
    }
}