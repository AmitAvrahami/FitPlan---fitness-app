package com.example.fitplan.UI.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentRunBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class RunFragment : Fragment() {
    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RunViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isRunningHappend = false


        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE


        // observe of distance
        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                viewModel.kmForMinute()
                viewModel.updateDistance(location)
            }
            binding.kmTv.text = viewModel.getTotalDistance().toString()
        }

        //observe of time
        viewModel.getTime().observe(viewLifecycleOwner) { time ->
            viewModel.kmForMinute()
            binding.timeTv.text = time

        }

        //observe of km for minute
        viewModel.getKmForMinute().observe(viewLifecycleOwner) { kmForMinute ->
            binding.kmMTv.text = kmForMinute
        }
        binding.stopPauseBtn.isEnabled = false

        binding.startBtn.setOnClickListener {
            viewModel.checkLocationPermissions(requireActivity(),
                object : RunViewModel.ICheckLocationPermissionListener {
                    override fun onPermissionGranted() {
                        Toast.makeText(requireContext(), "granted", Toast.LENGTH_LONG).show()
                        when (viewModel.isRunning()) {
                            false -> {
                                if (!isRunningHappend) {
                                    viewModel.onStartRunning(requireActivity())
                                    binding.startBtn.icon =
                                        resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                                    binding.startBtn.icon.setTint(resources.getColor(R.color.black))
                                    isRunningHappend = true
                                } else {
                                    viewModel.onResumeRunning(requireActivity())
                                    binding.startBtn.icon =
                                        resources.getDrawable(R.drawable.pause_svgrepo_com_full)
                                }

                            }

                            true -> { // If already started, pause the run
                                viewModel.onPauseRunning()
                                binding.startBtn.icon =
                                    resources.getDrawable(R.drawable.play_svgrepo_com_full)
                            }
                        }
                        binding.stopPauseBtn.isEnabled = true


                        binding.stopPauseBtn.setOnClickListener {
                            //viewModel.onStopRunning()
                            binding.startBtn.icon =
                                resources.getDrawable(R.drawable.play_svgrepo_com_full)
                            binding.startBtn.icon.setTint(resources.getColor(R.color.black))
                            findNavController().navigate(R.id.runSaveDetails)
                        }
                    }

                    override fun onPermissionDenied() {
                        // Permission denied, handle it accordingly
                        if (!viewModel.isPermissionDeniedBefore) {
                            // First time denied, show rationale and request permission again
                            showPermissionRationaleDialog()
                        } else {
                            // Permission denied previously, show rationale and ask again
                            requestPermissionAgain()
                        }
//                        Toast.makeText(requireContext(), "not granted", Toast.LENGTH_LONG).show()
//                        return
                    }
                })
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Location permission is required to continue.")
            .setPositiveButton("OK") { dialog, which ->
                // Request permissions again
                viewModel.requestPermissions(requireActivity())
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle cancelation
            }
            .show()
    }

    private fun requestPermissionAgain() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Location permission is required to continue.")
            .setPositiveButton("OK") { dialog, which ->
                // Request permissions again
                viewModel.requestPermissions(requireActivity())
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle cancelation
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}