package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplan.ExercisesViewModel
import com.example.fitplan.R
import com.example.fitplan.adapters.MyExerciseAdapter
import com.example.fitplan.databinding.FragmentMyWorkoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MyWorkoutFragment : Fragment() {

    private var _binding: FragmentMyWorkoutBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel : ExercisesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyWorkoutBinding.inflate(inflater, container, false)
        val bottomNavigationMenuView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationMenuView.visibility = View.VISIBLE

        binding.planWorkoutBtn.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_myWorkoutFragment_to_planWorkoutFragment2)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        viewModel.exercises?.observe(viewLifecycleOwner){exercises ->

            binding.recycler.adapter = MyExerciseAdapter(exercises, object : MyExerciseAdapter.ExerciseListener {
                override fun onExerciseClicked(index: Int) {
                    viewModel.setExercise(exercises[index])
                    //findNavController().navigate(R.id.action_allItemsFragment_to_detailItemFragment)
                }

                override fun onExerciseLongClicked(index: Int) {
                    Toast.makeText(requireContext(),"${exercises[index]}", Toast.LENGTH_SHORT).show()
                }


            }, viewModel)
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}